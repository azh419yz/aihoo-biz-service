package com.aihoo.domain.consultation.model.mapper;

import com.aihoo.domain.consultation.model.entity.MdtOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MdtOrderMapper extends BaseMapper<MdtOrder> {
    IPage<MdtOrder> selectListByDoctorIdCard(@Param("doctorUserId") String doctorUserId,
                                            @Param("statusList") List<String> statusList,
                                            IPage<MdtOrder> page);
}
