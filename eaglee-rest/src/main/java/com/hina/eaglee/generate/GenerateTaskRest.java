package com.hina.eaglee.generate;

import com.hina.eaglee.request.TaskInstancePageRequest;
import com.hina.eaglee.request.TaskInstanceSaveRequest;
import com.hina.eaglee.request.TaskProjectPageRequest;
import com.hina.eaglee.response.TaskInstanceResponse;
import com.hina.eaglee.response.TaskProjectResponse;
import com.hina.eaglee.sql.TaskInstanceHandler;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "任务", description = "任务接口")
@Slf4j
@RequiredArgsConstructor
@RestController
public class GenerateTaskRest {
    private final TaskInstanceHandler taskInstanceHandler;

    @Operation(summary = "保存任务(批量)")
    @PostMapping("generate/task/instances")
    public Boolean saves(@RequestBody List<TaskInstanceSaveRequest> taskInstanceSaveRequests) {
        return taskInstanceHandler.batchSave(taskInstanceSaveRequests);
    }

    @Operation(summary = "保存任务(批量)")
    @PostMapping("generate/task/instance")
    public Long save(@RequestBody TaskInstanceSaveRequest taskInstanceSaveRequest) {
        return taskInstanceHandler.save(taskInstanceSaveRequest);
    }



    @Operation(summary = "更新任务")
    @PutMapping("generate/task/instance")
    public Boolean update(@RequestBody TaskInstanceSaveRequest taskInstanceSaveRequest) {
        return taskInstanceHandler.update(taskInstanceSaveRequest);
    }


    @Operation(summary = "分页查询任务")
    @GetMapping("generate/task/page")
    public Page<TaskInstanceResponse> page(TaskInstancePageRequest taskInstancePageRequest) {
        return taskInstanceHandler.page(taskInstancePageRequest);
    }

}
