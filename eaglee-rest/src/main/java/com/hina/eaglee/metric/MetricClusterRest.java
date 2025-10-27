package com.hina.eaglee.metric;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "指标集群", description = "指标集群接口")
@RequiredArgsConstructor
@RestController
public class MetricClusterRest {

    @Operation(summary = "保存指标集群")
    @PutMapping("metric/cluster/save")
    @PostMapping("metric/cluster/save")
    public void save() {


    }


    @Operation(summary = "分页查询指标集群")
    @GetMapping("metric/cluster/page")
    public void page() {

    }
}
