package com.hina.eaglee.dao;

import com.hina.eaglee.entity.DolphinProjectEntity;
import com.hina.eaglee.entity.DolphinTaskEntity;
import com.hina.eaglee.mapper.DolphinProjectMapper;
import com.hina.eaglee.mapper.DolphinTaskMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class DolphinTaskDao extends ServiceImpl<DolphinTaskMapper, DolphinTaskEntity> {
    // 定义DAO方法
}