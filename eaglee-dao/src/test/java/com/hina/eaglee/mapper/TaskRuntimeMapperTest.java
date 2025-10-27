//package com.hina.eaglee.mapper;
//
//import com.hina.eaglee.config.TestDataSourceConfig;
//import com.hina.eaglee.entity.TaskRuntimeEntity;
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
// * TaskRuntimeMapper单元测试
// *
// * @author eaglee-system
// */
//@SpringBootTest
//@Import(TestDataSourceConfig.class)
//@ActiveProfiles("test")
//@Transactional
//class TaskRuntimeMapperTest {
//
//    @Resource
//    private TaskRuntimeMapper taskRuntimeMapper;
//
//    private TaskRuntimeEntity testRuntime;
//
//    @BeforeEach
//    void setUp() {
//        // 创建测试数据
//        testRuntime = new TaskRuntimeEntity();
//        testRuntime.setIp("192.168.1.100");
//        testRuntime.setCpuUsage(50);
//        testRuntime.setMemUsage(60);
//        testRuntime.setDiskUsage(40);
//        testRuntime.setNetUsage(30);
//        testRuntime.setCt(LocalDateTime.now());
//        testRuntime.setDeleted(false);
//    }
//
//    @Test
//    void testInsertAndSelectById() {
//        // 插入测试数据
//        int result = taskRuntimeMapper.insert(testRuntime);
//        assertEquals(1, result);
//        assertNotNull(testRuntime.getTaskRuntimeId());
//
//        // 根据ID查询
//        TaskRuntimeEntity found = taskRuntimeMapper.selectOneById(testRuntime.getTaskRuntimeId());
//        assertNotNull(found);
//        assertEquals(testRuntime.getIp(), found.getIp());
//        assertEquals(testRuntime.getCpuUsage(), found.getCpuUsage());
//    }
//
//    @Test
//    void testInsertBatch() {
//        // 创建批量测试数据
//        TaskRuntimeEntity runtime2 = new TaskRuntimeEntity();
//        runtime2.setIp("192.168.1.101");
//        runtime2.setCpuUsage(70);
//        runtime2.setMemUsage(80);
//        runtime2.setDiskUsage(50);
//        runtime2.setNetUsage(40);
//        runtime2.setCt(LocalDateTime.now());
//        runtime2.setDeleted(false);
//
//        List<TaskRuntimeEntity> runtimes = Arrays.asList(testRuntime, runtime2);
//
//        // 批量插入
//        int result = taskRuntimeMapper.insertBatch(runtimes);
//        assertEquals(2, result);
//    }
//
//    @Test
//    void testSelectByIpAndTimeRange() {
//        // 插入测试数据
//        taskRuntimeMapper.insert(testRuntime);
//
//        // 根据IP和时间范围查询
//        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
//        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
//
//        List<TaskRuntimeEntity> runtimes = taskRuntimeMapper.selectByIpAndTimeRange(
//            testRuntime.getIp(), startTime, endTime);
//
//        assertFalse(runtimes.isEmpty());
//        assertTrue(runtimes.stream().anyMatch(r -> r.getIp().equals(testRuntime.getIp())));
//    }
//
//    @Test
//    void testSelectByTimeRange() {
//        // 插入测试数据
//        taskRuntimeMapper.insert(testRuntime);
//
//        // 根据时间范围查询
//        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
//        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
//
//        List<TaskRuntimeEntity> runtimes = taskRuntimeMapper.selectByTimeRange(startTime, endTime);
//        assertFalse(runtimes.isEmpty());
//    }
//
//    @Test
//    void testSelectDistinctIpsByTimeRange() {
//        // 插入测试数据
//        taskRuntimeMapper.insert(testRuntime);
//
//        // 查询时间范围内的IP地址列表
//        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
//        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
//
//        List<String> ips = taskRuntimeMapper.selectDistinctIpsByTimeRange(startTime, endTime);
//        assertFalse(ips.isEmpty());
//        assertTrue(ips.contains(testRuntime.getIp()));
//    }
//
//    @Test
//    void testCountByIpAndTimeRange() {
//        // 插入测试数据
//        taskRuntimeMapper.insert(testRuntime);
//
//        // 统计指定IP和时间范围内的数据点数量
//        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
//        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
//
//        long count = taskRuntimeMapper.countByIpAndTimeRange(testRuntime.getIp(), startTime, endTime);
//        assertTrue(count > 0);
//    }
//
//    @Test
//    void testSelectLatestByIp() {
//        // 插入测试数据
//        taskRuntimeMapper.insert(testRuntime);
//
//        // 查询最新的运行时数据
//        List<TaskRuntimeEntity> latestRuntimes = taskRuntimeMapper.selectLatestByIp(testRuntime.getIp(), 10);
//        assertFalse(latestRuntimes.isEmpty());
//        assertEquals(testRuntime.getIp(), latestRuntimes.get(0).getIp());
//    }
//
//    @Test
//    void testSelectPageByQuery() {
//        // 插入测试数据
//        taskRuntimeMapper.insert(testRuntime);
//
//        // 分页查询
//        Page<TaskRuntimeEntity> page = new Page<>(1, 10);
//        QueryWrapper queryWrapper = QueryWrapper.create()
//            .eq("deleted", false);
//
//        Page<TaskRuntimeEntity> result = taskRuntimeMapper.selectPageByQuery(page, queryWrapper);
//        assertNotNull(result);
//        assertTrue(result.getTotalRow() > 0);
//        assertFalse(result.getRecords().isEmpty());
//    }
//
//    @Test
//    void testDeleteBeforeTime() {
//        // 插入测试数据
//        testRuntime.setCt(LocalDateTime.now().minusDays(2));
//        taskRuntimeMapper.insert(testRuntime);
//
//        // 删除指定时间之前的数据
//        LocalDateTime beforeTime = LocalDateTime.now().minusDays(1);
//        int result = taskRuntimeMapper.deleteBeforeTime(beforeTime);
//        assertTrue(result >= 0);
//    }
//}