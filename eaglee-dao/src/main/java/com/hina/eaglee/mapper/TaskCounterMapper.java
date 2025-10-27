package com.hina.eaglee.mapper;

import com.hina.eaglee.entity.TaskCounterEntity;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务计数器数据访问接口
 * 
 * @author eaglee-system
 */
@Mapper
public interface TaskCounterMapper extends BaseMapper<TaskCounterEntity> {
    
//    /**
//     * 根据任务标识查询任务计数器
//     *
//     * @param taskIdentifier 任务标识
//     * @return 任务计数器
//     */
//    TaskCounterEntity selectByTaskIdentifier(@Param("taskIdentifier") String taskIdentifier);
//
//    /**
//     * 增量更新任务计数器统计数据
//     *
//     * @param taskIdentifier 任务标识
//     * @param isSuccess 是否成功
//     * @param duration 耗时
//     * @return 更新行数
//     */
//    int incrementCounter(@Param("taskIdentifier") String taskIdentifier,
//                        @Param("isSuccess") Boolean isSuccess,
//                        @Param("duration") Long duration);
//
//    /**
//     * 重新计算平均耗时
//     *
//     * @param taskIdentifier 任务标识
//     * @return 更新行数
//     */
//    int recalculateAvgTime(@Param("taskIdentifier") String taskIdentifier);
//
//    /**
//     * 批量更新任务计数器
//     *
//     * @param taskCounters 任务计数器列表
//     * @return 更新行数
//     */
//    int updateBatch(@Param("taskCounters") List<TaskCounterEntity> taskCounters);
//
//    /**
//     * 根据任务标识列表查询任务计数器
//     *
//     * @param taskIdentifiers 任务标识列表
//     * @return 任务计数器列表
//     */
//    List<TaskCounterEntity> selectByTaskIdentifiers(@Param("taskIdentifiers") List<String> taskIdentifiers);
//
//    /**
//     * 重置任务计数器统计数据
//     *
//     * @param taskIdentifier 任务标识
//     * @return 更新行数
//     */
//    int resetCounter(@Param("taskIdentifier") String taskIdentifier);
//
//    /**
//     * 查询所有活跃的任务计数器
//     *
//     * @return 任务计数器列表
//     */
//    List<TaskCounterEntity> selectActiveCounters();
//
//    /**
//     * 更新最小和最大耗时
//     *
//     * @param taskIdentifier 任务标识
//     * @param duration 耗时
//     * @return 更新行数
//     */
//    int updateMinMaxTime(@Param("taskIdentifier") String taskIdentifier, @Param("duration") Long duration);
}