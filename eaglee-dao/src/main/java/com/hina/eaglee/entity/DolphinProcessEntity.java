package com.hina.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(value = "t_ds_process_instance",dataSource = "dolphinscheduler")
@Schema(description = "Dolphin流程实例")
public class DolphinProcessEntity {

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 流程实例名称
     */
    private String name;

    /**
     * 流程定义编码（非空）
     */
    private Long processDefinitionCode;

    /**
     * 流程定义版本（默认0）
     */
    private Integer processDefinitionVersion = 0;

    /**
     * 项目编码
     */
    private Long projectCode;

    /**
     * 流程实例状态：0提交成功,1运行中,2准备暂停,3暂停,4准备停止,5停止,6失败,7成功,8需要容错,9杀死,10等待线程,11等待依赖完成
     */
    private Integer state;

    /**
     * 状态历史描述
     */
    private String stateHistory;

    /**
     * 流程实例容错标识：0正常,1容错实例
     */
    private Integer recovery;

    /**
     * 流程实例开始时间
     */
    private LocalDateTime startTime;

    /**
     * 流程实例结束时间
     */
    private LocalDateTime endTime;

    /**
     * 流程实例运行次数
     */
    private Integer runTimes;

    /**
     * 流程实例执行主机
     */
    private String host;

    /**
     * 命令类型
     */
    private Integer commandType;

    /**
     * JSON格式命令参数
     */
    private String commandParam;

    /**
     * 任务依赖类型：0仅当前节点,1当前节点之前,2当前节点之后
     */
    private Integer taskDependType;

    /**
     * 最大重试次数（默认0）
     */
    private Integer maxTryTimes = 0;

    /**
     * 失败策略：0节点失败则终止流程,1节点失败则继续运行其他节点
     */
    private Integer failureStrategy = 0;

    /**
     * 告警类型：0不告警,1流程成功告警,2流程失败告警,3无论成败都告警
     */
    private Integer warningType = 0;

    /**
     * 告警组ID
     */
    private Integer warningGroupId;

    /**
     * 调度时间
     */
    private LocalDateTime scheduleTime;

    /**
     * 命令开始时间
     */
    private LocalDateTime commandStartTime;

    /**
     * 全局参数
     */
    private String globalParams;

    /**
     * 标识（默认1）
     */
    private Integer flag = 1;

    /**
     * 更新时间（自动更新）
     */
    private LocalDateTime updateTime;

    /**
     * 是否子流程标识：0否,1是（默认0）
     */
    private Integer isSubProcess = 0;

    /**
     * 执行人ID（非空）
     */
    private Integer executorId;

    /**
     * 执行人名称
     */
    private String executorName;

    /**
     * 流程实例操作历史命令
     */
    private String historyCmd;

    /**
     * 流程实例优先级：0最高,1高,2中,3低,4最低（默认2）
     */
    private Integer processInstancePriority = 2;

    /**
     * 工作节点组ID
     */
    private String workerGroup;

    /**
     * 环境编码（默认-1）
     */
    private Long environmentCode = -1L;

    /**
     * 超时时间（默认0）
     */
    private Integer timeout = 0;

    /**
     * 租户编码（默认default）
     */
    private String tenantCode = "default";

    /**
     * 变量池
     */
    private String varPool;

    /**
     * 空跑标识：0正常,1空跑（默认0）
     */
    private Integer dryRun = 0;

    /**
     * 串行队列下一个流程实例ID（默认0）
     */
    private Integer nextProcessInstanceId = 0;

    /**
     * 流程实例重启时间
     */
    private LocalDateTime restartTime;

    /**
     * 测试标识：0正常,1测试运行
     */
    private Integer testFlag;
}
