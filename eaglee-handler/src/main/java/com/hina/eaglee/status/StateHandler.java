package com.hina.eaglee.status;

import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.entity.DolphinTaskEntity;
import com.hina.eaglee.setting.TaskRuleSetting;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 状态处理器
 */
public interface StateHandler {
    void handle(DolphinTaskEntity dolphinTaskEntity, TaskRuleSetting taskRuleSetting, YarnApp yarnAppInstance);

    /**
     * 检查当前任务是否运行时间超过平均时间的150%，如果是则告警
     *
     * @param currentTask    当前任务
     * @param completedTasks 已完成任务队列
     */
    default TaskInfo checkYarnTaskInfo(YarnApp currentTask, LinkedBlockingDeque<YarnApp> completedTasks, Integer timeoutThreshold) {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setApplicationId(currentTask.getId());
        taskInfo.setYarnApp(currentTask);
        //taskInfo.setYarnApp(currentTask);
        if (completedTasks == null || completedTasks.isEmpty()) {
            taskInfo.setTimeout(false);
        }

        // 计算当前任务的运行时间（毫秒）
        long currentDuration = System.currentTimeMillis() - currentTask.getStartedTime();

        // 计算统计队列中的平均耗时,计算已完成任务的平均运行时间（毫秒）,
        double averageDuration = completedTasks.stream()
                .mapToLong(YarnApp::getElapsedTime)
                .average()
                .orElse(0);
        double value = timeoutThreshold / 100.0;
        // 检查比例是否超过xxx%,默认150%
        boolean timeout = averageDuration > 0 && (currentDuration / averageDuration) > value;
        // 设置任务超时状态
        taskInfo.setTimeout(timeout);
        // 设置任务运行时间
        taskInfo.setRunningTime(currentDuration);
        // 设置任务平均运行时间
        taskInfo.setAverageDuration(averageDuration);
        return taskInfo;
    }

}
