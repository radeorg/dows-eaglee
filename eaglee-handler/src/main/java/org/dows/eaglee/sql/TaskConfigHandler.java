package org.dows.eaglee.sql;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.dows.eaglee.dao.TaskConfigDao;
import org.dows.eaglee.entity.TaskConfigEntity;
import org.dows.eaglee.exception.BusinessException;
import org.dows.eaglee.request.TaskConfigPageRequest;
import org.dows.eaglee.request.TaskConfigSaveRequest;
import org.dows.eaglee.response.TaskConfigResponse;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskConfigHandler {

    private final TaskConfigDao taskConfigDao;


    @Transactional(rollbackFor = Exception.class)
    public Long createConfigKey(TaskConfigSaveRequest taskConfigSaveRequest) {
        log.info("保存配置键名：{}", JSONUtil.toJsonStr(taskConfigSaveRequest));
        // 构建实体对象
        TaskConfigEntity taskConfig = new TaskConfigEntity();
        BeanUtils.copyProperties(taskConfigSaveRequest, taskConfig);
        // 设置默认值
        LocalDateTime now = LocalDateTime.now();
        taskConfig.setCt(now);
        taskConfig.setUt(now);
        taskConfig.setDeleted(false);


        // 保存到数据库
        boolean save = taskConfigDao.save(taskConfig);
        if (!save) {
            throw new BusinessException(BusinessException.OPERATION_FAILED, "创建任务配置失败");
        }
        Long taskConfigId = taskConfig.getTaskConfigId();
        log.info("任务配置创建成功，ID: {}", taskConfigId);
        return taskConfigId;
    }

    public Page<TaskConfigResponse> pageQuery(TaskConfigPageRequest request) {
        QueryWrapper queryWrapper = QueryWrapper.create().from(TaskConfigEntity.class);
        //queryWrapper.where(BeanUtil.beanToMap(request));
        // 只添加有效的过滤条件，不包括分页参数
        if (StrUtil.isNotBlank(request.getKey())) {
            queryWrapper.and(TaskConfigEntity::getKey).like(request.getKey());
        }
        return taskConfigDao
                .pageAs(Page.of(request.getPageNo(), request.getPageSize()), queryWrapper, TaskConfigResponse.class);
    }


    public boolean deleteByIds(String taskConfigIds) {
        if (StrUtil.isBlank(taskConfigIds)) {
            throw new BusinessException(BusinessException.INVALID_PARAMETER, "任务配置ID不能为空");
        }
        List<Long> ids = Arrays.stream(taskConfigIds.split(","))
                .map(String::trim)
                .map(idStr -> {
                    try {
                        return Long.parseLong(idStr);
                    } catch (NumberFormatException e) {
                        throw new BusinessException(BusinessException.INVALID_PARAMETER, "任务配置ID格式错误: " + idStr);
                    }
                })
                .collect(Collectors.toList());
        return taskConfigDao.removeByIds(ids);
    }

    public boolean updateConfigKey(TaskConfigSaveRequest taskConfigSaveRequest) {
        log.info("更新任务配置，请求参数: {}", taskConfigSaveRequest);
        TaskConfigEntity taskConfig = taskConfigDao.getById(taskConfigSaveRequest.getTaskConfigId());
        if (taskConfig == null) {
            throw new BusinessException(BusinessException.INVALID_PARAMETER, "任务配置不存在");
        }
        BeanUtils.copyProperties(taskConfigSaveRequest, taskConfig);
        taskConfig.setUt(LocalDateTime.now());
        return taskConfigDao.updateById(taskConfig);
    }
}
