package com.hina.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务计数器实体类
 *
 * @author eaglee-system
 */
@Data
@Table("task_counter")
@Schema(description = "任务计数器")
public class TaskCounterEntity {
    
    @Id(keyType = KeyType.Auto)
    @Schema(description = "任务计数器ID")
    private Long taskCounterId;


    @Schema(description = "项目标识")
    public String projectIdentifier;

    @Schema(description = "任务标识（同一类任务）")
    public String taskIdentifier;

    @Schema(description = "总耗时（毫秒）")
    public Long expendTotal;

    @Schema(description = "任务实例数量")
    public Long taskCount;

    @Schema(description = "删除标记")
    public Boolean deleted;

    @Schema(description = "乐观锁版本")
    public Integer version;

//    @Schema(description = "任务标识")
//    private String taskIdentifier;
//
//    @Schema(description = "总执行次数")
//    private Integer totalCount;
//
//    @Schema(description = "成功次数")
//    private Integer successCount;
//
//    @Schema(description = "失败次数")
//    private Integer failureCount;
//
//    @Schema(description = "总耗时")
//    private Long totalTime;
//
//    @Schema(description = "平均耗时")
//    private Long avgTime;
//
//    @Schema(description = "最小耗时")
//    private Long minTime;
//
//    @Schema(description = "最大耗时")
//    private Long maxTime;
//
//    @Schema(description = "创建时间")
//    private LocalDateTime ct;
//
//    @Schema(description = "更新时间")
//    private LocalDateTime ut;
//
//    @Schema(description = "是否删除")
//    private Boolean deleted;
}