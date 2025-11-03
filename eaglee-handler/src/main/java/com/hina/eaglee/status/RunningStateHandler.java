package com.hina.eaglee.status;

import com.hina.eaglee.alert.StateAlert;
import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.entity.DolphinTaskEntity;
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
    private final Map<String, StateAlert> stateAlerts;

    /**
     * 运行时的项目需要监控，监控项目运行状态，如发现超时任务（根据任务规则设置的超时时间），需要及时告警
     *
     * @param dolphinTaskEntity
     * @param taskRuleSetting
     * @param yarnAppInstance
     */
    @Override
    public void handle(DolphinTaskEntity dolphinTaskEntity, TaskRuleSetting taskRuleSetting, YarnApp yarnAppInstance) {
        log.info("任务实例{}:{}正在运行中", dolphinTaskEntity.getName(), dolphinTaskEntity.getAppLink());
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
            resizeQueue(dolphinTaskEntity.getName(), taskCardinalCount);
        }
        LinkedBlockingDeque<YarnApp> queue = taskHistoryMap.computeIfAbsent(
                dolphinTaskEntity.getName(), k -> new LinkedBlockingDeque<>(taskCardinalCount)
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
            taskInfo.setDolphinTaskEntity(dolphinTaskEntity);
            if (taskInfo.isTimeout()) {
                triggerAlert(taskInfo);
            }
        }
    }

    /**
     * 触发告警逻辑
     */
    private void triggerAlert(TaskInfo taskInfo) {
        // 告警逻辑：可以发送邮件、日志记录或调用其他服务
        StateAlert stateAlert = stateAlerts.get(taskInfo.getDolphinTaskEntity().getState());
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
