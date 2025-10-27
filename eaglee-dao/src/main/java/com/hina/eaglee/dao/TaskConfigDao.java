package com.hina.eaglee.dao;

import com.hina.eaglee.entity.TaskConfigEntity;
import com.hina.eaglee.mapper.TaskConfigMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskConfigDao extends ServiceImpl<TaskConfigMapper, TaskConfigEntity> {

}
