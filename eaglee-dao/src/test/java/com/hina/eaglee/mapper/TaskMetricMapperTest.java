//package com.hina.eaglee.mapper;
//
//import com.hina.eaglee.config.TestDataSourceConfig;
//import com.hina.eaglee.entity.TaskMetricEntity;
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
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * TaskMetricMapper单元测试
// *
// * @author eaglee-system
// */
//@SpringBootTest
//@Import(TestDataSourceConfig.class)
//@ActiveProfiles("test")
//@Transactional
//class TaskMetricMapperTest {
//
//    @Resource
//    private TaskMetricMapper taskMetricMapper;
//
//    private TaskMetricEntity testMetric;
//
//    @BeforeEach
//    void setUp() {
//        // 创建测试数据
//        testMetric = new TaskMetricEntity();
//        testMetric.setIp("192.168.1.100");
//        testMetric.setTimeUnit("minute");
//        testMetric.setTimeValue(LocalDateTime.now().withSecond(0).withNano(0));
//        testMetric.setCpuTotal(3000L);
//        testMetric.setMemTotal(3600L);
//        testMetric.setDiskTotal(2400L);
//        testMetric.setNetTotal(1800L);
//        testMetric.setDataPoints(60);
//        testMetric.setCt(LocalDateTime.now());
//        testMetric.setUt(LocalDateTime.now());
//        testMetric.setDeleted(false);
//    }
//
//    @Test
//    void testInsertAndSelectById() {
//        // 插入测试数据
//        int result = taskMetricMapper.insert(testMetric);
//        assertEquals(1, result);
//        assertNotNull(testMetric.getTaskMetricId());
//
//        // 根据ID查询
//        TaskMetricEntity found = taskMetricMapper.selectOneById(testMetric.getTaskMetricId());
//        assertNotNull(found);
//        assertEquals(testMetric.getIp(), found.getIp());
//        assertEquals(testMetric.getTimeUnit(), found.getTimeUnit());
//    }
//
//    @Test
//    void testSelectByIpAndTimeUnit() {
//        // 插入测试数据
//        taskMetricMapper.insert(testMetric);
//
//        // 根据IP和时间单位查询
//        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
//        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
//
//        List<TaskMetricEntity> metrics = taskMetricMapper.selectByIpAndTimeUnit(
//            testMetric.getIp(), testMetric.getTimeUnit(), startTime, endTime);
//
//        assertFalse(metrics.isEmpty());
//        assertTrue(metrics.stream().anyMatch(m -> m.getIp().equals(testMetric.getIp())));
//    }
//
//    @Test
//    void testSelectByIpAndTimeUnitAndValue() {
//        // 插入测试数据
//        taskMetricMapper.insert(testMetric);
//
//        // 根据IP、时间单位和时间值查询
//        TaskMetricEntity found = taskMetricMapper.selectByIpAndTimeUnitAndValue(
//            testMetric.getIp(), testMetric.getTimeUnit(), testMetric.getTimeValue());
//
//        assertNotNull(found);
//        assertEquals(testMetric.getCpuTotal(), found.getCpuTotal());
//    }
//
//    @Test
//    void testUpdateAggregatedValues() {
//        // 插入测试数据
//        taskMetricMapper.insert(testMetric);
//
//        // 更新聚合值
//        Long newCpuTotal = 4000L;
//        Long newMemTotal = 4800L;
//        Integer newDataPoints = 80;
//
//        int result = taskMetricMapper.updateAggregatedValues(
//            testMetric.getTaskMetricId(), newCpuTotal, newMemTotal,
//            testMetric.getDiskTotal(), testMetric.getNetTotal(), newDataPoints);
//        assertEquals(1, result);
//
//        // 验证更新结果
//        TaskMetricEntity updated = taskMetricMapper.selectOneById(testMetric.getTaskMetricId());
//        assertEquals(newCpuTotal, updated.getCpuTotal());
//        assertEquals(newMemTotal, updated.getMemTotal());
//        assertEquals(newDataPoints, updated.getDataPoints());
//    }
//
//    @Test
//    void testSelectAggregatedMetrics() {
//        // 插入测试数据
//        taskMetricMapper.insert(testMetric);
//
//        // 聚合查询度量数据
//        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
//        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
//
//        List<Map<String, Object>> aggregated = taskMetricMapper.selectAggregatedMetrics(
//            testMetric.getTimeUnit(), startTime, endTime);
//
//        assertNotNull(aggregated);
//    }
//
//    @Test
//    void testInsertOrUpdateBatch() {
//        // 创建批量测试数据
//        TaskMetricEntity metric2 = new TaskMetricEntity();
//        metric2.setIp("192.168.1.101");
//        metric2.setTimeUnit("minute");
//        metric2.setTimeValue(LocalDateTime.now().withSecond(0).withNano(0));
//        metric2.setCpuTotal(2500L);
//        metric2.setMemTotal(3000L);
//        metric2.setDiskTotal(2000L);
//        metric2.setNetTotal(1500L);
//        metric2.setDataPoints(60);
//        metric2.setCt(LocalDateTime.now());
//        metric2.setUt(LocalDateTime.now());
//        metric2.setDeleted(false);
//
//        List<TaskMetricEntity> metrics = Arrays.asList(testMetric, metric2);
//
//        // 批量插入或更新
//        int result = taskMetricMapper.insertOrUpdateBatch(metrics);
//        assertEquals(2, result);
//    }
//
//    @Test
//    void testSelectDistinctIpsByTimeUnitAndRange() {
//        // 插入测试数据
//        taskMetricMapper.insert(testMetric);
//
//        // 查询时间范围内的IP地址
//        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
//        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
//
//        List<String> ips = taskMetricMapper.selectDistinctIpsByTimeUnitAndRange(
//            testMetric.getTimeUnit(), startTime, endTime);
//
//        assertFalse(ips.isEmpty());
//        assertTrue(ips.contains(testMetric.getIp()));
//    }
//
//    @Test
//    void testSelectClusterMetricSummary() {
//        // 插入测试数据
//        taskMetricMapper.insert(testMetric);
//
//        // 查询集群度量数据汇总
//        Map<String, Object> summary = taskMetricMapper.selectClusterMetricSummary(
//            testMetric.getTimeUnit(), testMetric.getTimeValue());
//
//        assertNotNull(summary);
//    }
//
//    @Test
//    void testSelectPageByQuery() {
//        // 插入测试数据
//        taskMetricMapper.insert(testMetric);
//
//        // 分页查询
//        Page<TaskMetricEntity> page = new Page<>(1, 10);
//        QueryWrapper queryWrapper = QueryWrapper.create()
//            .eq("deleted", false);
//
//        Page<TaskMetricEntity> result = taskMetricMapper.selectPageByQuery(page, queryWrapper);
//        assertNotNull(result);
//        assertTrue(result.getTotalRow() > 0);
//        assertFalse(result.getRecords().isEmpty());
//    }
//
//    @Test
//    void testDeleteBeforeTime() {
//        // 插入测试数据
//        testMetric.setCt(LocalDateTime.now().minusDays(2));
//        taskMetricMapper.insert(testMetric);
//
//        // 删除指定时间之前的数据
//        LocalDateTime beforeTime = LocalDateTime.now().minusDays(1);
//        int result = taskMetricMapper.deleteBeforeTime(beforeTime);
//        assertTrue(result >= 0);
//    }
//}