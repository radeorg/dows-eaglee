package com.hina.eaglee.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Swagger API文档配置类
 * 
 * @author dows
 * @version 1.0.0
 */
@Configuration
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${eaglee.app.name:鹰眼监控系统}")
    private String appName;

    @Value("${eaglee.app.version:1.0.0}")
    private String appVersion;

    @Value("${eaglee.app.description:鹰眼监控系统 - 企业级任务监控和性能分析平台}")
    private String appDescription;

    @Value("${eaglee.app.contact.name:Dows Support}")
    private String contactName;

    @Value("${eaglee.app.contact.email:support@dows.com}")
    private String contactEmail;

    @Value("${eaglee.app.contact.url:https://www.dows.com}")
    private String contactUrl;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

//    @Bean
//    public SchemaCustomizer localDateTimeSchemaCustomizer() {
//        // 自定义LocalDateTime类型的Schema
//        return (schema, type) -> {
//            // 判断当前处理的类型是否为LocalDateTime
//            if (type.getRawClass().equals(LocalDateTime.class)) {
//                // 设置Swagger文档中显示的格式说明
//                schema.setPattern(DATE_TIME_PATTERN);
//                // 设置示例值（按指定格式生成一个示例）
//                String example = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
//                schema.setExample(example);
//                // 可选：设置描述，提示用户格式
//                schema.setDescription("时间格式：" + DATE_TIME_PATTERN);
//            }
//        };
//    }

    /**
     * 创建OpenAPI配置
     */
    @Bean
    public OpenAPI createOpenAPI() {

        /*// 定义LocalDateTime类型的Schema
        Schema<LocalDateTime> localDateTimeSchema = new Schema<>();
        // 支持的格式说明
        localDateTimeSchema.setDescription("支持的时间格式：\n" +
                "1. yyyy-MM-dd HH:mm:ss（如2025-10-30 09:15:36）\n" +
                "2. yyyy-MM-dd'T'HH:mm:ss.SSSZ（如2025-10-30T09:15:36.485Z）");
        // 示例值
        String example1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String example2 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
        localDateTimeSchema.setExample(example1 + " 或 " + example2);

        // 将自定义Schema注册到全局
        Map<String, Schema> schemas = new HashMap<>();
        schemas.put("LocalDateTime", localDateTimeSchema);*/

        return new OpenAPI()
                //.components(new io.swagger.v3.oas.models.Components().schemas(schemas))
                .info(createApiInfo())
                .servers(createServers());
    }

    /**
     * 创建API信息
     */
    private Info createApiInfo() {
        return new Info()
                .title(appName + " API")
                .description(appDescription + "\n\n" +
                        "## 功能特性\n" +
                        "- 任务项目生命周期管理\n" +
                        "- 任务实例实时监控\n" +
                        "- 系统资源数据采集\n" +
                        "- 多维度性能度量分析\n" +
                        "- 灵活的配置管理\n" +
                        "- 完整的REST API支持\n\n" +
                        "## 技术栈\n" +
                        "- Spring Boot 3.x\n" +
                        "- JDK 21\n" +
                        "- MyBatis-Flex\n" +
                        "- MySQL 8.0\n" +
                        "- Maven 多模块架构\n\n" +
                        "当前环境: " + activeProfile)
                .version(appVersion)
                .contact(new Contact()
                        .name(contactName)
                        .email(contactEmail)
                        .url(contactUrl))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }

    /**
     * 创建服务器列表
     */
    private List<Server> createServers() {
        List<Server> servers = new ArrayList<>();
        
        // 当前环境服务器
        String currentUrl = "http://localhost:" + serverPort + contextPath;
        String currentDescription = getEnvironmentDescription(activeProfile);
        servers.add(new Server().url(currentUrl).description(currentDescription));
        
        // 根据环境添加其他服务器
        if ("dev".equals(activeProfile)) {
            servers.add(new Server()
                    .url("http://hinadt.com/eaglee/dev")
                    .description("开发环境"));
        } else if ("prd".equals(activeProfile)) {
            servers.add(new Server()
                    .url("http://hinadt.com/eaglee/prd")
                    .description("生产环境"));
        }
        return servers;
    }

    /**
     * 获取环境描述
     */
    private String getEnvironmentDescription(String profile) {
        return switch (profile) {
            case "dev" -> "开发环境";
            case "prd" -> "生产环境";
            default -> "默认环境";
        };
    }
}