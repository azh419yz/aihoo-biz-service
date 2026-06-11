package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.OfflineOderYue;
import com.aihoo.domain.payment.model.vo.OfflineAppointment;
import com.aihoo.domain.payment.model.vo.OfflineHuaGetPeriod;
import com.aihoo.domain.payment.model.vo.OfflineIsStatus;
import com.aihoo.domain.payment.model.vo.OfflineTreatmentTime;

import java.util.List;
import java.util.Map;

/**
 * 线下预约
 **/
public interface OfflineAppointmentService {
    /**
     * 查询线下预约的数据
     *
     * @param map 入参
     * @return PageResult<OfflineTreatmentTime>
     */
    PageResult<OfflineTreatmentTime> findAll(Map<String, Object> map);

    /**
     * 查询详情
     *
     * @param map 入参
     * @return List<OfflineAppointment></OfflineAppointment>
     */
    List<OfflineAppointment> findOne(Map<String, Object> map);

    /**
     * 订单管理列表
     *
     * @param map 入参
     * @return PageResult<OfflineOderYue>
     */
    PageResult<OfflineOderYue> findCostList(Map<String, Object> map);

    /**
     * 取消预约
     *
     * @param map 入参
     * @return Map<String, Object>
     */
    Map<String, Object> delReservation(Map<String, Object> map);

    /**
     * 支付接口
     *
     * @param map 入参
     * @return Map<String, Object>
     */
    Map<String, Object> payViaPolymerizationForThirdParty(Map<String, Object> map);

    /**
     * 主动查询支付结果
     *
     * @param map 入参
     * @return Map<String, Object>
     */
    Map<String, Object> findPayment(Map<String, Object> map);

    /**
     * 查询订单列表里的状态数量
     *
     * @return OfflineIsStatus
     */
    OfflineIsStatus findIsStatus();

    /**
     * 查询订单的详情
     *
     * @param map 入参
     * @return OfflineOderYue
     */
    OfflineOderYue findCostOne(Map<String, Object> map);

    /**
     * 确认预约
     *
     * @param map 入参
     * @return Map<String, Object>
     */
    boolean insertCost(Map<String, Object> map);

    /**
     * 获取专家的具体排班日期
     *
     * @param map 时段id
     * @return List<OfflineHuaGetPeriod>
     */
    List<OfflineHuaGetPeriod> getPeriod(Map<String, Object> map);

    /**
     * 取消蓝色底纹
     *
     * @param map 入参
     * @return boolean
     */
    boolean updateOnly(Map<String, Object> map);
}