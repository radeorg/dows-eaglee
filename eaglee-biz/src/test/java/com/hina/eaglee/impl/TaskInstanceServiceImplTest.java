//package com.hina.eaglee.impl;
//
//import com.hina.cloud.eaglee.api.dto.request.TaskInstanceSaveRequest;
//import com.hina.cloud.eaglee.api.dto.response.TaskInstanceResponse;
//import com.hina.cloud.eaglee.api.dto.response.TaskInstanceStatistics;
//import com.hina.eaglee.entity.TaskCounterEntity;
//import com.hina.eaglee.entity.TaskInstanceEntity;
//import com.hina.eaglee.entity.TaskProjectEntity;
//import com.hina.eaglee.mapper.TaskCounterMapper;
//import com.hina.eaglee.mapper.TaskInstanceMapper;
//import com.hina.eaglee.mapper.TaskProjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
///**
// * TaskInstanceService实现类单元测试
// *
// * @author eaglee-system
// */
//@ExtendWith(MockitoExtension.class)
//class TaskInstanceServiceImplTest {
//
//    @Mock
//    private TaskInstanceMapper taskInstanceMapper;
//
//    @Mock
//    private TaskCounterMapper taskCounterMapper;
//
//    @Mock
//    private TaskProjectMapper taskProjectMapper;
//
//    @InjectMocks
//    private TaskInstanceServiceImpl taskInstanceService;
//
//    private TaskProjectEntity mockTaskProject;
//    private TaskInstanceEntity mockTaskInstance;
//    private TaskCounterEntity mockTaskCounter;
//    private TaskInstanceSaveRequest mockSaveRequest;
//
//    @BeforeEach
//    void setUp() {
//        // 初始化模拟数据
//        mockTaskProject = new TaskProjectEntity();
//        mockTaskProject.setTaskProjectId(1L);
//        mockTaskProject.setProjectName("测试项目");
//        mockTaskProject.setDeleted(false);
//
//        mockTaskInstance = new TaskInstanceEntity();
//        mockTaskInstance.setTaskInstanceId(1L);
//        mockTaskInstance.setTaskProjectId(1L);
//        mockTaskInstance.setTaskName("测试任务");
//        mockTaskInstance.setTaskIdentifier("test-task");
//        mockTaskInstance.setState(0);
//        mockTaskInstance.setRetried(0);
//        mockTaskInstance.setDeleted(false);
//        mockTaskInstance.setCt(LocalDateTime.now());
//        mockTaskInstance.setUt(LocalDateTime.now());
//
//        mockTaskCounter = new TaskCounterEntity();
//        mockTaskCounter.setTaskCounterId(1L);
//        mockTaskCounter.setTaskIdentifier("test-task");
//        mockTaskCounter.setTotalCount(10);
//        mockTaskCounter.setSuccessCount(8);
//        mockTaskCounter.setFailureCount(2);
//        mockTaskCounter.setAvgTime(5000L);
//        mockTaskCounter.setMinTime(2000L);
//        mockTaskCounter.setMaxTime(8000L);
//        mockTaskCounter.setTotalTime(50000L);
//        mockTaskCounter.setDeleted(false);
//
//        mockSaveRequest = new TaskInstanceSaveRequest();
//        mockSaveRequest.setTaskProjectId(1L);
//        mockSaveRequest.setTaskName("测试任务");
//        mockSaveRequest.setTaskIdentifier("test-task");
//        mockSaveRequest.setState(0);
//    }
//
//    @Test
//    void testCreate_Success() {
//        // 准备测试数据
//        when(taskProjectMapper.selectOneById(1L)).thenReturn(mockTaskProject);
//        when(taskCounterMapper.selectByTaskIdentifier("test-task")).thenReturn(mockTaskCounter);
//        when(taskInstanceMapper.insert(any(TaskInstanceEntity.class))).thenReturn(1);
//
//        // 执行测试
//        TaskInstanceResponse response = taskInstanceService.create(mockSaveRequest);
//
//        // 验证结果
//        assertNotNull(response);
//        assertEquals("测试任务", response.getTaskName());
//        assertEquals("test-task", response.getTaskIdentifier());
//        assertEquals("测试项目", response.getProjectName());
//
//        // 验证方法调用
//        verify(taskProjectMapper).selectOneById(1L);
//        verify(taskCounterMapper).selectByTaskIdentifier("test-task");
//        verify(taskInstanceMapper).insert(any(TaskInstanceEntity.class));
//    }
//
//    @Test
//    void testStartTask_Success() {
//        // 准备测试数据
//        when(taskInstanceMapper.selectOneById(1L)).thenReturn(mockTaskInstance);
//        when(taskInstanceMapper.update(any(TaskInstanceEntity.class))).thenReturn(1);
//
//        // 执行测试
//        boolean result = taskInstanceService.startTask(1L);
//
//        // 验证结果
//        assertTrue(result);
//
//        // 验证方法调用
//        verify(taskInstanceMapper).selectOneById(1L);
//        verify(taskInstanceMapper).update(any(TaskInstanceEntity.class));
//    }
//
//    @Test
//    void testCompleteTask_Success() {
//        // 准备测试数据
//        mockTaskInstance.setStartTime(LocalDateTime.now().minusMinutes(5));
//        when(taskInstanceMapper.selectOneById(1L)).thenReturn(mockTaskInstance);
//        when(taskInstanceMapper.updateStateAndEndTime(anyLong(), anyInt(), any(LocalDateTime.class), anyLong())).thenReturn(1);
//        when(taskCounterMapper.selectByTaskIdentifier("test-task")).thenReturn(mockTaskCounter);
//        when(taskCounterMapper.incrementCounter(anyString(), anyBoolean(), anyLong())).thenReturn(1);
//        when(taskCounterMapper.recalculateAvgTime(anyString())).thenReturn(1);
//        when(taskCounterMapper.updateMinMaxTime(anyString(), anyLong())).thenReturn(1);
//        when(taskInstanceMapper.updateAvgTime(anyLong(), anyLong())).thenReturn(1);
//
//        // 执行测试
//        boolean result = taskInstanceService.completeTask(1L, true, null);
//
//        // 验证结果
//        assertTrue(result);
//
//        // 验证方法调用
//        verify(taskInstanceMapper).selectOneById(1L);
//        verify(taskInstanceMapper).updateStateAndEndTime(anyLong(), anyInt(), any(LocalDateTime.class), anyLong());
//    }
//
//    @Test
//    void testCalculateCurrentElapsedTime_RunningTask() {
//        // 准备测试数据
//        mockTaskInstance.setStartTime(LocalDateTime.now().minusMinutes(5));
//        mockTaskInstance.setState(1); // 运行中
//        when(taskInstanceMapper.selectOneById(1L)).thenReturn(mockTaskInstance);
//        when(taskInstanceMapper.update(any(TaskInstanceEntity.class))).thenReturn(1);
//
//        // 执行测试
//        Long elapsedTime = taskInstanceService.calculateCurrentElapsedTime(1L);
//
//        // 验证结果
//        assertNotNull(elapsedTime);
//        assertTrue(elapsedTime > 0);
//
//        // 验证方法调用
//        verify(taskInstanceMapper).selectOneById(1L);
//        verify(taskInstanceMapper).update(any(TaskInstanceEntity.class));
//    }
//
//    @Test
//    void testSyncTaskCounter_NewCounter() {
//        // 准备测试数据
//        when(taskCounterMapper.selectByTaskIdentifier("test-task")).thenReturn(null);
//        when(taskCounterMapper.insert(any(TaskCounterEntity.class))).thenReturn(1);
//
//        // 执行测试
//        boolean result = taskInstanceService.syncTaskCounter("test-task", true, 5000L);
//
//        // 验证结果
//        assertTrue(result);
//
//        // 验证方法调用
//        verify(taskCounterMapper).selectByTaskIdentifier("test-task");
//        verify(taskCounterMapper).insert(any(TaskCounterEntity.class));
//    }
//
//    @Test
//    void testSyncTaskCounter_ExistingCounter() {
//        // 准备测试数据
//        when(taskCounterMapper.selectByTaskIdentifier("test-task")).thenReturn(mockTaskCounter);
//        when(taskCounterMapper.incrementCounter("test-task", true, 5000L)).thenReturn(1);
//        when(taskCounterMapper.recalculateAvgTime("test-task")).thenReturn(1);
//        when(taskCounterMapper.updateMinMaxTime("test-task", 5000L)).thenReturn(1);
//
//        // 执行测试
//        boolean result = taskInstanceService.syncTaskCounter("test-task", true, 5000L);
//
//        // 验证结果
//        assertTrue(result);
//
//        // 验证方法调用
//        verify(taskCounterMapper).selectByTaskIdentifier("test-task");
//        verify(taskCounterMapper).incrementCounter("test-task", true, 5000L);
//        verify(taskCounterMapper).recalculateAvgTime("test-task");
//        verify(taskCounterMapper).updateMinMaxTime("test-task", 5000L);
//    }
//
//    @Test
//    void testBatchUpdateRunningElapsedTime() {
//        // 准备测试数据
//        TaskInstanceEntity runningInstance1 = new TaskInstanceEntity();
//        runningInstance1.setTaskInstanceId(1L);
//        runningInstance1.setStartTime(LocalDateTime.now().minusMinutes(5));
//        runningInstance1.setState(1);
//
//        TaskInstanceEntity runningInstance2 = new TaskInstanceEntity();
//        runningInstance2.setTaskInstanceId(2L);
//        runningInstance2.setStartTime(LocalDateTime.now().minusMinutes(3));
//        runningInstance2.setState(1);
//
//        List<TaskInstanceEntity> runningInstances = Arrays.asList(runningInstance1, runningInstance2);
//
//        when(taskInstanceMapper.selectRunningInstances()).thenReturn(runningInstances);
//        when(taskInstanceMapper.update(any(TaskInstanceEntity.class))).thenReturn(1);
//
//        // 执行测试
//        int updateCount = taskInstanceService.batchUpdateRunningElapsedTime();
//
//        // 验证结果
//        assertEquals(2, updateCount);
//
//        // 验证方法调用
//        verify(taskInstanceMapper).selectRunningInstances();
//        verify(taskInstanceMapper, times(2)).update(any(TaskInstanceEntity.class));
//    }
//
//    @Test
//    void testGetStatistics() {
//        // 准备测试数据
//        when(taskCounterMapper.selectByTaskIdentifier("test-task")).thenReturn(mockTaskCounter);
//        when(taskInstanceMapper.selectByState(1)).thenReturn(Arrays.asList(mockTaskInstance));
//        when(taskInstanceMapper.selectByState(0)).thenReturn(Arrays.asList());
//        when(taskInstanceMapper.selectByTaskIdentifier("test-task")).thenReturn(Arrays.asList(mockTaskInstance));
//
//        // 执行测试
//        TaskInstanceStatistics statistics = taskInstanceService.getStatistics("test-task");
//
//        // 验证结果
//        assertNotNull(statistics);
//        assertEquals("test-task", statistics.getTaskIdentifier());
//        assertEquals(10, statistics.getTotalCount());
//        assertEquals(8, statistics.getSuccessCount());
//        assertEquals(2, statistics.getFailureCount());
//        assertEquals(5000L, statistics.getAvgTime());
//        assertEquals(0.8, statistics.getSuccessRate(), 0.01);
//
//        // 验证方法调用
//        verify(taskCounterMapper).selectByTaskIdentifier("test-task");
//        verify(taskInstanceMapper).selectByState(1);
//        verify(taskInstanceMapper).selectByState(0);
//        verify(taskInstanceMapper).selectByTaskIdentifier("test-task");
//    }
//}