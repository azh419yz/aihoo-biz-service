package com.aihoo.api.doctor.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.api.doctor.app.controller.vo.HosSickVo;
import com.aihoo.api.doctor.app.model.HosSick;

import java.util.List;
import java.util.Map;

public interface HosSickMapper extends BaseMapper<HosSick> {
    List<Map> selectByDoctorId(String id,String sickName);

    List<HosSickVo> selectVoByDoctorId(String id, String sickName);
}