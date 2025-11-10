package org.dows.eaglee.dao;

import org.dows.eaglee.entity.TaskRuleEntity;
import org.dows.eaglee.mapper.TaskRuleMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TaskRuleDao extends ServiceImpl<TaskRuleMapper, TaskRuleEntity> {
}
