package com.hina.eaglee.analysis;

import cn.hutool.core.io.IoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

/**
 * 多线程日志关键字提取工具（支持字符串直接处理，自动去重异常记录）
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class KeywordExtractor {
    // 线程池（一般：核心线程数=CPU核心数，平衡效率与资源）
    private final ThreadPoolExecutor threadPoolExecutor;
    // 默认目标关键字（忽略大小写，支持"exception"、"caused by"、"error"）
    private static final List<String> TARGET_KEYWORDS = Arrays.asList("exception:", "caused by:", "error");

    // 字符串分块大小（字符数，根据日志长度调整，默认10万字符/块）
    private static final int STRING_BLOCK_SIZE = 100000;


    /**
     * 多线程提取日志字符串中含关键字的行（去重相同异常，保持原始顺序）
     *
     * @param logString 原始日志字符串（多行，含换行符）
     * @return 去重后的关键日志行（按原始顺序排列）
     * @throws InterruptedException 线程等待异常
     * @throws ExecutionException   线程执行异常
     */
    public String extractKeyLogFromString(String logString) throws InterruptedException, ExecutionException {
        if (logString == null || logString.trim().isEmpty()) {
            return "";
        }

        // 1. 将日志字符串按行分割，记录原始行号（解决分块时行截断问题）
        List<String> allLines = new ArrayList<>(Arrays.asList(logString.split("\n")));
        int totalLines = allLines.size();
        if (totalLines == 0) {
            return "";
        }

        // 2. 分割行列表为多个块（按行数均分，每个块分配给一个线程）
        List<LineBlock> lineBlocks = splitLinesIntoBlocks(allLines, totalLines);

        // 3. 多线程并行处理每个块，提取关键行
        List<Future<Map<Long, String>>> futures = new ArrayList<>();
        for (LineBlock block : lineBlocks) {
            futures.add(threadPoolExecutor.submit(new LogStringBlockTask(block)));
        }

        // 4. 收集所有线程结果（按行号存储，保证顺序）
        Map<Long, String> allKeyLines = new TreeMap<>(); // TreeMap自动按行号升序
        for (Future<Map<Long, String>> future : futures) {
            allKeyLines.putAll(future.get());
        }

        // 5. 对关键行去重（保留首次出现的行，用LinkedHashSet保证顺序）
        Set<String> uniqueLines = new LinkedHashSet<>(allKeyLines.values());

        // 6. 拼接结果（用系统换行符分隔）
        StringBuilder result = new StringBuilder();
        for (String line : uniqueLines) {
            result.append(line).append(System.lineSeparator());
        }

        return result.toString().trim();
    }

    /**
     * 辅助方法：将行列表分割为多个块（按行数均分）
     */
    private List<LineBlock> splitLinesIntoBlocks(List<String> allLines, int totalLines) {
        List<LineBlock> blocks = new ArrayList<>();
        // 正确获取核心线程数（若核心线程数为0，默认用1个线程）
        int corePoolSize = threadPoolExecutor.getCorePoolSize();
        int threadCount = corePoolSize > 0 ? corePoolSize : 1;
        // 每个块的行数（向上取整）
        int blockLineCount = (int) Math.ceil((double) totalLines / threadCount);
        if (blockLineCount < 1) {
            blockLineCount = 1;
        }

        int startIndex = 0;
        while (startIndex < totalLines) {
            int endIndex = Math.min(startIndex + blockLineCount, totalLines);
            List<String> blockLines = allLines.subList(startIndex, endIndex);
            blocks.add(new LineBlock(blockLines, startIndex));
            startIndex = endIndex;
        }
        return blocks;
    }


    /**
     * 线程任务：处理单个行块，提取含关键字的行
     */
    static class LogStringBlockTask implements Callable<Map<Long, String>> {
        private final LineBlock lineBlock;

        public LogStringBlockTask(LineBlock lineBlock) {
            this.lineBlock = lineBlock;
        }

        @Override
        public Map<Long, String> call() {
            Map<Long, String> keyLines = new HashMap<>();
            List<String> blockLines = lineBlock.blockLines;
            long startLineNum = lineBlock.startLineNum;

            for (int i = 0; i < blockLines.size(); i++) {
                long globalLineNum = startLineNum + i; // 计算全局行号
                String line = blockLines.get(i);
                if (isKeyLine(line)) {
                    keyLines.put(globalLineNum, line);
                }
            }
            return keyLines;
        }

        /**
         * 判断行是否含目标关键字（忽略大小写）
         */
        private boolean isKeyLine(String line) {
            if (line == null || line.trim().isEmpty()) {
                return false;
            }
            String lowerLine = line.toLowerCase();
            return TARGET_KEYWORDS.stream().anyMatch(lowerLine::contains);
        }
    }


    /**
     * 封装行块信息：块内的行列表 + 起始行号（全局）
     */
    static class LineBlock {
        List<String> blockLines; // 块内的行
        long startLineNum;       // 块的起始行号（全局行号，从0开始）

        public LineBlock(List<String> blockLines, long startLineNum) {
            this.blockLines = blockLines;
            this.startLineNum = startLineNum;
        }
    }


    public static void main(String[] args) throws IOException {

        String logFilePath = "C:\\Users\\yingliang_zhang\\Desktop\\ds-log.txt";
        // 线程池（一般：核心线程数=CPU核心数，平衡效率与资源）
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy());

        KeywordExtractor keywordExtractor = new KeywordExtractor(threadPoolExecutor);

        // 1. 校验文件合法性
        Path logPath = Paths.get(logFilePath);
        String testLog = IoUtil.read(Files.newInputStream(logPath), StandardCharsets.UTF_8);

        try {
            long startTime = System.currentTimeMillis();
            String result = keywordExtractor.extractKeyLogFromString(testLog);
            long costTime = System.currentTimeMillis() - startTime;

            System.out.println("=== 去重后的关键日志（耗时：" + costTime + "ms）===");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}