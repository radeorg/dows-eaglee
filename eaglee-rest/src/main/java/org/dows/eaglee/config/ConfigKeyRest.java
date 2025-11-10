package org.dows.eaglee.config;

import cn.hutool.json.JSONUtil;
import org.dows.eaglee.request.TaskConfigPageRequest;
import org.dows.eaglee.request.TaskConfigSaveRequest;
import org.dows.eaglee.response.TaskConfigResponse;
import org.dows.eaglee.sql.TaskConfigHandler;
import com.mybatisflex.core.paginate.Page;
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

    private final TaskConfigHandler taskConfigHandler;

    @Operation(summary = "保存配置键名")
    @PostMapping("/config/key/instance")
    public Long save(@RequestBody TaskConfigSaveRequest taskConfigSaveRequest) {
        return taskConfigHandler.createConfigKey(taskConfigSaveRequest);
    }

    @Operation(summary = "更新配置键名")
    @PutMapping("/config/key/instance")
    public Boolean update(@RequestBody TaskConfigSaveRequest taskConfigSaveRequest) {
        log.info("更新配置键名：{}", JSONUtil.toJsonStr(taskConfigSaveRequest));
        return taskConfigHandler.updateConfigKey(taskConfigSaveRequest);
    }

    @Operation(summary = "分页查询配置键名")
    @GetMapping("/config/key/page")
    public Page<TaskConfigResponse> page(TaskConfigPageRequest taskConfigPageRequest) {
        return taskConfigHandler.pageQuery(taskConfigPageRequest);
    }

    @Operation(summary = "删除配置键名()")
    @DeleteMapping("/config/key/ids")
    public Boolean delete(@RequestParam("taskConfigIds") String taskConfigIds) {
        return taskConfigHandler.deleteByIds(taskConfigIds);
    }
}
