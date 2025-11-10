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
public class KilledStateHandler implements StateHandler {
    @Override
    public void handle(DolphinTask dolphinTaskEntity, TaskRuleSetting taskRuleSetting, YarnApp yarnAppInstance) {
        log.info("任务实例{}:{}已被杀死", dolphinTaskEntity.getName(),dolphinTaskEntity.getAppLink());
    }
}
