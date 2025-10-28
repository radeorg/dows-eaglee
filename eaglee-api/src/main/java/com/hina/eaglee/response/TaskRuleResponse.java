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

    @Schema(description = "任务规则ID")
    public Long taskRuleId;

    @Schema(description = "规则名")
    public String ruleName;

    @Schema(description = "json配置")
    public String configJson;

    @Schema(description = "应用类型（0:项目, 1:任务）")
    public Integer referenceType;

    @Schema(description = "引用ID（项目或任务）")
    public Long referenceId;

    @Schema(description = "创建时间")
    public LocalDateTime ct;
    

}