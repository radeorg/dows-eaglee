package org.dows.eaglee.mapper;

import org.dows.eaglee.entity.TaskRuntimeEntity;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务运行时数据访问接口
 * 
 * @author eaglee-system
 */
@Mapper
public interface TaskRuntimeMapper extends BaseMapper<TaskRuntimeEntity> {
    
//    /**
//     * 分页查询运行时数据
//     *
//     * @param page 分页参数
//     * @param queryWrapper 查询条件
//     * @return 分页结果
//     */
//    Page<TaskRuntimeEntity> selectPageByQuery(Page<TaskRuntimeEntity> page, @Param("ew") QueryWrapper queryWrapper);
//
//    /**
//     * 批量插入运行时数据
//     *
//     * @param taskRuntimes 运行时数据列表
//     * @return 插入行数
//     */
//    int insertBatch(@Param("taskRuntimes") List<TaskRuntimeEntity> taskRuntimes);
//
//    /**
//     * 根据IP地址查询运行时数据
//     *
//     * @param ip IP地址
//     * @param startTime 开始时间
//     * @param endTime 结束时间
//     * @return 运行时数据列表
//     */
//    List<TaskRuntimeEntity> selectByIpAndTimeRange(@Param("ip") String ip,
//                                                   @Param("startTime") LocalDateTime startTime,
//                                                   @Param("endTime") LocalDateTime endTime);
//
//    /**
//     * 根据时间范围查询运行时数据
//     *
//     * @param startTime 开始时间
//     * @param endTime 结束时间
//     * @return 运行时数据列表
//     */
//    List<TaskRuntimeEntity> selectByTimeRange(@Param("startTime") LocalDateTime startTime,
//                                              @Param("endTime") LocalDateTime endTime);
//
//    /**
//     * 查询指定时间范围内的IP地址列表
//     *
//     * @param startTime 开始时间
//     * @param endTime 结束时间
//     * @return IP地址列表
//     */
//    List<String> selectDistinctIpsByTimeRange(@Param("startTime") LocalDateTime startTime,
//                                            @Param("endTime") LocalDateTime endTime);
//
//    /**
//     * 删除指定时间之前的运行时数据
//     *
//     * @param beforeTime 时间点
//     * @return 删除行数
//     */
//    int deleteBeforeTime(@Param("beforeTime") LocalDateTime beforeTime);
//
//    /**
//     * 统计指定时间范围内的数据点数量
//     *
//     * @param ip IP地址
//     * @param startTime 开始时间
//     * @param endTime 结束时间
//     * @return 数据点数量
//     */
//    long countByIpAndTimeRange(@Param("ip") String ip,
//                              @Param("startTime") LocalDateTime startTime,
//                              @Param("endTime") LocalDateTime endTime);
//
//    /**
//     * 查询最新的运行时数据
//     *
//     * @param ip IP地址
//     * @param limit 限制数量
//     * @return 运行时数据列表
//     */
//    List<TaskRuntimeEntity> selectLatestByIp(@Param("ip") String ip, @Param("limit") Integer limit);
}