package org.dows.eaglee.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

//@Builder
@Data
@XmlRootElement(name = "app")
@XmlAccessorType(XmlAccessType.FIELD)
public class YarnApp {
    @JsonProperty("applicationId")
    @XmlElement(name = "id")
    private String id; // 应用唯一ID

    @XmlElement(name = "user")
    private String user; // 提交用户

    @JsonProperty("taskName")
    @XmlElement(name = "name")
    private String name; // 应用名称

    @XmlElement(name = "queue")
    private String queue; // 队列名称

    @XmlElement(name = "state")
    private String state; // 运行状态（RUNNING/FAILED/SUCCEEDED等）

    @XmlElement(name = "finalStatus")
    private String finalStatus; // 最终状态（UNDEFINED/SUCCEEDED/FAILED等）

    @XmlElement(name = "progress")
    private Double progress; // 进度（百分比，如100.0）

    @XmlElement(name = "trackingUI")
    private String trackingUI; // 跟踪UI类型

    @XmlElement(name = "trackingUrl")
    private String trackingUrl; // 跟踪URL

    @XmlElement(name = "diagnostics")
    private String diagnostics; // 诊断信息（空字符串表示无异常）

    @XmlElement(name = "clusterId")
    private String clusterId; // 集群ID

    @XmlElement(name = "applicationType")
    private String applicationType; // 应用类型（Apache Flink/Spark等）

    @XmlElement(name = "applicationTags")
    private String applicationTags; // 应用标签（空字符串表示无标签）

    @XmlElement(name = "priority")
    private Integer priority; // 优先级（0为默认）

    @XmlElement(name = "startedTime")
    private Long startedTime; // 启动时间戳（毫秒）

    @XmlElement(name = "finishedTime")
    private Long finishedTime; // 结束时间戳（0表示未结束）

    @JsonProperty("duration")
    @XmlElement(name = "elapsedTime")
    private Long elapsedTime; // 运行时长（毫秒）

    @XmlElement(name = "amContainerLogs")
    private String amContainerLogs; // ApplicationMaster容器日志URL

    @XmlElement(name = "amHostHttpAddress")
    private String amHostHttpAddress; // AM节点HTTP地址

    @XmlElement(name = "amRPCAddress")
    private String amRPCAddress; // AM节点RPC地址

    @XmlElement(name = "allocatedMB")
    private Integer allocatedMB; // 分配内存（MB）

    @XmlElement(name = "allocatedVCores")
    private Integer allocatedVCores; // 分配虚拟核数

    @XmlElement(name = "reservedMB")
    private Integer reservedMB; // 预留内存（MB）

    @XmlElement(name = "reservedVCores")
    private Integer reservedVCores; // 预留虚拟核数

    @XmlElement(name = "runningContainers")
    private Integer runningContainers; // 运行中的容器数

    @XmlElement(name = "memorySeconds")
    private Long memorySeconds; // 内存使用时长（秒×MB）

    @XmlElement(name = "vcoreSeconds")
    private Long vcoreSeconds; // 虚拟核使用时长（秒×核数）

    @XmlElement(name = "queueUsagePercentage")
    private Double queueUsagePercentage; // 队列资源使用率（百分比）

    @XmlElement(name = "clusterUsagePercentage")
    private Double clusterUsagePercentage; // 集群资源使用率（百分比）

    // 映射<resourceSecondsMap>节点（包含多个<entry>子节点）
    @XmlElementWrapper(name = "resourceSecondsMap")
    @XmlElement(name = "entry")
    private List<ResourceEntry> resourceSecondsMap;

    @XmlElement(name = "preemptedResourceMB")
    private Integer preemptedResourceMB; // 被抢占的内存（MB）

    @XmlElement(name = "preemptedResourceVCores")
    private Integer preemptedResourceVCores; // 被抢占的虚拟核数

    @XmlElement(name = "numNonAMContainerPreempted")
    private Integer numNonAMContainerPreempted; // 被抢占的非AM容器数

    @XmlElement(name = "numAMContainerPreempted")
    private Integer numAMContainerPreempted; // 被抢占的AM容器数

    @XmlElement(name = "preemptedMemorySeconds")
    private Long preemptedMemorySeconds; // 被抢占的内存使用时长

    @XmlElement(name = "preemptedVcoreSeconds")
    private Long preemptedVcoreSeconds; // 被抢占的虚拟核使用时长

    @XmlElement(name = "preemptedResourceSecondsMap")
    private String preemptedResourceSecondsMap; // 被抢占的资源使用时长映射（空）

    @XmlElement(name = "logAggregationStatus")
    private String logAggregationStatus; // 日志聚合状态（NOT_START/IN_PROGRESS/COMPLETED）

    @XmlElement(name = "unmanagedApplication")
    private Boolean unmanagedApplication; // 是否为非托管应用（false表示托管）

    @XmlElement(name = "amNodeLabelExpression")
    private String amNodeLabelExpression; // AM节点标签表达式（空）

    // 映射<timeouts>节点（包含一个<timeout>子节点）
    @XmlElementWrapper(name = "timeouts")
    @XmlElement(name = "timeout")
    private List<Timeout> timeouts;
}