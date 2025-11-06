package com.hina.eaglee.analysis;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

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
    public AnalyseResult analyse(String resource) {
        AnalyseResult result = new AnalyseResult();
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
                result.setReason("日志提取异常");
                return result;
            }
        } else if (resource.startsWith("file://")) {
            //todo 读取文件按内容
        }
        if (StrUtil.isBlank(logContent)) {
            result.setReason("日志内容为空，无法分析原因");
            return result;
        }
        String systemPrompt = """
                你是一个日志分析师，擅长分析大数据任务调度相关的各种异常日志[dolphinscheduler,yarn,spark,flink]，能够根据日志类容推断出是资源异常(值为1)或业务异常(值为2)导致的任务失败，
                资源类异常如：容器分配失败、节点失联、资源不足、网络异常等
                业务类异常如：数据异常，格式/加解密异常，业务校验规则等
                并总结出150字左右的核心错误原因,输出如下json格式:
                {"exceptionType": 1,"reason": "资源类异常，如容器分配失败、节点失联等"}
                """;
        Flux<String> fresult = chatClient.prompt()
                .system(systemPrompt)
                .user(logContent)
                .stream()
                .content();
        // 将结果拼接成字符串
        String content = fresult.reduce("", (acc, next) -> acc + next).block();
        AnalyseResult entries = JSONUtil.toBean(content, AnalyseResult.class);
        log.info("分析结果:{}", content);
        return entries;
    }
}
