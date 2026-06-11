package com.aihoo.domain.payment.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.service.RevisitOrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 复诊订单 Service 实现
 *
 * <p>原 RevisitOrderServiceImpl 依赖 visit/patient/doctor/prescription 等域，
 * 在其他域迁入前，此处保留方法签名，待相关域迁入后做 1:1 迁移。</p>
 *
 * @author ad
 */
@Service
@Slf4j
public class RevisitOrderServiceImpl implements RevisitOrderService {

    @Override
    public PageResult<Object> visitList(Map<String, Object> map) {
        throw new UnsupportedOperationException("TODO: RevisitOrderServiceImpl.visitList - 等 visit/patient/doctor 域迁入");
    }

    @Override
    public Object getVisitDetails(String id) {
        throw new UnsupportedOperationException("TODO: RevisitOrderServiceImpl.getVisitDetails - 等 visit/patient 域迁入");
    }

    @Override
    public void revisitBulkExport(String orderNum, String status, String mobile,
                                  String name, String doctorUserName,
                                  HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("TODO: RevisitOrderServiceImpl.revisitBulkExport - 等 visit 域迁入");
    }

    @Override
    public void prescriptionBulkExport(String orderNum, String status, String mobile, String name, String doctorUserName, String checkPharmaceutist,
                                       String startTime, String endTime, String checkStatus,
                                       HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("TODO: RevisitOrderServiceImpl.prescriptionBulkExport - 等 prescription 域迁入");
    }

    @Override
    public void prescriptionDurgBulkExport(String orderNum, String status, String mobile, String name,
                                           HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("TODO: RevisitOrderServiceImpl.prescriptionDurgBulkExport - 等 prescription 域迁入");
    }
}