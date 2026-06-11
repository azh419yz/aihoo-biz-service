package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * @Classname OrderService
 * @Description hf
 * @Date 2020/9/22 16:41
 * @Created by ad
 */
public interface VisitOrderService {
    Object getDoctorAll();

    PageResult<Object> page(Map<String, Object> map);

    Object getInquiryDetails(String id);

    void visitBulkExport(String orderNum, String status, String mobile, String name,
                         String doctorUserName, String type, HttpServletRequest request, HttpServletResponse response);

    Object imList(Map<String, Object> map) throws Exception;
}