package com.hina.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 * 任务项目保存请求类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务项目保存请求")
public class TaskProjectSaveRequest {
    
    @Schema(description = "任务项目ID（更新时需要）")
    private Long taskProjectId;
    
    @NotBlank(message = "项目名称不能为空")
    @Schema(description = "项目名称", required = true, example = "用户管理系统")
    private String projectName;
    
    @NotBlank(message = "流程编码不能为空")
    @Schema(description = "流程编码", required = true, example = "USER_MGMT_001")
    private String processCode;
    
    @NotBlank(message = "项目标识不能为空")
    @Schema(description = "项目标识", required = true, example = "user-management")
    private String projectIdentifier;
    
    @NotNull(message = "任务数量不能为空")
    @Min(value = 1, message = "任务数量必须大于0")
    @Schema(description = "任务数量", required = true, example = "5")
    private Integer taskCount;
    
    @Schema(description = "状态：0-未完成，1-已完成", example = "0")
    private Integer state;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}