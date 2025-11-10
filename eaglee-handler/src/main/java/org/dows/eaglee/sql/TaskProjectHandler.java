package org.dows.eaglee.sql;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.dows.eaglee.dao.DolphinProcessDefinitionDao;
import org.dows.eaglee.dao.DolphinProjectDao;
import org.dows.eaglee.dao.DolphinTaskRelationDao;
import org.dows.eaglee.dao.TaskProjectDao;
import org.dows.eaglee.entity.DolphinProcessDefinitionEntity;
import org.dows.eaglee.entity.DolphinProjectEntity;
import org.dows.eaglee.entity.TaskProjectEntity;
import org.dows.eaglee.exception.BusinessException;
import org.dows.eaglee.request.TaskProcessPageRequest;
import org.dows.eaglee.request.TaskProcessSaveRequest;
import org.dows.eaglee.response.TaskProcessResponse;
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
public class TaskProjectHandler {
    private final TaskProjectDao taskProjectDao;

    private final DolphinProjectDao dolphinProjectDao;
    private final DolphinProcessDefinitionDao dolphinProcessDefinitionDao;
    private final DolphinTaskRelationDao dolphinTaskRelationDao;

    /**
     * 同步DS项目信息
     */
    public void sync() {


        // 查询所有DolphinProject和DolphinTaskRelation数据
        List<DolphinProjectEntity> dolphinProjects = dolphinProjectDao.list();
        List<Long> projectCodes = dolphinProjects.stream().map(DolphinProjectEntity::getCode).toList();

        QueryWrapper queryWrapper = QueryWrapper.create().from(DolphinProcessDefinitionEntity.class)
                .and(DolphinProcessDefinitionEntity::getProjectCode).in(projectCodes);
        List<DolphinProcessDefinitionEntity> dolphinProcessDefinitionEntities = dolphinProcessDefinitionDao.list(queryWrapper);



        TaskProjectEntity taskProject = new TaskProjectEntity();
        boolean save = taskProjectDao.save(taskProject);

    }

    public Long save(TaskProcessSaveRequest taskProjectSaveRequest) {

        log.info("保存任务项目：{}", JSONUtil.toJsonStr(taskProjectSaveRequest));
        // 构建实体对象
        TaskProjectEntity taskProject = new TaskProjectEntity();
        BeanUtils.copyProperties(taskProjectSaveRequest, taskProject);
        // 设置默认值
        LocalDateTime now = LocalDateTime.now();
        taskProject.setCt(now);
        taskProject.setUt(now);
        taskProject.setDeleted(false);
        // 保存到数据库
        boolean save = taskProjectDao.save(taskProject);
        if (!save) {
            throw new BusinessException(BusinessException.OPERATION_FAILED, "创建任务项目失败");
        }
        Long taskProjectId = taskProject.getTaskProjectId();
        log.info("任务项目创建成功，ID: {}", taskProjectId);
        return taskProjectId;
    }

    public Boolean update(TaskProcessSaveRequest taskProjectSaveRequest) {
        log.info("更新任务项目：{}", JSONUtil.toJsonStr(taskProjectSaveRequest));
        // 构建实体对象
        TaskProjectEntity taskProject = new TaskProjectEntity();
        BeanUtils.copyProperties(taskProjectSaveRequest, taskProject);
        // 设置默认值
        LocalDateTime now = LocalDateTime.now();
        taskProject.setUt(now);
        // 保存到数据库
        boolean update = taskProjectDao.updateById(taskProject);
        if (!update) {
            throw new BusinessException(BusinessException.OPERATION_FAILED, "更新任务项目失败");
        }
        log.info("任务项目更新成功，ID: {}", taskProject.getTaskProjectId());
        return true;
    }

    public Page<TaskProcessResponse> page(TaskProcessPageRequest request) {
        log.info("分页查询任务项目：{}", JSONUtil.toJsonStr(request));
        QueryWrapper queryWrapper = QueryWrapper.create().from(TaskProjectEntity.class);
        // 只添加有效的过滤条件，不包括分页参数
        if (StrUtil.isNotBlank(request.getProcessInstanceName())) {
            queryWrapper.and(TaskProjectEntity::getProjectName).like(request.getProcessInstanceName());
        }
//        if (StrUtil.isNotBlank(request.getProcessCode())) {
//            queryWrapper.and(TaskProjectEntity::getProcessCode).like(request.getProcessCode());
//        }
//        if (StrUtil.isNotBlank(request.getProjectIdentifier())) {
//            queryWrapper.and(TaskProjectEntity::getProjectIdentifier).like(request.getProjectIdentifier());
//        }
        if (request.getState() != null) {
            queryWrapper.and(TaskProjectEntity::getState).eq(request.getState());
        }
        if (request.getStartTime() != null) {
            queryWrapper.and(TaskProjectEntity::getStartTime).ge(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            queryWrapper.and(TaskProjectEntity::getEndTime).le(request.getEndTime());
        }
        Page<TaskProcessResponse> page = taskProjectDao
                .pageAs(Page.of(request.getPageNumber(), request.getPageSize()), queryWrapper, TaskProcessResponse.class);

        log.info("任务项目分页查询成功，当前页: {}, 每页大小: {}, 总页数: {}, 总条数: {}",
                page.getPageNumber(), page.getPageSize(), page.getTotalPage(), page.getTotalRow());
        return page;
    }

    public Boolean deleteByIds(String taskProjectIds) {
        if (StrUtil.isBlank(taskProjectIds)) {
            throw new BusinessException(BusinessException.INVALID_PARAMETER, "任务项目ID不能为空");
        }
        List<Long> ids = Arrays.stream(taskProjectIds.split(","))
                .map(String::trim)
                .map(idStr -> {
                    try {
                        return Long.parseLong(idStr);
                    } catch (NumberFormatException e) {
                        throw new BusinessException(BusinessException.INVALID_PARAMETER, "任务项目ID格式错误: " + idStr);
                    }
                })
                .collect(Collectors.toList());
        return taskProjectDao.removeByIds(ids);
    }
}
