package com.aihoo.domain.visit.model.mapper;

import com.aihoo.domain.visit.model.entity.HosSick;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface HosSickMapper extends BaseMapper<HosSick> {
    List<Map> selectByDoctorId(@Param("doctorId") String doctorId, @Param("sickName") String sickName);
    List<Object> selectVoByDoctorId(@Param("doctorId") String doctorId, @Param("sickName") String sickName);
}
