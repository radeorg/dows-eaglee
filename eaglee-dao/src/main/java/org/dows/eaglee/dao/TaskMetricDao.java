package org.dows.eaglee.dao;

import org.dows.eaglee.entity.TaskMetricEntity;
import org.dows.eaglee.mapper.TaskMetricMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskMetricDao extends ServiceImpl<TaskMetricMapper, TaskMetricEntity> {
}
