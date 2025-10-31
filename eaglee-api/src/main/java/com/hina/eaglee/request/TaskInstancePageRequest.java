package com.hina.eaglee.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer pageNumber = 1;
    
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "时间范围-分钟", example = "10")
    private Integer intervalMinutes;
    @Schema(description = "时间范围-天", example = "10")
    private Integer intervalDay;

    @Schema(description = "应用ID", example = "application_1645100000000_0001")
    private String applicationId;

    @Schema(description = "任务名称（模糊查询）", example = "用户数据")
    private String taskName;
    
//    @Schema(description = "应用ID", example = "app-001")
//    private String applicationId;
    private String taskCode;

    @Schema(description = "流程实例名称（模糊查询）", example = "用户管理")
    private String processInstanceName;
    
    @Schema(description = "状态", example = "1")
    private Integer state;

    @Schema(description = "开始时间范围-开始",example = "2025-10-30 09:15:36")
    private LocalDateTime startTime;
    @Schema(description = "结束时间范围-结束",example = "2025-10-30 09:15:36")
    private LocalDateTime endTime;
    
    @Schema(description = "耗时（毫秒）", example = "1000")
    private Long duration;

    @JsonIgnore
    private Integer offset;

    public Integer getOffset() {
        return this.offset = (this.pageNumber - 1) * this.pageSize;
    }
    
}