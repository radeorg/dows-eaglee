package com.hina.eaglee.request;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务执行详情实体类
 */
@Data
public class DolphinAlertRequest {

    /**
     * 项目编码
     */
    private Long projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 所有者
     */
    private String owner;

    /**
     * 流程ID
     */
    private Integer processId;

    /**
     * 流程定义编码
     */
    private Long processDefinitionCode;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 修改人
     */
    private String modifyBy;

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
     * DolphinScheduler平台URL
     */
    private String dsPatformUrl;  // 注意：原字段可能存在拼写问题（应为dsPlatformUrl），此处保持原拼写

    /**
     * Spark Jar包路径
     */
    private String sparkJarPath;

    /**
     * Sentry链接
     */
    private String sentryUrl;
}