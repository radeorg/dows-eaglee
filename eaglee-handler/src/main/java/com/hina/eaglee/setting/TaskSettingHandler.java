package com.hina.eaglee.setting;

import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.config.JsonConfig;
import com.hina.eaglee.dao.TaskRuleDao;
import com.hina.eaglee.dao.TaskSettingDao;
import com.hina.eaglee.entity.TaskRuleEntity;
import com.hina.eaglee.entity.TaskSettingEntity;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskSettingHandler {

    private static final Map<Long, TaskRuleSetting> metricSettingMap = new ConcurrentHashMap<>();
    private static final Map<String, TaskRuleSetting> taskSettingMap = new ConcurrentHashMap<>();

    private final TaskSettingDao taskSettingDao;
    private final TaskRuleDao taskRuleDao;
    @NotBlank(message = "节点IP地址不能为空")
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$", message = "IP地址格式不正确")
    public TaskRuleSetting getMetricSetting(String ip) {
        return null;
    }


    /**
     * 更新是调用，从数据库中刷新任务规则配置
     *
     * @param taskCode
     */
    public void refreshTaskSetting(Long taskCode) {
        TaskRuleSetting taskRuleSetting = getTaskSetting(taskCode);
        if (taskRuleSetting != null) {
            metricSettingMap.remove(taskCode);
        }
    }

    /**
     * 获取任务规则配置
     *
     * @param taskCode
     * @return
     */
    public TaskRuleSetting getTaskSetting(Long taskCode) {
        TaskRuleSetting taskRuleSetting = metricSettingMap.get(taskCode);
        if (taskRuleSetting != null) {
            return taskRuleSetting;
        }
        QueryWrapper queryWrapper = QueryWrapper.create().from(TaskSettingEntity.class)
                .and(TaskSettingEntity::getTaskCode).eq(taskCode);
        TaskSettingEntity one = taskSettingDao.getOne(queryWrapper);
        if (one != null) {
            TaskRuleEntity taskRuleEntity = taskRuleDao.getById(one.getTaskRuleId());
            if (taskRuleEntity == null) {
                return null;
            }
            taskRuleSetting = JsonConfig.fromJsonConfig(taskRuleEntity.getConfigJson(), TaskRuleSetting.class);
            metricSettingMap.put(taskCode, taskRuleSetting);
            return taskRuleSetting;
        }
        return null;
    }


    public TaskRuleSetting getTaskSetting(YarnApp yarnApp) {
        String taskName = yarnApp.getName();
        TaskRuleSetting taskRuleSetting = taskSettingMap.get(taskName);
        if (taskRuleSetting != null) {
            return taskRuleSetting;
        }
        QueryWrapper queryWrapper = QueryWrapper.create().from(TaskSettingEntity.class)
                .and(TaskSettingEntity::getTaskType).eq(yarnApp.getApplicationType())
                .and(TaskSettingEntity::getTaskName).eq(taskName);

        TaskSettingEntity one = taskSettingDao.getOne(queryWrapper);
        if (one != null) {
            TaskRuleEntity taskRuleEntity = taskRuleDao.getById(one.getTaskRuleId());
            if (taskRuleEntity == null) {
                return null;
            }
            taskRuleSetting = JsonConfig.fromJsonConfig(taskRuleEntity.getConfigJson(), TaskRuleSetting.class);
            taskSettingMap.put(taskName, taskRuleSetting);
            return taskRuleSetting;
        }
        return null;
    }

}
