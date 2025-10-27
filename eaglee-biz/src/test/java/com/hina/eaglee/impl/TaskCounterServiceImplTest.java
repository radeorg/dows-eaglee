//package com.hina.eaglee.impl;
//
//import com.hina.eaglee.entity.TaskCounterEntity;
//import com.hina.eaglee.entity.TaskInstanceEntity;
//import com.hina.eaglee.exception.BusinessException;
//import com.hina.eaglee.mapper.TaskCounterMapper;
//import com.hina.eaglee.mapper.TaskInstanceMapper;
//import com.mybatisflex.core.query.QueryWrapper;
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
// * TaskCounterService单元测试
// *
// * @author eaglee-system
// */
//@ExtendWith(MockitoExtension.class)
//class TaskCounterServiceImplTest {
//
//    @Mock
//    private TaskCounterMapper taskCounterMapper;
//
//    @Mock
//    private TaskInstanceMapper taskInstanceMapper;
//
//    @InjectMocks
//    private TaskCounterServiceImpl taskCounterService;
//
//    private TaskCounterEntity testCounter;
//    private String testTaskIdentifier;
//
//    @BeforeEach
//    void setUp() {
//        testTaskIdentifier = "test-task-001";
//        testCounter = new TaskCounterEntity();
//        testCounter.setTaskCounterId(1L);
//        testCounter.setTaskIdentifier(testTaskIdentifier);
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
//    void testGetByTaskIdentifier_Success() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(testCounter);
//
//        // When
//        TaskCounterEntity result = taskCounterService.getByTaskIdentifier(testTaskIdentifier);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(testTaskIdentifier, result.getTaskIdentifier());
//        assertEquals(testCounter.getTotalCount(), result.getTotalCount());
//        verify(taskCounterMapper).selectByTaskIdentifier(testTaskIdentifier);
//    }
//
//    @Test
//    void testGetByTaskIdentifier_EmptyIdentifier() {
//        // When & Then
//        assertThrows(BusinessException.class, () -> taskCounterService.getByTaskIdentifier(""));
//        assertThrows(BusinessException.class, () -> taskCounterService.getByTaskIdentifier(null));
//    }
//
//    @Test
//    void testCreateOrInitialize_NewCounter() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(null);
//        when(taskCounterMapper.insert(any(TaskCounterEntity.class))).thenReturn(1);
//
//        // When
//        TaskCounterEntity result = taskCounterService.createOrInitialize(testTaskIdentifier);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(testTaskIdentifier, result.getTaskIdentifier());
//        assertEquals(0, result.getTotalCount());
//        assertEquals(0, result.getSuccessCount());
//        assertEquals(0, result.getFailureCount());
//        verify(taskCounterMapper).insert(any(TaskCounterEntity.class));
//    }
//
//    @Test
//    void testCreateOrInitialize_ExistingCounter() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(testCounter);
//
//        // When
//        TaskCounterEntity result = taskCounterService.createOrInitialize(testTaskIdentifier);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(testCounter, result);
//        verify(taskCounterMapper, never()).insert(any(TaskCounterEntity.class));
//    }
//
//    @Test
//    void testIncrementCounter_Success() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(testCounter);
//        when(taskCounterMapper.incrementCounter(testTaskIdentifier, true, 1500L)).thenReturn(1);
//        when(taskCounterMapper.updateMinMaxTime(testTaskIdentifier, 1500L)).thenReturn(1);
//        when(taskCounterMapper.recalculateAvgTime(testTaskIdentifier)).thenReturn(1);
//
//        // When
//        boolean result = taskCounterService.incrementCounter(testTaskIdentifier, true, 1500L);
//
//        // Then
//        assertTrue(result);
//        verify(taskCounterMapper).incrementCounter(testTaskIdentifier, true, 1500L);
//        verify(taskCounterMapper).updateMinMaxTime(testTaskIdentifier, 1500L);
//        verify(taskCounterMapper).recalculateAvgTime(testTaskIdentifier);
//    }
//
//    @Test
//    void testIncrementCounter_CreateNewCounter() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(null);
//        when(taskCounterMapper.insert(any(TaskCounterEntity.class))).thenReturn(1);
//        when(taskCounterMapper.incrementCounter(testTaskIdentifier, true, 1500L)).thenReturn(1);
//        when(taskCounterMapper.updateMinMaxTime(testTaskIdentifier, 1500L)).thenReturn(1);
//        when(taskCounterMapper.recalculateAvgTime(testTaskIdentifier)).thenReturn(1);
//
//        // When
//        boolean result = taskCounterService.incrementCounter(testTaskIdentifier, true, 1500L);
//
//        // Then
//        assertTrue(result);
//        verify(taskCounterMapper).insert(any(TaskCounterEntity.class));
//        verify(taskCounterMapper).incrementCounter(testTaskIdentifier, true, 1500L);
//    }
//
//    @Test
//    void testRecalculateAvgTime_Success() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(testCounter);
//        when(taskCounterMapper.update(any(TaskCounterEntity.class))).thenReturn(1);
//
//        // When
//        Long result = taskCounterService.recalculateAvgTime(testTaskIdentifier);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(1000L, result); // 10000L / 10 = 1000L
//        verify(taskCounterMapper).update(any(TaskCounterEntity.class));
//    }
//
//    @Test
//    void testRecalculateAvgTime_ZeroCount() {
//        // Given
//        testCounter.setTotalCount(0);
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(testCounter);
//
//        // When
//        Long result = taskCounterService.recalculateAvgTime(testTaskIdentifier);
//
//        // Then
//        assertEquals(0L, result);
//    }
//
//    @Test
//    void testUpdateMinMaxTime_Success() {
//        // Given
//        when(taskCounterMapper.updateMinMaxTime(testTaskIdentifier, 1500L)).thenReturn(1);
//
//        // When
//        boolean result = taskCounterService.updateMinMaxTime(testTaskIdentifier, 1500L);
//
//        // Then
//        assertTrue(result);
//        verify(taskCounterMapper).updateMinMaxTime(testTaskIdentifier, 1500L);
//    }
//
//    @Test
//    void testResetCounter_Success() {
//        // Given
//        when(taskCounterMapper.resetCounter(testTaskIdentifier)).thenReturn(1);
//
//        // When
//        boolean result = taskCounterService.resetCounter(testTaskIdentifier);
//
//        // Then
//        assertTrue(result);
//        verify(taskCounterMapper).resetCounter(testTaskIdentifier);
//    }
//
//    @Test
//    void testBatchUpdate_Success() {
//        // Given
//        List<TaskCounterEntity> counters = Arrays.asList(testCounter);
//        when(taskCounterMapper.updateBatch(counters)).thenReturn(1);
//
//        // When
//        int result = taskCounterService.batchUpdate(counters);
//
//        // Then
//        assertEquals(1, result);
//        verify(taskCounterMapper).updateBatch(counters);
//    }
//
//    @Test
//    void testListByTaskIdentifiers_Success() {
//        // Given
//        List<String> identifiers = Arrays.asList(testTaskIdentifier);
//        List<TaskCounterEntity> expectedCounters = Arrays.asList(testCounter);
//        when(taskCounterMapper.selectByTaskIdentifiers(identifiers)).thenReturn(expectedCounters);
//
//        // When
//        List<TaskCounterEntity> result = taskCounterService.listByTaskIdentifiers(identifiers);
//
//        // Then
//        assertEquals(expectedCounters, result);
//        verify(taskCounterMapper).selectByTaskIdentifiers(identifiers);
//    }
//
//    @Test
//    void testListActiveCounters_Success() {
//        // Given
//        List<TaskCounterEntity> expectedCounters = Arrays.asList(testCounter);
//        when(taskCounterMapper.selectActiveCounters()).thenReturn(expectedCounters);
//
//        // When
//        List<TaskCounterEntity> result = taskCounterService.listActiveCounters();
//
//        // Then
//        assertEquals(expectedCounters, result);
//        verify(taskCounterMapper).selectActiveCounters();
//    }
//
//    @Test
//    void testSyncFromTaskInstances_Success() {
//        // Given
//        TaskInstanceEntity instance1 = new TaskInstanceEntity();
//        instance1.setTaskIdentifier(testTaskIdentifier);
//        instance1.setState(2); // 成功
//        instance1.setDuration(1000L);
//
//        TaskInstanceEntity instance2 = new TaskInstanceEntity();
//        instance2.setTaskIdentifier(testTaskIdentifier);
//        instance2.setState(3); // 失败
//        instance2.setDuration(1500L);
//
//        List<TaskInstanceEntity> instances = Arrays.asList(instance1, instance2);
//
//        when(taskInstanceMapper.selectListByQuery(any(QueryWrapper.class))).thenReturn(instances);
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(testCounter);
//        when(taskCounterMapper.update(any(TaskCounterEntity.class))).thenReturn(1);
//
//        // When
//        boolean result = taskCounterService.syncFromTaskInstances(testTaskIdentifier);
//
//        // Then
//        assertTrue(result);
//        verify(taskInstanceMapper).selectListByQuery(any(QueryWrapper.class));
//        verify(taskCounterMapper).update(any(TaskCounterEntity.class));
//    }
//
//    @Test
//    void testGetSuccessRate_Success() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(testCounter);
//
//        // When
//        Double result = taskCounterService.getSuccessRate(testTaskIdentifier);
//
//        // Then
//        assertEquals(80.0, result); // 8/10 * 100 = 80%
//    }
//
//    @Test
//    void testGetFailureRate_Success() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(testCounter);
//
//        // When
//        Double result = taskCounterService.getFailureRate(testTaskIdentifier);
//
//        // Then
//        assertEquals(20.0, result); // 2/10 * 100 = 20%
//    }
//
//    @Test
//    void testExists_True() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(testCounter);
//
//        // When
//        boolean result = taskCounterService.exists(testTaskIdentifier);
//
//        // Then
//        assertTrue(result);
//    }
//
//    @Test
//    void testExists_False() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(null);
//
//        // When
//        boolean result = taskCounterService.exists(testTaskIdentifier);
//
//        // Then
//        assertFalse(result);
//    }
//
//    @Test
//    void testDelete_Success() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(testCounter);
//        when(taskCounterMapper.update(any(TaskCounterEntity.class))).thenReturn(1);
//
//        // When
//        boolean result = taskCounterService.delete(testTaskIdentifier);
//
//        // Then
//        assertTrue(result);
//        verify(taskCounterMapper).update(any(TaskCounterEntity.class));
//    }
//
//    @Test
//    void testDelete_NotFound() {
//        // Given
//        when(taskCounterMapper.selectByTaskIdentifier(testTaskIdentifier)).thenReturn(null);
//
//        // When
//        boolean result = taskCounterService.delete(testTaskIdentifier);
//
//        // Then
//        assertTrue(result); // 不存在也认为删除成功
//        verify(taskCounterMapper, never()).update(any(TaskCounterEntity.class));
//    }
//}