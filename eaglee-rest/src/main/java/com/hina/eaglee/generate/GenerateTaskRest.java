package com.hina.eaglee.generate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "任务生成", description = "任务生成接口")
@Slf4j
@RequiredArgsConstructor
@RestController
public class GenerateTaskRest {

    @Operation(summary = "保存任务")
    @PostMapping("generate/task/save")
    public void save() {


    }

    @Operation(summary = "更新任务")
    @PutMapping("generate/task/update")
    public void update() {


    }

}
