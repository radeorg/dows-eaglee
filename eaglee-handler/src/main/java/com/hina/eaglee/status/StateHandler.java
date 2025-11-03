package com.hina.eaglee.status;

import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.entity.DolphinTaskEntity;
import com.hina.eaglee.setting.TaskRuleSetting;

/**
 * 状态处理器
 */
public interface StateHandler {
    void handle(DolphinTaskEntity dolphinTaskEntity, TaskRuleSetting taskRuleSetting, YarnApp yarnAppInstance);
}
