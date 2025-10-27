package com.hina.eaglee.dao;

import com.hina.eaglee.entity.TaskProjectEntity;
import com.hina.eaglee.mapper.TaskProjectMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskProjectDao extends ServiceImpl<TaskProjectMapper, TaskProjectEntity> {
}
