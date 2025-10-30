package com.hina.eaglee.manage;

import com.hina.eaglee.dolphin.DolphinQueryHandler;
import com.hina.eaglee.request.TaskInstancePageRequest;
import com.hina.eaglee.request.TaskProcessPageRequest;
import com.hina.eaglee.response.TaskInstanceResponse;
import com.hina.eaglee.response.TaskProcessResponse;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "DolphinScheduler统计接口")
public class DolphinQueryRest {


    private final DolphinQueryHandler dolphinQueryHandler;

    @Operation(summary = "项目分页")
    @GetMapping("/dolphin/process/page")
    public Page<TaskProcessResponse> pageProject(TaskProcessPageRequest taskProjectPageRequest) {
        return dolphinQueryHandler.pageDolphinProjectInstance(taskProjectPageRequest);
    }


    @Operation(summary = "任务分页")
    @GetMapping("/dolphin/task/page")
    public Page<TaskInstanceResponse> pageTask(TaskInstancePageRequest taskInstancePageRequest) {
        return dolphinQueryHandler.pageDolphinTaskInstance(taskInstancePageRequest);
    }

}
