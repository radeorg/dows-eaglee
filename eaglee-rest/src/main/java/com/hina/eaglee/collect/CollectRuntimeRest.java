package com.hina.eaglee.collect;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "运行时", description = "运行时接口")
@RequiredArgsConstructor
@RestController
public class CollectRuntimeRest {
    @Operation(summary = "保存运行时数据")
    @PostMapping("/collect/runtime/save")
    public void save(){


    }

    @Operation(summary = "分页查询运行时数据")
    @GetMapping("/collect/runtime/page")
    public void page(){


    }

}
