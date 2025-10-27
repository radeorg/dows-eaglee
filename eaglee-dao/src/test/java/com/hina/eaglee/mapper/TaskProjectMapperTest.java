//package com.hina.eaglee.mapper;
//
//import com.hina.eaglee.config.TestDataSourceConfig;
//import com.hina.eaglee.entity.TaskProjectEntity;
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
// * TaskProjectMapper单元测试
// *
// * @author eaglee-system
// */
//@SpringBootTest
//@Import(TestDataSourceConfig.class)
//@ActiveProfiles("test")
//@Transactional
//class TaskProjectMapperTest {
//
//    @Resource
//    private TaskProjectMapper taskProjectMapper;
//
//    private TaskProjectEntity testProject;
//
//    @BeforeEach
//    void setUp() {
//        // 创建测试数据
//        testProject = new TaskProjectEntity();
//        testProject.setProjectName("测试项目");
//        testProject.setProcessCode("TEST_PROCESS");
//        testProject.setProjectIdentifier("test-project-001");
//        testProject.setTaskCount(5);
//        testProject.setState(0);
//        testProject.setStartTime(LocalDateTime.now());
//        testProject.setCt(LocalDateTime.now());
//        testProject.setUt(LocalDateTime.now());
//        testProject.setCid(1L);
//        testProject.setDeleted(false);
//    }
//
//    @Test
//    void testInsertAndSelectById() {
//        // 插入测试数据
//        int result = taskProjectMapper.insert(testProject);
//        assertEquals(1, result);
//        assertNotNull(testProject.getTaskProjectId());
//
//        // 根据ID查询
//        TaskProjectEntity found = taskProjectMapper.selectOneById(testProject.getTaskProjectId());
//        assertNotNull(found);
//        assertEquals(testProject.getProjectName(), found.getProjectName());
//        assertEquals(testProject.getProcessCode(), found.getProcessCode());
//        assertEquals(testProject.getProjectIdentifier(), found.getProjectIdentifier());
//    }
//
//    @Test
//    void testSelectByProjectIdentifier() {
//        // 插入测试数据
//        taskProjectMapper.insert(testProject);
//
//        // 根据项目标识查询
//        TaskProjectEntity found = taskProjectMapper.selectByProjectIdentifier(testProject.getProjectIdentifier());
//        assertNotNull(found);
//        assertEquals(testProject.getProjectName(), found.getProjectName());
//    }
//
//    @Test
//    void testSelectByState() {
//        // 插入测试数据
//        taskProjectMapper.insert(testProject);
//
//        // 根据状态查询
//        List<TaskProjectEntity> projects = taskProjectMapper.selectByState(0);
//        assertFalse(projects.isEmpty());
//        assertTrue(projects.stream().anyMatch(p -> p.getProjectIdentifier().equals(testProject.getProjectIdentifier())));
//    }
//
//    @Test
//    void testUpdateStateById() {
//        // 插入测试数据
//        taskProjectMapper.insert(testProject);
//
//        // 更新状态
//        LocalDateTime endTime = LocalDateTime.now();
//        int result = taskProjectMapper.updateStateById(testProject.getTaskProjectId(), 1, endTime);
//        assertEquals(1, result);
//
//        // 验证更新结果
//        TaskProjectEntity updated = taskProjectMapper.selectOneById(testProject.getTaskProjectId());
//        assertEquals(1, updated.getState());
//        assertNotNull(updated.getEndTime());
//    }
//
//    @Test
//    void testSelectPageByQuery() {
//        // 插入测试数据
//        taskProjectMapper.insert(testProject);
//
//        // 分页查询
//        Page<TaskProjectEntity> page = new Page<>(1, 10);
//        QueryWrapper queryWrapper = QueryWrapper.create()
//            .eq("deleted", false);
//
//        Page<TaskProjectEntity> result = taskProjectMapper.selectPageByQuery(page, queryWrapper);
//        assertNotNull(result);
//        assertTrue(result.getTotalRow() > 0);
//        assertFalse(result.getRecords().isEmpty());
//    }
//
//    @Test
//    void testLogicalDeleteByIds() {
//        // 插入测试数据
//        taskProjectMapper.insert(testProject);
//
//        // 逻辑删除
//        List<Long> ids = Arrays.asList(testProject.getTaskProjectId());
//        int result = taskProjectMapper.logicalDeleteByIds(ids);
//        assertEquals(1, result);
//
//        // 验证逻辑删除结果
//        TaskProjectEntity deleted = taskProjectMapper.selectOneById(testProject.getTaskProjectId());
//        assertTrue(deleted.getDeleted());
//    }
//
//    @Test
//    void testCountByQuery() {
//        // 插入测试数据
//        taskProjectMapper.insert(testProject);
//
//        // 统计数量
//        QueryWrapper queryWrapper = QueryWrapper.create()
//            .eq("deleted", false);
//
//        long count = taskProjectMapper.countByQuery(queryWrapper);
//        assertTrue(count > 0);
//    }
//}