package org.dows.eaglee.collect;

import org.dows.eaglee.dolphin.DolphinAlertHandler;
import org.dows.eaglee.request.DolphinAlertRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "运行时", description = "运行时接口")
@RequiredArgsConstructor
@RestController
public class CollectRuntimeRest {

    private final DolphinAlertHandler dolphinAlertHandler;

    @Operation(summary = "收集集群运行时数据")
    @PostMapping("/collect/cluster/runtime")
    public void save(){


    }

    @Operation(summary = "分页查询集群运行时数据")
    @GetMapping("/collect/cluster/page")
    public void page(){

    }


    @Operation(summary = "收集Dolphin告警信息")
    @PostMapping("/collect/dolphin/alert")
    public void alert(@RequestParam("content") String content) {
        dolphinAlertHandler.collect(content);
    }

}
