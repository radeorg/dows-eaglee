package com.hina.eaglee.dao;

import com.hina.eaglee.entity.TaskRuntimeEntity;
import com.hina.eaglee.entity.TaskSettingEntity;
import com.hina.eaglee.mapper.TaskRuntimeMapper;
import com.hina.eaglee.mapper.TaskSettingMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskSettingDao extends ServiceImpl<TaskSettingMapper, TaskSettingEntity> {
}
