package com.hina.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

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
    private Long pageNo = 1L;
    
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    @Schema(description = "每页大小", example = "10")
    private Long pageSize = 10L;

    @Schema(description = "配置键")
    private String key;

    @Schema(description = "配置描述")
    private String description;

    @Schema(description = "数据类型")
    private String dataType;

    @Schema(description = "页面标签[input,select,checkbox......]")
    private String tag;

    @Schema(description = "选项值")
    private String options;
}