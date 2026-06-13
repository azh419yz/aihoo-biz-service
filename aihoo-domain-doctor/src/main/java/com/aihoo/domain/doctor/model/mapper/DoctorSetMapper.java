package com.aihoo.domain.doctor.model.mapper;

import com.aihoo.domain.doctor.model.entity.DoctorSet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DoctorSetMapper extends BaseMapper<DoctorSet> {
    DoctorSet selectDoctorUserId(@Param("doctorUserId") String doctorUserId);
}
