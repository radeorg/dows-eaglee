package org.dows.eaglee.mapper;

import org.dows.eaglee.entity.TaskRuleEntity;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务规则数据访问接口
 * 
 * @author eaglee-system
 */
@Mapper
public interface TaskRuleMapper extends BaseMapper<TaskRuleEntity> {
    
//    /**
//     * 分页查询任务规则
//     *
//     * @param page 分页参数
//     * @param queryWrapper 查询条件
//     * @return 分页结果
//     */
//    Page<TaskRuleEntity> selectPageByQuery(Page<TaskRuleEntity> page, @Param("ew") QueryWrapper queryWrapper);
//
//    /**
//     * 根据规则类型查询任务规则列表
//     *
//     * @param ruleType 规则类型
//     * @return 任务规则列表
//     */
//    List<TaskRuleEntity> selectByRuleType(@Param("ruleType") String ruleType);
//
//    /**
//     * 根据应用类型查询任务规则列表
//     *
//     * @param applicationType 应用类型
//     * @return 任务规则列表
//     */
//    List<TaskRuleEntity> selectByApplicationType(@Param("applicationType") String applicationType);
//
//    /**
//     * 查询启用的任务规则列表，按优先级排序
//     *
//     * @return 启用的任务规则列表
//     */
//    List<TaskRuleEntity> selectEnabledRulesOrderByPriority();
//
//    /**
//     * 根据规则类型和应用类型查询任务规则列表
//     *
//     * @param ruleType 规则类型
//     * @param applicationType 应用类型
//     * @return 任务规则列表
//     */
//    List<TaskRuleEntity> selectByRuleTypeAndApplicationType(@Param("ruleType") String ruleType,
//                                                            @Param("applicationType") String applicationType);
//
//    /**
//     * 批量更新规则启用状态
//     *
//     * @param ruleIds 规则ID列表
//     * @param enabled 启用状态
//     * @return 更新行数
//     */
//    int updateEnabledStatusByIds(@Param("ruleIds") List<Long> ruleIds, @Param("enabled") Boolean enabled);
//
//    /**
//     * 更新规则优先级
//     *
//     * @param taskRuleId 规则ID
//     * @param priority 优先级
//     * @return 更新行数
//     */
//    int updatePriorityById(@Param("taskRuleId") Long taskRuleId, @Param("priority") Integer priority);
//
//    /**
//     * 批量插入任务规则
//     *
//     * @param taskRules 任务规则列表
//     * @return 插入行数
//     */
//    int insertBatch(@Param("taskRules") List<TaskRuleEntity> taskRules);
//
//    /**
//     * 逻辑删除任务规则
//     *
//     * @param ruleIds 规则ID列表
//     * @return 更新行数
//     */
//    int logicalDeleteByIds(@Param("ruleIds") List<Long> ruleIds);
//
//    /**
//     * 物理删除任务规则
//     *
//     * @param ruleIds 规则ID列表
//     * @return 删除行数
//     */
//    int physicalDeleteByIds(@Param("ruleIds") List<Long> ruleIds);
//
//    /**
//     * 查询最大优先级值
//     *
//     * @return 最大优先级值
//     */
//    Integer selectMaxPriority();
}