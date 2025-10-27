package com.hina.eaglee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务规则响应类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务规则响应")
public class TaskRuleResponse {
    
    @Schema(description = "任务规则ID", example = "1")
    private Long taskRuleId;
    
    @Schema(description = "规则名称", example = "CPU使用率监控规则")
    private String ruleName;
    
    @Schema(description = "规则类型", example = "MONITOR")
    private String ruleType;
    
    @Schema(description = "规则类型描述", example = "监控规则")
    private String ruleTypeDesc;
    
    @Schema(description = "应用类型：project-项目级，task-任务级", example = "project")
    private String applicationType;
    
    @Schema(description = "应用类型描述", example = "项目级")
    private String applicationTypeDesc;
    
    @Schema(description = "规则条件", example = "cpu_usage > 80")
    private String ruleCondition;
    
    @Schema(description = "规则动作", example = "send_alert")
    private String ruleAction;
    
    @Schema(description = "优先级", example = "10")
    private Integer priority;
    
    @Schema(description = "优先级描述", example = "高优先级")
    private String priorityDesc;
    
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled;
    
    @Schema(description = "启用状态描述", example = "启用")
    private String enabledDesc;
    
    @Schema(description = "创建时间")
    private LocalDateTime ct;
    
    @Schema(description = "更新时间")
    private LocalDateTime ut;
    
    @Schema(description = "创建者ID", example = "1001")
    private Long cid;
    
    /**
     * 获取规则类型描述
     */
    public String getRuleTypeDesc() {
        if (ruleType == null) {
            return "未知";
        }
        return switch (ruleType.toUpperCase()) {
            case "MONITOR" -> "监控规则";
            case "ALERT" -> "告警规则";
            case "COLLECTION" -> "采集规则";
            case "ANALYSIS" -> "分析规则";
            case "BUSINESS" -> "业务规则";
            default -> ruleType;
        };
    }
    
    /**
     * 获取应用类型描述
     */
    public String getApplicationTypeDesc() {
        if (applicationType == null) {
            return "未知";
        }
        return switch (applicationType.toLowerCase()) {
            case "project" -> "项目级";
            case "task" -> "任务级";
            default -> applicationType;
        };
    }
    
    /**
     * 获取优先级描述
     */
    public String getPriorityDesc() {
        if (priority == null) {
            return "未知";
        }
        if (priority >= 80) {
            return "极高优先级";
        } else if (priority >= 60) {
            return "高优先级";
        } else if (priority >= 40) {
            return "中优先级";
        } else if (priority >= 20) {
            return "低优先级";
        } else {
            return "极低优先级";
        }
    }
    
    /**
     * 获取启用状态描述
     */
    public String getEnabledDesc() {
        if (enabled == null) {
            return "未知";
        }
        return enabled ? "启用" : "禁用";
    }
}