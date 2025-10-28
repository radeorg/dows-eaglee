package com.hina.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 * 任务实例保存请求类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务实例保存请求")
public class TaskInstanceSaveRequest {
    
    @Schema(description = "任务实例ID（更新时需要）")
    private Long taskInstanceId;
    
    @NotNull(message = "任务项目ID不能为空")
    @Schema(description = "任务项目ID", required = true, example = "1")
    private Long taskProjectId;
    
    @NotBlank(message = "任务名称不能为空")
    @Schema(description = "任务名称", required = true, example = "用户数据处理")
    private String taskName;

    @NotBlank(message = "项目标识不能为空")
    @Schema(description = "项目标识", required = true, example = "user-data-process")
    private String projectIdentifier;

    @NotBlank(message = "任务标识不能为空")
    @Schema(description = "任务标识", required = true, example = "user-data-process")
    private String taskIdentifier;
    
    @Schema(description = "应用ID", example = "app-001")
    private String applicationId;
    
    @Schema(description = "流程实例名称", example = "用户管理流程实例")
    private String processName;
    
    @Schema(description = "原因", example = "定时任务触发")
    private String reason;
    
    @Schema(description = "平均耗时（毫秒）", example = "5000")
    private Long avgTime;
    
    @Schema(description = "当前耗时（毫秒）", example = "3000")
    private Long elapsedTime;
    
    @Schema(description = "时长（毫秒）", example = "4500")
    private Long duration;
    
    @Min(value = 0, message = "重试次数不能小于0")
    @Schema(description = "重试次数", example = "0")
    private Integer retried = 0;
    
    @Schema(description = "状态", example = "1")
    private Integer state;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}