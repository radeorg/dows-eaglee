package com.hina.eaglee.mapper;

import com.hina.eaglee.entity.DolphinProjectEntity;
import com.hina.eaglee.entity.DolphinTaskEntity;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DolphinTaskMapper extends BaseMapper<DolphinTaskEntity> {
}