package com.hina.eaglee.cluster;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceEntry {
    @XmlElement(name = "key")
    private String key; // 资源类型（memory-mb/vcores）

    @XmlElement(name = "value")
    private Long value; // 资源使用时长（秒*资源量）
}