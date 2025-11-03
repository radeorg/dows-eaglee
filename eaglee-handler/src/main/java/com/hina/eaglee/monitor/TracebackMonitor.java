package com.hina.eaglee.monitor;

import com.hina.eaglee.cache.TaskSettingCache;
import com.hina.eaglee.cluster.ClusterClient;
import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.cluster.YarnApps;
import com.hina.eaglee.dao.DolphinTaskDao;
import com.hina.eaglee.dolphin.DolphinMonitor;
import com.hina.eaglee.dolphin.MonitorSetting;
import com.hina.eaglee.entity.DolphinTaskEntity;
import com.hina.eaglee.setting.TaskRuleSetting;
import com.hina.eaglee.status.StateHandler;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.processor.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统一回溯项目监控
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TracebackMonitor implements DolphinMonitor {


    private final DolphinTaskDao dolphinTaskDao;

    private final TaskSettingCache taskSettingCache;

    private final ClusterClient clusterClient;

    private final Map<String, StateHandler> stateHandlers;


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
            return;
        }

        // 转换为Map<String, YarnApp>，key为任务实例id，value为任务实例详情
        Map<String, YarnApp> yarnAppMap = yarnApps.getAppList().stream()
                .collect(Collectors.toMap(YarnApp::getId, yarnApp -> yarnApp));

        // 根据monitorSetting，获取所有任务实例
        List<DolphinTaskEntity> list = getDolphinTaskInstance(monitorSetting);
        for (DolphinTaskEntity dolphinTaskEntity : list) {
            Long taskCode = dolphinTaskEntity.getTaskCode();
            TaskRuleSetting taskRuleSetting = taskSettingCache.getTaskSetting(taskCode);
            if (taskRuleSetting == null) {
                continue;
            }
            // 获取任务实例的appLink
            String appLink = dolphinTaskEntity.getAppLink();
            if(StrUtil.isBlank(appLink)){
                continue;
            }
            YarnApp yarnApp = clusterClient.node(appLink);
            if (yarnApp == null) {
                continue;
            }

            YarnApp yarnAppInstance = yarnAppMap.get(dolphinTaskEntity.getAppLink());
            if (yarnAppInstance == null) {
                continue;
            }

            /*
             * yarn任务实例状态:
             * NEW
             * NEW_SAVING
             * SUBMITTED
             * ACCEPTED
             * RUNNING
             * FINISHED
             * FAILED
             * KILLED
             * 1. 如果任务实例状态为"运行中"，则继续监控
             * 2. 如果任务实例状态为"失败"，或任务执行时间超过预设的超时时间，则触发告警机制
             * 3. 如果任务实例状态为"成功"，则触发告警机制
             * 4. 如果任务实例状态为"杀死"，则触发告警机制
             */
            String state = yarnAppInstance.getState();
            if (!StrUtil.isBlank(state)) {
                StateHandler stateHandler = stateHandlers.get(state.toLowerCase() + "StateHandler");
                if (stateHandler != null) {
                    log.info("监控到：{} 类型任务实例 {} : {} 运行中", yarnApp.getApplicationType(), taskCode, yarnApp.getId());
                    stateHandler.handle(dolphinTaskEntity, taskRuleSetting, yarnAppInstance);
                }
            }
        }
    }

    private List<DolphinTaskEntity> getDolphinTaskInstance(MonitorSetting monitorSetting) {
        // 获取项目code列表
        List<Long> projectCodes = Arrays.stream(monitorSetting.getProjectCode().split(","))
                .map(Long::parseLong).toList();
        // 获取查询任务状态列表
        List<Integer> status = Arrays.stream(monitorSetting.getStatus().split(","))
                .map(Integer::parseInt).toList();
        // 获取间隔时间（单位分钟），前推时间，计算出查询时间范围（开始时间）
        LocalDateTime previousDateTime = getPreviousDateTime(monitorSetting.getIntervalTime());

        // 查dolphin数据库，根据项目code 和 状态查询任务实例，查询所有运行中的任务实例
        QueryWrapper queryWrapper = QueryWrapper.create().from(DolphinTaskEntity.class)
                .and(DolphinTaskEntity::getSubmitTime).between(previousDateTime, LocalDateTime.now())
                .and(DolphinTaskEntity::getProjectCode).in(projectCodes)
                .and(DolphinTaskEntity::getState).in(status);
        List<DolphinTaskEntity> list = dolphinTaskDao.list(queryWrapper);

        // todo 直接saveOrUpdate 会导致数据库中没有的数据，会被删除，需要判断是否存在，如果存在，则更新，如果不存在，则新增
        //dolphinTaskDao.saveOrUpdateBatch(list);

        log.info("监控查询到DolphinScheduler中 {} 条任务实例", list.size());
        return list;
    }


    /**
     * 获取当前时间向前推指定分钟间隔的 DateTime
     * @param minutes 分钟间隔
     * @return 计算后的 LocalDateTime
     */
    public static LocalDateTime getPreviousDateTime(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(minutes);
        return now.minus(duration);
    }
}
