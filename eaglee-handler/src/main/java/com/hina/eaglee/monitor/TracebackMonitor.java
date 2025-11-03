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
import com.hina.eaglee.task.TaskRetry;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.processor.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
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

        // 查yarn 集群中运行中的任务实例
        YarnApps yarnApps = clusterClient.apps("RUNNING");
        if (yarnApps == null) {
            log.info("监控yarn集群中没有运行中的任务实例");
            return;
        }

        //Map<String, List<YarnApp>> appMap = yarnApps.getAppList().stream().collect(Collectors.groupingBy(YarnApp::getId));
        // 转换为Map<String, YarnApp>，key为任务实例id，value为任务实例详情
        Map<String, YarnApp> yarnAppMap = yarnApps.getAppList().stream()
                .collect(Collectors.toMap(YarnApp::getId, yarnApp -> yarnApp));
        String projectCode = monitorSetting.getProjectCode();
        // 查dolphin数据库，根据项目code 和 状态查询任务实例，查询所有运行中的任务实例
        QueryWrapper queryWrapper = QueryWrapper.create().from(DolphinTaskEntity.class)
                .and(DolphinTaskEntity::getProjectCode).eq(projectCode)
                .and(DolphinTaskEntity::getState).eq(1);
        List<DolphinTaskEntity> list = dolphinTaskDao.list(queryWrapper);

        log.info("监控查询到DolphinScheduler中 {} 条任务实例", list.size());
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
            log.info("监控到：{} 类型任务实例 {} : {} 运行中", yarnApp.getApplicationType(), taskCode, yarnApp.getId());
            String trackingUrl = yarnApp.getTrackingUrl();
            String amContainerLogs = yarnApp.getAmContainerLogs();

            // 告警级别
            String alarmLevel = taskRuleSetting.getAlarmLevel();

            YarnApp yarnAppInstance = yarnAppMap.get(dolphinTaskEntity.getAppLink());
            if (yarnAppInstance == null) {
                continue;
            }

            //SUBMITTED
            //ACCEPTED
            //RUNNING
            //FINISHED
            //FAILED
            //KILLED
            String state = yarnAppInstance.getState();
            if (!StrUtil.isBlank(state)) {
                StateHandler stateHandler = stateHandlers.get(state.toLowerCase() + "StateHandler");
                if (stateHandler != null) {
                    stateHandler.handle(dolphinTaskEntity, taskRuleSetting, yarnAppInstance);
                }
            }
            /*if (state.equals("FAILED")) {
                log.info("监控任务实例 {} 运行失败", taskCode);
            } else if (state.equals("KILLED")) {
                log.info("监控任务实例 {} 被杀死", taskCode);
            } else if (state.equals("FINISHED")) {
                log.info("监控任务实例 {} 运行完成", taskCode);
            } else if (state.equals("RUNNING")) {
                log.info("监控任务实例 {} 运行中", taskCode);
            } else if (state.equals("SUBMITTED")) {
                log.info("监控任务实例 {} 提交中", taskCode);
            } else if (state.equals("ACCEPTED")) {
                log.info("监控任务实例 {} 接受中", taskCode);
            }*/
            /*for (YarnApp yarnApp : appList) {
                if (yarnApp.getName().equals(dolphinTaskEntity.getName())) {
                    log.info("监控任务实例 {} 运行中", taskCode);
                    if (yarnApp.getState().equals("FAILED")) {
                        log.info("监控任务实例 {} 运行失败", taskCode);
                        // 获取dolphin中最大重试次数
                        Integer maxRetryTimes = dolphinTaskEntity.getMaxRetryTimes();
                        // 获取dolphin中当前重试次数
                        Integer retryTimes = dolphinTaskEntity.getRetryTimes();
                        // 获取dolphin中任务实例的运行时间
                        log.info("任务实例 {} ", taskCode);
                        //线程处理逻辑,重试
                        threadPoolExecutor.execute(() -> {
                            // 重试
                        });
                    }
                }
            }*/
        }
    }
}
