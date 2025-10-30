package com.hina.eaglee.dao;

import com.hina.eaglee.entity.DolphinProcessDefinitionEntity;
import com.hina.eaglee.entity.DolphinProjectEntity;
import com.hina.eaglee.mapper.DolphinProcessDefinitionMapper;
import com.hina.eaglee.mapper.DolphinProjectMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public class DolphinProcessDefinitionDao extends ServiceImpl<DolphinProcessDefinitionMapper, DolphinProcessDefinitionEntity> {

}