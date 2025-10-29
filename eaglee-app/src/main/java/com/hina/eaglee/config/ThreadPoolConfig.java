package com.hina.eaglee.config;

import java.util.concurrent.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadPoolConfig {

    /**
     * 配置一个固定大小的线程池，用于处理异步任务
     * 
     * @return Executor 用于执行异步任务
     */
    @Bean(name = "threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
            10, // 核心线程数
            20, // 最大线程数
            60L, // 空闲线程存活时间（秒）
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), // 阻塞队列容量
            new NamedThreadFactory("Task-"), // 自定义线程工厂
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );
    }

    /**
     * 自定义线程工厂，为每个线程设置名称
     */
    private static class NamedThreadFactory implements ThreadFactory {
        private final String prefix;

        public NamedThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(prefix + Thread.currentThread().getId());
            thread.setDaemon(true);
            return thread;
        }
    }
}