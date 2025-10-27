package com.hina.eaglee.dao;

import com.hina.eaglee.entity.TaskCounterEntity;
import com.hina.eaglee.mapper.TaskCounterMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskCounterDao extends ServiceImpl<TaskCounterMapper, TaskCounterEntity> {
}
