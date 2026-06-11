package com.aihoo.api.doctor.app.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.controller.request.DoctorVisitSetRequest;
import com.aihoo.api.doctor.app.controller.request.DoctorWelcomeMessageRequest;
import com.aihoo.api.doctor.app.controller.vo.DoctorUserVo;
import com.aihoo.api.doctor.app.model.DoctorUser;
import com.aihoo.api.doctor.app.model.DoctorVisitSet;
import com.aihoo.api.doctor.app.model.DoctorWelcomeMessageSet;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * <p>
 * 医生用户表 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-09-15
 */
public interface DoctorUserService extends IService<DoctorUser> {
    /**
     * @param mobile
     * @Author: 14891
     * @Description: 根据手机号码去查询
     * @Date: 2020/9/16
     * @Return: DoctorUser
     **/
    DoctorUser selectMobile(String mobile);

    JSONObject sendCode(Map<String, Object> map);

    JSONObject phoneLogin(Map<String, Object> map, HttpServletRequest request);

    JSONObject versionsUpdate(Map<String, Object> map);

    boolean sendCode(String mobile);

    DoctorUserVo phoneLogin(String mobile, String code, HttpServletRequest request);

    DoctorVisitSet setVisit(DoctorVisitSetRequest request);

    DoctorVisitSet getVisitSet();

    DoctorUserVo detail(String id);

    DoctorWelcomeMessageSet getWelcomeMessage();

    DoctorWelcomeMessageSet setWelcomeMessage(DoctorWelcomeMessageRequest request);

    DoctorUserVo loginUser(String mobile, HttpServletRequest request);
}
