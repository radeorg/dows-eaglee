//package org.dows.eaglee.mapper;
//
//import config.org.dows.eaglee.TestDataSourceConfig;
//import entity.org.dows.eaglee.TaskCounterEntity;
//import jakarta.annotation.Resource;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * TaskCounterMapper单元测试
// *
// * @author eaglee-system
// */
//@SpringBootTest
//@Import(TestDataSourceConfig.class)
//@ActiveProfiles("test")
//@Transactional
//class TaskCounterMapperTest {
//
//    @Resource
//    private TaskCounterMapper taskCounterMapper;
//
//    private TaskCounterEntity testCounter;
//
//    @BeforeEach
//    void setUp() {
//        // 创建测试数据
//        testCounter = new TaskCounterEntity();
//        testCounter.setTaskIdentifier("test-task-001");
//        testCounter.setTotalCount(10);
//        testCounter.setSuccessCount(8);
//        testCounter.setFailureCount(2);
//        testCounter.setTotalTime(10000L);
//        testCounter.setAvgTime(1000L);
//        testCounter.setMinTime(500L);
//        testCounter.setMaxTime(2000L);
//        testCounter.setCt(LocalDateTime.now());
//        testCounter.setUt(LocalDateTime.now());
//        testCounter.setDeleted(false);
//    }
//
//    @Test
//    void testInsertAndSelectById() {
//        // 插入测试数据
//        int result = taskCounterMapper.insert(testCounter);
//        assertEquals(1, result);
//        assertNotNull(testCounter.getTaskCounterId());
//
//        // 根据ID查询
//        TaskCounterEntity found = taskCounterMapper.selectOneById(testCounter.getTaskCounterId());
//        assertNotNull(found);
//        assertEquals(testCounter.getTaskIdentifier(), found.getTaskIdentifier());
//        assertEquals(testCounter.getTotalCount(), found.getTotalCount());
//    }
//
//    @Test
//    void testSelectByTaskIdentifier() {
//        // 插入测试数据
//        taskCounterMapper.insert(testCounter);
//
//        // 根据任务标识查询
//        TaskCounterEntity found = taskCounterMapper.selectByTaskIdentifier(testCounter.getTaskIdentifier());
//        assertNotNull(found);
//        assertEquals(testCounter.getAvgTime(), found.getAvgTime());
//    }
//
//    @Test
//    void testIncrementCounter() {
//        // 插入测试数据
//        taskCounterMapper.insert(testCounter);
//
//        // 增量更新计数器
//        int result = taskCounterMapper.incrementCounter(testCounter.getTaskIdentifier(), true, 1500L);
//        assertEquals(1, result);
//
//        // 验证更新结果
//        TaskCounterEntity updated = taskCounterMapper.selectByTaskIdentifier(testCounter.getTaskIdentifier());
//        assertEquals(testCounter.getTotalCount() + 1, updated.getTotalCount());
//        assertEquals(testCounter.getSuccessCount() + 1, updated.getSuccessCount());
//    }
//
//    @Test
//    void testUpdateMinMaxTime() {
//        // 插入测试数据
//        taskCounterMapper.insert(testCounter);
//
//        // 更新最小最大耗时
//        Long newDuration = 3000L;
//        int result = taskCounterMapper.updateMinMaxTime(testCounter.getTaskIdentifier(), newDuration);
//        assertEquals(1, result);
//
//        // 验证更新结果
//        TaskCounterEntity updated = taskCounterMapper.selectByTaskIdentifier(testCounter.getTaskIdentifier());
//        assertEquals(newDuration, updated.getMaxTime());
//    }
//
//    @Test
//    void testSelectByTaskIdentifiers() {
//        // 插入测试数据
//        taskCounterMapper.insert(testCounter);
//
//        // 根据任务标识列表查询
//        List<String> identifiers = Arrays.asList(testCounter.getTaskIdentifier());
//        List<TaskCounterEntity> counters = taskCounterMapper.selectByTaskIdentifiers(identifiers);
//
//        assertFalse(counters.isEmpty());
//        assertEquals(testCounter.getTaskIdentifier(), counters.get(0).getTaskIdentifier());
//    }
//
//    @Test
//    void testSelectActiveCounters() {
//        // 插入测试数据
//        testCounter.setDeleted(false);
//        taskCounterMapper.insert(testCounter);
//
//        // 查询活跃的计数器
//        List<TaskCounterEntity> activeCounters = taskCounterMapper.selectActiveCounters();
//        assertFalse(activeCounters.isEmpty());
//        assertTrue(activeCounters.stream().noneMatch(TaskCounterEntity::getDeleted));
//    }
//
//    @Test
//    void testResetCounter() {
//        // 插入测试数据
//        taskCounterMapper.insert(testCounter);
//
//        // 重置计数器
//        int result = taskCounterMapper.resetCounter(testCounter.getTaskIdentifier());
//        assertEquals(1, result);
//
//        // 验证重置结果
//        TaskCounterEntity reset = taskCounterMapper.selectByTaskIdentifier(testCounter.getTaskIdentifier());
//        assertEquals(0, reset.getTotalCount());
//        assertEquals(0, reset.getSuccessCount());
//        assertEquals(0, reset.getFailureCount());
//    }
//
//    @Test
//    void testRecalculateAvgTime() {
//        // 插入测试数据
//        taskCounterMapper.insert(testCounter);
//
//        // 重新计算平均耗时
//        int result = taskCounterMapper.recalculateAvgTime(testCounter.getTaskIdentifier());
//        assertEquals(1, result);
//
//        // 验证计算结果
//        TaskCounterEntity recalculated = taskCounterMapper.selectByTaskIdentifier(testCounter.getTaskIdentifier());
//        assertNotNull(recalculated.getAvgTime());
//    }
//}