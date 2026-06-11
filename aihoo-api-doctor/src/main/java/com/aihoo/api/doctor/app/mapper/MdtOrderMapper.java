package com.aihoo.api.doctor.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aihoo.api.doctor.app.model.MdtOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/21 20:22
 */
public interface MdtOrderMapper extends BaseMapper<MdtOrder> {

    List<MdtOrder> selectListByDoctorIdCard(@Param("doctorUserId") String doctorUserId, @Param("statusList") List<String> statusList, IPage<MdtOrder> page);

}