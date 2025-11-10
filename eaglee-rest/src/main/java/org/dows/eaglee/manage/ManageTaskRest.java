package org.dows.eaglee.manage;

import org.dows.eaglee.dolphin.DolphinQueryHandler;
import org.dows.eaglee.request.TaskInstancePageRequest;
import org.dows.eaglee.request.TaskInstanceSaveRequest;
import org.dows.eaglee.response.TaskInstanceResponse;
import org.dows.eaglee.sql.TaskInstanceHandler;
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
public class ManageTaskRest {
    private final TaskInstanceHandler taskInstanceHandler;



    @Operation(summary = "保存任务(批量)")
    @PostMapping("manage/task/instances")
    public Boolean saves(@RequestBody List<TaskInstanceSaveRequest> taskInstanceSaveRequests) {
        return taskInstanceHandler.batchSave(taskInstanceSaveRequests);
    }

    @Operation(summary = "保存任务")
    @PostMapping("manage/task/instance")
    public Long save(@RequestBody TaskInstanceSaveRequest taskInstanceSaveRequest) {
        return taskInstanceHandler.save(taskInstanceSaveRequest);
    }

    @Operation(summary = "更新任务")
    @PutMapping("manage/task/instance")
    public Boolean update(@RequestBody TaskInstanceSaveRequest taskInstanceSaveRequest) {
        return taskInstanceHandler.update(taskInstanceSaveRequest);
    }

    @Operation(summary = "重试任务")
    @PutMapping("manage/task/id")
    public Boolean retry(Long taskInstanceId) {
        return taskInstanceHandler.retry(taskInstanceId);
    }


    @Operation(summary = "任务分页查询")
    @GetMapping("manage/task/page")
    public Page<TaskInstanceResponse> page(TaskInstancePageRequest taskInstancePageRequest) {
        return taskInstanceHandler.page(taskInstancePageRequest);
    }





}
