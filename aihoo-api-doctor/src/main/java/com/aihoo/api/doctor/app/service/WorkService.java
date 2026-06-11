package com.aihoo.api.doctor.app.service;

import com.alibaba.fastjson2.JSONObject;
import com.aihoo.api.doctor.app.controller.vo.ChangeBalance;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/21 16:24
 * @description：工作台详细信息
 */
public interface WorkService {
    /**
     * 工作台详细信息
     *
     * @return
     */
    Map<String, Object> getWorkbench();

    /**
     * @Author: 李圣龙
     * @Description: 医查询生个人信息
     * @Return: com.aihoo.common.JsonResult
     **/
    JSONObject doctorMessage();

    /**
     * 保存医生的擅长和简介
     *
     * @param map
     * @return
     */
    JSONObject saveContent(Map<String, String> map);

    /**
     * @Description: 医生余额
     * @Date: 2020/9/29
     * @Return: com.aihoo.common.JsonResult
     **/
    List<ChangeBalance> balanceLog(Map<String, String> map);

    /**
     * @Description: 医生接诊设置
     * @Date: 2020/9/29
     * @Return: com.aihoo.common.JsonResult
     **/
    JSONObject doctorSet();

    /**
     * 医生某个接诊手段的排班时间
     *
     * @param map
     * @return
     */
    List doctorSetTimes(Map<String, String> map);

    boolean logout(HttpServletRequest request);

    JSONObject sendCancelCode(Map<String, Object> map);

    JSONObject doctorUserCancel(Map<String, Object> map, HttpServletRequest request);

    List visitOrderList();

    List revisitOrderList();

    Map<String, Object> getWorkbenchTest();

    List balanceLogType(Map<String, String> map);
}
