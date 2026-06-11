package com.aihoo.api.doctor.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.api.doctor.app.controller.vo.MdtTeamDoctorVo;
import com.aihoo.api.doctor.app.model.MdtTeamDoctor;

import java.util.List;

/**
 * <p>
 * 会诊团队医生标签 Mapper 接口
 * </p>
 *
 * @author mcp
 * @since 2020-12-22
 */
public interface MdtTeamDoctorMapper extends BaseMapper<MdtTeamDoctor> {

    List<MdtTeamDoctorVo> listByDoctor(String mdtTeamId);
}
