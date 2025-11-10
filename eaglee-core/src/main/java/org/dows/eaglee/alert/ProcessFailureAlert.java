package org.dows.eaglee.alert;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 流程实例执行失败请求实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessFailureAlert extends ProcessAlert {


    //----------------------------------

    /**
     * 任务编码
     */
    private Long taskCode;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型（如SPARK）
     */
    private String taskType;

    /**
     * 任务状态（如FAILURE）
     */
    private String taskState;

    /**
     * 任务开始时间
     */
    private LocalDateTime taskStartTime;

    /**
     * 任务结束时间
     */
    private LocalDateTime taskEndTime;

    /**
     * 任务执行主机（IP:端口）
     */
    private String taskHost;

    /**
     * 任务优先级（如medium）
     */
    private String taskPriority;

    /**
     * 日志路径
     */
    private String logPath;

    /**
     * 任务持续时间
     */
    private String taskDuration;

    /**
     * DolphinScheduler平台URL（原字段可能存在拼写问题，保持原始拼写）
     */
    private String dsPatformUrl;

    /**
     * Spark Jar包路径
     */
    private String sparkJarPath;
}