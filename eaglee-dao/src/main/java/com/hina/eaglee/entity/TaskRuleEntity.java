package com.hina.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务规则实体类
 * 
 * @author eaglee-system
 */
@Data
@Table("task_rule")
@Schema(description = "任务规则")
public class TaskRuleEntity {
    
    @Id(keyType = KeyType.Auto)
    @Schema(description = "任务规则ID")
    private Long taskRuleId;
    
    @Schema(description = "规则名称")
    private String ruleName;
    
    @Schema(description = "规则类型")
    private String ruleType;
    
    @Schema(description = "应用类型：project-项目级，task-任务级")
    private String applicationType;
    
    @Schema(description = "规则条件")
    private String ruleCondition;
    
    @Schema(description = "规则动作")
    private String ruleAction;
    
    @Schema(description = "优先级")
    private Integer priority;
    
    @Schema(description = "是否启用")
    private Boolean enabled;
    
    @Schema(description = "创建时间")
    private LocalDateTime ct;
    
    @Schema(description = "更新时间")
    private LocalDateTime ut;
    
    @Schema(description = "创建者ID")
    private Long cid;
    
    @Schema(description = "是否删除")
    private Boolean deleted;
}