package com.hina.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 任务运行时数据保存请求类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务运行时数据保存请求")
public class TaskRuntimeSaveRequest {
    
    @Schema(description = "任务运行时ID（更新时需要）")
    private Long taskRuntimeId;
    
    @NotBlank(message = "节点IP地址不能为空")
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$", 
             message = "IP地址格式不正确")
    @Schema(description = "节点IP地址", required = true, example = "192.168.1.100")
    private String ip;
    
    @NotNull(message = "CPU使用量不能为空")
    @Min(value = 0, message = "CPU使用量不能小于0")
    @Max(value = 100, message = "CPU使用量不能大于100")
    @Schema(description = "CPU使用量（百分比）", required = true, example = "75")
    private Integer cpuUsage;
    
    @NotNull(message = "内存使用量不能为空")
    @Min(value = 0, message = "内存使用量不能小于0")
    @Max(value = 100, message = "内存使用量不能大于100")
    @Schema(description = "内存使用量（百分比）", required = true, example = "60")
    private Integer memUsage;
    
    @NotNull(message = "磁盘使用量不能为空")
    @Min(value = 0, message = "磁盘使用量不能小于0")
    @Max(value = 100, message = "磁盘使用量不能大于100")
    @Schema(description = "磁盘使用量（百分比）", required = true, example = "45")
    private Integer diskUsage;
    
    @NotNull(message = "网络使用量不能为空")
    @Min(value = 0, message = "网络使用量不能小于0")
    @Max(value = 100, message = "网络使用量不能大于100")
    @Schema(description = "网络使用量（百分比）", required = true, example = "30")
    private Integer netUsage;
    
    @Schema(description = "采集时间（不传则使用当前时间）")
    private LocalDateTime ct;
}