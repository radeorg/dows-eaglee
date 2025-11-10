//package org.dows.eaglee.config;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
//public class MultiFormatLocalDateTimeConfig {
//
//    // 1. 定义支持的格式列表（按优先级排序，优先尝试前面的格式）
//    private static final List<DateTimeFormatter> SUPPORTED_FORMATTERS = Arrays.asList(
//        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),  // 空格分隔格式
//        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"),  // ISO带毫秒和时区（如2025-10-30T09:15:36.485Z）
//        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"),  // ISO不带毫秒带时区（如2025-10-30T09:15:36Z）
//        DateTimeFormatter.ISO_LOCAL_DATE_TIME  // 基础ISO格式（如2025-10-30T09:15:36）
//    );
//
//    // 2. 定义序列化格式（后端返回给前端的格式，可选一种主要格式）
//    private static final DateTimeFormatter SERIALIZE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        JavaTimeModule module = new JavaTimeModule();
//
//        // 配置反序列化器：支持多种格式解析
//        module.addDeserializer(LocalDateTime.class, new StdDeserializer<LocalDateTime>(LocalDateTime.class) {
//            @Override
//            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//                String dateStr = p.getValueAsString();
//                if (dateStr == null || dateStr.trim().isEmpty()) {
//                    return null; // 允许空值
//                }
//
//                // 依次尝试支持的格式，直到解析成功
//                for (DateTimeFormatter formatter : SUPPORTED_FORMATTERS) {
//                    try {
//                        return LocalDateTime.parse(dateStr, formatter);
//                    } catch (Exception e) {
//                        // 解析失败，继续尝试下一种格式
//                    }
//                }
//
//                // 所有格式都不匹配时，抛出明确的错误提示
//                throw new IOException("不支持的时间格式：" + dateStr + "，支持格式：" + SUPPORTED_FORMATTERS);
//            }
//        });
//
//        // 配置序列化器：后端返回固定格式
//        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(SERIALIZE_FORMATTER));
//
//        objectMapper.registerModule(module);
//        return objectMapper;
//    }
//}