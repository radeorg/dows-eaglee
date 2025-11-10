package org.dows.eaglee.dify;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "hina.eaglee.dify")
public class DifyProperties {

    private ApiSetting chat;
    private ApiSetting steam;
    private ApiSetting upload;


}
