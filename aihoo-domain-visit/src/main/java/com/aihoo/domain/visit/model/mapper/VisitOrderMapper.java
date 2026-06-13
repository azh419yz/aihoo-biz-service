package com.aihoo.domain.visit.model.mapper;

import com.aihoo.domain.visit.model.entity.Visit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VisitOrderMapper extends BaseMapper<Visit> {
}
