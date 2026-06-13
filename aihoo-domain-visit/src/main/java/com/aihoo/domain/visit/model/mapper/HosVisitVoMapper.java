package com.aihoo.domain.visit.model.mapper;

import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.vo.HosVisitSelectVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface HosVisitVoMapper extends BaseMapper<HosVisit> {
    List<HosVisitSelectVo> patientList(@Param("map") Map<String, Object> map);
}
