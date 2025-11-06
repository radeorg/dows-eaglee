package com.hina.eaglee.status;

import com.hina.eaglee.alert.AlertInfo;
import com.hina.eaglee.alert.TaskAlert;
import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.dolphin.DolphinTask;
import com.hina.eaglee.processor.StateProcessor;
import com.hina.eaglee.setting.TaskRuleSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@RequiredArgsConstructor
public class RunningStateHandler implements StateHandler {

    // 记录每个任务实例的前三次运行记录，用于计算任务运行时间
    private final Map<String, LinkedBlockingDeque<YarnApp>> taskHistoryMap = new ConcurrentHashMap<>();
    // 任务计数器，用于动态调整队列大小
    private final AtomicInteger taskCount = new AtomicInteger(0);
    // 状态处理器
    private final Map<String, TaskAlert> stateAlerts;

    private final Map<String, StateProcessor> stateProcessors;

    private static final Map<String,String> appIds = new ConcurrentHashMap<>();
    /**
     * 运行时的项目需要监控，监控项目运行状态，如发现超时任务（根据任务规则设置的超时时间），需要及时告警
     *
     * @param dolphinTaskEntity
     * @param taskRuleSetting
     * @param yarnAppInstance
     */
    @Override
    public void handle(DolphinTask dolphinTaskEntity, TaskRuleSetting taskRuleSetting, YarnApp yarnAppInstance) {
        log.info("dolphin任务实例 : yarn任务实例 = {}:{} 正在运行中", dolphinTaskEntity.getName(), dolphinTaskEntity.getAppLink());


        String appId = appIds.get(yarnAppInstance.getId());
        // 如果不空说明在运行中
        if(appId == null){
            appIds.put(yarnAppInstance.getId(),yarnAppInstance.getName());
        } else {
            /*
            超时告警阈值(%) 监控任务运行时间，如超过超时时间，需要告警
            计算逻辑：
            1. 任务运行时间 = 当前时间 - 任务启动时间
            2. 如果任务运行时间 > 超时告警阈值(%) * 任务启动时间，需要告警
            3. 如果任务运行时间 < 超时告警阈值(%) * 任务启动时间，不需要告警
            4. 如果超时告警阈值(%)为空，则不进行告警
            */
            Integer taskCardinalCount = taskRuleSetting.getTaskCardinalCount();
            if (taskCardinalCount != taskCount.get()) {
                taskCount.set(taskCardinalCount);
                resizeQueue(yarnAppInstance.getName(), taskCardinalCount);
            }
            LinkedBlockingDeque<YarnApp> queue = taskHistoryMap.computeIfAbsent(
                    yarnAppInstance.getName(), k -> new LinkedBlockingDeque<>(taskCardinalCount)
            );
            if (queue.size() >= taskCardinalCount) {
                // 移除队列头部元素，
                queue.pollFirst();
            }
            queue.addLast(yarnAppInstance);

            // 超时告警阈值(%)
            Integer timeoutThreshold = taskRuleSetting.getTimeoutThreshold();
            if (timeoutThreshold != null) {
                TaskInfo taskInfo = checkYarnTaskInfo(yarnAppInstance, queue, timeoutThreshold);
                taskInfo.setProjectCode(dolphinTaskEntity.getProjectCode());
                taskInfo.setDolphinTask(dolphinTaskEntity);
                if (taskInfo.isTimeout()) {
                    taskInfo.setStateType(StateType.timeout);
                    stateAlert(taskInfo);
                    // todo ,这里可以根据配置要求，如果自动处理，则需要调用状态处理器，否则不处理由人工处理
                    stateProcess(taskInfo);
                }
            }
        }
    }


    /**
     * 状态处理
     * @param taskInfo
     */
    private void stateProcess(TaskInfo taskInfo) {
        StateProcessor stateProcessor = stateProcessors.get(taskInfo.getStateType().name() + "Processor");
        if (stateProcessor != null) {
            stateProcessor.handle(taskInfo);
        }
    }
    /**
     * 状态告警
     * @param taskInfo
     */
    private void stateAlert(TaskInfo taskInfo) {
        // 告警逻辑：可以发送邮件、日志记录或调用其他服务
        TaskAlert stateAlert = stateAlerts.get(taskInfo.getStateType().name() + "Alert");
        if (stateAlert != null) {
            stateAlert.handle(taskInfo);
        }
    }



    /**
     * 动态调整队列大小
     *
     * @param taskId  任务ID
     * @param newSize 新的队列大小
     */
    private void resizeQueue(String taskId, int newSize) {

        LinkedBlockingDeque<YarnApp> oldQueue = taskHistoryMap.get(taskId);
        if (oldQueue == null) {
            return;
        }
        // 创建新队列并迁移数据
        LinkedBlockingDeque<YarnApp> newQueue = new LinkedBlockingDeque<>(newSize);
        for (YarnApp yarnApp : oldQueue) {
            if (newQueue.size() >= newSize) {
                break; // 如果新队列已满，停止迁移
            }
            newQueue.addLast(yarnApp);
        }
        // 替换旧队列
        taskHistoryMap.put(taskId, newQueue);
    }

}
