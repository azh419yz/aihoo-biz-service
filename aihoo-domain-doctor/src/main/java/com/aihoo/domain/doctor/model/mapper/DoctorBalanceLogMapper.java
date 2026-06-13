package com.aihoo.domain.doctor.model.mapper;

import com.aihoo.domain.doctor.model.entity.DoctorBalanceLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface DoctorBalanceLogMapper extends BaseMapper<DoctorBalanceLog> {
    IPage<DoctorBalanceLog> selectByDoctorId(@Param("doctorId") String doctorId,
                                            @Param("type") String type,
                                            @Param("beginOfMonth") Date beginOfMonth,
                                            @Param("endOfMonth") Date endOfMonth,
                                            IPage<DoctorBalanceLog> iPage);
}
