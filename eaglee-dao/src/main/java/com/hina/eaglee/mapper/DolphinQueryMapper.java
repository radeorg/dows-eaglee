package com.hina.eaglee.mapper;

import com.hina.eaglee.dolphin.DolphinTaskDefinition;
import com.hina.eaglee.request.TaskInstancePageRequest;
import com.hina.eaglee.request.TaskProcessPageRequest;
import com.hina.eaglee.response.TaskInstanceResponse;
import com.hina.eaglee.response.TaskProcessResponse;
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


    List<TaskInstanceResponse> listDolphinTaskInstanceByProcessInstanceId(Integer processInstanceId);

    // 根据
    List<DolphinTaskDefinition> listTaskDefinitionByProjectCodeFromTaskDefinition(Long projectCode);
}
