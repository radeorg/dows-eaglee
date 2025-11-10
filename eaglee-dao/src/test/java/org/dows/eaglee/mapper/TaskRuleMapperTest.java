//package org.dows.eaglee.mapper;
//
//import config.org.dows.eaglee.TestDataSourceConfig;
//import entity.org.dows.eaglee.TaskRuleEntity;
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
// * TaskRuleMapper单元测试
// *
// * @author eaglee-system
// */
//@SpringBootTest
//@Import(TestDataSourceConfig.class)
//@ActiveProfiles("test")
//@Transactional
//class TaskRuleMapperTest {
//
//    @Resource
//    private TaskRuleMapper taskRuleMapper;
//
//    private TaskRuleEntity testRule;
//
//    @BeforeEach
//    void setUp() {
//        // 创建测试数据
//        testRule = new TaskRuleEntity();
//        testRule.setRuleName("测试规则");
//        testRule.setRuleType("monitoring");
//        testRule.setApplicationType("project");
//        testRule.setRuleCondition("cpu_usage > 80");
//        testRule.setRuleAction("send_alert");
//        testRule.setPriority(1);
//        testRule.setEnabled(true);
//        testRule.setCt(LocalDateTime.now());
//        testRule.setUt(LocalDateTime.now());
//        testRule.setCid(1L);
//        testRule.setDeleted(false);
//    }
//
//    @Test
//    void testInsertAndSelectById() {
//        // 插入测试数据
//        int result = taskRuleMapper.insert(testRule);
//        assertEquals(1, result);
//        assertNotNull(testRule.getTaskRuleId());
//
//        // 根据ID查询
//        TaskRuleEntity found = taskRuleMapper.selectOneById(testRule.getTaskRuleId());
//        assertNotNull(found);
//        assertEquals(testRule.getRuleName(), found.getRuleName());
//        assertEquals(testRule.getRuleType(), found.getRuleType());
//    }
//
//    @Test
//    void testSelectByRuleType() {
//        // 插入测试数据
//        taskRuleMapper.insert(testRule);
//
//        // 根据规则类型查询
//        List<TaskRuleEntity> rules = taskRuleMapper.selectByRuleType(testRule.getRuleType());
//        assertFalse(rules.isEmpty());
//        assertTrue(rules.stream().anyMatch(r -> r.getRuleName().equals(testRule.getRuleName())));
//    }
//
//    @Test
//    void testSelectByApplicationType() {
//        // 插入测试数据
//        taskRuleMapper.insert(testRule);
//
//        // 根据应用类型查询
//        List<TaskRuleEntity> rules = taskRuleMapper.selectByApplicationType(testRule.getApplicationType());
//        assertFalse(rules.isEmpty());
//        assertTrue(rules.stream().anyMatch(r -> r.getRuleName().equals(testRule.getRuleName())));
//    }
//
//    @Test
//    void testSelectEnabledRulesOrderByPriority() {
//        // 插入启用的测试数据
//        testRule.setEnabled(true);
//        testRule.setPriority(1);
//        taskRuleMapper.insert(testRule);
//
//        // 查询启用的规则，按优先级排序
//        List<TaskRuleEntity> enabledRules = taskRuleMapper.selectEnabledRulesOrderByPriority();
//        assertFalse(enabledRules.isEmpty());
//        assertTrue(enabledRules.stream().allMatch(TaskRuleEntity::getEnabled));
//    }
//
//    @Test
//    void testSelectByRuleTypeAndApplicationType() {
//        // 插入测试数据
//        taskRuleMapper.insert(testRule);
//
//        // 根据规则类型和应用类型查询
//        List<TaskRuleEntity> rules = taskRuleMapper.selectByRuleTypeAndApplicationType(
//            testRule.getRuleType(), testRule.getApplicationType());
//
//        assertFalse(rules.isEmpty());
//        assertTrue(rules.stream().anyMatch(r -> r.getRuleName().equals(testRule.getRuleName())));
//    }
//
//    @Test
//    void testUpdateEnabledStatusByIds() {
//        // 插入测试数据
//        taskRuleMapper.insert(testRule);
//
//        // 更新启用状态
//        List<Long> ids = Arrays.asList(testRule.getTaskRuleId());
//        int result = taskRuleMapper.updateEnabledStatusByIds(ids, false);
//        assertEquals(1, result);
//
//        // 验证更新结果
//        TaskRuleEntity updated = taskRuleMapper.selectOneById(testRule.getTaskRuleId());
//        assertFalse(updated.getEnabled());
//    }
//
//    @Test
//    void testUpdatePriorityById() {
//        // 插入测试数据
//        taskRuleMapper.insert(testRule);
//
//        // 更新优先级
//        Integer newPriority = 5;
//        int result = taskRuleMapper.updatePriorityById(testRule.getTaskRuleId(), newPriority);
//        assertEquals(1, result);
//
//        // 验证更新结果
//        TaskRuleEntity updated = taskRuleMapper.selectOneById(testRule.getTaskRuleId());
//        assertEquals(newPriority, updated.getPriority());
//    }
//
//    @Test
//    void testInsertBatch() {
//        // 创建批量测试数据
//        TaskRuleEntity rule2 = new TaskRuleEntity();
//        rule2.setRuleName("测试规则2");
//        rule2.setRuleType("collection");
//        rule2.setApplicationType("task");
//        rule2.setRuleCondition("mem_usage > 90");
//        rule2.setRuleAction("restart_service");
//        rule2.setPriority(2);
//        rule2.setEnabled(true);
//        rule2.setCt(LocalDateTime.now());
//        rule2.setUt(LocalDateTime.now());
//        rule2.setCid(1L);
//        rule2.setDeleted(false);
//
//        List<TaskRuleEntity> rules = Arrays.asList(testRule, rule2);
//
//        // 批量插入
//        int result = taskRuleMapper.insertBatch(rules);
//        assertEquals(2, result);
//    }
//
//    @Test
//    void testLogicalDeleteByIds() {
//        // 插入测试数据
//        taskRuleMapper.insert(testRule);
//
//        // 逻辑删除
//        List<Long> ids = Arrays.asList(testRule.getTaskRuleId());
//        int result = taskRuleMapper.logicalDeleteByIds(ids);
//        assertEquals(1, result);
//
//        // 验证逻辑删除结果
//        TaskRuleEntity deleted = taskRuleMapper.selectOneById(testRule.getTaskRuleId());
//        assertTrue(deleted.getDeleted());
//    }
//
//    @Test
//    void testSelectMaxPriority() {
//        // 插入测试数据
//        testRule.setPriority(10);
//        taskRuleMapper.insert(testRule);
//
//        // 查询最大优先级
//        Integer maxPriority = taskRuleMapper.selectMaxPriority();
//        assertNotNull(maxPriority);
//        assertTrue(maxPriority >= testRule.getPriority());
//    }
//
//    @Test
//    void testSelectPageByQuery() {
//        // 插入测试数据
//        taskRuleMapper.insert(testRule);
//
//        // 分页查询
//        Page<TaskRuleEntity> page = new Page<>(1, 10);
//        QueryWrapper queryWrapper = QueryWrapper.create()
//            .eq("deleted", false);
//
//        Page<TaskRuleEntity> result = taskRuleMapper.selectPageByQuery(page, queryWrapper);
//        assertNotNull(result);
//        assertTrue(result.getTotalRow() > 0);
//        assertFalse(result.getRecords().isEmpty());
//    }
//}