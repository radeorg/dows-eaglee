package com.hina.eaglee.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "规则", description = "配置规则接口")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ConfigRuleRest {

    @Operation(summary = "保存规则")
    @PostMapping("config/rule/save")
    public void save(){


    }

    @Operation(summary = "更新规则")
    @PutMapping("config/rule/update")
    public void update(){


    }

    @Operation(summary = "分页查询规则")
    @PostMapping("config/rule/page")
    public void page(){


    }

    @Operation(summary = "删除规则")
    @DeleteMapping("config/rule/delete")
    public void delete(){


    }

    @Operation(summary = "批量删除规则")
    @DeleteMapping("config/rule/remove")
    public void remove(){


    }

}
