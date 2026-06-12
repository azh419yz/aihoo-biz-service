package com.aihoo.api.doctor.app.service.impl;


import com.aihoo.oss.OssComponent;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.controller.vo.DoctorDirectoryVo;
import com.aihoo.api.doctor.app.mapper.DoctorDirectoryMapper;
import com.aihoo.domain.visit.model.mapper.HosSickMapper;
import com.aihoo.api.doctor.app.model.DoctorDirectory;
import com.aihoo.api.doctor.app.model.HosSick;
import com.aihoo.api.doctor.app.service.DoctorDirectoryService;
import com.aihoo.api.doctor.common.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/5 15:59
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DoctorDirectoryServiceImpl extends ServiceImpl<DoctorDirectoryMapper, DoctorDirectory> implements DoctorDirectoryService {

    private final HosSickMapper hosSickMapper;

    private final OssComponent ossComponent;


    @Override
    public List<DoctorDirectoryVo> findDoctorDirectoryList(String sickName) {
//        if (AuthUtil.getLoginUser() == null) {
//            log.info("没有登录");
//            return List.of();
//        }
        IPage<DoctorDirectory> page = new Page<>(1, 50);
        QueryWrapper<DoctorDirectory> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(sickName), "sick_name", sickName);
//        queryWrapper.eq("doctor_id", AuthUtil.getLoginUser().getId());
        queryWrapper.eq("doctor_id", 42);
        queryWrapper.orderByDesc("create_time");
        List<DoctorDirectory> list = this.baseMapper.selectPage(page, queryWrapper).getRecords();

        List<Long> sickId = list.stream().map(DoctorDirectory::getSickId).toList();

        QueryWrapper<HosSick> sickQuery = new QueryWrapper<>();
        sickQuery.in("id", sickId);
        List<HosSick> sickList = hosSickMapper.selectList(sickQuery);

        return list.stream().map(dir -> {
            List<HosSick> hosSick = sickList
                    .stream()
                    .filter(sick -> Long.valueOf(sick.getId()).equals(dir.getSickId()))
                    .toList();
            DoctorDirectoryVo doctorDirectoryVo = new DoctorDirectoryVo();

            if (CollectionUtils.isEmpty(hosSick)) {
                doctorDirectoryVo.setSickName("患者" + new Random().nextInt(9000));
                return doctorDirectoryVo;
            }

            HosSick sick = hosSick.get(0);

            doctorDirectoryVo.setSickAge(sick.getAge());
            doctorDirectoryVo.setMobile(sick.getMobile());
            doctorDirectoryVo.setSource(dir.getSource());
            doctorDirectoryVo.setSickId(dir.getSickId());
            doctorDirectoryVo.setPatientUserId(dir.getPatientUserId());
            doctorDirectoryVo.setSickSex(sick.getSex());
            doctorDirectoryVo.setSickName(sick.getName());
            doctorDirectoryVo.setSaveTime(sick.getCreateTime());
            doctorDirectoryVo.setAvatar(ossComponent.getUrl(getAvatarPath(sick.getSex(), sick.getAge())));
            return doctorDirectoryVo;
        }).toList();
    }

    private String getAvatarPath(String sex, String ageStr) {
        String genderPrefix = "1".equals(sex) ? "M" : "W";
        int age = 0;
        try {
            if (StringUtil.isNotBlank(ageStr)) {
                String numericAge = ageStr.replaceAll("\\D+", "");
                if (StringUtil.isNotBlank(numericAge)) {
                    age = Integer.parseInt(numericAge);
                }
            }
        } catch (Exception e) {
            // ignore parse error, default to 0
        }

        String ageSuffix;
        if (age <= 6) {
            ageSuffix = "1";
        } else if (age <= 20) {
            ageSuffix = "2";
        } else if (age <= 60) {
            ageSuffix = "3";
        } else {
            ageSuffix = "4";
        }

        return "patient_aihoo/avatar/" + genderPrefix + ageSuffix + ".jpg";
    }
}
