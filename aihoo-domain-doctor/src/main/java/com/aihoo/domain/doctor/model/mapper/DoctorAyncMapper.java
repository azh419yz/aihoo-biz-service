package com.aihoo.domain.doctor.model.mapper;

import com.aihoo.domain.doctor.model.entity.DoctorAync;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DoctorAyncMapper extends BaseMapper<DoctorAync> {
    DoctorAync selectByDoctorId(@Param("doctorId") String doctorId);
}
