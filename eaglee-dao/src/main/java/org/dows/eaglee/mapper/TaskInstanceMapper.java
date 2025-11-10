package org.dows.eaglee.mapper;

import org.dows.eaglee.entity.TaskInstanceEntity;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务实例数据访问接口
 * 
 * @author eaglee-system
 */
@Mapper
public interface TaskInstanceMapper extends BaseMapper<TaskInstanceEntity> {
    
//    /**
//     * 分页查询任务实例
//     *
//     * @param page 分页参数
//     * @param queryWrapper 查询条件
//     * @return 分页结果
//     */
//    Page<TaskInstanceEntity> selectPageByQuery(Page<TaskInstanceEntity> page, @Param("ew") QueryWrapper queryWrapper);
//
//    /**
//     * 根据任务项目ID查询任务实例列表
//     *
//     * @param taskProjectId 任务项目ID
//     * @return 任务实例列表
//     */
//    List<TaskInstanceEntity> selectByTaskProjectId(@Param("taskProjectId") Long taskProjectId);
//
//    /**
//     * 根据任务标识查询任务实例列表
//     *
//     * @param taskIdentifier 任务标识
//     * @return 任务实例列表
//     */
//    List<TaskInstanceEntity> selectByTaskIdentifier(@Param("taskIdentifier") String taskIdentifier);
//
//    /**
//     * 根据状态查询任务实例列表
//     *
//     * @param state 状态
//     * @return 任务实例列表
//     */
//    List<TaskInstanceEntity> selectByState(@Param("state") Integer state);
//
//    /**
//     * 更新任务实例状态和结束时间
//     *
//     * @param taskInstanceId 任务实例ID
//     * @param state 状态
//     * @param endTime 结束时间
//     * @param duration 时长
//     * @return 更新行数
//     */
//    int updateStateAndEndTime(@Param("taskInstanceId") Long taskInstanceId,
//                             @Param("state") Integer state,
//                             @Param("endTime") LocalDateTime endTime,
//                             @Param("duration") Long duration);
//
//    /**
//     * 更新任务实例的平均耗时
//     *
//     * @param taskInstanceId 任务实例ID
//     * @param avgTime 平均耗时
//     * @return 更新行数
//     */
//    int updateAvgTime(@Param("taskInstanceId") Long taskInstanceId, @Param("avgTime") Long avgTime);
//
//    /**
//     * 批量插入任务实例
//     *
//     * @param taskInstances 任务实例列表
//     * @return 插入行数
//     */
//    int insertBatch(@Param("taskInstances") List<TaskInstanceEntity> taskInstances);
//
//    /**
//     * 统计任务实例数量
//     *
//     * @param queryWrapper 查询条件
//     * @return 数量
//     */
//    long countByQuery(@Param("ew") QueryWrapper queryWrapper);
//
//    /**
//     * 查询正在运行的任务实例
//     *
//     * @return 正在运行的任务实例列表
//     */
//    List<TaskInstanceEntity> selectRunningInstances();
}