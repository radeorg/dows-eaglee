package org.dows.eaglee.status;

import org.dows.eaglee.cluster.YarnApp;
import org.dows.eaglee.dolphin.DolphinTask;
import org.dows.eaglee.entity.DolphinTaskEntity;
import org.dows.eaglee.setting.TaskRuleSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FailedStateHandler implements StateHandler {
    @Override
    public void handle(DolphinTask dolphinTaskEntity, TaskRuleSetting taskRuleSetting, YarnApp yarnApp) {
        log.info("任务实例{}:{}已失败", dolphinTaskEntity.getName(),dolphinTaskEntity.getAppLink());

        String applicationType = yarnApp.getApplicationType();
        String trackingUrl = yarnApp.getTrackingUrl();
        String amContainerLogs = yarnApp.getAmContainerLogs();

        // 告警级别
        String alarmLevel = taskRuleSetting.getAlarmLevel();
    }
}
