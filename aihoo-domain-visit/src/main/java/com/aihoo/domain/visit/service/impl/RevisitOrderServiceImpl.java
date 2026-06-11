package com.aihoo.domain.visit.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.model.entity.Dict;
import com.aihoo.domain.sys.model.mapper.DiceMapper;
import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.visit.model.excel.HosPreDrugEntity;
import com.aihoo.domain.visit.model.excel.HosPrescriptionEntity;
import com.aihoo.domain.visit.model.excel.HosRevisitEntity;
import com.aihoo.domain.visit.model.mapper.RevisitOrderMapper;
import com.aihoo.domain.visit.model.vo.RevisitDiseaseVo;
import com.aihoo.domain.visit.model.vo.RevisitListVo;
import com.aihoo.domain.visit.service.RevisitOrderService;
import com.aihoo.domain.visit.stub.DoctorUser;
import com.aihoo.domain.visit.stub.DoctorUserMapper;
import com.aihoo.domain.visit.stub.HosPreDrugMapper;
import com.aihoo.domain.visit.stub.HosPreDrugOrder;
import com.aihoo.domain.visit.stub.HosPrescription;
import com.aihoo.domain.visit.stub.HosPrescriptionDrug;
import com.aihoo.domain.visit.stub.HosPrescriptionDrugMapper;
import com.aihoo.domain.visit.stub.HosPrescriptionMapper;
import com.aihoo.domain.visit.stub.PatientUser;
import com.aihoo.domain.visit.stub.PatientUserMapper;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Classname RevisitOrderServiceImpl
 * @Description hf
 * @Date 2020/9/22 20:54
 * @Created by ad
 */
@Service
public class RevisitOrderServiceImpl implements RevisitOrderService {

    @Resource
    private RevisitOrderMapper revisitOrderMapper;
    @Resource
    private DoctorUserMapper doctorUserMapper;

    @Resource
    private PatientUserMapper patientUserMapper;

    @Resource
    private HosPrescriptionMapper hosPrescriptionMapper;

    @Resource
    private HosPreDrugMapper hosPreDrugMapper;

    @Resource
    private HosPrescriptionDrugMapper hosPrescriptionDrugMapper;

    @Resource
    private DiceMapper diceMapper;


    @Override
    public PageResult<HosRevisit> visitList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))){
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<HosRevisit> wrapper = new QueryWrapper<>();

        if (null != map.get("orderNum") && !"".equals(map.get("orderNum"))) {
            wrapper.eq("order_num",map.get("orderNum"));
        }
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            wrapper.like("name",map.get("name"));
        }
        if (null != map.get("mobile") && !"".equals(map.get("mobile"))) {
            wrapper.eq("mobile",map.get("mobile"));
        }

        if (null != map.get("doctorUserName") && !"".equals(map.get("doctorUserName"))) {
            List<DoctorUser> doctorUsers = this.doctorUserMapper.
                    selectList(new QueryWrapper<DoctorUser>().like("name", map.get("doctorUserName")));
            if (!CollectionUtils.isEmpty(doctorUsers)){
                List<String> doctorUserIds = doctorUsers.stream().map(DoctorUser::getId).collect(Collectors.toList());
                wrapper.in("doctor_user_id", doctorUserIds);
            }else {
                return  new PageResult<>(Lists.newArrayList(),0);
            }
        }
        if (null != map.get("status") && !"".equals(map.get("status"))) {
            wrapper.eq("status",map.get("status"));
        }
        wrapper.orderByDesc("create_time");
        IPage<HosRevisit> hosRevisits = this.revisitOrderMapper.selectPage(new Page<>(page, limit), wrapper);
        List<HosRevisit> resp = hosRevisits.getRecords();
        if (CollectionUtils.isEmpty(resp)){
            return new PageResult<>(resp,hosRevisits.getTotal());
        }
        // 查询关联数据
        List<String> doctorUserIds = resp.stream().map(HosRevisit::getDoctorUserId).collect(Collectors.toList());
        List<DoctorUser> doctorUsers = doctorUserMapper.selectBatchIds(doctorUserIds);
        if (CollectionUtils.isEmpty(doctorUsers)) {
            return new PageResult<>("根据医生id没有查询到医生信息 id ： " + doctorUserIds.toString());
        }
        Map<String, DoctorUser> doctorUserMap = doctorUsers.stream().collect(Collectors.toMap(DoctorUser::getId, doctorUser -> doctorUser));

        List<String> patientUserIds = resp.stream().map(HosRevisit::getPatientUserId).collect(Collectors.toList());
        List<PatientUser> patientUsers = this.patientUserMapper.selectBatchIds(patientUserIds);
        if (CollectionUtils.isEmpty(patientUsers)){
            return new PageResult<>("根据患者id没有查询到患者信息 id ： " + patientUserIds.toString());
        }
        Map<String, PatientUser> patientUserMap = patientUsers.stream().collect(Collectors.toMap(PatientUser::getId, patientUser -> patientUser));

        List<String> revisitIds = resp.stream().map(HosRevisit::getOrderNum).collect(Collectors.toList());
//
//        List<RevisitListVo> hosPrescriptions = this.hosPrescriptionMapper.selectPrescriptionByVisitMdtNum(revisitIds);
        Map<String, RevisitListVo> voMap = null;
//        if (!CollectionUtils.isEmpty(hosPrescriptions)){
//             voMap = hosPrescriptions.stream().collect(Collectors.toMap(RevisitListVo::getVisitMdtNum, s -> s));
//        }
//        List<RevisitDiseaseVo> revisitDiseaseVos = this.hosPrescriptionMapper.getDiseaseNamesByVisitId(revisitIds);
        Map<String, RevisitDiseaseVo> revisitDiseaseVoMap = null;
//        if (!CollectionUtils.isEmpty(revisitDiseaseVos)){
//            revisitDiseaseVoMap  = revisitDiseaseVos.stream().collect(Collectors.toMap(RevisitDiseaseVo::getVisitMdtNum, s -> s));
//        }
        // 数据拼接 voMap 为null 当前复诊单还未生成 处方单
        Map<String, RevisitListVo> finalVoMap = voMap;
        Map<String, RevisitDiseaseVo> finalRevisitDiseaseVoMap = revisitDiseaseVoMap;
        resp.forEach(r->{
            r.setDoctorName(doctorUserMap.get(r.getDoctorUserId()).getName());
            r.setRevisitHospitalName(doctorUserMap.get(r.getDoctorUserId()).getHospitalName());
            r.setRevisitDepartName(doctorUserMap.get(r.getDoctorUserId()).getDepartName());
            r.setUserMobile(patientUserMap.get(r.getPatientUserId()).getMobile());
            // 处方单未生成的时候 所对应的疾病是空
            if (null != finalRevisitDiseaseVoMap){
                if (finalRevisitDiseaseVoMap.containsKey(r.getOrderNum())){
                    r.setRevisitDiseaseName(finalRevisitDiseaseVoMap.get(r.getOrderNum()).getRevisitDiseaseNames());
                }
            }
            // 复诊订单生成时，对应的处方单和药品订单不一定生成
            if (null != finalVoMap){
                if (finalVoMap.containsKey(r.getOrderNum())){
                    r.setPrescriptionId(finalVoMap.get(r.getOrderNum()).getPrescriptionNum());
                    r.setDrugOrderId(finalVoMap.get(r.getOrderNum()).getDrugNum());
                    r.setDrugOrderKeyId(finalVoMap.get(r.getOrderNum()).getDrugNumKeyId());
                    r.setPrescriptionKeyId(finalVoMap.get(r.getOrderNum()).getPrescriptionKeyId());
                }
            }
        });
        return new PageResult<>(resp,hosRevisits.getTotal());
    }

    @Override
    public HosRevisit getVisitDetails(String id) {
        HosRevisit res = revisitOrderMapper.getVisitDetails(id);
        if (StringUtils.isEmpty(res)) {
            return res;
        }
        PatientUser patientUser = patientUserMapper.selectById(res.getPatientUserId());
        if (StringUtils.isEmpty(patientUser)) {
            return res;
        }
        DoctorUser doctorUser = this.doctorUserMapper.selectById(res.getDoctorUserId());
//        List<RevisitDiseaseVo> revisitDiseaseNames = hosPrescriptionMapper.getDiseaseNamesByVisitId(Collections.singletonList(res.getOrderNum()));
        res.setDoctorName(doctorUser.getName());
        res.setUserMobile(patientUser.getMobile());
        res.setRevisitDepartName(doctorUser.getDepartName());
        res.setRevisitHospitalName(doctorUser.getHospitalName());
//        if (!CollectionUtils.isEmpty(revisitDiseaseNames)) {
//            res.setRevisitDiseaseName(revisitDiseaseNames.get(0).getRevisitDiseaseNames());
//        }
//        List<RevisitListVo> hosPrescriptions = this.hosPrescriptionMapper.
//                selectPrescriptionByVisitMdtNum(Stream.of(res.getOrderNum()).collect(Collectors.toList()));
//        // hosPrescriptions为  null  说明还没生成 处方单
//        if (!CollectionUtils.isEmpty(hosPrescriptions)) {
//            res.setPrescriptionId(hosPrescriptions.get(0).getPrescriptionNum());
//            res.setDrugOrderId(hosPrescriptions.get(0).getDrugNum());
//            res.setDrugOrderKeyId(hosPrescriptions.get(0).getDrugNumKeyId());
//            res.setPrescriptionKeyId(hosPrescriptions.get(0).getPrescriptionKeyId());
//        }
        List<Dict> dicts = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", "PAY_TYPE"));
        Map<String, String> typeMap = dicts.stream().collect(Collectors.toMap(Dict::getCode, Dict::getName));
        if (!StringUtils.isEmpty(res.getPayType())){
            res.setPayType(typeMap.get(res.getPayType()));
        }
        return res;
    }

    @Override
    public void revisitBulkExport(String orderNum, String status, String mobile, String name, String doctorUserName,
                                  HttpServletRequest request, HttpServletResponse response) {

        QueryWrapper<HosRevisit> wrapper = new QueryWrapper<>();

        if (null != orderNum && !"".equals(orderNum)){
            wrapper.eq("order_num",orderNum);
        }
        if (null != name && !"".equals(name)){
            wrapper.like("name",name);
        }
        if (null != mobile && !"".equals(mobile)){
            wrapper.eq("mobile",mobile);
        }
        if (null != doctorUserName && !"".equals(doctorUserName)){
            List<DoctorUser> doctorUsers = this.doctorUserMapper.
                    selectList(new QueryWrapper<DoctorUser>().like("name", doctorUserName));
            if (!CollectionUtils.isEmpty(doctorUsers)){
                List<String> doctorUserIds = doctorUsers.stream().map(DoctorUser::getId).collect(Collectors.toList());
                wrapper.in("doctor_user_id", doctorUserIds);
            }else {
                throw new  BizException("当前条件无数据");
            }
        }
        if (null != status && !"".equals(status)){
            wrapper.eq("status",status);
        }
        List<HosRevisit> hosRevisits = this.revisitOrderMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(hosRevisits)){
            throw new  BizException("当前条件无数据");
        }
        // 复诊订单对应的疾病名称
        List<String> revisitIds = hosRevisits.stream().map(HosRevisit::getOrderNum).collect(Collectors.toList());
//        List<RevisitDiseaseVo> revisitDiseaseVos = this.hosPrescriptionMapper.getDiseaseNamesByVisitId(revisitIds);
        Map<String, RevisitDiseaseVo> revisitDiseaseVoMap = null;
//        if (!CollectionUtils.isEmpty(revisitDiseaseVos)){
//            revisitDiseaseVoMap  = revisitDiseaseVos.stream().collect(Collectors.toMap(RevisitDiseaseVo::getVisitMdtNum, s -> s));
//        }
        Map<String, RevisitDiseaseVo> finalRevisitDiseaseVoMap = revisitDiseaseVoMap;

        List<HosRevisitEntity> list = new ArrayList<>();
        hosRevisits.forEach(s->{

            // 处方单未生成的时候 所对应的疾病是空
            if (null != finalRevisitDiseaseVoMap){
                if (finalRevisitDiseaseVoMap.containsKey(s.getOrderNum())){
                    s.setRevisitDiseaseName(finalRevisitDiseaseVoMap.get(s.getOrderNum()).getRevisitDiseaseNames());
                }
            }

           if ("1".equals(s.getSex())){
               s.setSex("男");
           }else {
               s.setSex("女");
           }
            if ("ALIPAY".equals(s.getPayType())){
                s.setPayType("支付宝");
            }else {
                s.setPayType("微信");
            }
            //状态 DONE订单关闭 CANCEL取消订单 DECLINE拒单 WAIT待付款 PAY已付款|待接单 HAVE已接单
            // START复诊进行中 END订单完成  REFUNDWAIT退款进行中 REFUNDSUCCESS退款成功 CHANGE退款异常  REFUNDCLOSE退款关闭'
            switch (s.getStatus()){
                case "DONE":
                    s.setStatus("订单关闭");
                    break;
                case "CANCEL":
                    s.setStatus("取消订单");
                    break;
                case "DECLINE":
                    s.setStatus("拒单");
                    break;
                case "WAIT":
                    s.setStatus("待付款");
                    break;
                case "PAY":
                    s.setStatus("已付款");
                    break;
                case "HAVE":
                    s.setStatus("已接单");
                    break;
                case "START":
                    s.setStatus("复诊进行中");
                    break;
                case "END":
                    s.setStatus("订单完成");
                    break;
                case "REFUNDWAIT":
                    s.setStatus("退款进行中");
                    break;
                case "REFUNDSUCCESS":
                    s.setStatus("退款成功");
                    break;
                case "CHANGE":
                    s.setStatus("退款异常");
                    break;
                case "REFUNDCLOSE":
                    s.setStatus("退款关闭");
                    break;
            }
            if ("1".equals(s.getIsReadIm())){
                s.setIsReadIm("否");
            }else {
                s.setIsReadIm("是");
            }
            if("1".equals(s.getIsPay())){
                s.setIsPay("未支付");
            }else {
                s.setIsPay("已支付");
            }
            HosRevisitEntity entity = new HosRevisitEntity();
            BeanUtils.copyProperties(s,entity);
            list.add(entity);
        });
        ExcelUtils.writeExcel(request,response,list,HosRevisitEntity.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + "复诊订单表格.xlsx");
    }

    @Override
    public void prescriptionBulkExport(String orderNum, String status, String mobile, String name, String doctorUserName,
                                       String checkPharmaceutist, String startTime,String endTime,String checkStatus,
                                       HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<HosPrescription> wrapper = new QueryWrapper<>();

        if (null != orderNum && !"".equals(orderNum)){
            wrapper.eq("order_num",orderNum);
        }
        if (null != name && !"".equals(name)){
            wrapper.like("name",name);
        }
        if (null != mobile && !"".equals(mobile)){
            wrapper.eq("mobile",mobile);
        }
        if (null != doctorUserName && !"".equals(doctorUserName)){
            List<DoctorUser> doctorUsers = this.doctorUserMapper.
                    selectList(new QueryWrapper<DoctorUser>().like("name", doctorUserName));
            if (!CollectionUtils.isEmpty(doctorUsers)){
                List<String> doctorUserIds = doctorUsers.stream().map(DoctorUser::getId).collect(Collectors.toList());
                wrapper.in("doctor_user_id", doctorUserIds);
            }else {
                throw new  BizException("当前条件无数据");
            }
        }
        if (null != checkPharmaceutist && !"".equals(checkPharmaceutist)){
            wrapper.like("check_pharmaceutist",checkPharmaceutist);
        }
        if (null != startTime && !"".equals(startTime) ){
            wrapper.ge("create_time",startTime);
        }
        if (null != endTime && !"".equals(endTime) ){
            wrapper.le("create_time",endTime);
        }

        if (null != checkStatus && !"".equals(checkStatus)){
            wrapper.eq("check_status",checkStatus);
        }
        if (null != status && !"".equals(status)){
            wrapper.eq("status",status);
        }
        List<HosPrescription> hosPrescriptions = this.hosPrescriptionMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(hosPrescriptions)){
            throw new  BizException("当前条件无数据");
        }
        List<HosPrescriptionEntity> list = new ArrayList<>();
        hosPrescriptions.forEach(s->{
            DoctorUser doctorUser = doctorUserMapper.selectOne(new QueryWrapper<DoctorUser>().eq("id",s.getDoctorUserId()));
            if (null==doctorUser){
                throw new  BizException("当前医生无数据");
            }
            s.setDoctorUserId(doctorUser.getName());
            if ("1".equals(s.getSex())){
                s.setSex("男");
            }else {
                s.setSex("女");
            }
            if ("ALIPAY".equals(s.getPayType())){
                s.setPayType("支付宝");
            }else {
                s.setPayType("微信");
            }
            if("REVISIT".equals(s.getType())){
                s.setType("复诊订单");
            }else if ("VISIT".equals(s.getType())){
                s.setType("在线复诊订单");
            }else {
                s.setType("会诊订单");
            }
            //状态 DONE订单关闭 CANCEL取消订单  WAIT待付款 PAY已付款
            //  END订单完成  REFUNDWAIT退款进行中 REFUNDSUCCESS退款成功 CHANGE退款异常  REFUNDCLOSE退款关闭'
            //NOTSIGN-未签名 SIGN-已签名 PASS-审核通过 REJECT-审核驳回
            switch (s.getStatus()){
                case "DONE":
                    s.setStatus("订单关闭");
                    break;
                case "CANCEL":
                    s.setStatus("取消订单");
                    break;
                case "WAIT":
                    s.setStatus("待付款");
                    break;
                case "PAY":
                    s.setStatus("已付款");
                    break;
                case "END":
                    s.setStatus("订单完成");
                    break;
                case "REFUNDWAIT":
                    s.setStatus("退款进行中");
                    break;
                case "REFUNDSUCCESS":
                    s.setStatus("退款成功");
                    break;
                case "CHANGE":
                    s.setStatus("退款异常");
                    break;
                case "REFUNDCLOSE":
                    s.setStatus("退款关闭");
                    break;
            }
            switch (s.getCheckStatus()){
                case "NOTSIGN":
                    s.setCheckStatus("未签名");
                    break;
                case "SIGN":
                    s.setCheckStatus("已签名");
                    break;
                case "WAIT":
                    s.setCheckStatus("待审核");
                    break;
                case "PASS":
                    s.setCheckStatus("审核通过");
                    break;
                case "REJECT":
                    s.setCheckStatus("审核驳回");
                    break;
            }
            if ("0".equals(s.getIsPay())){
                s.setIsPay("否");
            }else {
                s.setIsPay("是");
            }
            if("1".equals(s.getIsPay())){
                s.setIsPay("未支付");
            }else {
                s.setIsPay("已支付");
            }
            HosPrescriptionEntity entity = new HosPrescriptionEntity();
            BeanUtils.copyProperties(s,entity);
            list.add(entity);
        });
        ExcelUtils.writeExcel(request,response,list,HosPrescriptionEntity.class,"处方订单表格.xlsx");
    }

    @Override
    public void prescriptionDurgBulkExport(String orderNum, String status, String mobile, String name, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<HosPreDrugOrder> wrapper = new QueryWrapper<HosPreDrugOrder>();

        if (null != orderNum && !"".equals(orderNum)){
            wrapper.eq("order_num",orderNum);
        }
        if (null != name && !"".equals(name)){
            wrapper.like("name",name);
        }
        if (null != mobile && !"".equals(mobile)){
            wrapper.eq("mobile",mobile);
        }
        if (null != status && !"".equals(status)){
            wrapper.eq("status",status);
        }
        List<HosPreDrugOrder> hosPreDrugOrders = this.hosPreDrugMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(hosPreDrugOrders)){
            throw new  BizException("当前条件无数据");
        }
        List<HosPreDrugEntity> list = new ArrayList<>();
        hosPreDrugOrders.forEach(s->{
            //  状态 DONE订单关闭 CANCEL取消订单 WAIT待付款 PAY已付款 END订单完成 REFUNDWAIT退款进行中 REFUNDSUCCESS退款成功 CHANGE退款异常  REFUNDCLOSE退款关闭'
            switch (s.getStatus()){
                case "DONE":
                    s.setStatus("订单关闭");
                    break;
                case "CANCEL":
                    s.setStatus("取消订单");
                    break;
                case "WAIT":
                    s.setStatus("待付款");
                    break;
                case "PAY":
                    s.setStatus("已付款");
                    break;
                case "END":
                    s.setStatus("订单完成");
                    break;
                case "REFUNDWAIT":
                    s.setStatus("退款进行中");
                    break;
                case "REFUNDSUCCESS":
                    s.setStatus("退款成功");
                    break;
                case "CHANGE":
                    s.setStatus("退款异常");
                    break;
                case "REFUNDCLOSE":
                    s.setStatus("退款关闭");
                    break;
            }
            HosPreDrugEntity entity = new HosPreDrugEntity();
            BeanUtils.copyProperties(s,entity);
            QueryWrapper<HosPrescription> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",s.getHosPrescriptionId());
            HosPrescription hosPrescription = hosPrescriptionMapper.selectOne(queryWrapper);
            if (null==hosPrescription){
                throw new  BizException("当前处方无数据");
            }
            entity.setAddress(s.getProvince()+""+s.getCity()+""+s.getDistrict()+""+s.getAddress());
            entity.setVisitMdtNum(hosPrescription.getVisitMdtNum());
            entity.setTotalPrice(hosPrescription.getTotalPrice());
            if ("REVISIT".equals(hosPrescription.getType())){
                entity.setType("复诊订单");
            }else if ("VISIT".equals(hosPrescription.getType())){
                entity.setType("在线复诊订单");
            }else {
                entity.setType("会诊订单");
            }
            entity.setHoSickName(hosPrescription.getName());
            entity.setHoSickMobile(hosPrescription.getMobile());
            entity.setPayTime(hosPrescription.getPayTime());
            List<HosPrescriptionDrug> hosPrescriptionDrugs = hosPrescriptionDrugMapper.selectList(new QueryWrapper<HosPrescriptionDrug>().eq("hos_prescription_id", s.getHosPrescriptionId()));
            int drugSumNumber = 0;
            double priceSum = 0;
            double freight = 0;
            for (HosPrescriptionDrug hosPrescriptionDrug : hosPrescriptionDrugs) {
                entity.setDrugName(hosPrescriptionDrug.getName());
                entity.setPrice(hosPrescriptionDrug.getPrice());
                entity.setNumber(hosPrescriptionDrug.getNumber());
                Double price = Double.valueOf(entity.getPrice());
                Integer number = Integer.valueOf(hosPrescriptionDrug.getNumber());
                String sumPrice = String.valueOf(number*price);
                entity.setSumPrice(sumPrice);
                drugSumNumber += number;
                priceSum += Double.valueOf(sumPrice);
            }
            entity.setDrugSumNumber(String.valueOf(drugSumNumber));
            entity.setPriceSum(String.valueOf(priceSum+freight));
            entity.setFreight("0");

            list.add(entity);
        });
        ExcelUtils.writeExcel(request,response,list,HosPreDrugEntity.class,"药品订单表格.xlsx");
    }
}
