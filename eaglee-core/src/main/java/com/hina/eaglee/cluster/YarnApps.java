package com.hina.eaglee.cluster;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import java.util.List;

@Data
@XmlRootElement(name = "apps")
@XmlAccessorType(XmlAccessType.FIELD)
public class YarnApps {
    @XmlElement(name = "app")
    private List<YarnApp> appList; // 应用列表
}