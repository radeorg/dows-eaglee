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
    public Long taskRuleId;

    @Schema(description = "规则名")
    public String ruleName;

    @Schema(description = "json配置")
    public String configJson;

//    @Schema(description = "所属工作流标识", example = "workflow_xxx")
//    public String workflowIdentifier;
//
//    @Schema(description = "所属任务标识", example = "task_xxx")
//    public String taskIdentifier;

    @Schema(description = "创建时间")
    public LocalDateTime ct;

    @Schema(description = "更新时间")
    public LocalDateTime ut;

    @Schema(description = "创建者ID")
    public Long cid;

    @Schema(description = "更新者ID")
    public Long uid;

    @Schema(description = "删除标记")
    public Boolean deleted;

    @Schema(description = "乐观锁版本")
    public Integer version;
}