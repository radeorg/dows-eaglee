package org.dows.eaglee.response;

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

    @Schema(description = "所属工作流标识", example = "15091097411584")
    public Long projectCode;

    @Schema(description = "json配置")
    public String configJson;

//    @Schema(description = "项目标识")
//    public String workflowIdentifier;
//
//    @Schema(description = "任务标识")
//    public String taskIdentifier;

}