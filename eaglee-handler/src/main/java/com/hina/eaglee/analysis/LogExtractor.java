package com.hina.eaglee.analysis;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 多线程日志关键字提取工具（按行顺序保留，支持大文件加速）
 */
public class LogExtractor {
    // 目标关键字（忽略大小写）
    private static final List<String> TARGET_KEYWORDS = Arrays.asList("exception", "caused by", "error");
    // 线程池（核心线程数=CPU核心数，避免线程过多开销）
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );
    // 每个块的大小（1MB，可根据文件大小调整，建议512KB~4MB）
    private static final long BLOCK_SIZE = 1024 * 1024;


    /**
     * 多线程提取日志中含关键字的行（保持原始行顺序）
     * @param logFilePath 日志文件路径（如 "C:\\Users\\yingliang_zhang\\Desktop\\ds-log.txt"）
     * @return 按原始顺序排列的关键日志行（拼接为字符串）
     * @throws IOException  文件操作异常
     * @throws InterruptedException 线程等待异常
     * @throws ExecutionException 线程执行异常
     */
    public static String extractKeyLogLines(String logFilePath) throws IOException, InterruptedException, ExecutionException {
        // 1. 校验文件合法性
        Path logPath = Paths.get(logFilePath);
        if (!Files.exists(logPath) || !Files.isRegularFile(logPath)) {
            return "日志文件不存在";
            //throw new FileNotFoundException("日志文件不存在或不是常规文件：" + logFilePath);
        }
        long fileTotalSize = Files.size(logPath);
        if (fileTotalSize == 0) {
            return ""; // 空文件直接返回
        }

        // 2. 分割文件为多个块（确保每个块起始是完整行）
        List<FileBlock> fileBlocks = splitFileIntoBlocks(logPath, fileTotalSize);

        // 3. 提交线程任务，并行处理每个块
        List<Future<Map<Long, String>>> futures = new ArrayList<>();
        for (FileBlock block : fileBlocks) {
            futures.add(THREAD_POOL.submit(new LogBlockTask(logPath, block)));
        }

        // 4. 收集所有线程结果（线程安全集合）
        Map<Long, String> allKeyLines = new ConcurrentHashMap<>();
        for (Future<Map<Long, String>> future : futures) {
            Map<Long, String> blockResult = future.get(); // 等待线程完成并获取结果
            allKeyLines.putAll(blockResult);
        }

        // 5. 按行号排序，保持原始顺序
        List<Map.Entry<Long, String>> sortedEntries = new ArrayList<>(allKeyLines.entrySet());
        sortedEntries.sort(Comparator.comparingLong(Map.Entry::getKey)); // 按行号升序

        // 6. 拼接结果（每行用系统换行符分隔）
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Long, String> entry : sortedEntries) {
            result.append(entry.getValue()).append(System.lineSeparator());
        }

        // 7. 关闭线程池（优雅关闭）
        THREAD_POOL.shutdown();
        if (!THREAD_POOL.awaitTermination(5, TimeUnit.SECONDS)) {
            THREAD_POOL.shutdownNow();
        }

        return result.toString().trim(); // 去除末尾多余换行
    }


    /**
     * 辅助方法：将文件分割为多个块（确保块起始是完整行，避免截断行）
     * @param logPath 日志文件路径
     * @param fileTotalSize 文件总大小
     * @return 分块列表（含每个块的起始位置、结束位置、起始行号）
     * @throws IOException 文件读取异常
     */
    private static List<FileBlock> splitFileIntoBlocks(Path logPath, long fileTotalSize) throws IOException {
        List<FileBlock> blocks = new ArrayList<>();
        long currentStart = 0;
        AtomicLong currentLineNum = new AtomicLong(0); // 记录全局行号

        // 第一次读取：计算每个块的起始行号（确保块起始是完整行）
        try (RandomAccessFile raf = new RandomAccessFile(logPath.toFile(), "r");
             BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(raf.getFD()), StandardCharsets.UTF_8))) {

            while (currentStart < fileTotalSize) {
                long currentEnd = Math.min(currentStart + BLOCK_SIZE, fileTotalSize);

                // 移动到当前块的结束位置，寻找最后一个换行符（避免截断行）
                raf.seek(currentEnd);
                if (currentEnd < fileTotalSize) {
                    // 从currentEnd向前找换行符，确保块结束在换行符后
                    while (currentEnd < fileTotalSize && raf.read() != '\n') {
                        currentEnd++;
                    }
                    currentEnd++; // 跳过换行符，下一个块从新行开始
                }

                // 计算当前块的起始行号（读取currentStart到currentEnd之间的行数）
                long startLineNum = currentLineNum.get();
                raf.seek(currentStart);
                String line;
                while ((line = reader.readLine()) != null && raf.getFilePointer() <= currentEnd) {
                    currentLineNum.incrementAndGet();
                }

                // 添加块信息（起始位置、结束位置、起始行号）
                blocks.add(new FileBlock(currentStart, currentEnd, startLineNum));
                currentStart = currentEnd;
            }
        }
        return blocks;
    }


    /**
     * 线程任务：处理单个文件块，读取行并过滤关键日志
     */
    static class LogBlockTask implements Callable<Map<Long, String>> {
        private final Path logPath;
        private final FileBlock block;

        public LogBlockTask(Path logPath, FileBlock block) {
            this.logPath = logPath;
            this.block = block;
        }

        @Override
        public Map<Long, String> call() throws Exception {
            Map<Long, String> keyLines = new HashMap<>(); // 存储（行号，日志行）
            long currentLineNum = block.startLineNum; // 从块的起始行号开始计数

            try (RandomAccessFile raf = new RandomAccessFile(logPath.toFile(), "r");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(raf.getFD()), StandardCharsets.UTF_8))) {

                // 移动到当前块的起始位置
                raf.seek(block.startPos);

                String line;
                // 读取块内的所有行（直到块结束位置）
                while ((line = reader.readLine()) != null && raf.getFilePointer() <= block.endPos) {
                    // 过滤含关键字的行（忽略大小写）
                    if (isKeyLine(line)) {
                        keyLines.put(currentLineNum, line);
                    }
                    currentLineNum++;
                }
            }
            return keyLines;
        }

        /**
         * 校验行是否含目标关键字（忽略大小写）
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
     * 封装文件块信息：起始位置、结束位置、起始行号
     */
    static class FileBlock {
        long startPos;    // 块在文件中的起始字节位置
        long endPos;      // 块在文件中的结束字节位置
        long startLineNum;// 块的起始行号（全局行号）

        public FileBlock(long startPos, long endPos, long startLineNum) {
            this.startPos = startPos;
            this.endPos = endPos;
            this.startLineNum = startLineNum;
        }
    }


    // ------------------- 测试示例 -------------------
    public static void main(String[] args) {
        // 测试：替换为你的日志文件路径
        String logFilePath = "C:\\Users\\yingliang_zhang\\Desktop\\ds-log.txt";

        try {
            long startTime = System.currentTimeMillis();
            String keyLogs = extractKeyLogLines(logFilePath);
            long costTime = System.currentTimeMillis() - startTime;

            // 输出结果
            System.out.println("=== 提取的关键日志（耗时：" + costTime + "ms）===");
            System.out.println(keyLogs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}