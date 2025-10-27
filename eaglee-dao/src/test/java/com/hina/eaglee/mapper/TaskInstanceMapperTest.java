//package com.hina.eaglee.mapper;
//
//import com.hina.eaglee.config.TestDataSourceConfig;
//import com.hina.eaglee.entity.TaskInstanceEntity;
//import com.mybatisflex.core.paginate.Page;
//import com.mybatisflex.core.query.QueryWrapper;
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
// * TaskInstanceMapper单元测试
// *
// * @author eaglee-system
// */
//@SpringBootTest
//@Import(TestDataSourceConfig.class)
//@ActiveProfiles("test")
//@Transactional
//class TaskInstanceMapperTest {
//
//    @Resource
//    private TaskInstanceMapper taskInstanceMapper;
//
//    private TaskInstanceEntity testInstance;
//
//    @BeforeEach
//    void setUp() {
//        // 创建测试数据
//        testInstance = new TaskInstanceEntity();
//        testInstance.setTaskProjectId(1L);
//        testInstance.setTaskName("测试任务");
//        testInstance.setTaskIdentifier("test-task-001");
//        testInstance.setApplicationId("app-001");
//        testInstance.setProcessName("测试流程");
//        testInstance.setReason("测试原因");
//        testInstance.setAvgTime(1000L);
//        testInstance.setElapsedTime(500L);
//        testInstance.setDuration(0L);
//        testInstance.setRetried(0);
//        testInstance.setState(0);
//        testInstance.setStartTime(LocalDateTime.now());
//        testInstance.setCt(LocalDateTime.now());
//        testInstance.setUt(LocalDateTime.now());
//        testInstance.setCid(1L);
//        testInstance.setDeleted(false);
//    }
//
//    @Test
//    void testInsertAndSelectById() {
//        // 插入测试数据
//        int result = taskInstanceMapper.insert(testInstance);
//        assertEquals(1, result);
//        assertNotNull(testInstance.getTaskInstanceId());
//
//        // 根据ID查询
//        TaskInstanceEntity found = taskInstanceMapper.selectOneById(testInstance.getTaskInstanceId());
//        assertNotNull(found);
//        assertEquals(testInstance.getTaskName(), found.getTaskName());
//        assertEquals(testInstance.getTaskIdentifier(), found.getTaskIdentifier());
//    }
//
//    @Test
//    void testSelectByTaskProjectId() {
//        // 插入测试数据
//        taskInstanceMapper.insert(testInstance);
//
//        // 根据任务项目ID查询
//        List<TaskInstanceEntity> instances = taskInstanceMapper.selectByTaskProjectId(testInstance.getTaskProjectId());
//        assertFalse(instances.isEmpty());
//        assertTrue(instances.stream().anyMatch(i -> i.getTaskIdentifier().equals(testInstance.getTaskIdentifier())));
//    }
//
//    @Test
//    void testSelectByTaskIdentifier() {
//        // 插入测试数据
//        taskInstanceMapper.insert(testInstance);
//
//        // 根据任务标识查询
//        List<TaskInstanceEntity> instances = taskInstanceMapper.selectByTaskIdentifier(testInstance.getTaskIdentifier());
//        assertFalse(instances.isEmpty());
//        assertEquals(testInstance.getTaskName(), instances.get(0).getTaskName());
//    }
//
//    @Test
//    void testUpdateStateAndEndTime() {
//        // 插入测试数据
//        taskInstanceMapper.insert(testInstance);
//
//        // 更新状态和结束时间
//        LocalDateTime endTime = LocalDateTime.now();
//        Long duration = 2000L;
//        int result = taskInstanceMapper.updateStateAndEndTime(
//            testInstance.getTaskInstanceId(), 1, endTime, duration);
//        assertEquals(1, result);
//
//        // 验证更新结果
//        TaskInstanceEntity updated = taskInstanceMapper.selectOneById(testInstance.getTaskInstanceId());
//        assertEquals(1, updated.getState());
//        assertNotNull(updated.getEndTime());
//        assertEquals(duration, updated.getDuration());
//    }
//
//    @Test
//    void testUpdateAvgTime() {
//        // 插入测试数据
//        taskInstanceMapper.insert(testInstance);
//
//        // 更新平均耗时
//        Long newAvgTime = 1500L;
//        int result = taskInstanceMapper.updateAvgTime(testInstance.getTaskInstanceId(), newAvgTime);
//        assertEquals(1, result);
//
//        // 验证更新结果
//        TaskInstanceEntity updated = taskInstanceMapper.selectOneById(testInstance.getTaskInstanceId());
//        assertEquals(newAvgTime, updated.getAvgTime());
//    }
//
//    @Test
//    void testInsertBatch() {
//        // 创建批量测试数据
//        TaskInstanceEntity instance2 = new TaskInstanceEntity();
//        instance2.setTaskProjectId(1L);
//        instance2.setTaskName("测试任务2");
//        instance2.setTaskIdentifier("test-task-002");
//        instance2.setApplicationId("app-002");
//        instance2.setProcessName("测试流程2");
//        instance2.setState(0);
//        instance2.setStartTime(LocalDateTime.now());
//        instance2.setCt(LocalDateTime.now());
//        instance2.setUt(LocalDateTime.now());
//        instance2.setCid(1L);
//        instance2.setDeleted(false);
//
//        List<TaskInstanceEntity> instances = Arrays.asList(testInstance, instance2);
//
//        // 批量插入
//        int result = taskInstanceMapper.insertBatch(instances);
//        assertEquals(2, result);
//    }
//
//    @Test
//    void testSelectPageByQuery() {
//        // 插入测试数据
//        taskInstanceMapper.insert(testInstance);
//
//        // 分页查询
//        Page<TaskInstanceEntity> page = new Page<>(1, 10);
//        QueryWrapper queryWrapper = QueryWrapper.create()
//            .eq("deleted", false);
//
//        Page<TaskInstanceEntity> result = taskInstanceMapper.selectPageByQuery(page, queryWrapper);
//        assertNotNull(result);
//        assertTrue(result.getTotalRow() > 0);
//        assertFalse(result.getRecords().isEmpty());
//    }
//
//    @Test
//    void testSelectRunningInstances() {
//        // 插入正在运行的任务实例
//        testInstance.setState(0); // 0表示运行中
//        taskInstanceMapper.insert(testInstance);
//
//        // 查询正在运行的任务实例
//        List<TaskInstanceEntity> runningInstances = taskInstanceMapper.selectRunningInstances();
//        assertFalse(runningInstances.isEmpty());
//        assertTrue(runningInstances.stream().anyMatch(i -> i.getState() == 0));
//    }
//}