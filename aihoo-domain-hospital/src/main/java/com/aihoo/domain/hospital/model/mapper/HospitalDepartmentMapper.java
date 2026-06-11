package com.aihoo.domain.hospital.model.mapper;

import com.aihoo.domain.hospital.model.entity.HospitalDepartment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Classname HospitalDepartmentMapper
 * @Description hf
 * @Date 2020/9/17 11:16
 * @Created by ad
 */
public interface HospitalDepartmentMapper extends BaseMapper<HospitalDepartment> {
    List<HospitalDepartment> findDepartmentByIds(@Param("ids") List<String> ids);

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<HospitalDepartment> findByObject(Map<String, String> params);

    void deleteByDepartCodes(@Param("newIds") List<String> newIds,
                             @Param("hospitalId") String hospitalId);
}
