package com.aihoo.domain.doctor.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.excel.DoctorEntity;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface DoctorUserService extends IService<DoctorUser> {

    List<DoctorUser> findDoctorUserAll();

    PageResult<DoctorUser> list(Map<String, Object> map);

    DoctorUser findDoctorUserById(String otherId);

    List<DoctorUser> findDoctorUserByIds(List<String> updateIds, String hospitalId);

    Boolean enableDisable(Map<String, Object> map, HttpServletRequest request);

    DoctorUser doctorDetails(String id);

    JSONArray hospitalDepartmentAll(String hospitalId);

    void doctorBulkExport(String memberNum, String name, String hospitalName, String departName,
                          HttpServletRequest request, HttpServletResponse response);

    JSONArray doctorBulkImport(List<DoctorEntity> doctorEntities, HttpServletRequest request) throws Exception;

    void doctorUserAdd(Map<String, Object> map, HttpServletRequest request) throws Exception;

    void doctorUpdate(Map<String, Object> map, HttpServletRequest request) throws Exception;

    void cancel(Map<String, Object> map, HttpServletRequest request);

    boolean doctorCA();
}
