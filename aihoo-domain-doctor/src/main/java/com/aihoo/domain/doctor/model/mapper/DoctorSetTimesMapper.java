package com.aihoo.domain.doctor.model.mapper;

import com.aihoo.domain.doctor.model.entity.DoctorSetTimes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DoctorSetTimesMapper extends BaseMapper<DoctorSetTimes> {
    List<Map> selectListByTypeAndDoctorId(@Param("doctorId") String doctorId, @Param("type") String type);
}
