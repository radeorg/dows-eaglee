package com.hina.eaglee.generate;

import com.hina.eaglee.request.TaskProjectPageRequest;
import com.hina.eaglee.request.TaskProjectSaveRequest;
import com.hina.eaglee.response.TaskProjectResponse;
import com.hina.eaglee.sql.TaskProjectHandler;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "项目", description = "项目接口")
@Slf4j
@RequiredArgsConstructor
@RestController
public class GenerateProjectRest {

    private final TaskProjectHandler taskProjectHandler;

    @Operation(summary = "保存项目")
    @PostMapping("generate/project/save")
    public Long save(@RequestBody TaskProjectSaveRequest taskProjectSaveRequest) {
        return taskProjectHandler.save(taskProjectSaveRequest);
    }


    @Operation(summary = "保存项目")
    @PutMapping("generate/project/save")
    public Boolean update(@RequestBody TaskProjectSaveRequest taskProjectSaveRequest) {
        return taskProjectHandler.update(taskProjectSaveRequest);
    }

    @Operation(summary = "分页查询项目")
    @GetMapping("generate/project/page")
    public Page<TaskProjectResponse> page(TaskProjectPageRequest taskProjectPageRequest) {
        return taskProjectHandler.page(taskProjectPageRequest);
    }

    @Operation(summary = "根据IDS批量删除项目")
    @DeleteMapping("generate/project/ids")
    public Boolean ids(String taskProjectIds) {
        return taskProjectHandler.deleteByIds(taskProjectIds);


    }

}
