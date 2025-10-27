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
@Tag(name = "指标节点", description = "指标节点接口")
@RequiredArgsConstructor
@RestController
public class MetricNodeRest {

    @Operation(summary = "保存指标节点")
    @PutMapping("/metric/node/update")
    @PostMapping("/metric/node/save")
    public void save() {


    }


    @Operation(summary = "分页查询指标节点")
    @GetMapping("/metric/node/page")
    public void page() {


    }
}
