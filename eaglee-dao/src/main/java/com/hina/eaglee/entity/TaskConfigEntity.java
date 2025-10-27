package com.hina.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务配置实体类
 * 
 * @author eaglee-system
 */
@Data
@Table("task_config")
@Schema(description = "任务配置")
public class TaskConfigEntity {
    
    @Id(keyType = KeyType.Auto)
    @Schema(description = "任务配置ID")
    private Long taskConfigId;
    
    @Schema(description = "配置名称")
    private String configName;
    
    @Schema(description = "配置键")
    private String configKey;
    
    @Schema(description = "配置值")
    private String configValue;
    
    @Schema(description = "配置描述")
    private String configDesc;
    
    @Schema(description = "配置类型")
    private String configType;
    
    @Schema(description = "是否启用")
    private Boolean enabled;
    
    @Schema(description = "创建时间")
    private LocalDateTime ct;
    
    @Schema(description = "更新时间")
    private LocalDateTime ut;
    
    @Schema(description = "创建者ID")
    private Long cid;
    
    @Schema(description = "是否删除")
    private Boolean deleted;
}