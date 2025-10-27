package com.hina.eaglee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务度量数据响应类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务度量数据响应")
public class TaskMetricResponse {
    
    @Schema(description = "任务度量ID", example = "1")
    private Long taskMetricId;
    
    @Schema(description = "节点IP地址", example = "192.168.1.100")
    private String ip;
    
    @Schema(description = "时间单位：minute-分钟，hour-小时，day-天", example = "hour")
    private String timeUnit;
    
    @Schema(description = "时间单位描述", example = "小时")
    private String timeUnitDesc;
    
    @Schema(description = "时间值")
    private LocalDateTime timeValue;
    
    @Schema(description = "CPU使用总量", example = "4500")
    private Long cpuTotal;
    
    @Schema(description = "内存使用总量", example = "3600")
    private Long memTotal;
    
    @Schema(description = "磁盘使用总量", example = "2700")
    private Long diskTotal;
    
    @Schema(description = "网络使用总量", example = "1800")
    private Long netTotal;
    
    @Schema(description = "数据点数量", example = "60")
    private Integer dataPoints;
    
    @Schema(description = "CPU平均使用率", example = "75.0")
    private Double cpuAverage;
    
    @Schema(description = "内存平均使用率", example = "60.0")
    private Double memAverage;
    
    @Schema(description = "磁盘平均使用率", example = "45.0")
    private Double diskAverage;
    
    @Schema(description = "网络平均使用率", example = "30.0")
    private Double netAverage;
    
    @Schema(description = "总体平均使用率", example = "52.5")
    private Double overallAverage;
    
    @Schema(description = "创建时间")
    private LocalDateTime ct;
    
    @Schema(description = "更新时间")
    private LocalDateTime ut;
    
    /**
     * 获取时间单位描述
     */
    public String getTimeUnitDesc() {
        if (timeUnit == null) {
            return "未知";
        }
        return switch (timeUnit) {
            case "minute" -> "分钟";
            case "hour" -> "小时";
            case "day" -> "天";
            default -> "未知";
        };
    }
    
    /**
     * 计算CPU平均使用率
     */
    public Double getCpuAverage() {
        if (cpuTotal != null && dataPoints != null && dataPoints > 0) {
            return cpuTotal.doubleValue() / dataPoints;
        }
        return null;
    }
    
    /**
     * 计算内存平均使用率
     */
    public Double getMemAverage() {
        if (memTotal != null && dataPoints != null && dataPoints > 0) {
            return memTotal.doubleValue() / dataPoints;
        }
        return null;
    }
    
    /**
     * 计算磁盘平均使用率
     */
    public Double getDiskAverage() {
        if (diskTotal != null && dataPoints != null && dataPoints > 0) {
            return diskTotal.doubleValue() / dataPoints;
        }
        return null;
    }
    
    /**
     * 计算网络平均使用率
     */
    public Double getNetAverage() {
        if (netTotal != null && dataPoints != null && dataPoints > 0) {
            return netTotal.doubleValue() / dataPoints;
        }
        return null;
    }
    
    /**
     * 计算总体平均使用率
     */
    public Double getOverallAverage() {
        Double cpu = getCpuAverage();
        Double mem = getMemAverage();
        Double disk = getDiskAverage();
        Double net = getNetAverage();
        
        if (cpu != null && mem != null && disk != null && net != null) {
            return (cpu + mem + disk + net) / 4.0;
        }
        return null;
    }
    
    /**
     * 获取资源使用趋势
     */
    public String getUsageTrend() {
        Double overall = getOverallAverage();
        if (overall == null) {
            return "未知";
        }
        
        if (overall >= 80) {
            return "高负载";
        } else if (overall >= 60) {
            return "中等负载";
        } else if (overall >= 40) {
            return "较低负载";
        } else {
            return "低负载";
        }
    }
}