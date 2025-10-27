package com.hina.eaglee.mapper;

import com.hina.eaglee.entity.TaskMetricEntity;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 任务度量数据访问接口
 * 
 * @author eaglee-system
 */
@Mapper
public interface TaskMetricMapper extends BaseMapper<TaskMetricEntity> {
//
//    /**
//     * 分页查询度量数据
//     *
//     * @param page 分页参数
//     * @param queryWrapper 查询条件
//     * @return 分页结果
//     */
//    Page<TaskMetricEntity> selectPageByQuery(Page<TaskMetricEntity> page, @Param("ew") QueryWrapper queryWrapper);
//
//    /**
//     * 根据IP地址和时间维度查询度量数据
//     *
//     * @param ip IP地址
//     * @param timeUnit 时间单位
//     * @param startTime 开始时间
//     * @param endTime 结束时间
//     * @return 度量数据列表
//     */
//    List<TaskMetricEntity> selectByIpAndTimeUnit(@Param("ip") String ip,
//                                                 @Param("timeUnit") String timeUnit,
//                                                 @Param("startTime") LocalDateTime startTime,
//                                                 @Param("endTime") LocalDateTime endTime);
//
//    /**
//     * 聚合查询度量数据统计
//     *
//     * @param timeUnit 时间单位
//     * @param startTime 开始时间
//     * @param endTime 结束时间
//     * @return 聚合统计结果
//     */
//    List<Map<String, Object>> selectAggregatedMetrics(@Param("timeUnit") String timeUnit,
//                                                     @Param("startTime") LocalDateTime startTime,
//                                                     @Param("endTime") LocalDateTime endTime);
//
//    /**
//     * 批量插入或更新度量数据
//     *
//     * @param taskMetrics 度量数据列表
//     * @return 影响行数
//     */
//    int insertOrUpdateBatch(@Param("taskMetrics") List<TaskMetricEntity> taskMetrics);
//
//    /**
//     * 根据时间单位和时间值查询度量数据
//     *
//     * @param ip IP地址
//     * @param timeUnit 时间单位
//     * @param timeValue 时间值
//     * @return 度量数据
//     */
//    TaskMetricEntity selectByIpAndTimeUnitAndValue(@Param("ip") String ip,
//                                                   @Param("timeUnit") String timeUnit,
//                                                   @Param("timeValue") LocalDateTime timeValue);
//
//    /**
//     * 更新度量数据的聚合值
//     *
//     * @param taskMetricId 度量数据ID
//     * @param cpuTotal CPU总量
//     * @param memTotal 内存总量
//     * @param diskTotal 磁盘总量
//     * @param netTotal 网络总量
//     * @param dataPoints 数据点数量
//     * @return 更新行数
//     */
//    int updateAggregatedValues(@Param("taskMetricId") Long taskMetricId,
//                              @Param("cpuTotal") Long cpuTotal,
//                              @Param("memTotal") Long memTotal,
//                              @Param("diskTotal") Long diskTotal,
//                              @Param("netTotal") Long netTotal,
//                              @Param("dataPoints") Integer dataPoints);
//
//    /**
//     * 查询指定时间范围内的所有IP地址
//     *
//     * @param timeUnit 时间单位
//     * @param startTime 开始时间
//     * @param endTime 结束时间
//     * @return IP地址列表
//     */
//    List<String> selectDistinctIpsByTimeUnitAndRange(@Param("timeUnit") String timeUnit,
//                                                   @Param("startTime") LocalDateTime startTime,
//                                                   @Param("endTime") LocalDateTime endTime);
//
//    /**
//     * 删除指定时间之前的度量数据
//     *
//     * @param beforeTime 时间点
//     * @return 删除行数
//     */
//    int deleteBeforeTime(@Param("beforeTime") LocalDateTime beforeTime);
//
//    /**
//     * 查询集群度量数据汇总
//     *
//     * @param timeUnit 时间单位
//     * @param timeValue 时间值
//     * @return 集群度量数据汇总
//     */
//    Map<String, Object> selectClusterMetricSummary(@Param("timeUnit") String timeUnit,
//                                                  @Param("timeValue") LocalDateTime timeValue);
}