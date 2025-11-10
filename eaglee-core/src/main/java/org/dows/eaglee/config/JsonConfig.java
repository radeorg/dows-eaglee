package org.dows.eaglee.config;

import cn.hutool.json.JSONUtil;
import lombok.Data;

@Data
public class JsonConfig {
    private Class<?> clazz;
    private Object properties;


    public static String toJsonConfig(Object object) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setClazz(object.getClass());
        jsonConfig.setProperties(object);
        return JSONUtil.toJsonStr(jsonConfig);
    }


    public static <T> T fromJsonConfig(String jsonConfig, Class<T> clazz) {
        return JSONUtil.toBean(jsonConfig, clazz);
    }
}
