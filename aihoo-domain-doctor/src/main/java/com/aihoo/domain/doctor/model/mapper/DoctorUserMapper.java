package com.aihoo.domain.doctor.model.mapper;

import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.vo.SearchTeamDoctorVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DoctorUserMapper extends BaseMapper<DoctorUser> {
    List<SearchTeamDoctorVo> getByTeamId(@Param("teamId") String teamId);
    List<DoctorUser> findTeamDoctorByMdtOrderNum(@Param("isMain") String isMain, @Param("mdtOrderNum") String mdtOrderNum);
}
