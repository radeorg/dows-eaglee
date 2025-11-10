package org.dows.eaglee.dao;

import org.dows.eaglee.entity.TaskRuntimeEntity;
import org.dows.eaglee.mapper.TaskRuntimeMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskRuntimeDao extends ServiceImpl<TaskRuntimeMapper, TaskRuntimeEntity> {
}
