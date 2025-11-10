package org.dows.eaglee.mapper;

import org.dows.eaglee.dolphin.DolphinTaskDefinition;
import org.dows.eaglee.request.TaskInstancePageRequest;
import org.dows.eaglee.request.TaskProcessPageRequest;
import org.dows.eaglee.response.TaskInstanceResponse;
import org.dows.eaglee.response.TaskProcessResponse;
import org.dows.eaglee.response.TaskProjectResponse;
import com.mybatisflex.annotation.UseDataSource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@UseDataSource("dolphinscheduler")
@Mapper
public interface DolphinQueryMapper {

    // 根据条件查询流程实例总数
    Integer queryProcessInstanceForCount(TaskProcessPageRequest request);

    // 根据条件查询流程分页
    List<TaskProcessResponse> queryProcessInstanceForPage(TaskProcessPageRequest request);

    // 根据条件查询任务实例总数
    Integer queryTaskInstanceForCount(TaskInstancePageRequest request);

    // 根据条件查询任务实例分页
    List<TaskInstanceResponse> queryTaskInstanceForPage(TaskInstancePageRequest request);

    // 根据流程实例ID查询任务实例
    List<TaskInstanceResponse> listDolphinTaskInstanceByProcessInstanceId(Long processInstanceId);

    // 根据项目code标识查询任务定义
    List<DolphinTaskDefinition> listTaskDefinitionByProjectCodeFromTaskDefinition(Long projectCode);

    List<TaskProjectResponse> findProjectByProjectName(String projectName);

    List<TaskInstanceResponse> listTaskByProcessInstanceId(Long processInstanceId);
}
