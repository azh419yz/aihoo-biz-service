package com.aihoo.domain.doctor.model.mapper;

import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.vo.SearchTeamDoctorVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Classname DoctorUserMapper
 * @Description hf
 * @Date 2020/9/16 10:45
 * @Created by ad
 */
public interface DoctorUserMapper extends BaseMapper<DoctorUser> {
    List<DoctorUser> findDoctorUserByIds(@Param("updateIds") List<String> updateIds, @Param("hospitalId") String hospitalId);

    DoctorUser findDoctorDetailsById(@Param("id") String id);

    int cancel(String mobile);

    List<DoctorUser> findByDoctorUserIds(@Param("ids") List<String> ids);

    List<DoctorUser> findByObject(Map<String, String> params);

    IPage<DoctorUser> selectDoctorUserPage(Page<DoctorUser> setPage, @Param("map") Map<String, Object> map);

    List<Integer> selectDoctorIdByIndex(String index);

    void updateDoctorIdByIndex(@Param("ids") List<Integer> ids);

    List<SearchTeamDoctorVo> getByTeamId(String teamId);
}
