package com.aihoo.domain.hospital.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.entity.Hospital;
import com.aihoo.domain.hospital.model.excel.HospitalEntity;
import com.aihoo.domain.hospital.model.vo.HospitalPageVo;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Classname HospitalService
 * @Description
 * @Date 2020/9/16 20:37
 * @Created by ad
 */
public interface HospitalService extends IService<Hospital> {
    PageResult<HospitalPageVo> list(Map<String,Object> map);

    JSONObject controlHospital(String id);

    JSONArray departmentRelation();

    JSONObject hospitalUpdate(Map<String, Object> map,HttpServletRequest request) throws Exception;

    JSONObject hospitalInsert(Map<String, Object> map,HttpServletRequest request) throws Exception;

    JSONArray provincesRelation();

    Boolean enableDisable(Map<String,Object> map,HttpServletRequest request);

    Boolean updateDel(Map<String,Object> map,HttpServletRequest request);

    JSONArray hospitalBulkImport(List<HospitalEntity> hospitals,HttpServletRequest request);

    void hospitalBulkExport(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response);

    void hospitalDepartBulkExport(HttpServletRequest request, HttpServletResponse response);
}
