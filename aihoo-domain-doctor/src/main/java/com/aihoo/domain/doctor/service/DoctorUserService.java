package com.aihoo.domain.doctor.service;

import com.alibaba.fastjson2.JSONObject;
import com.aihoo.domain.doctor.dto.DoctorUserVo;
import com.aihoo.domain.doctor.dto.DoctorVisitSetRequest;
import com.aihoo.domain.doctor.dto.DoctorWelcomeMessageRequest;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.entity.DoctorVisitSet;
import com.aihoo.domain.doctor.model.entity.DoctorWelcomeMessageSet;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface DoctorUserService extends IService<DoctorUser> {
    DoctorUser findDoctorUserById(String id);
    List<DoctorUser> findDoctorUserAll();
    Object list(Map<String, Object> map);

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
