package com.hina.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * 任务实例分页查询请求类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务实例分页查询请求")
public class TaskInstancePageRequest {
    
    @Min(value = 1, message = "页码必须大于0")
    @Schema(description = "页码", example = "1")
    private Long current = 1L;
    
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    @Schema(description = "每页大小", example = "10")
    private Long size = 10L;
    
    @Schema(description = "任务项目ID", example = "1")
    private Long taskProjectId;
    
    @Schema(description = "任务名称（模糊查询）", example = "用户数据")
    private String taskName;
    
    @Schema(description = "任务标识", example = "user-data-process")
    private String taskIdentifier;
    
    @Schema(description = "应用ID", example = "app-001")
    private String applicationId;
    
    @Schema(description = "流程实例名称（模糊查询）", example = "用户管理")
    private String processName;
    
    @Schema(description = "状态", example = "1")
    private Integer state;
    
    @Schema(description = "开始时间范围-开始")
    private LocalDateTime startTimeBegin;
    
    @Schema(description = "开始时间范围-结束")
    private LocalDateTime startTimeEnd;
    
    @Schema(description = "结束时间范围-开始")
    private LocalDateTime endTimeBegin;
    
    @Schema(description = "结束时间范围-结束")
    private LocalDateTime endTimeEnd;
    
    @Schema(description = "最小耗时（毫秒）", example = "1000")
    private Long minDuration;
    
    @Schema(description = "最大耗时（毫秒）", example = "10000")
    private Long maxDuration;
    
    @Schema(description = "是否包含已删除数据", example = "false")
    private Boolean includeDeleted = false;
}