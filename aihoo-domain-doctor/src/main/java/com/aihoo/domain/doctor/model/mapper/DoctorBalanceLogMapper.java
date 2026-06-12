package com.aihoo.domain.doctor.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aihoo.domain.doctor.model.entity.DoctorBalanceLog;

import java.util.Date;
import java.util.List;

public interface DoctorBalanceLogMapper extends BaseMapper<DoctorBalanceLog> {
    /**
     * 通过医生id查询余额变更日志
     * 操作类型
     * REVISIT_ADD-复诊增加
     * REVISIT_LOSE-复诊减少
     * VISIT_ADD-问诊增加
     * VISIT_LOSE-问诊减少
     * MDT_ADD-会诊增加
     * MDT_LOSE-会诊减少
     *
     * @param doctorId
     * @return
     */
    List<DoctorBalanceLog> selectByDoctorId(String doctorId, String type, Date beginOfMonth, Date endOfMonth, IPage iPage);

}