//package org.dows.eaglee.mapper;
//
//import config.org.dows.eaglee.TestDataSourceConfig;
//import entity.org.dows.eaglee.TaskConfigEntity;
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
// * TaskConfigMapper单元测试
// *
// * @author eaglee-system
// */
//@SpringBootTest
//@Import(TestDataSourceConfig.class)
//@ActiveProfiles("test")
//@Transactional
//class TaskConfigMapperTest {
//
//    @Resource
//    private TaskConfigMapper taskConfigMapper;
//
//    private TaskConfigEntity testConfig;
//
//    @BeforeEach
//    void setUp() {
//        // 创建测试数据
//        testConfig = new TaskConfigEntity();
//        testConfig.setConfigName("测试配置");
//        testConfig.setConfigKey("test.config.key");
//        testConfig.setConfigValue("test-value");
//        testConfig.setConfigDesc("测试配置描述");
//        testConfig.setConfigType("system");
//        testConfig.setEnabled(true);
//        testConfig.setCt(LocalDateTime.now());
//        testConfig.setUt(LocalDateTime.now());
//        testConfig.setCid(1L);
//        testConfig.setDeleted(false);
//    }
//
//    @Test
//    void testInsertAndSelectById() {
//        // 插入测试数据
//        int result = taskConfigMapper.insert(testConfig);
//        assertEquals(1, result);
//        assertNotNull(testConfig.getTaskConfigId());
//
//        // 根据ID查询
//        TaskConfigEntity found = taskConfigMapper.selectOneById(testConfig.getTaskConfigId());
//        assertNotNull(found);
//        assertEquals(testConfig.getConfigName(), found.getConfigName());
//        assertEquals(testConfig.getConfigKey(), found.getConfigKey());
//    }
//
//    @Test
//    void testSelectByConfigKey() {
//        // 插入测试数据
//        taskConfigMapper.insert(testConfig);
//
//        // 根据配置键查询
//        TaskConfigEntity found = taskConfigMapper.selectByConfigKey(testConfig.getConfigKey());
//        assertNotNull(found);
//        assertEquals(testConfig.getConfigValue(), found.getConfigValue());
//    }
//
//    @Test
//    void testSelectByConfigType() {
//        // 插入测试数据
//        taskConfigMapper.insert(testConfig);
//
//        // 根据配置类型查询
//        List<TaskConfigEntity> configs = taskConfigMapper.selectByConfigType(testConfig.getConfigType());
//        assertFalse(configs.isEmpty());
//        assertTrue(configs.stream().anyMatch(c -> c.getConfigKey().equals(testConfig.getConfigKey())));
//    }
//
//    @Test
//    void testSelectEnabledConfigs() {
//        // 插入启用的测试数据
//        testConfig.setEnabled(true);
//        taskConfigMapper.insert(testConfig);
//
//        // 查询启用的配置
//        List<TaskConfigEntity> enabledConfigs = taskConfigMapper.selectEnabledConfigs();
//        assertFalse(enabledConfigs.isEmpty());
//        assertTrue(enabledConfigs.stream().allMatch(TaskConfigEntity::getEnabled));
//    }
//
//    @Test
//    void testUpdateConfigValueByKey() {
//        // 插入测试数据
//        taskConfigMapper.insert(testConfig);
//
//        // 更新配置值
//        String newValue = "updated-value";
//        int result = taskConfigMapper.updateConfigValueByKey(testConfig.getConfigKey(), newValue);
//        assertEquals(1, result);
//
//        // 验证更新结果
//        TaskConfigEntity updated = taskConfigMapper.selectByConfigKey(testConfig.getConfigKey());
//        assertEquals(newValue, updated.getConfigValue());
//    }
//
//    @Test
//    void testUpdateEnabledStatusByIds() {
//        // 插入测试数据
//        taskConfigMapper.insert(testConfig);
//
//        // 更新启用状态
//        List<Long> ids = Arrays.asList(testConfig.getTaskConfigId());
//        int result = taskConfigMapper.updateEnabledStatusByIds(ids, false);
//        assertEquals(1, result);
//
//        // 验证更新结果
//        TaskConfigEntity updated = taskConfigMapper.selectOneById(testConfig.getTaskConfigId());
//        assertFalse(updated.getEnabled());
//    }
//
//    @Test
//    void testInsertBatch() {
//        // 创建批量测试数据
//        TaskConfigEntity config2 = new TaskConfigEntity();
//        config2.setConfigName("测试配置2");
//        config2.setConfigKey("test.config.key2");
//        config2.setConfigValue("test-value2");
//        config2.setConfigType("system");
//        config2.setEnabled(true);
//        config2.setCt(LocalDateTime.now());
//        config2.setUt(LocalDateTime.now());
//        config2.setCid(1L);
//        config2.setDeleted(false);
//
//        List<TaskConfigEntity> configs = Arrays.asList(testConfig, config2);
//
//        // 批量插入
//        int result = taskConfigMapper.insertBatch(configs);
//        assertEquals(2, result);
//    }
//
//    @Test
//    void testSelectByConfigKeys() {
//        // 插入测试数据
//        taskConfigMapper.insert(testConfig);
//
//        // 根据配置键列表查询
//        List<String> keys = Arrays.asList(testConfig.getConfigKey());
//        List<TaskConfigEntity> configs = taskConfigMapper.selectByConfigKeys(keys);
//        assertFalse(configs.isEmpty());
//        assertEquals(testConfig.getConfigKey(), configs.get(0).getConfigKey());
//    }
//
//    @Test
//    void testLogicalDeleteByIds() {
//        // 插入测试数据
//        taskConfigMapper.insert(testConfig);
//
//        // 逻辑删除
//        List<Long> ids = Arrays.asList(testConfig.getTaskConfigId());
//        int result = taskConfigMapper.logicalDeleteByIds(ids);
//        assertEquals(1, result);
//
//        // 验证逻辑删除结果
//        TaskConfigEntity deleted = taskConfigMapper.selectOneById(testConfig.getTaskConfigId());
//        assertTrue(deleted.getDeleted());
//    }
//
//    @Test
//    void testSelectPageByQuery() {
//        // 插入测试数据
//        taskConfigMapper.insert(testConfig);
//
//        // 分页查询
//        Page<TaskConfigEntity> page = new Page<>(1, 10);
//        QueryWrapper queryWrapper = QueryWrapper.create()
//            .eq("deleted", false);
//
//        Page<TaskConfigEntity> result = taskConfigMapper.selectPageByQuery(page, queryWrapper);
//        assertNotNull(result);
//        assertTrue(result.getTotalRow() > 0);
//        assertFalse(result.getRecords().isEmpty());
//    }
//}