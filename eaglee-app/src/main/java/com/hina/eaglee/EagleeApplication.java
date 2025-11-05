package com.hina.eaglee;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.TimeZone;

@SpringBootApplication
public class EagleeApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    public static void main(String[] args) {

        String property = System.getProperty("user.home");
        Dotenv dotenv = Dotenv.configure()
                .directory(property + File.separator + "env") // 指定 env 目录路径
                .ignoreIfMissing()
                .load();
        // 将 .env 文件中的键值对设为系统属性
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(EagleeApplication.class, args);
    }
}
