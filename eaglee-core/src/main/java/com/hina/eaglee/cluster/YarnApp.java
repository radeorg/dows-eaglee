package com.hina.eaglee.cluster;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import org.apache.catalina.loader.ResourceEntry;

import java.util.List;

@Data
@XmlRootElement(name = "app")
@XmlAccessorType(XmlAccessType.FIELD)
public class YarnApp {
    @XmlElement(name = "id")
    private String id; // 应用ID

    @XmlElement(name = "user")
    private String user; // 提交用户

    @XmlElement(name = "name")
    private String name; // 应用名称

    @XmlElement(name = "queue")
    private String queue; // 队列名称

    @XmlElement(name = "state")
    private String state; // 运行状态（RUNNING/FAILED/SUCCEEDED等）

    @XmlElement(name = "finalStatus")
    private String finalStatus; // 最终状态

    @XmlElement(name = "progress")
    private Double progress; // 进度（百分比）

    @XmlElement(name = "trackingUrl")
    private String trackingUrl; // 跟踪URL

    @XmlElement(name = "applicationType")
    private String applicationType; // 应用类型（Apache Flink/Spark等）

    @XmlElement(name = "startedTime")
    private Long startedTime; // 启动时间（时间戳，毫秒）

    @XmlElement(name = "finishedTime")
    private Long finishedTime; // 结束时间（0表示未结束）

    @XmlElement(name = "elapsedTime")
    private Long elapsedTime; // 运行时长（毫秒）

    @XmlElement(name = "allocatedMB")
    private Integer allocatedMB; // 分配内存（MB）

    @XmlElement(name = "allocatedVCores")
    private Integer allocatedVCores; // 分配虚拟核数

    @XmlElement(name = "runningContainers")
    private Integer runningContainers; // 运行中的容器数

    @XmlElementWrapper(name = "resourceSecondsMap")
    @XmlElement(name = "entry")
    private List<ResourceEntry> resourceSecondsMap; // 资源使用时长统计
}