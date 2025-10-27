package com.hina.eaglee.dao;

import com.hina.eaglee.entity.TaskRuntimeEntity;
import com.hina.eaglee.mapper.TaskRuntimeMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskRuntimeDao extends ServiceImpl<TaskRuntimeMapper, TaskRuntimeEntity> {
}
