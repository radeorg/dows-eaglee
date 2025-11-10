package org.dows.eaglee.dao;

import org.dows.eaglee.entity.TaskProcessEntity;
import org.dows.eaglee.mapper.TaskProcessMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskProcessDao extends ServiceImpl<TaskProcessMapper, TaskProcessEntity> {
}
