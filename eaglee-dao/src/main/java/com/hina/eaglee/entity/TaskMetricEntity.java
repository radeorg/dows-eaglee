package com.hina.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务度量数据实体类
 * 
 * @author eaglee-system
 */
@Data
@Table("task_metric")
@Schema(description = "任务度量数据")
public class TaskMetricEntity {
    
    @Id(keyType = KeyType.Auto)
    @Schema(description = "任务度量ID")
    private Long taskMetricId;
    
    @Schema(description = "节点IP地址")
    private String ip;
    
    @Schema(description = "时间单位：minute-分钟，hour-小时，day-天")
    private String timeUnit;
    
    @Schema(description = "时间值")
    private LocalDateTime timeValue;
    
    @Schema(description = "CPU使用总量")
    private Long cpuTotal;
    
    @Schema(description = "内存使用总量")
    private Long memTotal;
    
    @Schema(description = "磁盘使用总量")
    private Long diskTotal;
    
    @Schema(description = "网络使用总量")
    private Long netTotal;
    
    @Schema(description = "数据点数量")
    private Integer dataPoints;
    
    @Schema(description = "创建时间")
    private LocalDateTime ct;
    
    @Schema(description = "更新时间")
    private LocalDateTime ut;
    
    @Schema(description = "是否删除")
    private Boolean deleted;
}