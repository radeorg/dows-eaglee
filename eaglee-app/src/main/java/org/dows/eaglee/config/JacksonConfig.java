//package org.dows.eaglee.config;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class JacksonConfig {
//
//    // 定义统一的时间格式（根据业务需求修改）
//    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
////    @Bean
////    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
////        return builder -> {
////            // 序列化：LocalDateTime -> String（响应给前端的格式）
////            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
////            // 反序列化：String -> LocalDateTime（接收前端请求的格式）
////            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
////        };
////    }
//    // 定义支持的时间格式列表
//    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
//        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
//        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
//        //DateTimeFormatter.ISO_LOCAL_DATE_TIME  // 额外支持ISO标准格式（无时区）
//    );
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        JavaTimeModule module = new JavaTimeModule();
//        // 自定义LocalDateTime反序列化器，支持多种格式
//        module.addDeserializer(LocalDateTime.class, new StdDeserializer<LocalDateTime>(LocalDateTime.class) {
//            @Override
//            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//                String dateStr = p.getValueAsString();
//                for (DateTimeFormatter formatter : FORMATTERS) {
//                    try {
//                        return LocalDateTime.parse(dateStr, formatter);
//                    } catch (Exception e) {
//                        // 尝试下一种格式
//                    }
//                }
//                // 所有格式都不匹配时，抛出异常
//                throw new IOException("无法解析时间格式：" + dateStr + "，支持的格式：" + FORMATTERS);
//            }
//        });
//        objectMapper.registerModule(module);
//        return objectMapper;
//    }
//}