package com.hina.eaglee.config;

import cn.hutool.json.JSONUtil;
import com.hina.eaglee.request.TaskRulePageRequest;
import com.hina.eaglee.request.TaskRuleSaveRequest;
import com.hina.eaglee.response.TaskConfigResponse;
import com.hina.eaglee.response.TaskRuleResponse;
import com.hina.eaglee.sql.TaskRuleHandler;
import com.mybatisflex.core.paginate.Page;
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

    private final TaskRuleHandler taskRuleHandler;
    @Operation(summary = "保存规则")
    @PostMapping("config/rule/instance")
    public Long save(@RequestBody TaskRuleSaveRequest taskRuleSaveRequest) {
        return taskRuleHandler.saveAndBind(taskRuleSaveRequest);
    }

    @Operation(summary = "更新规则")
    @PutMapping("config/rule/instance")
    public Boolean update(@RequestBody TaskRuleSaveRequest taskRuleSaveRequest){
        return taskRuleHandler.update(taskRuleSaveRequest);
    }


    @Operation(summary = "分页查询规则")
    @GetMapping("config/rule/page")
    public Page<TaskRuleResponse> page(TaskRulePageRequest taskRulePageRequest){
        return taskRuleHandler.page(taskRulePageRequest);
    }

    @Operation(summary = "删除规则")
    @DeleteMapping("config/rule/ids")
    public Boolean delete(@RequestParam("taskRuleIds") String taskRuleIds){
        return taskRuleHandler.deleteByIds(taskRuleIds);
    }

//    @Operation(summary = "批量删除规则(逻辑)")
//    @PutMapping("config/rule/ids")
//    public void remove(){
//
//
//    }

}
