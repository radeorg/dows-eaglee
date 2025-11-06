package com.hina.eaglee.monitor;


import cn.hutool.core.util.StrUtil;
import com.hina.eaglee.alert.TaskAlert;
import com.hina.eaglee.alert.TimeoutAlert;
import com.hina.eaglee.cache.TaskSettingHandler;
import com.hina.eaglee.cluster.YarnClient;
import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.cluster.YarnApps;
import com.hina.eaglee.dao.DolphinTaskDao;
import com.hina.eaglee.status.TaskStatus;
import com.hina.eaglee.entity.DolphinTaskEntity;
import com.hina.eaglee.setting.TaskRuleSetting;
import com.hina.eaglee.status.TaskInfo;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * yarn 集群监控
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class YarnMonitor implements ResourceMonitor {


    private final DolphinTaskDao dolphinTaskDao;

    private final TaskSettingHandler taskSettingCache;

    private final YarnClient clusterClient;

    // 处理器
    private final Map<String, TaskAlert> taskAlerts;


    /**
     * 任务实例监控
     * 1. 获取所有任务实例
     * 2. 筛选出状态为"运行中"的任务实例
     * 3. 查询这些任务实例的详细运行状态
     * 4. 如果运行状态为"失败"，或任务执行时间超过预设的超时时间，则触发告警机制
     *
     */
    @Override
    public void monitor(MonitorSetting monitorSetting) {
        log.info("开始执行任务监控: {}", this.getClass().getSimpleName());

        // 根据monitorSetting 查yarn 集群中运行中的任务实例
        YarnApps yarnApps = clusterClient.apps(monitorSetting);
        if (yarnApps == null) {
            log.info("监控yarn集群中没有运行中的任务实例");
            // 检查是否有之前运行但现在消失的任务
            return;
        }

        // 转换为Map<String, YarnApp>，key为任务实例的任务名称，value为任务实例详情
        Map<String, YarnApp> yarnAppMap = yarnApps.getApp().stream()
                .collect(Collectors.toMap(YarnApp::getId, yarnApp -> yarnApp));

        yarnAppMap.forEach((appId, yarnApp) -> {
            // 获取任务实例的appLink
            String appLink = yarnApp.getId();
            String taskName = yarnApp.getName();
            // 查寻任务实例的任务实体，判断是否有任务规则配置，如果有，则计算平均耗时
            TaskRuleSetting taskSetting = taskSettingCache.getTaskSetting(yarnApp);
            if (taskSetting != null) {
                QueryWrapper queryWrapper = QueryWrapper.create().from(DolphinTaskEntity.class)
                        .likeLeft(DolphinTaskEntity::getName, taskName)
                        .and(DolphinTaskEntity::getProjectCode).eq(taskSetting.getProjectCode())
                        .and(DolphinTaskEntity::getState).eq(TaskStatus.FINISHED.getValue())
                        //.and(DolphinTaskEntity::getAppLink).eq(appLink)
                        .orderBy(DolphinTaskEntity::getSubmitTime, false)
                        .limit(taskSetting.getTaskCardinalCount());
                try {
                    // 查询dolphin数据库,找到任务实例的任务实体
                    List<DolphinTaskEntity> dolphinTaskEntities = dolphinTaskDao.list(queryWrapper);
                    log.info("任务 {} 的前 {} 次成功数量为 {}", taskName, taskSetting.getTaskCardinalCount(), dolphinTaskEntities.size());
                    // 计算他们的平均耗时,如果平均耗时超过预设的超时阈值，则触发告警机制
                    double averageDuration = calculateAverageTaskDuration(dolphinTaskEntities);
                    // 计算当前yarn任务的运行时长(当前时间-任务开始时间)
                    long currentDuration = System.currentTimeMillis() - yarnApp.getStartedTime();
                    double ratio = currentDuration / averageDuration;
                    double timeoutThreshold = taskSetting.getTimeoutThreshold() / 100.0;
                    if (ratio > timeoutThreshold) {
                        log.info("任务 {} 的平均耗时 {} 秒，当前运行时长 {} 秒，超过了预设的超时阈值 {} 秒，触发告警机制", taskName, averageDuration, currentDuration, timeoutThreshold);
                        // todo 触发告警,这里改线程池执行
                        doAlert(appId, averageDuration);
                    }
                } catch (Exception e) {
                    log.error("查询任务实例的任务实体失败", e);
                }
            }
        });
    }

    private void doAlert(String appId, double averageDuration) {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setApplicationId(appId);
        taskInfo.setAverageDuration(averageDuration);
        // 告警
        taskAlerts.get(StrUtil.lowerFirst(TimeoutAlert.class.getSimpleName())).handle(taskInfo);
    }


    /**
     * 计算DolphinTaskEntity集合中每个任务的时长并求平均值
     *
     * @param dolphinTaskEntities DolphinTaskEntity集合
     * @return 平均时长（秒），如果没有有效数据则返回0
     */
    public double calculateAverageTaskDuration(List<DolphinTaskEntity> dolphinTaskEntities) {
        if (dolphinTaskEntities == null || dolphinTaskEntities.isEmpty()) {
            log.info("任务集合为空，无法计算平均时长");
            return 0;
        }

        // 计算每个任务的时长（秒），并过滤掉无效数据
        OptionalDouble averageDuration = dolphinTaskEntities.stream()
                .map(this::calculateTaskDuration)
                .filter(duration -> duration > 0) // 过滤掉无效的时长（<=0的情况）
                .mapToLong(Long::longValue)
                .average();

        double result = averageDuration.orElse(0);
        log.info("计算了 {} 个任务的平均时长: {} 秒", dolphinTaskEntities.size(), result);

        return result;
    }

    /**
     * 计算单个任务的时长（秒）
     *
     * @param task 任务实体
     * @return 任务时长（秒），如果数据无效返回0
     */
    private long calculateTaskDuration(DolphinTaskEntity task) {
        if (task == null || task.getStartTime() == null) {
            return 0;
        }

        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();

        // 如果结束时间为空，使用当前时间作为结束时间（表示任务还在运行）
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }

        // 确保结束时间不早于开始时间
        if (endTime.isBefore(startTime)) {
            log.warn("任务 {} 的结束时间早于开始时间，使用当前时间计算", task.getId());
            endTime = LocalDateTime.now();
        }

        // 计算时长（秒）
        Duration duration = Duration.between(startTime, endTime);
        return duration.getSeconds();
    }

}
