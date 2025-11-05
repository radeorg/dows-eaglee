package com.hina.eaglee.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("task_process")
@Schema(description = "任务流程数据")
public class TaskProcessEntity {

    @Id(keyType = KeyType.Auto)
    @Schema(description = "任务流程ID")
    private Long taskProcessId;

    @Schema(description = "项目编码")
    private Long projectCode;

    @Schema(description = "项目名称")
    private String projectName;


    @JsonProperty("processId")
    @Schema(description = "流程ID")
    private Long processInstanceId;

    @Schema(description = "流程编码")
    private String processCode;

    @Schema(description = "流程名称")
    private String processName;

    @Schema(description = "流程类型")
    private String processType;

    @Schema(description = "流程状态")
    private String processState;

    @Schema(description = "流程定义编码")
    private Long processDefinitionCode;

    @Schema(description = "所有者")
    private String owner;


    @Schema(description = "运行次数")
    private Integer runTimes;


    @Schema(description = "流程开始时间")
    private LocalDateTime processStartTime;


    @Schema(description = "流程结束时间")
    private LocalDateTime processEndTime;


    @Schema(description = "流程执行主机")
    private String processHost;


    @Schema(description = "流程持续时间")
    private String processDuration;


    @Schema(description = "任务数量")
    private Integer taskCount;

    @Schema(description = "状态：0-未完成，1-已完成")
    private Integer state;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "创建时间")
    private LocalDateTime ct;

    @Schema(description = "更新时间")
    private LocalDateTime ut;

    @Schema(description = "创建者ID")
    private Long cid;

    @Schema(description = "是否删除")
    private Boolean deleted;

//    @Schema(description = "乐观锁版本")
//    private Integer version;

}
