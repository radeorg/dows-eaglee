package com.hina.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 任务配置保存请求类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务配置保存请求")
public class TaskConfigSaveRequest {
    
    @Schema(description = "任务配置ID（更新时需要）")
    private Long taskConfigId;
    
    @NotBlank(message = "配置名称不能为空")
    @Size(max = 100, message = "配置名称长度不能超过100个字符")
    @Schema(description = "配置名称", required = true, example = "数据采集间隔配置")
    private String configName;
    
    @NotBlank(message = "配置键不能为空")
    @Size(max = 50, message = "配置键长度不能超过50个字符")
    @Schema(description = "配置键", required = true, example = "data.collection.interval")
    private String configKey;
    
    @NotBlank(message = "配置值不能为空")
    @Size(max = 500, message = "配置值长度不能超过500个字符")
    @Schema(description = "配置值", required = true, example = "30")
    private String configValue;
    
    @Size(max = 200, message = "配置描述长度不能超过200个字符")
    @Schema(description = "配置描述", example = "数据采集的时间间隔，单位为秒")
    private String configDesc;
    
    @Size(max = 20, message = "配置类型长度不能超过20个字符")
    @Schema(description = "配置类型", example = "SYSTEM")
    private String configType;
    
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled = true;
}