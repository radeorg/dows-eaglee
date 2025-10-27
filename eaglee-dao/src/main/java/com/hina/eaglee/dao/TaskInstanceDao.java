package com.hina.eaglee.dao;

import com.hina.eaglee.entity.TaskInstanceEntity;
import com.hina.eaglee.mapper.TaskInstanceMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskInstanceDao extends ServiceImpl<TaskInstanceMapper, TaskInstanceEntity> {
}
