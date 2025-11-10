package org.dows.eaglee.dao;

import org.dows.eaglee.entity.TaskInstanceEntity;
import org.dows.eaglee.mapper.TaskInstanceMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskInstanceDao extends ServiceImpl<TaskInstanceMapper, TaskInstanceEntity> {
}
