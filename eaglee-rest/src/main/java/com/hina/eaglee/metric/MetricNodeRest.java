package com.hina.eaglee.metric;

import com.hina.eaglee.annotation.Skip;
import com.hina.eaglee.request.TaskRuntimePageRequest;
import com.hina.eaglee.request.TaskRuntimeSaveRequest;
import com.hina.eaglee.response.TaskRuntimeResponse;
import com.hina.eaglee.sql.TaskMetricHandler;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "指标节点", description = "指标节点接口")
@RequiredArgsConstructor
@RestController
public class MetricNodeRest {

    private final TaskMetricHandler taskMetricHandler;

    @Skip
    @Operation(summary = "保存运行时节点指标[cpu, memory, disk, network]")
    @PostMapping("metric/node/runtime")
    public Long saveTaskRuntime(@RequestBody TaskRuntimeSaveRequest request) {
        return taskMetricHandler.save(request);
    }

    @Operation(summary = "分页查询指标节点")
    @GetMapping("/metric/node/page")
    public Page<TaskRuntimeResponse> page(TaskRuntimePageRequest request) {
        return taskMetricHandler.page(request);
    }
}
