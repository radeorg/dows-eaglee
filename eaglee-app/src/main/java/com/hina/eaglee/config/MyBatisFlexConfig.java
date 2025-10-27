package com.hina.eaglee.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.ConsoleMessageCollector;
import com.mybatisflex.core.audit.MessageCollector;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Flex配置类
 * 
 * @author dows
 * @version 1.0.0
 */
@MapperScan("com.hina.eaglee.mapper")
@Configuration
public class MyBatisFlexConfig implements MyBatisFlexCustomizer {

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        // 开启审计功能
        AuditManager.setAuditEnable(true);
        
        // 设置 SQL 审计收集器
        MessageCollector collector = new ConsoleMessageCollector();
        AuditManager.setMessageCollector(collector);
        
        // 配置逻辑删除
        globalConfig.setLogicDeleteColumn("deleted");
        
        // 配置乐观锁
        globalConfig.setVersionColumn("version");
        
        // 打印 MyBatis-Flex 的 LOGO
        globalConfig.setPrintBanner(false);
    }

    /**
     * 自定义消息收集器
     */
    @Bean
    public MessageCollector messageCollector() {
        return new ConsoleMessageCollector();
    }
}