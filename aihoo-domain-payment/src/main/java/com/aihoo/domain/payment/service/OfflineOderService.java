package com.aihoo.domain.payment.service;

import com.aihoo.common.HospitalOrderResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.*;
import com.aihoo.domain.payment.model.vo.OfflineBooking;
import com.aihoo.domain.payment.model.vo.OfflineDepartment;
import com.aihoo.domain.payment.model.vo.OfflineDistrict;
import com.aihoo.domain.payment.model.vo.OfflineStaff;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface OfflineOderService {
    PageResult<OfflineOder> findAll(Map<String, Object> map);

    boolean updPrice(Map<String, Object> map);

    List<OfflineOderUser> findCertificates(Map<String, Object> map);

    List<OfflineTreatment> findTreatment();

    List<OfflineDetails> findDetails(Map<String, Object> map);

    OfflineOder findOderOne(Map<String, Object> map);

    HospitalOrderResult<OfflineOder> reportForm(Map<String, Object> map);

    void oderOutExcel(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response);

    List<String> getPatIDList(Map<String, Object> map);

    boolean createPatId(Map<String, Object> map);

    Map<String, Object> bindPatId(Map<String, Object> map);

    String insertOder(Map<String, Object> map);

    boolean updOnly(List<String> ids);

    List<OfflineCompany> findCompany(Map<String, Object> map);

    List<OfflineDistrict> findHospital();

    List<OfflineDepartment> findDepartment(Map<String, Object> map);

    boolean updateOder(Map<String, Object> map);

    List<OfflineHospital> findHospitalCard();

    List<String> preparationNameList(Map<String, Object> map);

    List<OfflineStaff> findStaff(Map<String, Object> map);

    List<OfflineBooking> findBooking(Map<String, Object> map);

    List<OfflineDepartment> queryDepart(Map<String, Object> map);

    List<OfflineClinicCard> findClinicCard(Map<String, Object> map);

    boolean deleteCard(Map<String, Object> map);

    List<String> likeOder(Map<String, Object> map);

    boolean createClinicCard(Map<String, Object> map);

    boolean bulkImport(List<Map<String, Object>> mapList, HttpServletRequest request);
}