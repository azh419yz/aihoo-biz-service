package com.aihoo.api.doctor.app.service.impl;

import com.aihoo.oss.OssComponent;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.controller.vo.HosSickVo;
import com.aihoo.api.doctor.app.controller.vo.HosVisitVo;
import com.aihoo.domain.visit.model.mapper.HosSickMapper;
import com.aihoo.api.doctor.app.model.*;
import com.aihoo.api.doctor.app.service.*;
import com.aihoo.domain.sys.model.entity.Area;
import com.aihoo.domain.sys.service.AreaService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/6 14:21
 */
@Service("doctorApiHosSickServiceImpl")
@RequiredArgsConstructor
public class HosSickServiceImpl extends ServiceImpl<HosSickMapper, HosSick> implements HosSickService {

    private final HosVisitService hosVisitService;
    private final HosPrescriptionService hosPrescriptionService;
    private final HosSickHealthRecordsService hosSickHealthRecordsService;
    private final OssComponent ossComponent;
    private final AreaService areaService;


    @Override
    public HosSickVo findHosSickViewById(Long sickId) {
        QueryWrapper<HosSick> sickWrapper = new QueryWrapper<>();
        sickWrapper.eq("id", sickId);
        sickWrapper.eq("is_delete", "0");
        List<HosSick> hosSicks = this.baseMapper.selectList(sickWrapper);
        if (hosSicks.isEmpty()) {
            return null;
        }

        HosSick sick = hosSicks.get(0);
        QueryWrapper<HosSickHealthRecords> healthRecordWrapper = new QueryWrapper<>();
        healthRecordWrapper.eq("hos_sick_id", sick.getId());
        List<HosSickHealthRecords> healthRecords = hosSickHealthRecordsService.list(healthRecordWrapper);
        HosSickHealthRecords healthRecord = healthRecords.isEmpty() ? null : healthRecords.get(0);

        HosSickVo result = new HosSickVo();
        result.setId(sick.getId());
        result.setName(sick.getName());
        result.setIdCard(sick.getIdCard());
        result.setSex(sick.getSex());
        result.setAge(sick.getAge());
        result.setMobile(sick.getMobile());
        if (healthRecord != null) {
            if (StringUtils.isNotEmpty(healthRecord.getArea())) {
                List<Area> arealist = areaService.list(new LambdaQueryWrapper<Area>()
                        .in(Area::getAreaCode, Lists.newArrayList(healthRecord.getArea().split(","))));
                result.setArea(arealist.stream().map(Area::getName).collect(Collectors.joining("")));
            }

            result.setHeight(healthRecord.getHeight());
            result.setWeight(healthRecord.getWeight());
            result.setPastHistory(healthRecord.getPastHistory());
            result.setAllergyHistory(healthRecord.getAllergyHistory());
            result.setIdCardVerify(healthRecord.getIdCardVerify());
            if (StringUtils.isNotEmpty(healthRecord.getFaceImages()))
                result.setFaceImages(Lists.newArrayList(healthRecord.getFaceImages().split(",")));
            if (StringUtils.isNotEmpty(healthRecord.getTongueImages()))
                result.setTongueImages(Lists.newArrayList(healthRecord.getTongueImages().split(",")));
            if (StringUtils.isNotEmpty(healthRecord.getMedicalRecordImages()))
                result.setMedicalRecordImages(Lists.newArrayList(healthRecord.getMedicalRecordImages().split(",")));
        }

        QueryWrapper<HosVisit> visitWrapper = new QueryWrapper<>();
        visitWrapper.eq("hos_sick_id", sick.getId());
        visitWrapper.orderByDesc("create_time");
        List<HosVisit> visitList = hosVisitService.list(visitWrapper);

        List<HosVisitVo> list = visitList.stream().map(visit -> {
            HosVisitVo hosVisitVo = new HosVisitVo();
            hosVisitVo.setVisitId(Long.valueOf(visit.getId()));
            hosVisitVo.setVisitNo(visit.getOrderNum());
            hosVisitVo.setCreateTime(visit.getCreateTime());
            hosVisitVo.setContent(visit.getContent());
            QueryWrapper<HosPrescription> hosPrescriptionQueryWrapper = new QueryWrapper<>();
            hosPrescriptionQueryWrapper.eq("type", "REVISIT");
            hosPrescriptionQueryWrapper.eq("other_id", visit.getHosSickId());
            hosVisitVo.setHosPrescriptions(hosPrescriptionService.list(hosPrescriptionQueryWrapper));
            return hosVisitVo;
        }).toList();
        result.setHosVisits(list);
        result.setAvatar(ossComponent.getUrl(getAvatarPath(sick.getSex(), sick.getAge())));
        return result;
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
