package org.dows.eaglee.mapper;

import org.dows.eaglee.entity.DolphinProjectEntity;
import org.dows.eaglee.entity.DolphinTaskEntity;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DolphinTaskMapper extends BaseMapper<DolphinTaskEntity> {
}