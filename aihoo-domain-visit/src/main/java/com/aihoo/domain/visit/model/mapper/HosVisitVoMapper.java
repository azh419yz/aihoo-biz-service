package com.aihoo.domain.visit.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.domain.visit.model.vo.HosVisitSelectVo;


import java.util.List;
import java.util.Map;

public interface HosVisitVoMapper extends BaseMapper<HosVisitSelectVo> {
    List<HosVisitSelectVo> patientList(Map<String, Object> map);
}
