package com.hina.eaglee.sql;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hina.eaglee.config.JsonConfig;
import com.hina.eaglee.dao.TaskRuleDao;
import com.hina.eaglee.dao.TaskSettingDao;
import com.hina.eaglee.dolphin.DolphinClient;
import com.hina.eaglee.dolphin.DolphinTaskDefinition;
import com.hina.eaglee.entity.TaskRuleEntity;
import com.hina.eaglee.entity.TaskSettingEntity;
import com.hina.eaglee.exception.BusinessException;
import com.hina.eaglee.mapper.DolphinQueryMapper;
import com.hina.eaglee.request.TaskRulePageRequest;
import com.hina.eaglee.request.TaskRuleSaveRequest;
import com.hina.eaglee.response.TaskRuleResponse;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskRuleHandler {
    private final TaskRuleDao taskRuleDao;
    private final TaskSettingDao taskSettingDao;
    private final DolphinClient dolphinClient;
    private final DolphinQueryMapper dolphinQueryMapper;


    @Transactional
    public Long saveAndBind(TaskRuleSaveRequest taskRuleSaveRequest) {
        log.info("保存配置键名：{}", JSONUtil.toJsonStr(taskRuleSaveRequest));
        Long projectCode = taskRuleSaveRequest.getProjectCode();
        // 根据项目ID查询所有任务定义
        List<DolphinTaskDefinition> taskDefinitions = dolphinQueryMapper
                .listTaskDefinitionByProjectCodeFromTaskDefinition(projectCode);

        if (taskDefinitions == null || taskDefinitions.isEmpty()) {
            throw new BusinessException(BusinessException.INVALID_PARAMETER, "项目ID不存在,或者没有任务定义");
        }
        // 构建实体对象
        TaskRuleEntity taskRuleEntity = new TaskRuleEntity();
        BeanUtils.copyProperties(taskRuleSaveRequest, taskRuleEntity);
        // json 化处理
        taskRuleEntity.setConfigJson(JsonConfig.toJsonConfig(taskRuleSaveRequest.getSettings()));

        if (taskRuleDao.save(taskRuleEntity)) {
            // 绑定任务
            bind(taskRuleEntity.getTaskRuleId(),taskDefinitions);
            return taskRuleEntity.getTaskRuleId();
        }
        throw new BusinessException(BusinessException.OPERATION_FAILED, "创建任务规则失败");
    }

    /**
     * 保存规则并绑定任务标识
     * @param taskRuleSaveRequest
     * @return
     */
    @Deprecated
    @Transactional
    public Long save(TaskRuleSaveRequest taskRuleSaveRequest) {
        log.info("保存配置键名：{}", JSONUtil.toJsonStr(taskRuleSaveRequest));
        Long projectCode = taskRuleSaveRequest.getProjectCode();
        // todo 去dolphin根据流程ID查询所有任务定义
        List<DolphinTaskDefinition> taskDefinitions = dolphinClient.getTaskDefinitionByWorkflowId(projectCode);
        if (taskDefinitions == null || taskDefinitions.isEmpty()) {
            throw new BusinessException(BusinessException.INVALID_PARAMETER, "流程ID不存在,或者没有任务定义");
        }
        // 构建实体对象
        TaskRuleEntity taskRuleEntity = new TaskRuleEntity();
        BeanUtils.copyProperties(taskRuleSaveRequest, taskRuleEntity);
        // 设置默认值
        LocalDateTime now = LocalDateTime.now();
        taskRuleEntity.setCt(now);
        taskRuleEntity.setUt(now);

        String jsonConfig = JsonConfig.toJsonConfig(taskRuleSaveRequest.getSettings());
        taskRuleEntity.setConfigJson(jsonConfig);

        if (taskRuleDao.save(taskRuleEntity)) {
            // 绑定任务
            bind(taskRuleEntity.getTaskRuleId(),taskDefinitions);
            return taskRuleEntity.getTaskRuleId();
        }
        throw new BusinessException(BusinessException.OPERATION_FAILED, "创建任务规则失败");
    }

    /**
     * 绑定任务规则
     * @param taskRuleId
     * @param taskDefinitions
     */
    private void bind(Long taskRuleId, List<DolphinTaskDefinition> taskDefinitions) {
        List<TaskSettingEntity> taskSettingEntities = new ArrayList<>(taskDefinitions.size());
        for (DolphinTaskDefinition taskDefinition : taskDefinitions) {
            TaskSettingEntity taskSettingEntity = new TaskSettingEntity();
            taskSettingEntity.setTaskRuleId(taskRuleId);
            taskSettingEntity.setProjectCode(taskDefinition.getProjectCode());
            taskSettingEntity.setTaskCode(taskDefinition.getTaskCode());
            taskSettingEntity.setTaskName(taskDefinition.getTaskName());
            taskSettingEntity.setTaskType(taskDefinition.getTaskType());
            taskSettingEntity.setProjectName(taskDefinition.getProjectName());
            taskSettingEntities.add(taskSettingEntity);
        }
        taskSettingDao.saveBatch(taskSettingEntities);
    }



    public Boolean update(TaskRuleSaveRequest taskRuleSaveRequest) {
        log.info("更新任务配置，请求参数: {}", taskRuleSaveRequest);
        TaskRuleEntity taskRuleEntity = taskRuleDao.getById(taskRuleSaveRequest.getTaskRuleId());
        if (taskRuleEntity == null) {
            throw new BusinessException(BusinessException.INVALID_PARAMETER, "任务规则不存在");
        }
        BeanUtils.copyProperties(taskRuleSaveRequest, taskRuleEntity);
        taskRuleEntity.setConfigJson(JsonConfig.toJsonConfig(taskRuleSaveRequest.getSettings()));
        return taskRuleDao.updateById(taskRuleEntity);
    }

    public Page<TaskRuleResponse> page(TaskRulePageRequest taskRulePageRequest) {
        QueryWrapper queryWrapper = QueryWrapper.create().from(TaskRuleEntity.class);
        //queryWrapper.where(BeanUtil.beanToMap(request));
        // 只添加有效的过滤条件，不包括分页参数
        if (StrUtil.isNotBlank(taskRulePageRequest.getRuleName())) {
            queryWrapper.and(TaskRuleEntity::getRuleName).like(taskRulePageRequest.getRuleName());
        }
        queryWrapper.orderBy(TaskRuleEntity::getCt, false);
//        if (taskRulePageRequest.getWorkflowIdentifier() != null) {
//            queryWrapper.and(TaskRuleEntity::getWorkflowIdentifier).like(taskRulePageRequest.getWorkflowIdentifier());
//        }
//        if (taskRulePageRequest.getTaskIdentifier() != null) {
//            queryWrapper.and(TaskRuleEntity::getTaskIdentifier).like(taskRulePageRequest.getTaskIdentifier());
//        }
        return taskRuleDao
                .pageAs(Page.of(taskRulePageRequest.getPageNo(), taskRulePageRequest.getPageSize()), queryWrapper, TaskRuleResponse.class);
    }

    public Boolean deleteByIds(String taskRuleIds) {
        if (StrUtil.isBlank(taskRuleIds)) {
            throw new BusinessException(BusinessException.INVALID_PARAMETER, "任务规则ID不能为空");
        }
        List<Long> ids = Arrays.stream(taskRuleIds.split(","))
                .map(String::trim)
                .map(idStr -> {
                    try {
                        return Long.parseLong(idStr);
                    } catch (NumberFormatException e) {
                        throw new BusinessException(BusinessException.INVALID_PARAMETER, "任务规则ID格式错误: " + idStr);
                    }
                })
                .collect(Collectors.toList());
        return taskRuleDao.removeByIds(ids);
    }
}
