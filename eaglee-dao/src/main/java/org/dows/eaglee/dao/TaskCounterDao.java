package org.dows.eaglee.dao;

import org.dows.eaglee.entity.TaskCounterEntity;
import org.dows.eaglee.mapper.TaskCounterMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskCounterDao extends ServiceImpl<TaskCounterMapper, TaskCounterEntity> {
}
