package com.hina.eaglee.analysis;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogAnalysis implements Analysable {
    private final ChatClient chatClient;

    private final RestTemplate restTemplate;

    private final KeywordExtractor keywordExtractor;

    /**
     * 分析原因,ai分析
     * - 资源类异常（如：容器分配失败、节点失联等）：自动重试，间隔1分钟
     * - 业务类异常（如：数据异常，格式/加解密异常，业务校验规则等）：不自动重试，需人工介入排查
     * @return
     */
    @Override
    public String analyse(String resource) {
        String logContent = null;
        if (resource.startsWith("txt://")) {
            // 直接赋值字符串内容
            logContent = resource.substring(6);
        } else if (resource.startsWith("http://") || resource.startsWith("https://")) {
            logContent = restTemplate.getForObject(resource, String.class);
            try {
                //  todo 这里可以通多配置对日志进行提取关键字
                logContent = keywordExtractor.extractKeyLogFromString(logContent);
            } catch (Exception e) {
                log.error("日志提取异常", e);
                return "日志提取异常";
            }
        } else if (resource.startsWith("file://")) {
            //todo 读取文件按内容
        }
        if (StrUtil.isBlank(logContent)) {
            return "日志内容为空，无法分析原因";
        }
        Flux<String> result = chatClient.prompt()
                .system("你是一个分析日志的AI，擅长分析大数据任务调度相关的各种异常(dolphinscheduler,yarn,spark,flink)，判断是否是资源类异常或业务类异常，并总结输出概要原因")
                .user(String.format("分析日志:%s,判断是否是资源类异常或业务类异常,并总结输出概要原因", logContent))
                .stream()
                .content();
        // 将结果拼接成字符串
        String content = result.reduce("", (acc, next) -> acc + next).block();
        log.info("分析结果:{}", content);
        return content;
    }
}
