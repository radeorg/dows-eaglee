package com.hina.eaglee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务运行时数据响应类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务运行时数据响应")
public class TaskRuntimeResponse {
    
    @Schema(description = "任务运行时ID", example = "1")
    private Long taskRuntimeId;
    
    @Schema(description = "节点IP地址", example = "192.168.1.100")
    private String ip;
    
    @Schema(description = "CPU使用量（百分比）", example = "75")
    private Integer cpuUsage;
    
    @Schema(description = "内存使用量（百分比）", example = "60")
    private Integer memUsage;
    
    @Schema(description = "磁盘使用量（百分比）", example = "45")
    private Integer diskUsage;
    
    @Schema(description = "网络使用量（百分比）", example = "30")
    private Integer netUsage;
    
    @Schema(description = "采集时间")
    private LocalDateTime ct;
    
    @Schema(description = "资源使用等级", example = "中等")
    private String resourceLevel;
    
    @Schema(description = "总体资源使用率", example = "52.5")
    private Double overallUsage;
    
    /**
     * 计算总体资源使用率
     */
    public Double getOverallUsage() {
        if (cpuUsage != null && memUsage != null && diskUsage != null && netUsage != null) {
            return (cpuUsage + memUsage + diskUsage + netUsage) / 4.0;
        }
        return null;
    }
    
    /**
     * 获取资源使用等级
     */
    public String getResourceLevel() {
        Double overall = getOverallUsage();
        if (overall == null) {
            return "未知";
        }
        
        if (overall >= 80) {
            return "高";
        } else if (overall >= 60) {
            return "中等";
        } else if (overall >= 40) {
            return "较低";
        } else {
            return "低";
        }
    }
    
    /**
     * 判断是否为高负载
     */
    public boolean isHighLoad() {
        return cpuUsage != null && cpuUsage >= 80 ||
               memUsage != null && memUsage >= 80 ||
               diskUsage != null && diskUsage >= 80;
    }
    
    /**
     * 获取最高使用率的资源类型
     */
    public String getHighestUsageResource() {
        if (cpuUsage == null || memUsage == null || diskUsage == null || netUsage == null) {
            return "未知";
        }
        
        int maxUsage = Math.max(Math.max(cpuUsage, memUsage), Math.max(diskUsage, netUsage));
        
        if (cpuUsage == maxUsage) {
            return "CPU";
        } else if (memUsage == maxUsage) {
            return "内存";
        } else if (diskUsage == maxUsage) {
            return "磁盘";
        } else {
            return "网络";
        }
    }
}