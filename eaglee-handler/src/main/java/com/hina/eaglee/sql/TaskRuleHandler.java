package com.hina.eaglee.sql;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hina.eaglee.dao.TaskRuleDao;
import com.hina.eaglee.entity.TaskConfigEntity;
import com.hina.eaglee.entity.TaskRuleEntity;
import com.hina.eaglee.exception.BusinessException;
import com.hina.eaglee.request.TaskRulePageRequest;
import com.hina.eaglee.request.TaskRuleSaveRequest;
import com.hina.eaglee.response.TaskRuleResponse;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskRuleHandler {
    private final TaskRuleDao taskRuleDao;


    public Long save(TaskRuleSaveRequest taskRuleSaveRequest) {

        log.info("保存配置键名：{}", JSONUtil.toJsonStr(taskRuleSaveRequest));
        // 构建实体对象
        TaskRuleEntity taskRuleEntity = new TaskRuleEntity();
        BeanUtils.copyProperties(taskRuleSaveRequest, taskRuleEntity);
        // 设置默认值
        LocalDateTime now = LocalDateTime.now();
        taskRuleEntity.setCt(now);
        taskRuleEntity.setUt(now);
        taskRuleEntity.setDeleted(false);
        // 保存到数据库
        boolean save = taskRuleDao.save(taskRuleEntity);
        if (!save) {
            throw new BusinessException(BusinessException.OPERATION_FAILED, "创建任务规则失败");
        }
        Long taskConfigId = taskRuleEntity.getTaskRuleId();
        log.info("任务规则创建成功，ID: {}", taskConfigId);
        return taskConfigId;
    }

    public Boolean update(TaskRuleSaveRequest taskRuleSaveRequest) {
        log.info("更新任务配置，请求参数: {}", taskRuleSaveRequest);
        TaskRuleEntity taskConfig = taskRuleDao.getById(taskRuleSaveRequest.getTaskRuleId());
        if (taskConfig == null) {
            throw new BusinessException(BusinessException.INVALID_PARAMETER, "任务规则不存在");
        }
        BeanUtils.copyProperties(taskRuleSaveRequest, taskConfig);
        taskConfig.setUt(LocalDateTime.now());
        return taskRuleDao.updateById(taskConfig);
    }

    public Page<TaskRuleResponse> page(TaskRulePageRequest taskRulePageRequest) {
        QueryWrapper queryWrapper = QueryWrapper.create().from(TaskConfigEntity.class);
        //queryWrapper.where(BeanUtil.beanToMap(request));
        // 只添加有效的过滤条件，不包括分页参数
        if (StrUtil.isNotBlank(taskRulePageRequest.getRuleName())) {
            queryWrapper.and(TaskRuleEntity::getRuleName).like(taskRulePageRequest.getRuleName());
        }
        if (taskRulePageRequest.getReferenceType() != null) {
            queryWrapper.and(TaskRuleEntity::getReferenceType).eq(taskRulePageRequest.getReferenceType());
        }
        if (taskRulePageRequest.getReferenceId() != null) {
            queryWrapper.and(TaskRuleEntity::getReferenceId).eq(taskRulePageRequest.getReferenceId());
        }
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
