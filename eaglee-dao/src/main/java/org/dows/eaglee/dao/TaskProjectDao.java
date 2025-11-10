package org.dows.eaglee.dao;

import org.dows.eaglee.entity.TaskProjectEntity;
import org.dows.eaglee.mapper.TaskProjectMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskProjectDao extends ServiceImpl<TaskProjectMapper, TaskProjectEntity> {
}
