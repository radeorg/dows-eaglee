package com.hina.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务实例实体类
 * 
 * @author eaglee-system
 */
@Data
@Table("task_instance")
@Schema(description = "任务实例")
public class TaskInstanceEntity {
    
    @Id(keyType = KeyType.Auto)
    @Schema(description = "任务实例ID")
    private Long taskInstanceId;

    @Schema(description = "流程实例ID")
    private Long processInstanceId;

    @Schema(description = "流程实例名称")
    private String processInstanceName;
    
    @Schema(description = "任务项目ID")
    private Long projectCode;
    
    @Schema(description = "任务名称")
    private String taskName;
    
    @Schema(description = "任务标识")
    private Long taskCode;
    
    @Schema(description = "yarn 应用ID")
    private String applicationId;

    // ------------------- 以下为任务实例字段 -------------------
    @Schema(description = "日志路径")
    private String s3Log;

    @Schema(description = "原因")
    private String reason;

    @Schema(description = "异常类型")
    private Integer exceptionType;
    
    @Schema(description = "平均耗时")
    private Long avgTime;
    
    @Schema(description = "当前耗时")
    private Long elapsedTime;
    
    @Schema(description = "时长")
    private Long duration;
    
    @Schema(description = "重试次数")
    private Integer retryTimes;

    @Schema(description = "最大重试次数")
    private Integer maxRetryTimes;
    
    @Schema(description = "状态: 0-提交成功, 1-运行中, 2-准备暂停, 3-暂停, 4-准备停止, 5-停止, 6-失败, 7-成功, 8-需要容错, 9-杀死, 10-等待线程, 11-等待依赖完成")
    private Integer state;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "任务提交时间")
    private LocalDateTime submitTime;

    @Schema(description = "任务类型")
    private String taskType;

    @Schema(description = "任务执行类型: 0-批处理, 1-流处理")
    private Integer taskExecuteType;

    @Schema(description = "任务定义版本")
    private Integer taskDefinitionVersion;

    @Schema(description = "是否告警")
    private Integer alertFlag;

    @Schema(description = "任务进程ID")
    private Integer pid;

    @Schema(description = "YARN应用ID")
    private String appLink;













    
    @Schema(description = "创建时间")
    private LocalDateTime ct;
    
    @Schema(description = "更新时间")
    private LocalDateTime ut;
    
    @Schema(description = "创建者ID")
    private Long cid;
    
    @Schema(description = "是否删除")
    private Boolean deleted;
}