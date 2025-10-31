package com.hina.eaglee.config;

import java.util.concurrent.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10); // 设置线程池大小
        scheduler.setThreadNamePrefix("scheduled-task-"); // 线程名前缀
        scheduler.setAwaitTerminationSeconds(60); // 等待任务完成时间（秒）
        scheduler.setWaitForTasksToCompleteOnShutdown(true); // 关闭时等待任务完成
        scheduler.setRemoveOnCancelPolicy(true); // 取消任务后立即移除
        return scheduler;
    }

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