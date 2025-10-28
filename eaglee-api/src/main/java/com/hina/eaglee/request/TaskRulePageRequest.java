package com.hina.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 任务规则分页查询请求类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务规则分页查询请求")
public class TaskRulePageRequest {
    
    @Min(value = 1, message = "页码必须大于0")
    @Schema(description = "页码", example = "1")
    private Long pageNo = 1L;
    
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    @Schema(description = "每页大小", example = "10")
    private Long pageSize = 10L;


    @Schema(description = "规则名")
    public String ruleName;

    @Schema(description = "json配置")
    public String configJson;

    @Schema(description = "应用类型（0:项目, 1:任务）")
    public Integer referenceType;

    @Schema(description = "引用ID（项目或任务）")
    public Long referenceId;


}