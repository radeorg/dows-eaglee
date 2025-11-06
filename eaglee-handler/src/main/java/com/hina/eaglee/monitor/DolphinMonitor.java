package com.hina.eaglee.monitor;

import com.hina.eaglee.cache.TaskSettingHandler;
import com.hina.eaglee.cluster.YarnClient;
import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.dao.DolphinTaskDao;
import com.hina.eaglee.dolphin.TaskStatus;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一回溯项目监控
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DolphinMonitor implements ResourceMonitor {

    // 记录之前调度周期中的任务ID集合，用于检测已完成的任务
    private final Set<String> previousTaskIds = ConcurrentHashMap.newKeySet();

    private final DolphinTaskDao dolphinTaskDao;

    private final TaskSettingHandler taskSettingCache;

    private final YarnClient clusterClient;

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

        /*// 根据monitorSetting 查yarn 集群中运行中的任务实例
        YarnApps yarnApps = clusterClient.apps(monitorSetting);
        if (yarnApps == null) {
            log.info("监控yarn集群中没有运行中的任务实例");
            // 检查是否有之前运行但现在消失的任务
            checkCompletedTasks(new HashSet<>());
            return;
        }

        // 转换为Map<String, YarnApp>，key为任务实例id，value为任务实例详情
        Map<String, YarnApp> yarnAppMap = yarnApps.getApp().stream()
                .collect(Collectors.toMap(YarnApp::getId, yarnApp -> yarnApp));

        // 获取当前调度周期的任务ID集合
        Set<String> currentTaskIds = new HashSet<>(yarnAppMap.keySet());
        // 检查已完成的任务（在之前调度中存在但在当前调度中消失的任务）
        checkCompletedTasks(currentTaskIds);

        // 更新之前任务ID集合为当前集合
        previousTaskIds.clear();
        previousTaskIds.addAll(currentTaskIds);*/


        // 根据monitorSetting，获取所有任务实例
        /*List<DolphinTaskEntity> list = getDolphinTaskInstance(monitorSetting);
        for (DolphinTaskEntity dolphinTaskEntity : list) {
            Long taskCode = dolphinTaskEntity.getTaskCode();
            TaskRuleSetting taskRuleSetting = taskSettingCache.getTaskSetting(taskCode);

            if (taskRuleSetting == null) {

                continue;
            }
            // 获取任务实例的appLink
            String appLink = dolphinTaskEntity.getAppLink();
            if (StrUtil.isBlank(appLink)) {
                continue;
            }
            // 这里查到的appLink 是运行中的任务实例的appLink
            YarnApp runtimeYarnApp = clusterClient.node(appLink);
            if (runtimeYarnApp == null) {
                continue;
            }

            // 根据appLink 获取任务实例的详细信息
            YarnApp earlyYarnApp = yarnAppMap.get(dolphinTaskEntity.getAppLink());
            if (earlyYarnApp == null) {
                continue;
            }
            doExec(dolphinTaskEntity, earlyYarnApp, runtimeYarnApp, taskRuleSetting);
        }*/
    }


    /**
     * 检查已完成的任务
     * 比较之前调度周期和当前调度周期的任务ID，找出消失的任务
     *
     * @param currentTaskIds 当前调度周期的任务ID集合
     */
    private void checkCompletedTasks(Set<String> currentTaskIds) {
        if (previousTaskIds.isEmpty()) {
            // 第一次调度，没有之前的任务记录
            return;
        }

        // 找出在之前调度中存在但在当前调度中消失的任务
        Set<String> completedTaskIds = new HashSet<>(previousTaskIds);
        completedTaskIds.removeAll(currentTaskIds);

        if (!completedTaskIds.isEmpty()) {
            log.info("检测到 {} 个已完成或不再运行的任务: {}", completedTaskIds.size(), completedTaskIds);

            // 对每个已完成的任务进行详细查询和记录
            for (String taskId : completedTaskIds) {
                try {
                    YarnApp completedTask = clusterClient.node(taskId);
                    if (completedTask != null) {
                        // 记录已完成任务的详细信息
                        logCompletedTask(completedTask);
                    } else {
                        // 如果查询不到任务详情，说明任务已经完全消失
                        log.info("任务 {} 已完全消失，无法查询详情", taskId);
                    }
                } catch (Exception e) {
                    log.warn("查询已完成任务 {} 详情时发生异常: {}", taskId, e.getMessage());
                }
            }
        }
    }

    /**
     * 记录已完成任务的详细信息
     *
     * @param completedTask 已完成的任务
     */
    private void logCompletedTask(YarnApp completedTask) {
        String taskId = completedTask.getId();
        String state = completedTask.getState();
        String finalStatus = completedTask.getFinalStatus();
        Long finishedTime = completedTask.getFinishedTime();
        Long elapsedTime = completedTask.getElapsedTime();

        log.info("已完成任务详情 - ID: {}, 状态: {}, 最终状态: {}, 完成时间: {}, 运行时长: {}ms",
                taskId, state, finalStatus, finishedTime, elapsedTime);

        // 这里可以根据业务需求进行进一步处理，比如：
        // 1. 保存到数据库
        // 2. 发送通知
        // 3. 更新任务统计信息
        // 4. 触发后续处理逻辑
    }


    /**
     * 根据yarn 任务实例状态，判断任务实例是否运行中，是否超时，是否失败，是否成功
     * 根据任务实例状态，调用不同的状态处理器，处理不同的状态
     * <p>
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
     * <p>
     * 状态处理器：
     * 1. runningStateHandler：处理任务实例状态为"运行中"的情况
     * 2. failedStateHandler：处理任务实例状态为"失败"的情况
     * 3. successStateHandler：处理任务实例状态为"成功"的情况
     * 4. killedStateHandler：处理任务实例状态为"杀死"的情况
     * 5. timeoutStateHandler：处理任务实例状态为"超时"的情况
     * 6. finishedStateHandler：处理任务实例状态为"完成"的情况
     *
     */
    private void doExec(DolphinTaskEntity dolphinTaskEntity, YarnApp earlyYarnApp, YarnApp runtimeYarnApp, TaskRuleSetting taskRuleSetting) {

        String state = earlyYarnApp.getState();
        if (!StrUtil.isBlank(state)) {
            StateHandler stateHandler = stateHandlers.get(state.toLowerCase() + "StateHandler");
            if (stateHandler != null) {
                log.info("监控到：{} 类型任务实例 {} : {} 运行中", runtimeYarnApp.getApplicationType(), dolphinTaskEntity.getTaskCode(), runtimeYarnApp.getId());
                stateHandler.handle(dolphinTaskEntity, taskRuleSetting, earlyYarnApp);
            }
        }
    }

    private YarnApp mock(String appLink, YarnApp yarnApp) {
        YarnApp yarnAppInstance = new YarnApp();
        yarnAppInstance.setId(appLink);
        yarnAppInstance.setState(yarnApp.getState());
        yarnAppInstance.setFinalStatus(yarnApp.getFinalStatus());
        yarnAppInstance.setProgress(yarnApp.getProgress());
        yarnAppInstance.setTrackingUI(yarnApp.getTrackingUI());
        yarnAppInstance.setTrackingUrl(yarnApp.getTrackingUrl());
        yarnAppInstance.setDiagnostics(yarnApp.getDiagnostics());
        return yarnAppInstance;
    }

    private List<DolphinTaskEntity> getDolphinTaskInstance(MonitorSetting monitorSetting) {
        // 获取项目code列表
        List<Long> projectCodes = Arrays.stream(monitorSetting.getProjectCode().split(","))
                .map(Long::parseLong).toList();
        // 获取查询任务状态列表
        List<Integer> status = Arrays.stream(monitorSetting.getStatus().split(","))
                .map(state -> TaskStatus.valueOf(state).getValue()).toList();
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
     *
     * @param minutes 分钟间隔
     * @return 计算后的 LocalDateTime
     */
    public static LocalDateTime getPreviousDateTime(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(minutes);
        return now.minus(duration);
    }
}
