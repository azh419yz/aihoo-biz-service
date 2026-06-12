package com.aihoo.api.doctor.app.service;


import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.controller.vo.HosOrder;
import com.aihoo.api.doctor.app.controller.vo.HosVisitBaseInfoVo;
import com.aihoo.api.doctor.app.controller.vo.HosVisitHealthInfoVo;
import com.aihoo.domain.visit.model.entity.HosVisit;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 在线问诊信息表 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-09-18
 */
public interface HosVisitService extends IService<HosVisit> {

    /**
     * 拒单
     *
     * @param map
     * @return
     */
    /*String refuseVisit(Map<String, String> map);*/

    /**
     * 未完成订单接口
     *
     * @return
     */
    List visitOrderList(Map<String, String> map);

    /**
     * 问诊详情
     *
     * @param map
     * @return
     */
    HosOrder visitData(String id);

    /**
     * 问诊接单
     *
     * @param map
     * @return
     */
    String haveVisit(Map<String, String> map);

    /**
     * 写医嘱
     *
     * @param map
     * @return
     */
    String writeVisitResult(Map<String, String> map);

    /**
     * 诊断结果页面
     *
     * @param map
     * @return
     */
    Map visitResult(Map<String, String> map);

    Map<String, String> visitOrderCount(Map<String, String> map);

    Long countByDoctorUserId(String doctorUserId);

    HosVisitHealthInfoVo getHealthInfo(String hosVisitId);

    HosVisitBaseInfoVo getBaseInfo(String hosVisitId);

    public JSONArray patientList(Map<String, Object> map);
}
