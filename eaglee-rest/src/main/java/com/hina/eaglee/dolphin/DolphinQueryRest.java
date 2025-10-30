package com.hina.eaglee.dolphin;

import com.hina.eaglee.request.TaskInstancePageRequest;
import com.hina.eaglee.request.TaskProcessPageRequest;
import com.hina.eaglee.response.TaskInstanceResponse;
import com.hina.eaglee.response.TaskProcessResponse;
import com.hina.eaglee.response.TaskProjectResponse;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "DolphinScheduler接口")
public class DolphinQueryRest {


    private final DolphinQueryHandler dolphinQueryHandler;

    @Operation(summary = "项目分页")
    @GetMapping("/dolphin/process/page")
    public Page<TaskProcessResponse> pageProject(TaskProcessPageRequest taskProjectPageRequest) {
        return dolphinQueryHandler.pageDolphinProjectInstance(taskProjectPageRequest);
    }

    @Operation(summary = "任务实例列表")
    @GetMapping("/dolphin/process/task")
    public List<TaskInstanceResponse> listTaskByProcessInstanceId(@RequestParam("processInstanceId") Long processInstanceId) {
        return dolphinQueryHandler.listDolphinTaskInstanceByProcessInstanceId(processInstanceId);
    }


    @Operation(summary = "任务分页")
    @GetMapping("/dolphin/task/page")
    public Page<TaskInstanceResponse> pageTask(TaskInstancePageRequest taskInstancePageRequest) {
        return dolphinQueryHandler.pageDolphinTaskInstance(taskInstancePageRequest);
    }

    @Operation(summary = "根据名称查询项目列表")
    @GetMapping("/dolphin/project/list")
    public List<TaskProjectResponse> pageTaskInstance(@RequestParam("projectName") String projectName) {
        return dolphinQueryHandler.listDolphinProject(projectName);
    }
}
