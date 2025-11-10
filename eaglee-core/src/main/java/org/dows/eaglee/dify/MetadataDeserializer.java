package org.dows.eaglee.dify;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MetadataDeserializer extends JsonDeserializer<Metadata> {
    @Override
    public Metadata deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        String jsonString = p.getValueAsString();
        // 解析 JSON 字符串并创建 Metadata 实例
        return JSONUtil.parseObj(jsonString).toBean(Metadata.class);
    }
}
