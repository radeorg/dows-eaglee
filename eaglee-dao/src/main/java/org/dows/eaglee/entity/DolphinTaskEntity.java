package org.dows.eaglee.entity;


import org.dows.eaglee.dolphin.DolphinTask;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
@Table(value = "t_ds_task_instance", dataSource = "dolphinscheduler")
@Schema(description = "Dolphin任务实例")
public class DolphinTaskEntity extends DolphinTask {

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 任务执行类型: 0-批处理, 1-流处理
     */
    private Integer taskExecuteType;

    /**
     * 任务定义代码
     */
    private Long taskCode;

    /**
     * 任务定义版本
     */
    private Integer taskDefinitionVersion;

    /**
     * 流程实例ID
     */
    private Integer processInstanceId;

    /**
     * 流程实例名称
     */
    private String processInstanceName;

    /**
     * 项目代码
     */
    private Long projectCode;

    /**
     * 状态: 0-提交成功, 1-运行中, 2-准备暂停, 3-暂停, 4-准备停止, 5-停止, 6-失败, 7-成功, 8-需要容错, 9-杀死, 10-等待线程, 11-等待依赖完成
     */
    private Integer state;

    /**
     * 任务提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 任务开始时间
     */
    private LocalDateTime startTime;

    /**
     * 任务结束时间
     */
    private LocalDateTime endTime;

    /**
     * 任务运行主机
     */
    private String host;

    /**
     * 任务执行路径
     */
    private String executePath;

    /**
     * 任务日志路径
     */
    private String logPath;

    /**
     * 是否告警
     */
    private Integer alertFlag;

    /**
     * 任务重试次数
     */
    private Integer retryTimes;

    /**
     * 任务进程ID
     */
    private Integer pid;

    /**
     * YARN应用ID
     */
    private String appLink;

    /**
     * 任务参数
     */
    private String taskParams;

    /**
     * 可用标志: 0-不可用, 1-可用
     */
    private Integer flag;

    /**
     * 是否缓存: 0-不可用, 1-可用
     */
    private Integer isCache;

    /**
     * 缓存键
     */
    private String cacheKey;

    /**
     * 重试间隔(任务失败时)
     */
    private Integer retryInterval;

    /**
     * 最大重试次数
     */
    private Integer maxRetryTimes;

    /**
     * 任务实例优先级: 0-最高, 1-高, 2-中, 3-低, 4-最低
     */
    private Integer taskInstancePriority;

    /**
     * 工作组ID
     */
    private String workerGroup;

    /**
     * 环境代码
     */
    private Long environmentCode;

    /**
     * 环境配置
     */
    private String environmentConfig;

    /**
     * 执行者ID
     */
    private Integer executorId;

    /**
     * 执行者名称
     */
    private String executorName;

    /**
     * 任务首次提交时间
     */
    private LocalDateTime firstSubmitTime;

    /**
     * 任务延迟执行时间
     */
    private Integer delayTime;

    /**
     * 变量池
     */
    private String varPool;

    /**
     * 任务组ID
     */
    private Integer taskGroupId;

    /**
     * 试运行标志: 0-正常, 1-试运行
     */
    private Integer dryRun;

    /**
     * CPU配额(%): -1-无限
     */
    private Integer cpuQuota;

    /**
     * 最大内存(MB): -1-无限
     */
    private Integer memoryMax;

    /**
     * 测试标志: 0-正常, 1-测试运行
     */
    private Integer testFlag;
}
