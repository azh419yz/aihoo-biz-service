package com.aihoo.api.doctor.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.api.doctor.app.controller.vo.HosVisitSelectVo;


import java.util.List;
import java.util.Map;

public interface HosVisitVoMapper extends BaseMapper<HosVisitSelectVo> {
    List<HosVisitSelectVo> patientList(Map<String, Object> map);
}
