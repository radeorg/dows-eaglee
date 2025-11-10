package org.dows.eaglee.dify;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class AnswerDeserializer extends JsonDeserializer<Answer> {
    @Override
    public Answer deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        String jsonString = p.getValueAsString();
        // 解析 JSON 字符串并创建 Answer 实例
        return JSONUtil.parseObj(jsonString).toBean(Answer.class);
    }
}