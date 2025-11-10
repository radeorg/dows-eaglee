package org.dows.eaglee.cluster;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import java.util.List;

@Data
@XmlRootElement(name = "apps")
@XmlAccessorType(XmlAccessType.FIELD)
//@JsonRootName("apps")
public class YarnApps {
    @XmlElement(name = "app")
//    @JsonProperty("app")
    private List<YarnApp> app; // 应用列表
}