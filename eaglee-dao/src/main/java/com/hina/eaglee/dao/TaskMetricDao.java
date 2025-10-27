package com.hina.eaglee.dao;

import com.hina.eaglee.entity.TaskMetricEntity;
import com.hina.eaglee.mapper.TaskMetricMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskMetricDao extends ServiceImpl<TaskMetricMapper, TaskMetricEntity> {
}
