package org.dows.eaglee.dao;

import org.dows.eaglee.entity.DolphinProjectEntity;
import org.dows.eaglee.entity.DolphinTaskEntity;
import org.dows.eaglee.mapper.DolphinProjectMapper;
import org.dows.eaglee.mapper.DolphinTaskMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class DolphinTaskDao extends ServiceImpl<DolphinTaskMapper, DolphinTaskEntity> {
    // 定义DAO方法
}