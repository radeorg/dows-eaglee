package org.dows.eaglee.mapper;

import org.dows.eaglee.entity.DolphinAlertEntity;
import org.dows.eaglee.entity.TaskProcessEntity;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskProcessMapper extends BaseMapper<TaskProcessEntity> {
}