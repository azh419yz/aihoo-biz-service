package com.aihoo.domain.doctor.service;

import com.aihoo.domain.doctor.model.entity.DoctorSetTimes;
import com.aihoo.domain.doctor.model.vo.DoctorSetTimeVo;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Classname DoctorSetTimesService
 * @Description hf
 * @Date 2020/9/21 19:14
 * @Created by ad
 */
public interface DoctorSetTimesService extends IService<DoctorSetTimes> {
    void addAcceptsTime(List<DoctorSetTimeVo> doctorSetTimeVos, String doctorUserId, String type);

    JSONArray workingScheduleDetails(String doctorUserId, String type);

    Boolean workingScheduleDelete(Map<String, Object> map);
}
