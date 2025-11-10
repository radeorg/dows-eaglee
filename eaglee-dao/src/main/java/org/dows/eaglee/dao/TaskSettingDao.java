package org.dows.eaglee.dao;

import org.dows.eaglee.entity.TaskRuntimeEntity;
import org.dows.eaglee.entity.TaskSettingEntity;
import org.dows.eaglee.mapper.TaskRuntimeMapper;
import org.dows.eaglee.mapper.TaskSettingMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskSettingDao extends ServiceImpl<TaskSettingMapper, TaskSettingEntity> {
}
