package org.dows.eaglee.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务设置实体
 */
@Schema(description = "任务设置")
@Data
@Table("task_setting")
public class TaskSettingEntity {

    @Schema(description = "任务设置ID")
    public Long taskSettingId;

    @Schema(description = "任务规则ID")
    public Long taskRuleId;

    //    @Schema(description = "流程标识")
//    public String workflowIdentifier;
//
//    @Schema(description = "同一类任务的标识")
//    public String taskIdentifier;
//
//    @Schema(description = "code标识码（projectIdentifier_taskIdentifier）")
//    public String codeIdentifier;
    @Schema(description = "项目code")
    private Long projectCode;
    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "任务code")
    private Long taskCode;
    @Schema(description = "任务名称")
    private String taskName;
    @Schema(description = "任务类型")
    private String taskType;




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