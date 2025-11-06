package com.hina.eaglee.status;

import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.dolphin.DolphinTask;
import com.hina.eaglee.entity.DolphinTaskEntity;
import com.hina.eaglee.setting.TaskRuleSetting;
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
