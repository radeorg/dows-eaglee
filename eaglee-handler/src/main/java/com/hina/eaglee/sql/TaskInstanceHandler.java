package com.hina.eaglee.sql;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hina.eaglee.dao.TaskCounterDao;
import com.hina.eaglee.dao.TaskInstanceDao;
import com.hina.eaglee.entity.TaskConfigEntity;
import com.hina.eaglee.entity.TaskCounterEntity;
import com.hina.eaglee.entity.TaskInstanceEntity;
import com.hina.eaglee.entity.TaskProjectEntity;
import com.hina.eaglee.exception.BusinessException;
import com.hina.eaglee.request.TaskInstancePageRequest;
import com.hina.eaglee.request.TaskInstanceSaveRequest;
import com.hina.eaglee.response.TaskInstanceResponse;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskInstanceHandler {
    private final TaskInstanceDao taskInstanceDao;
    private final TaskCounterDao taskCounterDao;

    @Transactional(rollbackFor = Exception.class)
    public Long save(TaskInstanceSaveRequest taskInstanceSaveRequest) {
        TaskInstanceEntity taskInstanceEntity = BeanUtil
                .copyProperties(taskInstanceSaveRequest, TaskInstanceEntity.class);


        boolean save = taskInstanceDao.save(taskInstanceEntity);
        if (save) {
            //Long taskInstanceId = taskInstanceEntity.getTaskInstanceId();
            TaskCounterEntity taskCounterEntity = new TaskCounterEntity();

            taskCounterEntity.setTaskIdentifier(taskInstanceSaveRequest.getTaskIdentifier());
            taskCounterEntity.setProjectIdentifier(taskInstanceSaveRequest.getProjectIdentifier());
            // 根据项目标识和任务标识查询任务计数器是否存在
            TaskCounterEntity one = taskCounterDao.getOne(QueryWrapper.create()
                    .from(TaskCounterEntity.class)
                    .where(TaskCounterEntity::getTaskIdentifier)
                    .eq(taskInstanceSaveRequest.getTaskIdentifier())
                    .and(TaskCounterEntity::getProjectIdentifier)
                    .eq(taskInstanceSaveRequest.getProjectIdentifier()));
            if (one != null) {
                // 存在则更新任务计数器 累计任务数
                taskCounterEntity.setTaskCount(one.getTaskCount() + 1);
            } else {
                // 不存在则创建任务计数器 初始化任务数为1
                taskCounterEntity.setTaskCount(1L);
            }
            taskCounterDao.save(taskCounterEntity);
            return taskInstanceEntity.getTaskInstanceId();
        }
        throw new BusinessException(BusinessException.OPERATION_FAILED, "保存任务实例失败");
    }

    public Boolean update(TaskInstanceSaveRequest taskInstanceSaveRequest) {
        log.info("更新任务项目：{}", JSONUtil.toJsonStr(taskInstanceSaveRequest));
        // 构建实体对象
        TaskInstanceEntity taskProjectEntity = BeanUtil
                .copyProperties(taskInstanceSaveRequest, TaskInstanceEntity.class);
        // 设置默认值
        LocalDateTime now = LocalDateTime.now();
        taskProjectEntity.setUt(now);
        // 保存到数据库
        if (taskInstanceDao.updateById(taskProjectEntity)) {
            log.info("任务实例更新成功，ID: {}", taskProjectEntity.getTaskInstanceId());
            return true;
        }
        throw new BusinessException(BusinessException.OPERATION_FAILED, "更新任务实例失败");
    }


    /**
     * 跟新项目失败
     * @param taskInstanceSaveRequest
     * @return
     */
    public Boolean updateFailure(TaskInstanceSaveRequest taskInstanceSaveRequest) {
        log.info("更新任务项目：{}", JSONUtil.toJsonStr(taskInstanceSaveRequest));
        // 构建实体对象
        TaskInstanceEntity taskProjectEntity = BeanUtil
                .copyProperties(taskInstanceSaveRequest, TaskInstanceEntity.class);
        // 设置默认值
        LocalDateTime now = LocalDateTime.now();
        taskProjectEntity.setUt(now);
        // 保存到数据库
        if (taskInstanceDao.updateById(taskProjectEntity)) {
            log.info("任务实例更新成功，ID: {}", taskProjectEntity.getTaskInstanceId());
            return true;
        }
        throw new BusinessException(BusinessException.OPERATION_FAILED, "更新任务实例失败");
    }

    public Boolean batchSave(List<TaskInstanceSaveRequest> taskInstanceSaveRequests) {
        List<TaskInstanceEntity> taskInstanceEntities = BeanUtil
                .copyToList(taskInstanceSaveRequests, TaskInstanceEntity.class);
        return taskInstanceDao.saveBatch(taskInstanceEntities);
    }

    public Page<TaskInstanceResponse> page(TaskInstancePageRequest request) {
        log.info("分页查询任务：{}", JSONUtil.toJsonStr(request));
        QueryWrapper queryWrapper = QueryWrapper.create().from(TaskProjectEntity.class);
        // 只添加有效的过滤条件，不包括分页参数
        if (StrUtil.isNotBlank(request.getTaskName())) {
            queryWrapper.and(TaskConfigEntity::getKey).like(request.getTaskName());
        }
        Page<TaskInstanceResponse> page = taskInstanceDao
                .pageAs(Page.of(request.getCurrent(), request.getSize()), queryWrapper, TaskInstanceResponse.class);

        log.info("任务分页查询成功，当前页: {}, 每页大小: {}, 总页数: {}, 总条数: {}",
                page.getPageNumber(), page.getPageSize(), page.getTotalPage(), page.getTotalRow());
        return page;
    }
}
