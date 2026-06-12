package com.aihoo.api.doctor.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.aihoo.domain.visit.model.entity.HosRevisit;

import java.util.List;
import java.util.Map;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/22 19:01
 * @description：复诊订单
 */
public interface HosRevisitService extends IService<HosRevisit> {
    /**
     * 接单
     *
     * @param map
     * @return
     */
    String haveRevisit(Map<String, String> map);

    /**
     * 查询复诊订单
     *
     * @param map
     * @return
     */
    List revisitOrderList(Map<String, String> map);

//    /**
//     * 复诊-》订单-》转单
//     *
//     * @param map
//     * @return
//     */
//    String revisitTransfer(Map<String, String> map);

    /**
     * 复诊详情
     *
     * @param map
     * @return
     */
    Object revisitData(Map<String, Object> map);

    /**
     * 复诊—》开处方-》疾病列表
     *
     * @param map
     * @return
     */
    List diseaseList(Map<String, Object> map);

    /**
     * 复诊—》开处方-》药品列表
     *
     * @param map page   limit
     * @return
     */
    List drugList(Map<String, String> map);

    /**
     * 查询dist字典表list
     *
     * @param type 类型
     * @return
     */
    List<Map<String, String>> dictList(String type);

    /**
     * 提交签名接口
     *
     * @param map
     * @return
     */
    String sign(Map<String, Object> map);

    /**
     * 开处方接口
     *
     * @param map
     * @return
     */
    String setPrescription(Map<String, Object> map);

    /**
     * 查询处方审核状态
     *
     * @param map
     * @return
     */
    Map getPrescriptionStatus(Map<String, Object> map);

    /**
     * 查看厨处方详情
     *
     * @param map
     * @return
     */
    Map prescription(Map<String, Object> map);

    /**
     * 写医嘱
     *
     * @param map
     * @return
     */
    String writeRevisitResult(Map<String, String> map);

    /**
     * 查看医嘱结果页面
     *
     * @param map
     * @return
     */
    Map revisitResult(Map<String, String> map);

    /**
     * 第二次提交审核（修改认证状态为已认证）
     *
     * @param id 处方id
     * @return
     */
    boolean commitPrescription(String id);

    Drug getOneDrug(Map<String, String> map);

    /**
     * 处方笺
     *
     * @param map
     * @return
     */
    Map<String, Object> getPrescription(Map<String, String> map);

    /**
     * 患者处方记录 列表（审核中，审核成功，审核失败）
     *
     * @param map
     * @return
     */
    Object getPrescriptionList(Map<String, String> map);

    /**
     * 复诊订单档案调阅+1
     *
     * @param id 复诊id
     * @return
     */
    Object getArchives(String id);

    Map<String, String> revisitOrderCount(Map<String, String> map);
}
