package com.hina.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

/**
 * 任务规则保存请求类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务规则保存请求")
public class TaskRuleSaveRequest {
    
    @Schema(description = "任务规则ID（更新时需要）")
    private Long taskRuleId;
    
    @NotBlank(message = "规则名称不能为空")
    @Size(max = 100, message = "规则名称长度不能超过100个字符")
    @Schema(description = "规则名称", required = true, example = "CPU使用率监控规则")
    private String ruleName;
    
    @NotBlank(message = "规则类型不能为空")
    @Size(max = 20, message = "规则类型长度不能超过20个字符")
    @Schema(description = "规则类型", required = true, example = "MONITOR")
    private String ruleType;
    
    @NotBlank(message = "应用类型不能为空")
    @Pattern(regexp = "^(project|task)$", message = "应用类型只能是project或task")
    @Schema(description = "应用类型：project-项目级，task-任务级", required = true, example = "project")
    private String applicationType;
    
    @NotBlank(message = "规则条件不能为空")
    @Size(max = 1000, message = "规则条件长度不能超过1000个字符")
    @Schema(description = "规则条件", required = true, example = "cpu_usage > 80")
    private String ruleCondition;
    
    @NotBlank(message = "规则动作不能为空")
    @Size(max = 1000, message = "规则动作长度不能超过1000个字符")
    @Schema(description = "规则动作", required = true, example = "send_alert")
    private String ruleAction;
    
    @NotNull(message = "优先级不能为空")
    @Min(value = 1, message = "优先级必须大于0")
    @Max(value = 100, message = "优先级不能超过100")
    @Schema(description = "优先级", required = true, example = "10")
    private Integer priority;
    
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled = true;
}