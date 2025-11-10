package org.dows.eaglee.cluster;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Timeout {

    @XmlElement(name = "type")
    private String type; // 超时类型（LIFETIME表示生命周期）

    @XmlElement(name = "expiryTime")
    private String expiryTime; // 过期时间（UNLIMITED表示无限制）

    @XmlElement(name = "remainingTimeInSeconds")
    private Integer remainingTimeInSeconds; // 剩余时间（-1表示无限制）
}