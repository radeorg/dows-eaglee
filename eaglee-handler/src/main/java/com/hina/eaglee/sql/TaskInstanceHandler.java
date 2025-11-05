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

import java.time.Duration;
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
     * 跟新项目实例状态为已完成,添加任务计数器 累计任务数和总时耗
     * @param taskInstanceSaveRequest
     * @return
     */
    public Boolean updateFinished(TaskInstanceSaveRequest taskInstanceSaveRequest) {
        log.info("更新任务项目：{}", JSONUtil.toJsonStr(taskInstanceSaveRequest));
        // todo 优先计算耗时，然后更新任务状态 耗时 = 结束时间 - 开始时间，耗时单位为毫秒
        if (taskInstanceSaveRequest.getDuration() == null) {
            LocalDateTime startTime = taskInstanceSaveRequest.getStartTime();
            LocalDateTime endTime = taskInstanceSaveRequest.getEndTime();
            Duration duration = Duration.between(startTime, endTime);
            taskInstanceSaveRequest.setDuration(duration.toMillis());
        }
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
            Long currentCount = one.getTaskCount() + 1;
            taskCounterEntity.setTaskCount(currentCount);
            Long expendTotal = one.getExpendTotal() + taskInstanceSaveRequest.getDuration();
            one.setExpendTotal(expendTotal);

            Long taskCount = taskCounterEntity.getTaskCount();
            // 计算平均耗时
            if (taskCount != null && taskCount != 0) {
                // 平均耗时=项目中某类任务耗时总量/项目中某任类任务总量，基于taskCounter动态同步更新
                Long avgTime = expendTotal / taskCount;
                taskInstanceSaveRequest.setAvgTime(avgTime);
            }
            taskCounterDao.updateById(taskCounterEntity);
        } else {
            // 不存在则创建任务计数器 初始化任务数为1
            taskCounterEntity.setTaskCount(1L);
            taskCounterDao.save(taskCounterEntity);
        }

        // 构建实体对象
        TaskInstanceEntity taskInstanceEntity = BeanUtil
                .copyProperties(taskInstanceSaveRequest, TaskInstanceEntity.class);
        // 设置更新时间，todo 可自动更新
        taskInstanceEntity.setUt(LocalDateTime.now());
        // 任务时长
        taskInstanceEntity.setDuration(taskInstanceSaveRequest.getDuration());
        // 保存到数据库
        if (taskInstanceDao.updateById(taskInstanceEntity)) {
            log.info("任务实例更新成功，ID: {}", taskInstanceEntity.getTaskInstanceId());
            // 更新成功后添加任务计数器 累计任务数和总时耗
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
//        if (StrUtil.isNotBlank(request.getTaskIdentifier())) {
//            queryWrapper.and(TaskInstanceEntity::getTaskIdentifier).like(request.getTaskIdentifier());
//        }
//        if (StrUtil.isNotBlank(request.getApplicationId())) {
//            queryWrapper.and(TaskInstanceEntity::getApplicationId).like(request.getApplicationId());
//        }
//        if (StrUtil.isNotBlank(request.getProcessName())) {
//            queryWrapper.and(TaskInstanceEntity::getProcessName).like(request.getProcessName());
//        }
        if (request.getState() != null) {
            queryWrapper.and(TaskInstanceEntity::getState).eq(request.getState());
        }
        if (request.getStartTime() != null) {
            queryWrapper.and(TaskInstanceEntity::getStartTime).ge(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            queryWrapper.and(TaskInstanceEntity::getEndTime).le(request.getEndTime());
        }
        if (request.getDuration() != null) {
            queryWrapper.and(TaskInstanceEntity::getDuration).ge(request.getDuration());
        }
        Page<TaskInstanceResponse> page = taskInstanceDao
                .pageAs(Page.of(request.getPageNumber(), request.getPageSize()), queryWrapper, TaskInstanceResponse.class);

        log.info("任务分页查询成功，当前页: {}, 每页大小: {}, 总页数: {}, 总条数: {}",
                page.getPageNumber(), page.getPageSize(), page.getTotalPage(), page.getTotalRow());
        return page;
    }

    public Boolean retry(Long taskInstanceId) {
        // 根据任务实例ID查询任务实例
        TaskInstanceEntity taskInstanceEntity = taskInstanceDao.getById(taskInstanceId);
        if (taskInstanceEntity == null) {
            throw new BusinessException(BusinessException.OPERATION_FAILED, "任务实例不存在");
        }

        // todo 调用接口重试任务

        taskInstanceEntity.setRetryTimes(taskInstanceEntity.getRetryTimes() + 1);
        // 保存到数据库
        return taskInstanceDao.updateById(taskInstanceEntity);
    }

    /**
     * 根据项目ID查询任务实例
     * @param taskProjectId
     * @return
     */
    public List<TaskInstanceResponse> listTaskByProjectId(String taskProjectId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(TaskInstanceEntity.class)
                .where(TaskInstanceEntity::getProjectCode)
                .eq(taskProjectId);
        return taskInstanceDao.listAs(queryWrapper, TaskInstanceResponse.class);
    }
}
