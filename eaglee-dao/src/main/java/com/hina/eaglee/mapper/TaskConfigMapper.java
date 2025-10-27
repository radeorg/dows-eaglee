package com.hina.eaglee.mapper;

import com.hina.eaglee.entity.TaskConfigEntity;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务配置数据访问接口
 * 
 * @author eaglee-system
 */
@Mapper
public interface TaskConfigMapper extends BaseMapper<TaskConfigEntity> {
    
//    /**
//     * 分页查询任务配置
//     *
//     * @param page 分页参数
//     * @param queryWrapper 查询条件
//     * @return 分页结果
//     */
//    Page<TaskConfigEntity> selectPageByQuery(Page<TaskConfigEntity> page, @Param("ew") QueryWrapper queryWrapper);
//
//    /**
//     * 根据配置键查询任务配置
//     *
//     * @param configKey 配置键
//     * @return 任务配置
//     */
//    TaskConfigEntity selectByConfigKey(@Param("configKey") String configKey);
//
//    /**
//     * 根据配置类型查询任务配置列表
//     *
//     * @param configType 配置类型
//     * @return 任务配置列表
//     */
//    List<TaskConfigEntity> selectByConfigType(@Param("configType") String configType);
//
//    /**
//     * 查询启用的任务配置列表
//     *
//     * @return 启用的任务配置列表
//     */
//    List<TaskConfigEntity> selectEnabledConfigs();
//
//    /**
//     * 根据配置键更新配置值
//     *
//     * @param configKey 配置键
//     * @param configValue 配置值
//     * @return 更新行数
//     */
//    int updateConfigValueByKey(@Param("configKey") String configKey, @Param("configValue") String configValue);
//
//    /**
//     * 批量更新配置启用状态
//     *
//     * @param configIds 配置ID列表
//     * @param enabled 启用状态
//     * @return 更新行数
//     */
//    int updateEnabledStatusByIds(@Param("configIds") List<Long> configIds, @Param("enabled") Boolean enabled);
//
//    /**
//     * 批量插入任务配置
//     *
//     * @param taskConfigs 任务配置列表
//     * @return 插入行数
//     */
//    int insertBatch(@Param("taskConfigs") List<TaskConfigEntity> taskConfigs);
//
//    /**
//     * 根据配置键列表查询任务配置
//     *
//     * @param configKeys 配置键列表
//     * @return 任务配置列表
//     */
//    List<TaskConfigEntity> selectByConfigKeys(@Param("configKeys") List<String> configKeys);
//
//    /**
//     * 逻辑删除任务配置
//     *
//     * @param configIds 配置ID列表
//     * @return 更新行数
//     */
//    int logicalDeleteByIds(@Param("configIds") List<Long> configIds);
//
//    /**
//     * 物理删除任务配置
//     *
//     * @param configIds 配置ID列表
//     * @return 删除行数
//     */
//    int physicalDeleteByIds(@Param("configIds") List<Long> configIds);
}