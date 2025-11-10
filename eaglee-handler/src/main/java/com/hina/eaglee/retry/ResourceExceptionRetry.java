package com.hina.eaglee.retry;

import com.hina.eaglee.alert.AlertInfo;
import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.dolphin.DolphinClient;
import com.hina.eaglee.dolphin.RerunProcessInstanceRequest;
import com.hina.eaglee.setting.TaskRuleSetting;
import com.hina.eaglee.setting.TaskSettingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源异常重试，自动重试
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ResourceExceptionRetry implements TaskRetry {
    private final TaskSettingHandler taskSettingHandler;

    private final DolphinClient dolphinClient;
    // 任务重试计数器
    private final Map<String, Integer> retryCountMap = new ConcurrentHashMap<>();
    @Override
    public void retry(AlertInfo taskAlert) {
        /*YarnApp yarnApp = new YarnApp();
        yarnApp.setName(taskAlert.getTaskName());
        yarnApp.setApplicationType(taskAlert.getTaskType());
        yarnApp.setId(taskAlert.getApplicationId());*/
        // 获取任务规则中的重试次数，基于规则重试
        TaskRuleSetting taskSetting = taskSettingHandler.getTaskSetting(taskAlert.getTaskCode());
        if (taskSetting == null) {
            log.warn("资源异常重试，任务：{}，未配置任务规则", taskAlert.getTaskName());
            return;
        }
        log.info("资源异常导致任务重跑:{} 将在: {} 秒后重试", taskAlert.getTaskName(), taskSetting.getRetryInterval());
        if (taskSetting.getRetryInterval() > 0) {
            try {
                Thread.sleep(taskSetting.getRetryInterval() * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (taskSetting.getRetryTimes() > 0) {
            Integer retryCount = retryCountMap.getOrDefault(taskAlert.getTaskName(), 0);
            if (retryCount >= taskSetting.getRetryTimes()) {
                log.warn("资源异常重试，任务：{}，重试次数已达上限", taskAlert.getTaskName());
                return;
            }
            retryCountMap.put(taskAlert.getTaskName(), retryCount + 1);
        }
        // todo 调用DS-API 对任务进行重试
        RerunProcessInstanceRequest rerunProcessInstanceRequest = new RerunProcessInstanceRequest();
        rerunProcessInstanceRequest.setProjectCode(taskAlert.getProjectCode());
        rerunProcessInstanceRequest.setProcessInstanceId(taskAlert.getProcessId());
        rerunProcessInstanceRequest.setExecuteType("REPEAT_RUNNING");
        rerunProcessInstanceRequest.setButtonType("run");
        String result = dolphinClient.exchange(rerunProcessInstanceRequest);
        log.info("资源异常重试，任务：{}，结果：{}", taskAlert.getTaskName(), result);
        // todo 如果ds重试调用成功（返回成功），则更新任务状态为RETRYING
        if (result.contains("success")) {
            log.info("资源异常重试，任务：{}，重试成功", taskAlert.getTaskName());
            // 重试成功后，移除重试计数器
            retryCountMap.remove(taskAlert.getTaskName());
            //todo 记录或通知，暂时没有需求
        } else {
            // 递归调用，直到重试成功或重试次数用完
            log.info("资源异常重试，任务：{}，重试失败", taskAlert.getTaskName());
            retry(taskAlert);
        }
    }
}
