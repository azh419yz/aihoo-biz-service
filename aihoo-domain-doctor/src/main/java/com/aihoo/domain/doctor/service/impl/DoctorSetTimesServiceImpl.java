package com.aihoo.domain.doctor.service.impl;

import com.aihoo.constant.DictTypeEnum;
import com.aihoo.domain.doctor.model.entity.DoctorSetTimes;
import com.aihoo.domain.doctor.model.mapper.DoctorSetTimeMapper;
import com.aihoo.domain.doctor.model.vo.DoctorSetTimeVo;
import com.aihoo.domain.doctor.model.vo.DoctorSetTimesVo;
import com.aihoo.domain.doctor.service.DoctorSetTimesService;
import com.aihoo.domain.sys.service.DiceService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname DoctorSetTimesServiceImpl
 * @Description hf
 * @Date 2020/9/21 19:15
 * @Created by ad
 */
@Service
public class DoctorSetTimesServiceImpl extends ServiceImpl<DoctorSetTimeMapper, DoctorSetTimes> implements DoctorSetTimesService {

    @Resource
    private DoctorSetTimeMapper doctorSetTimeMapper;

    @Resource
    private DiceService diceService;


    @Override
    public void addAcceptsTime(List<DoctorSetTimeVo> doctorSetTimeVos, String doctorUserId, String type) {
        DoctorSetTimes doctorSetTimes = new DoctorSetTimes();
        // 每次新增删除当前星期的数据，重新set
        List<String> codes = doctorSetTimeVos.stream().map(DoctorSetTimeVo::getWeekCode).collect(Collectors.toList());
        QueryWrapper<DoctorSetTimes> wrapper = new QueryWrapper<>();
        wrapper.eq("doctor_user_id", doctorUserId);
        doctorSetTimeMapper.delete(wrapper);
        doctorSetTimes.setDoctorUserId(doctorUserId);
        doctorSetTimes.setType(type);
        doctorSetTimeVos.forEach(s -> {
            String name = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.WEEK.getType(), s.getWeekCode());
            s.getSetTimes().forEach(setTime -> {
                doctorSetTimes.setWeekCode(s.getWeekCode());
                doctorSetTimes.setWeekName(name);
                doctorSetTimes.setStartTime(setTime.getStartTime());
                doctorSetTimes.setEndTime(setTime.getEndTime());
                this.doctorSetTimeMapper.insert(doctorSetTimes);
            });

        });
    }

    @Override
    public JSONArray workingScheduleDetails(String doctorUserId, String type) {
        QueryWrapper<DoctorSetTimes> wrapper = new QueryWrapper<>();
        wrapper.eq("doctor_user_id", doctorUserId);
        if (!StringUtils.isEmpty(type)) {
            wrapper.eq("type", type);
        }
        List<DoctorSetTimes> doctorSetTimes = this.doctorSetTimeMapper.selectList(wrapper);
        JSONArray jsonArray = new JSONArray();
        Map<String, List<DoctorSetTimes>> map = doctorSetTimes.stream().collect(Collectors.groupingBy(DoctorSetTimes::getWeekName));
        map.forEach((key, val) -> {
            List<DoctorSetTimesVo> list = new ArrayList<>();
            val.forEach(s -> {
                DoctorSetTimesVo doctorSetTimesVo = new DoctorSetTimesVo();
                BeanUtils.copyProperties(s, doctorSetTimesVo);
                list.add(doctorSetTimesVo);
            });
            JSONObject object = new JSONObject();
            object.put("name", key);
            object.put("array", list);
            jsonArray.add(object);
        });
        return jsonArray;
    }

    @Override
    public Boolean workingScheduleDelete(Map<String, Object> map) {
        QueryWrapper<DoctorSetTimes> wrapper = new QueryWrapper<>();
        wrapper.eq("week_code", map.get("weekCode"));
        wrapper.eq("doctor_user_id", map.get("doctorUserId"));
        int i = this.doctorSetTimeMapper.delete(wrapper);
        return i > 0;
    }
}
