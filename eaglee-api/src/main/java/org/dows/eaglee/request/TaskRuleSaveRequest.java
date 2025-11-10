package org.dows.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dows.eaglee.setting.TaskRuleSetting;

/**
 * 任务规则保存请求类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务规则保存请求")
public class TaskRuleSaveRequest {

    @Schema(description = "任务规则ID(更新时必填)")
    public Long taskRuleId;

    @Schema(description = "规则名")
    public String ruleName;

    @Schema(description = "json配置")
    public TaskRuleSetting settings;


    @Schema(description = "所属工作流标识", example = "15091097411584")
    public Long projectCode;

//    @Schema(description = "所属任务标识", example = "task_xxx")
//    public String taskIdentifier;
}