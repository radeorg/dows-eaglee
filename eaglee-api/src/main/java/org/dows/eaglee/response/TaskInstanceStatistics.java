package org.dows.eaglee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务实例统计信息响应类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务实例统计信息")
public class TaskInstanceStatistics {
    
    @Schema(description = "任务标识", example = "user-data-process")
    private String taskIdentifier;
    
    @Schema(description = "总执行次数", example = "100")
    private Integer totalCount;
    
    @Schema(description = "成功次数", example = "95")
    private Integer successCount;
    
    @Schema(description = "失败次数", example = "5")
    private Integer failureCount;
    
    @Schema(description = "成功率", example = "0.95")
    private Double successRate;
    
    @Schema(description = "平均耗时（毫秒）", example = "5000")
    private Long avgTime;
    
    @Schema(description = "最小耗时（毫秒）", example = "2000")
    private Long minTime;
    
    @Schema(description = "最大耗时（毫秒）", example = "10000")
    private Long maxTime;
    
    @Schema(description = "总耗时（毫秒）", example = "500000")
    private Long totalTime;
    
    @Schema(description = "正在运行的任务数", example = "3")
    private Integer runningCount;
    
    @Schema(description = "待执行的任务数", example = "2")
    private Integer pendingCount;
    
    @Schema(description = "最近执行时间")
    private LocalDateTime lastExecuteTime;
    
    @Schema(description = "最近成功时间")
    private LocalDateTime lastSuccessTime;
    
    @Schema(description = "最近失败时间")
    private LocalDateTime lastFailureTime;
    
    /**
     * 计算成功率
     */
    public Double getSuccessRate() {
        if (totalCount == null || totalCount == 0) {
            return 0.0;
        }
        if (successCount == null) {
            return 0.0;
        }
        return (double) successCount / totalCount;
    }
    
    /**
     * 获取成功率百分比字符串
     */
    public String getSuccessRatePercent() {
        return String.format("%.2f%%", getSuccessRate() * 100);
    }
}