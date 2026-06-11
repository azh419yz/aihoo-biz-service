package com.aihoo.domain.payment.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.service.VisitOrderService;
import com.alibaba.fastjson2.JSONArray;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 访问订单 Service 实现
 *
 * <p>原 VisitOrderServiceImpl 严重依赖 visit/patient/doctor/im/sys 等域，
 * 在其他域迁入前，此处保留方法签名，待相关域迁入后做 1:1 迁移。</p>
 *
 * @author ad
 */
@Service
@Slf4j
public class VisitOrderServiceImpl implements VisitOrderService {

    @Override
    public Object getDoctorAll() {
        throw new UnsupportedOperationException("TODO: VisitOrderServiceImpl.getDoctorAll - 等 doctor 域迁入");
    }

    @Override
    public PageResult<Object> page(Map<String, Object> map) {
        throw new UnsupportedOperationException("TODO: VisitOrderServiceImpl.page - 等 visit/doctor/patient/sys 域迁入");
    }

    @Override
    public Object getInquiryDetails(String id) {
        throw new UnsupportedOperationException("TODO: VisitOrderServiceImpl.getInquiryDetails - 等 visit/patient 域迁入");
    }

    @Override
    public void visitBulkExport(String orderNum, String status, String mobile, String name,
                                String doctorUserName, String type,
                                HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("TODO: VisitOrderServiceImpl.visitBulkExport - 等 visit 域迁入");
    }

    @Override
    public Object imList(Map<String, Object> map) throws Exception {
        throw new UnsupportedOperationException("TODO: VisitOrderServiceImpl.imList - 等 im 域迁入");
    }
}