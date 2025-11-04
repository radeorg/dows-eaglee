package com.hina.eaglee.dao;

import com.hina.eaglee.entity.TaskProcessEntity;
import com.hina.eaglee.mapper.TaskProcessMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskProcessDao extends ServiceImpl<TaskProcessMapper, TaskProcessEntity> {
}
