package com.aihoo.domain.visit.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.vo.ImMsgVo;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Classname OrderService
 * @Description hf
 * @Date 2020/9/22 16:41
 * @Created by ad
 */
public interface VisitOrderService extends IService<HosVisit> {
    JSONArray getDoctorAll();

    PageResult<HosVisit> page(Map<String, Object> map);

    HosVisit  getInquiryDetails(String id);

    void visitBulkExport(String orderNum, String status, String mobile, String name,
                         String doctorUserName, String type, HttpServletRequest request, HttpServletResponse response);

    List<ImMsgVo> imList(Map<String, Object> map) throws Exception;
}
