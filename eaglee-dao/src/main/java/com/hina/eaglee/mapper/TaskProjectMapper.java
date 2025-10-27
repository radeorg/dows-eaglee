package com.hina.eaglee.mapper;

import com.hina.eaglee.entity.TaskProjectEntity;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务项目数据访问接口
 * 
 * @author eaglee-system
 */
@Mapper
public interface TaskProjectMapper extends BaseMapper<TaskProjectEntity> {
    
//    /**
//     * 分页查询任务项目
//     *
//     * @param page 分页参数
//     * @param queryWrapper 查询条件
//     * @return 分页结果
//     */
//    Page<TaskProjectEntity> selectPageByQuery(Page<TaskProjectEntity> page, @Param("ew") QueryWrapper queryWrapper);
//
//    /**
//     * 根据项目标识查询任务项目
//     *
//     * @param projectIdentifier 项目标识
//     * @return 任务项目
//     */
//    TaskProjectEntity selectByProjectIdentifier(@Param("projectIdentifier") String projectIdentifier);
//
//    /**
//     * 根据状态查询任务项目列表
//     *
//     * @param state 状态
//     * @return 任务项目列表
//     */
//    List<TaskProjectEntity> selectByState(@Param("state") Integer state);
//
//    /**
//     * 更新任务项目状态
//     *
//     * @param taskProjectId 任务项目ID
//     * @param state 状态
//     * @param endTime 结束时间
//     * @return 更新行数
//     */
//    int updateStateById(@Param("taskProjectId") Long taskProjectId,
//                       @Param("state") Integer state,
//                       @Param("endTime") LocalDateTime endTime);
//
//    /**
//     * 批量逻辑删除任务项目
//     *
//     * @param taskProjectIds 任务项目ID列表
//     * @return 更新行数
//     */
//    int logicalDeleteByIds(@Param("taskProjectIds") List<Long> taskProjectIds);
//
//    /**
//     * 批量物理删除任务项目
//     *
//     * @param taskProjectIds 任务项目ID列表
//     * @return 删除行数
//     */
//    int physicalDeleteByIds(@Param("taskProjectIds") List<Long> taskProjectIds);
//
//    /**
//     * 统计任务项目数量
//     *
//     * @param queryWrapper 查询条件
//     * @return 数量
//     */
//    long countByQuery(@Param("ew") QueryWrapper queryWrapper);
}