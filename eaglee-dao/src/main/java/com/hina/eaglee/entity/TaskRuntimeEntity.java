package com.hina.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务运行时数据实体类
 * 
 * @author eaglee-system
 */
@Data
@Table("task_runtime")
@Schema(description = "任务运行时数据")
public class TaskRuntimeEntity {
    
    @Id(keyType = KeyType.Auto)
    @Schema(description = "任务运行时ID")
    private Long taskRuntimeId;
    
    @Schema(description = "节点IP地址")
    private String ip;
    
    @Schema(description = "CPU使用量")
    private Integer cpuUsage;
    
    @Schema(description = "内存使用量")
    private Integer memUsage;
    
    @Schema(description = "磁盘使用量")
    private Integer diskUsage;
    
    @Schema(description = "网络使用量")
    private Integer netUsage;
    
    @Schema(description = "创建时间")
    private LocalDateTime ct;
    
    @Schema(description = "是否删除")
    private Boolean deleted;
}