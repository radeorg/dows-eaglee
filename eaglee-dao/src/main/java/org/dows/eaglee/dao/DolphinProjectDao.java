package org.dows.eaglee.dao;

import org.dows.eaglee.entity.DolphinProjectEntity;
import org.dows.eaglee.mapper.DolphinProjectMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class DolphinProjectDao extends ServiceImpl<DolphinProjectMapper, DolphinProjectEntity> {
    // 定义DAO方法
}