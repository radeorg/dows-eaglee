package com.hina.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * 任务配置分页查询请求类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务配置分页查询请求")
public class TaskConfigPageRequest {
    
    @Min(value = 1, message = "页码必须大于0")
    @Schema(description = "页码", example = "1")
    private Long current = 1L;
    
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    @Schema(description = "每页大小", example = "10")
    private Long size = 10L;
    
    @Schema(description = "配置名称（模糊查询）", example = "数据采集")
    private String configName;
    
    @Schema(description = "配置键（精确查询）", example = "data.collection.interval")
    private String configKey;
    
    @Schema(description = "配置类型", example = "SYSTEM")
    private String configType;
    
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled;
    
    @Schema(description = "创建时间范围-开始")
    private LocalDateTime ctBegin;
    
    @Schema(description = "创建时间范围-结束")
    private LocalDateTime ctEnd;
    
    @Schema(description = "更新时间范围-开始")
    private LocalDateTime utBegin;
    
    @Schema(description = "更新时间范围-结束")
    private LocalDateTime utEnd;
    
    @Schema(description = "创建者ID", example = "1001")
    private Long cid;
    
    @Schema(description = "是否包含已删除数据", example = "false")
    private Boolean includeDeleted = false;
}