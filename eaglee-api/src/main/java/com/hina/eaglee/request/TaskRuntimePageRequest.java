package com.hina.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * 任务运行时数据分页查询请求类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务运行时数据分页查询请求")
public class TaskRuntimePageRequest {
    
    @Min(value = 1, message = "页码必须大于0")
    @Schema(description = "页码", example = "1")
    private Long current = 1L;
    
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    @Schema(description = "每页大小", example = "10")
    private Long size = 10L;
    
    @Schema(description = "节点IP地址", example = "192.168.1.100")
    private String ip;
    
    @Schema(description = "最小CPU使用量", example = "50")
    private Integer minCpuUsage;
    
    @Schema(description = "最大CPU使用量", example = "90")
    private Integer maxCpuUsage;
    
    @Schema(description = "最小内存使用量", example = "40")
    private Integer minMemUsage;
    
    @Schema(description = "最大内存使用量", example = "80")
    private Integer maxMemUsage;
    
    @Schema(description = "最小磁盘使用量", example = "20")
    private Integer minDiskUsage;
    
    @Schema(description = "最大磁盘使用量", example = "70")
    private Integer maxDiskUsage;
    
    @Schema(description = "最小网络使用量", example = "10")
    private Integer minNetUsage;
    
    @Schema(description = "最大网络使用量", example = "60")
    private Integer maxNetUsage;
    
    @Schema(description = "采集时间范围-开始")
    private LocalDateTime ctBegin;
    
    @Schema(description = "采集时间范围-结束")
    private LocalDateTime ctEnd;
    
    @Schema(description = "是否包含已删除数据", example = "false")
    private Boolean includeDeleted = false;
    
    @Schema(description = "排序字段", example = "ct")
    private String orderBy = "ct";
    
    @Schema(description = "排序方向：asc-升序，desc-降序", example = "desc")
    private String orderDirection = "desc";
}