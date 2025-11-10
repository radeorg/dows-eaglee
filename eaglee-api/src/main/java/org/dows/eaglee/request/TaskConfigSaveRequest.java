package org.dows.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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