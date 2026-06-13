package com.aihoo.domain.doctor.service;

import com.aihoo.domain.doctor.dto.ChangeBalance;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface WorkService {
    Map<String, Object> getWorkbench();
    JSONObject doctorMessage();
    JSONObject saveContent(Map<String, String> map);
    List<ChangeBalance> balanceLog(Map<String, String> map);
    JSONObject doctorSet();
    List doctorSetTimes(Map<String, String> map);
    boolean logout(HttpServletRequest request);
    JSONObject sendCancelCode(Map<String, Object> map);
    JSONObject doctorUserCancel(Map<String, Object> map, HttpServletRequest request);
    List visitOrderList();
    List revisitOrderList();
    Map<String, Object> getWorkbenchTest();
    List balanceLogType(Map<String, String> map);
}
