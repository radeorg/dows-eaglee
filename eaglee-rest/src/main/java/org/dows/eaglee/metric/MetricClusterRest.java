package org.dows.eaglee.metric;

import org.dows.eaglee.request.TaskMetricAnalyseRequest;
import org.dows.eaglee.request.TaskMetricQueryRequest;
import org.dows.eaglee.response.TaskMetricQueryResponse;
import org.dows.eaglee.sql.TaskMetricHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "指标集群", description = "指标集群接口")
@RequiredArgsConstructor
@RestController
public class MetricClusterRest {

    private final TaskMetricHandler taskMetricHandler;
    /*@Operation(summary = "保存指标集群")
    @PutMapping("metric/cluster/save")
    @PostMapping("metric/cluster/save")
    public void save() {

    }

    @Operation(summary = "分页查询指标集群")
    @GetMapping("metric/cluster/page")
    public void page() {

    }*/

    @Operation(summary = "集群指标查询")
    @GetMapping("metric/cluster/instance")
    public List<TaskMetricQueryResponse> query(TaskMetricQueryRequest request) {
        return taskMetricHandler.aggregate(request);
    }


    @Operation(summary = "集群指标分析")
    @GetMapping("metric/cluster/status")
    public List<TaskMetricQueryResponse> analyse(TaskMetricAnalyseRequest request) {
        return taskMetricHandler.analyse(request);
    }
}
