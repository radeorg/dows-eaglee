package com.hina.eaglee.sql;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.dows.eaglee.api.dto.request.TaskConfigPageRequest;
import com.dows.eaglee.api.dto.request.TaskConfigSaveRequest;
import com.dows.eaglee.api.dto.response.TaskConfigResponse;
import com.hina.eaglee.dao.TaskConfigDao;
import com.hina.eaglee.entity.TaskConfigEntity;
import com.hina.eaglee.exception.BusinessException;
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
    public Long createConfigKey(TaskConfigSaveRequest request) {
        log.info("创建任务配置，请求参数: {}", request);
        // 构建实体对象
        TaskConfigEntity taskConfig = new TaskConfigEntity();
        BeanUtils.copyProperties(request, taskConfig);
        // 设置默认值
        LocalDateTime now = LocalDateTime.now();
        taskConfig.setCt(now);
        taskConfig.setUt(now);
        taskConfig.setDeleted(false);

        if (taskConfig.getEnabled() == null) {
            taskConfig.setEnabled(true);
        }
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
        QueryWrapper queryWrapper = QueryWrapper.create(TaskConfigEntity.class).where(BeanUtil.beanToMap(request));
        return taskConfigDao
                .pageAs(Page.of(request.getCurrent(), request.getSize()), queryWrapper, TaskConfigResponse.class);
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

}
