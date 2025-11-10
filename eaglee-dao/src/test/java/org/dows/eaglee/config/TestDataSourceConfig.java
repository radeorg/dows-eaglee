package org.dows.eaglee.config;

import com.mybatisflex.core.MybatisFlexBootstrap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 测试数据源配置
 * 
 * @author eaglee-system
 */
@TestConfiguration
public class TestDataSourceConfig {
    
    @Bean
    @Primary
    public DataSource testDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        return new HikariDataSource(config);
    }
    
    @Bean
    public MybatisFlexBootstrap mybatisFlexBootstrap(DataSource dataSource) {
        MybatisFlexBootstrap bootstrap = MybatisFlexBootstrap.getInstance()
            .setDataSource(dataSource);
            //.addMapper("org.dows.eaglee.mapper");
        bootstrap.start();
        return bootstrap;
    }
}