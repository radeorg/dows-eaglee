package com.hina.eaglee.retry;

import com.hina.eaglee.cache.TaskSettingHandler;
import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.dolphin.DolphinClient;
import com.hina.eaglee.alert.AlertInfo;
import com.hina.eaglee.dolphin.RerunProcessInstanceRequest;
import com.hina.eaglee.setting.TaskRuleSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 资源异常重试，自动重试
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ResourceExceptionRetry implements TaskRetry {
    private final TaskSettingHandler taskSettingHandler;

    private final DolphinClient dolphinClient;

    @Override
    public void retry(AlertInfo taskAlert) {
        log.info("资源异常重试，任务：{}", taskAlert.getTaskName());
        YarnApp yarnApp = new YarnApp();
        yarnApp.setName(taskAlert.getTaskName());
        yarnApp.setApplicationType(taskAlert.getTaskType());
        yarnApp.setId(taskAlert.getApplicationId());
        TaskRuleSetting taskSetting = taskSettingHandler.getTaskSetting(yarnApp);
        /*if (taskSetting == null) {
            log.warn("资源异常重试，任务：{}，未配置任务规则", taskAlert.getTaskName());
            return;
        }*/
//        Integer retryTimes = taskSetting.getRetryTimes();
//        if (taskSetting.getRetryTimes() > 0) {
            // todo 重试
            RerunProcessInstanceRequest rerunProcessInstanceRequest = new RerunProcessInstanceRequest();
            rerunProcessInstanceRequest.setProjectCode(taskAlert.getProjectCode());
            rerunProcessInstanceRequest.setProcessInstanceId(taskAlert.getProcessId());
            rerunProcessInstanceRequest.setExecuteType("REPEAT_RUNNING");
            rerunProcessInstanceRequest.setButtonType("run");
            dolphinClient.exchange(rerunProcessInstanceRequest);
//        }


    }
}
