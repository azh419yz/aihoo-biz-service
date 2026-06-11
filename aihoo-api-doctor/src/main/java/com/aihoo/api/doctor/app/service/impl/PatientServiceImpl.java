package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.controller.vo.HosSickVo;
import com.aihoo.api.doctor.app.mapper.HosSickMapper;
import com.aihoo.api.doctor.app.mapper.PatientUserMapper;
import com.aihoo.api.doctor.app.model.HosSick;
import com.aihoo.api.doctor.app.model.PatientUser;
import com.aihoo.api.doctor.app.service.PatientService;
import com.aihoo.api.doctor.common.utils.AuthUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 患者
 * @author: Mr.Li
 * @create: 2020-09-29 14:01
 **/
@Service
public class PatientServiceImpl extends ServiceImpl<PatientUserMapper, PatientUser> implements PatientService {
    @Autowired
    private PatientUserMapper patientUserMapper;
    @Autowired
    private HosSickMapper hosSickMapper;

    /**
     * 患者列表
     *
     * @return
     */
    @Override
    public List<Map> patientList(Map<String, String> map) {
        String sickName = map.get("sickName");
        if (StringUtils.isEmpty(sickName)) {
            sickName = "%%";
        } else {
            sickName = "%" + sickName + "%";
        }
        List<Map> result = hosSickMapper.selectByDoctorId(AuthUtil.getLoginUser().getId(), sickName);
        return result;
    }

    /**
     * 患者详情
     *
     * @param map
     * @return
     */
    @Override
    public Map patientMsg(Map<String, String> map) {
        String id = map.get("id");
        HosSick hosSick = hosSickMapper.selectById(id);
        if (hosSick == null) {
            return null;
        }
        String name = hosSick.getName();
        String sex = hosSick.getSex();
        String age = hosSick.getAge();
        Map obj = new HashMap();
        obj.put("sex", sex.equals("0") ? "女" : "男");
        obj.put("name", name);
        obj.put("age", age);
        return obj;
    }

    @Override
    public List<HosSickVo> patientList(String sickName) {
        String loginUserId = AuthUtil.getLoginUserIds();
        if (StringUtils.isEmpty(sickName)) {
            sickName = "%%";
        } else {
            sickName = "%" + sickName + "%";
        }
        return hosSickMapper.selectVoByDoctorId(loginUserId, sickName);
    }

    @Override
    public HosSickVo patientMsg(String id) {
        HosSick hosSick = hosSickMapper.selectById(id);
        HosSickVo hosSickVo = new HosSickVo();
        BeanUtils.copyProperties(hosSick, hosSickVo);
        return hosSickVo;
    }
}
