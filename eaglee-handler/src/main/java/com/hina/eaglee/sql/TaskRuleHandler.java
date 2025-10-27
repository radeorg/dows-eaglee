package com.hina.eaglee.sql;

import com.hina.eaglee.dao.TaskRuleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskRuleHandler {
    private final TaskRuleDao taskRuleDao;


}
