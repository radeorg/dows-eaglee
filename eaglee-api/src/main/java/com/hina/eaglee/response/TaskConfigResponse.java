package com.hina.eaglee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务配置响应类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务配置响应")
public class TaskConfigResponse {
    
    @Schema(description = "任务配置ID", example = "1")
    private Long taskConfigId;
    
    @Schema(description = "配置名称", example = "数据采集间隔配置")
    private String configName;
    
    @Schema(description = "配置键", example = "data.collection.interval")
    private String configKey;
    
    @Schema(description = "配置值", example = "30")
    private String configValue;
    
    @Schema(description = "配置描述", example = "数据采集的时间间隔，单位为秒")
    private String configDesc;
    
    @Schema(description = "配置类型", example = "SYSTEM")
    private String configType;
    
    @Schema(description = "配置类型描述", example = "系统配置")
    private String configTypeDesc;
    
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled;
    
    @Schema(description = "启用状态描述", example = "启用")
    private String enabledDesc;
    
    @Schema(description = "创建时间")
    private LocalDateTime ct;
    
    @Schema(description = "更新时间")
    private LocalDateTime ut;
    
    @Schema(description = "创建者ID", example = "1001")
    private Long cid;
    
    /**
     * 获取配置类型描述
     */
    public String getConfigTypeDesc() {
        if (configType == null) {
            return "未知";
        }
        return switch (configType.toUpperCase()) {
            case "SYSTEM" -> "系统配置";
            case "BUSINESS" -> "业务配置";
            case "MONITOR" -> "监控配置";
            case "COLLECTION" -> "采集配置";
            default -> configType;
        };
    }
    
    /**
     * 获取启用状态描述
     */
    public String getEnabledDesc() {
        if (enabled == null) {
            return "未知";
        }
        return enabled ? "启用" : "禁用";
    }
}