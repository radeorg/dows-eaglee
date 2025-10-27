package com.hina.eaglee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务实例响应类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务实例响应")
public class TaskInstanceResponse {
    
    @Schema(description = "任务实例ID", example = "1")
    private Long taskInstanceId;
    
    @Schema(description = "任务项目ID", example = "1")
    private Long taskProjectId;
    
    @Schema(description = "项目名称", example = "用户管理系统")
    private String projectName;
    
    @Schema(description = "任务名称", example = "用户数据处理")
    private String taskName;
    
    @Schema(description = "任务标识", example = "user-data-process")
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
    
    @Schema(description = "重试次数", example = "0")
    private Integer retried;
    
    @Schema(description = "状态", example = "1")
    private Integer state;
    
    @Schema(description = "状态描述", example = "运行中")
    private String stateDesc;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "创建时间")
    private LocalDateTime ct;
    
    @Schema(description = "更新时间")
    private LocalDateTime ut;
    
    @Schema(description = "创建者ID", example = "1001")
    private Long cid;
    
    /**
     * 获取状态描述
     */
    public String getStateDesc() {
        if (state == null) {
            return "未知";
        }
        return switch (state) {
            case 0 -> "待执行";
            case 1 -> "运行中";
            case 2 -> "已完成";
            case 3 -> "失败";
            case 4 -> "已取消";
            default -> "未知";
        };
    }
    
    /**
     * 计算实际耗时（如果任务已完成）
     */
    public Long getActualDuration() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).toMillis();
        }
        return duration;
    }
    
    /**
     * 计算当前耗时（如果任务正在运行）
     */
    public Long getCurrentElapsedTime() {
        if (startTime != null && endTime == null) {
            return java.time.Duration.between(startTime, LocalDateTime.now()).toMillis();
        }
        return elapsedTime;
    }
}