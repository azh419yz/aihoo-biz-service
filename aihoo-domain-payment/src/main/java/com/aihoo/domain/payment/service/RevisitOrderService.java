package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * @Classname RevisitOrderService
 * @Description hf
 * @Date 2020/9/22 20:53
 * @Created by ad
 */
public interface RevisitOrderService {
    PageResult<Object> visitList(Map<String, Object> map);

    Object getVisitDetails(String id);

    void revisitBulkExport(String orderNum, String status, String mobile,
                           String name, String doctorUserName, HttpServletRequest request, HttpServletResponse response);

    void prescriptionBulkExport(String orderNum, String status, String mobile, String name, String doctorUserName, String checkPharmaceutist,
                                String startTime,String endTime,String checkStatus, HttpServletRequest request, HttpServletResponse response);

    void prescriptionDurgBulkExport(String orderNum, String status, String mobile, String name, HttpServletRequest request, HttpServletResponse response);
}