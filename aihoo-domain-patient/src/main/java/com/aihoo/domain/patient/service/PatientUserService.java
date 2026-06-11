package com.aihoo.domain.patient.service;


import com.aihoo.common.PageResult;
import com.aihoo.domain.patient.model.entity.PatientUser;
import com.aihoo.domain.patient.model.entity.PatientUserVo;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 患者用户表 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-10-08
 */
public interface PatientUserService extends IService<PatientUser> {

    JSONArray patientUserList(Map<String, Object> map);

    JSONObject patientUserDetails(String id);

    Boolean enableDisable(Map<String, Object> map, HttpServletRequest request);

    int cancel(String mobile);

    int getCount(Map<String, Object> map);

    void patientUserBulkExport(String mobile, HttpServletRequest request, HttpServletResponse response);

    public PageResult<PatientUserVo> page(Map<String, Object> map);

    boolean patientApprove(Map<String, Object> map);
}
