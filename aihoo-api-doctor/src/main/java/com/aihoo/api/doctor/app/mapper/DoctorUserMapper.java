package com.aihoo.api.doctor.app.mapper;


import com.aihoo.api.doctor.app.controller.vo.DoctorWorkVo;
import com.aihoo.api.doctor.app.model.DoctorUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


/**
 * <p>
 * 医生用户表 Mapper 接口
 * </p>
 *
 * @author mcp
 * @since 2020-09-15
 */

public interface DoctorUserMapper extends BaseMapper<DoctorUser> {
    List<DoctorUser> findTeamDoctorByMdtOrderNum(String isMain, String mdtOrderNum);
}
