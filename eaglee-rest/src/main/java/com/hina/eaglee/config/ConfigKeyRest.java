package com.hina.eaglee.config;

import cn.hutool.json.JSONUtil;
import com.hina.eaglee.request.TaskConfigSaveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Tag(name = "配置键名", description = "配置键名接口")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ConfigKeyRest {


    @Operation(summary = "保存配置键名")
    @PostMapping("/config/key/save")
    public void save(@RequestBody TaskConfigSaveRequest taskConfigSaveRequest) {
        log.info("保存配置键名：{}", JSONUtil.toJsonStr(taskConfigSaveRequest));


    }

    @Operation(summary = "更新配置键名")
    @PutMapping("/config/key/update")
    public void update() {


    }

    @Operation(summary = "分页查询配置键名")
    @PostMapping("/config/key/page")
    public void page() {


    }

    @Operation(summary = "删除配置键名")
    @DeleteMapping("/config/key/delete")
    public void delete() {


    }
}
