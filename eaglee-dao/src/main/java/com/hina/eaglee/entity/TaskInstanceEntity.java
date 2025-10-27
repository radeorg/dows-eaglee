package com.hina.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务实例实体类
 * 
 * @author eaglee-system
 */
@Data
@Table("task_instance")
@Schema(description = "任务实例")
public class TaskInstanceEntity {
    
    @Id(keyType = KeyType.Auto)
    @Schema(description = "任务实例ID")
    private Long taskInstanceId;
    
    @Schema(description = "任务项目ID")
    private Long taskProjectId;
    
    @Schema(description = "任务名称")
    private String taskName;
    
    @Schema(description = "任务标识")
    private String taskIdentifier;
    
    @Schema(description = "应用ID")
    private String applicationId;
    
    @Schema(description = "流程实例名称")
    private String processName;
    
    @Schema(description = "原因")
    private String reason;
    
    @Schema(description = "平均耗时")
    private Long avgTime;
    
    @Schema(description = "当前耗时")
    private Long elapsedTime;
    
    @Schema(description = "时长")
    private Long duration;
    
    @Schema(description = "重试次数")
    private Integer retried;
    
    @Schema(description = "状态")
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
}