package com.hina.eaglee.dao;

import com.hina.eaglee.entity.TaskRuleEntity;
import com.hina.eaglee.mapper.TaskRuleMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskRuleDao extends ServiceImpl<TaskRuleMapper, TaskRuleEntity> {
}
