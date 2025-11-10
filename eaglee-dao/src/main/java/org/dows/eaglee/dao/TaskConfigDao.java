package org.dows.eaglee.dao;

import org.dows.eaglee.entity.TaskConfigEntity;
import org.dows.eaglee.mapper.TaskConfigMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskConfigDao extends ServiceImpl<TaskConfigMapper, TaskConfigEntity> {

}
