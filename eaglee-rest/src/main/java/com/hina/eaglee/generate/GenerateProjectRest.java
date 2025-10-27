package com.hina.eaglee.generate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "项目生成", description = "项目生成接口")
@Slf4j
@RequiredArgsConstructor
@RestController
public class GenerateProjectRest {

    @Operation(summary = "保存项目")
    @PutMapping("generate/project/save")
    @PostMapping("generate/project/save")
    public void save() {


    }


    @Operation(summary = "分页查询项目")
    @GetMapping("generate/project/page")
    public void page() {


    }

    @Operation(summary = "根据IDS批量删除项目")
    @DeleteMapping("generate/project/ids")
    public void ids() {


    }

}
