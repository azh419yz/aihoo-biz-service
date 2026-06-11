package com.aihoo.domain.patient.model.mapper;


import com.aihoo.domain.patient.model.entity.PatientUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 患者用户表 Mapper 接口
 * </p>
 *
 * @author mcp
 * @since 2020-10-08
 */
public interface PatientUserMapper extends BaseMapper<PatientUser> {

    PatientUser patientUserDetails(@Param("id") String id);

    int cancel(String mobile);

    List<PatientUser> patientUserList(Map<String, Object> map);

    int getCount(Map<String, Object> map);
}
