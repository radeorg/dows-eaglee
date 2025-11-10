package org.dows.eaglee.request;

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
    
    @Schema(description = "CPU使用量", example = "50")
    private Integer cpuUsage;
    
    @Schema(description = "内存使用量", example = "40")
    private Integer memUsage;

    @Schema(description = "磁盘使用量", example = "20")
    private Integer diskUsage;

    @Schema(description = "网络使用量", example = "10")
    private Integer netUsage;
    
    @Schema(description = "采集时间范围-开始")
    private LocalDateTime beginTime;
    
    @Schema(description = "采集时间范围-结束")
    private LocalDateTime endTime;

}