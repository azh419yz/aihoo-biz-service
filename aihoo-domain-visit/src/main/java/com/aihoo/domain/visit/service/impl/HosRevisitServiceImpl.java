package com.aihoo.domain.visit.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aihoo.enums.PushMessageType;
import com.aihoo.util.CastUtil;
import com.aihoo.util.OssFileUtils;
import com.aihoo.util.SecurityUtil;
import com.aihoo.util.StringUtil;
import com.aihoo.exception.BizException;
import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.visit.dto.HosOrder;
import com.aihoo.domain.visit.dto.OrderResult;
import com.aihoo.domain.consultation.model.mapper.MdtOrderMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDrugMapper;
import com.aihoo.domain.visit.model.mapper.HosRevisitMapper;
import com.aihoo.domain.sys.model.mapper.DictMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionMapper;
import com.aihoo.domain.visit.model.mapper.HealthRecordsMapper;
import com.aihoo.domain.sys.model.mapper.DiseaseMapper;
import com.aihoo.domain.visit.model.mapper.HosRevisitImgMapper;


import com.aihoo.properties.CaProperties;
import com.aihoo.constant.DictTypeEnum;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.security.AuthUtil;
import com.aihoo.domain.im.enums.IMMsgType;
import com.aihoo.util.OrderNoUtil;
import com.google.common.collect.Maps;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;
import com.aihoo.domain.hospital.model.mapper.DrugMapper;
import com.aihoo.domain.patient.model.mapper.PatientUserMapper;
import com.aihoo.domain.sys.model.mapper.TBaseMapper;
import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDisease;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrug;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.visit.model.entity.HosRevisitImg;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionError;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDiseaseError;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrugError;
import com.aihoo.domain.doctor.model.entity.DoctorSet;
import com.aihoo.domain.doctor.model.entity.DoctorSetTimes;
import com.aihoo.domain.visit.model.entity.HealthRecords;
import com.aihoo.domain.patient.model.entity.PatientUser;
import com.aihoo.domain.consultation.model.entity.MdtOrder;
import com.aihoo.domain.sys.model.entity.Disease;
import com.aihoo.domain.sys.model.entity.Dict;
import com.aihoo.domain.sys.model.entity.TBase;
import com.aihoo.domain.prescription.service.PrescriptionDrugErrorService;
import com.aihoo.domain.im.service.PushMessageService;
import com.aihoo.domain.prescription.service.PrescriptionDrugService;
import com.aihoo.domain.prescription.service.PrescriptionDiseaseErrorService;
import com.aihoo.domain.prescription.service.PrescriptionDiseaseService;
import com.aihoo.domain.im.service.IMService;
import com.aihoo.domain.visit.service.LogService;
import com.aihoo.domain.payment.service.JudgeService;
import com.aihoo.domain.visit.service.HosRevisitService;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/22 19:01
 * @description：复诊订单
 */
@Service("doctorApiHosRevisitServiceImpl")
public class HosRevisitServiceImpl extends ServiceImpl<HosRevisitMapper, HosRevisit> implements HosRevisitService {
    private final Log log = LogFactory.get();
    @Resource
    private HosRevisitMapper hosRevisitMapper;//复诊订单
    @Resource
    private DoctorUserMapper doctorUserMapper;//医生用户
    @Resource
    private HosRevisitImgMapper hosRevisitImgMapper;//复诊图片
    @Resource
    private PatientUserMapper patientUserMapper;//患者信息
    @Resource
    private TBaseMapper tBaseMapper;//
    @Resource
    private LogService logService;//日志
    @Resource
    private DiseaseMapper diseaseMapper;
    @Resource
    private DictMapper dictMapper;
    @Resource
    private DrugMapper drugMapper;
    @Resource
    private HosPrescriptionMapper hosPrescriptionMapper;
    @Resource
    private PrescriptionDiseaseService prescriptionDiseaseService;
    @Resource
    private PrescriptionDrugService prescriptionDrugService;
    @Resource
    private IMService imService;
    @Resource
    private PrescriptionDiseaseErrorService prescriptionDiseaseErrorService;
    @Resource
    private PrescriptionDrugErrorService prescriptionDrugErrorService;
    @Resource
    private HealthRecordsMapper healthRecordsMapper;
    @Resource
    private CaProperties caProperties;
    @Resource
    private JudgeService judgeService;
    @Resource
    private HosPrescriptionDrugMapper hosPrescriptionDrugMapper;
    @Resource
    private MdtOrderMapper mdtOrderMapper;

    // 患者
    private static final String PATIENT = "PATIENT_";

    // 医生
    private static final String DOCTOR = "DOCTOR_";
    @Resource
    private PushMessageService pushMessageService;

    /**
     * 接单
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String haveRevisit(Map<String, String> map) {
        String id = map.get("id");
        HosRevisit revisit = hosRevisitMapper.selectById(id);
        if (StatusEnumUtil.PAY.equals(revisit.getStatus())) {
            LambdaQueryWrapper<HosRevisit> wrapper = new QueryWrapper<HosRevisit>().lambda();
            wrapper.eq(HosRevisit::getId, id).eq(HosRevisit::getStatus, "PAY");
            HosRevisit hosRevisit = new HosRevisit();
            hosRevisit.setHaveTime(DateUtil.now());
            hosRevisit.setStatus(StatusEnumUtil.HAVE);
            int i = hosRevisitMapper.update(hosRevisit, wrapper);
            if (i == 0) {
                return "ERROR";
            }
            serviceHaveRevisit(revisit);
            logService.setRevisitLog(revisit, StatusEnumUtil.HAVE, "医生" + logService.getDoctorUserName() + "接单患者" + logService.getSickName(revisit.getHosSickId()) + "的复诊订单");
//            QueryWrapper<HosRevisitImg> hosRevisitImgQueryWrapper = new QueryWrapper<>();
//            hosRevisitImgQueryWrapper.eq("hos_revisit_id", id);
//            List<HosRevisitImg> hosRevisitImgs = hosRevisitImgMapper.selectList(hosRevisitImgQueryWrapper);
//            List<String> imgList = new ArrayList<>();
//            for (HosRevisitImg hosRevisitImg : hosRevisitImgs) {
//                imgList.add(hosRevisitImg.getImg());
//            }
//            revisit.setImgList(imgList);
//            revisit.setMsgType(IMMsgType.RevisitOrderCard);
//            revisit.setBusinessID("MSHCustomMsg");
//            String desc = revisit.getName() + "," + (org.apache.commons.lang3.StringUtils.isNotEmpty(revisit.getSex()) ? ("0".equals(revisit.getSex()) ? "女" : "1".equals(revisit.getStatus()) ? "男" : "未知") : "未知") + "," + revisit.getAge() + "岁";
//            imService.sendPostHttpRequest(PATIENT + revisit.getPatientUserId(), DOCTOR + revisit.getDoctorUserId(), revisit,desc,"","");
            return "接单成功";
        }
        return "ERROR";
    }

    /**
     * 医生接复诊订单服务提醒
     *
     * @param hosRevisit
     * @return
     */
    private boolean serviceHaveRevisit(HosRevisit hosRevisit) {
        try {
            DoctorUser doctorUser = doctorUserMapper.selectOne(new QueryWrapper<DoctorUser>().eq("id", hosRevisit.getDoctorUserId()));
            if (null == doctorUser) {
                return true;
            }
            String name = doctorUser.getName();
            pushMessageService.insertPatient("复诊订单", hosRevisit.getPatientUserId(), "您预约的" + name + "医生复诊已预约成功，请在预约时间内前往与医生交流病情。",
                    PushMessageType.messageType_REVISIT, hosRevisit.getId(), "您预约的" + name + "医生复诊已预约成功，请在预约时间内前往与医生交流病情。", "0");
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    /**
     * 复诊订单列表
     *
     * @param map
     * @return
     */
    @Override
    public List revisitOrderList(Map<String, String> map) {
        String status = map.get("status");
        String sickId = map.get("sickId");
        if (status == null) {
            status = "";
        }
        List<OrderResult> orderResultArrayList = new ArrayList<>();
        DoctorUser loginUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
        String id = loginUser.getId();
        LambdaQueryWrapper<HosRevisit> hosVisitQueryWrapper = new QueryWrapper<HosRevisit>().lambda();
        hosVisitQueryWrapper.select().eq(HosRevisit::getDoctorUserId, id).orderByDesc(HosRevisit::getCreateTime);
        //状态 DECLINE 拒单 PAY 已付款|待接单 HAVE 已接单  START 复诊进行中 END 订单完成
        if (StrUtil.isNotBlank(sickId)) {
            hosVisitQueryWrapper.eq(HosRevisit::getHosSickId, sickId);
        }
        if (status.toUpperCase().equals("SUCCESS")) {
            hosVisitQueryWrapper.and(wrapper -> wrapper.eq(HosRevisit::getStatus, StatusEnumUtil.END));
        } else if (status.toUpperCase().equals("WAIT")) {
            hosVisitQueryWrapper.select().and(wrapper -> wrapper.eq(HosRevisit::getStatus, StatusEnumUtil.PAY).or()
                    .eq(HosRevisit::getStatus, StatusEnumUtil.HAVE).or().eq(HosRevisit::getStatus, StatusEnumUtil.START));
        } else {
            hosVisitQueryWrapper.select().and(wrapper -> wrapper.eq(HosRevisit::getStatus, StatusEnumUtil.END).or()
                    .eq(HosRevisit::getStatus, StatusEnumUtil.PAY).or().eq(HosRevisit::getStatus, StatusEnumUtil.HAVE).or()
                    .eq(HosRevisit::getStatus, StatusEnumUtil.START));
        }
        Integer page;
        Integer limit;
        try {
            page = Integer.parseInt(map.get("page"));
            limit = Integer.parseInt(map.get("limit"));
        } catch (Exception e) {
            page = 1;
            limit = 10;
            log.error("复诊列表无分页条件，默认展示前十条");
        }
        Page<HosRevisit> hosRevisitPage = new Page<>(page, limit);
        IPage<HosRevisit> hosRevisitIPage = hosRevisitMapper.selectPage(hosRevisitPage, hosVisitQueryWrapper);
        List<HosRevisit> records = hosRevisitIPage.getRecords();
        for (HosRevisit hosRevisit : records) {
            OrderResult orderResult = new OrderResult();
            Date revisitStartTime = DateUtil.parse(hosRevisit.getRevisitStartTime(), "yyyy-MM-dd HH:mm");
            Date revisitEndTime = DateUtil.parse(hosRevisit.getRevisitEndTime(), "yyyy-MM-dd HH:mm");
            String start = DateUtil.format(revisitStartTime, "yyyy-MM-dd HH:mm");
            String end = DateUtil.format(revisitEndTime, "HH:mm");
            orderResult.setDate(start + "-" + end);
            orderResult.setOrderId(hosRevisit.getId() + "");
            orderResult.setName(hosRevisit.getName());
            orderResult.setContent(hosRevisit.getContent());
            orderResult.setStatus(hosRevisit.getStatus());
            orderResult.setTotalPrice(hosRevisit.getTotalPrice());
            orderResult.setCreateTime(hosRevisit.getCreateTime());
            orderResult.setEndTime(hosRevisit.getEndTime());
            orderResult.setType("REVISIT");
            orderResult.setTypeName("复诊订单");
//            审核处方 WAIT-等待审核 PASS-审核通过 REJECT-审核驳回
            HashMap<String, Object> param = new HashMap<>();
            param.put("id", hosRevisit.getId());
            if (null != param.get("id")) {
                Map prescriptionStatus = getPrescriptionStatus(param);
                if (null != prescriptionStatus) {
                    orderResult.setCheckStatus(prescriptionStatus.get("status").toString());
                    orderResult.setCheckStatusName(prescriptionStatus.get("statusName").toString());
                }
            }
            orderResult.setPatientId(hosRevisit.getPatientUserId());
            orderResult.makeRevisitbtnJson();
            if (DateUtil.parse(hosRevisit.getRevisitStartTime(), "yyyy-MM-dd HH:mm:ss").before(DateUtil.date()) && DateUtil.parse(hosRevisit.getRevisitEndTime(), "yyyy-MM-dd HH:mm:ss").after(DateUtil.date())) {
                orderResult.setIsCanChat("1");
            } else if (StatusEnumUtil.START.equals(hosRevisit.getStatus()) && DateUtil.parse(hosRevisit.getRevisitStartTime(), "yyyy-MM-dd HH:mm:ss").before(DateUtil.date())) {
                orderResult.setIsCanChat("1");
            } else {
                orderResult.setIsCanChat("0");
            }
            orderResultArrayList.add(orderResult);
        }

        return orderResultArrayList;
    }

    /* *//**
     * 复诊-》订单-》转单
     *
     * @param map
     * @return
     *//*
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String revisitTransfer(Map<String, String> map) {
        String id = map.get("id");
        String msg = map.get("msg");
        DoctorUser loginUser = AuthUtil.getLoginUser();
        String departId = loginUser.getDepartId();
        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
        wrapper.eq("depart_id", departId);
        List<DoctorUser> doctorUsers = doctorUserMapper.selectList(wrapper);
        //是否有同科室医生
        if (doctorUsers != null || doctorUsers.size() > 0) {
            for (DoctorUser doctorUser : doctorUsers) {
                if (doctorUser.getId().equals(hosRevisit.getDoctorUserId() + "")) {
                    continue;
                }
                DoctorSet doctorSet = doctorSetMapper.selectDoctorUserId(doctorUser.getId());
                if (doctorSet != null) {
                    String isRevisit = doctorSet.getIsRevisit();
                    //同科室医生是否开启复诊
                    if (isRevisit.equals("1")) {
                        Date revisitStartTime = DateUtil.parse(hosRevisit.getRevisitStartTime(), "yyyy-MM-dd HH:mm");
                        Date revisitEndTime = DateUtil.parse(hosRevisit.getRevisitEndTime(), "yyyy-MM-dd HH:mm");
                        String weekCode = WeekUtil.dateToWeek(revisitStartTime);
                        QueryWrapper<DoctorSetTimes> doctorSetTimesQueryWrapper = new QueryWrapper<>();
                        doctorSetTimesQueryWrapper.eq("doctor_user_id", doctorUser.getId())
                                .eq("week_code", weekCode)
                                .eq("type", "REVISIT");
                        List<DoctorSetTimes> doctorSetTimesList = doctorSetTimesMapper.selectList(doctorSetTimesQueryWrapper);
                        //当前订单预约时间内候选医生是否有排班
                        if (doctorSetTimesList != null && doctorSetTimesList.size() > 0) {
                            for (DoctorSetTimes doctorSetTimes : doctorSetTimesList) {
                                String format1 = DateUtil.format(revisitStartTime, "HH:mm");
                                String format2 = DateUtil.format(revisitEndTime, "HH:mm");
                                String startTime = doctorSetTimes.getStartTime();
                                String endTime = doctorSetTimes.getEndTime();
                                if (WeekUtil.inEqTime(format1, format2, startTime, endTime)) {
                                    //排班时间接单每一小时最大4单
                                    int revisitStartMinute = WeekUtil.getMinute(revisitStartTime);
                                    Date start = DateUtil.parse(startTime, "HH:mm");
                                    int startMinute = WeekUtil.getMinute(start);
                                    if (revisitStartMinute == 30 && startMinute == 0) {
                                        revisitStartTime = WeekUtil.addMinute(revisitStartTime, -30);
                                    }
                                    String startHHmm = DateUtil.format(start, "HH:mm");
//                                    QueryWrapper<HosRevisit> hosRevisitQueryWrapper = new QueryWrapper<>();
//                                    hosRevisitQueryWrapper.eq("doctor_user_id", doctorUser.getId())
//                                            .eq("status", "HAVE").or()
//                                            .eq("status", "PAY")
//                                            .eq("revisit_start_time", revisitStartTime);
                                    List<HosRevisit> hosRevisits = hosRevisitMapper.selectDoctorIdAndStartTime(doctorUser.getId(), revisitStartTime);
                                    int sum = 0;
                                    for (HosRevisit hosRevisit1 : hosRevisits) {
                                        String format = DateUtil.format(DateUtil.parse(hosRevisit1.getRevisitStartTime(), "yyyy-MM-dd HH:mm:ss"), "HH:mm");
                                        if (startHHmm.equals(format)) {
                                            sum++;
                                        }
                                    }
                                    if ((startMinute == 0 && sum < 4) || (startMinute == 30 && sum < 2)) {
                                        hosRevisit.setDoctorUserId(doctorUser.getId());
                                        hosRevisit.setMsg(msg);
                                        hosRevisitMapper.updateById(hosRevisit);
                                        logService.setRevisitLog(hosRevisit, StatusEnumUtil.DECLINE, logService.getDoctorUserName() + "医生转单到" + logService.getSickName(hosRevisit.getHosSickId()) + "医生");
                                        //转单成功
                                        return "ok";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return "转单失败!转单要求：必须是同一科室的医生，开启复诊接单，在复诊时间区间内，每一小时最大4单";
    }*/

    /**
     * 复诊详情
     *
     * @param map
     * @return
     */
    @Override
    public Object revisitData(Map<String, Object> map) {
        String id = String.valueOf(map.get("id"));
        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        HosOrder hosOrder = new HosOrder();
        hosOrder.setDoctorId(hosRevisit.getDoctorUserId());
        hosOrder.setOrderId(hosRevisit.getId());
        hosOrder.setSex(hosRevisit.getSex());
        hosOrder.setAge(hosRevisit.getAge());
        hosOrder.setName(hosRevisit.getName());
        hosOrder.setPatientId(hosRevisit.getPatientUserId());
        String payTime = hosRevisit.getPayTime();
        hosOrder.setPayTime(payTime);
        hosOrder.setContent(hosRevisit.getContent());
        hosOrder.setOrderNum(hosRevisit.getOrderNum());
        hosOrder.setCreateTime(hosRevisit.getCreateTime());
        hosOrder.setDoctorAdvice(hosRevisit.getDoctorAdvice());
        hosOrder.setDiseaseName(hosRevisit.getDiseaseName());
        String revisitRevisitStartTime = hosRevisit.getRevisitStartTime();
        String revisitRevisitEndTime = hosRevisit.getRevisitEndTime();
        hosOrder.setRevisitStartTime(revisitRevisitStartTime);
        hosOrder.setRevisitEndTime(revisitRevisitEndTime);
        hosOrder.setRevisitTimeStr(hosRevisit.getRevisitDateStr() + " " + hosRevisit.getRevisitTimeStr());
        //临床诊断
        QueryWrapper<HosPrescription> hosPrescriptionQueryWrapper = new QueryWrapper<>();
        hosPrescriptionQueryWrapper.eq("visit_mdt_num", hosRevisit.getOrderNum()).orderByDesc("create_time");
        HosPrescription hosPrescription = hosPrescriptionMapper.selectOne(hosPrescriptionQueryWrapper);
        if (hosPrescription != null) {
            hosOrder.setMedicalCertificate(hosPrescription.getMedicalCertificate());
        }
        hosOrder.setType("REVISIT");
        //获取患者获取头像
        PatientUser patientUser = patientUserMapper.selectById(hosRevisit.getPatientUserId());
        //获取附件图片
        hosOrder.setHeadImg(patientUser.getHeadImg());
        List<HosRevisitImg> hosRevisitImgList = hosRevisitImgMapper.selectByRevisitId(id);
        List<Object> imgList = new ArrayList<>();
        for (HosRevisitImg hosRevisitImg : hosRevisitImgList) {
            imgList.add(hosRevisitImg.getImg());
        }
        hosOrder.setImgList(imgList);
        //状态
        String status = hosRevisit.getStatus();
        hosOrder.setStatus(status);
        hosOrder.setStatusName("复诊" + StatusEnumUtil.getStatus(status));
        hosOrder.makeRevisitbtnJson();
        // PAY 未接单（根据支付时间保持24小时）：剩余21小时15分自动结束结束复诊
        // HAVE 已接单，未到预约时间：（NULL）
        // START 已接单，到预约时间（根据接单时间往后推24小时关闭）：剩余接诊时间：21小时5分
        // END 已完成：患者评价：5.0
        //支付时间payTime
        //预约时间yyyy-MM-dd HH:mm:ss
        Date date = DateUtil.date();
        String revisitRevisitHaveTime = hosRevisit.getHaveTime();
        Date revisitRevisitStartTimeParse;
        if (status.equals(StatusEnumUtil.PAY)) {
            //倒计时聊天时间
            TBase bases = tBaseMapper.selectByKey("REVISIT_TIMES");
            //倒计时聊天时间
            String mills = "0";
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(hosRevisit.getPayTime())) {
                long cutSecond = null != bases ? (StrUtil.isNotEmpty(bases.getContent()) ? Long.parseLong(bases.getContent()) : 86400) : 86400;
                if (StrUtil.isBlank(hosRevisit.getPayTime()) || StrUtil.isBlank(hosRevisit.getRevisitEndTime())) {
                    mills = "0";
                } else {
                    Date revisitEndTime = DateUtil.parseDateTime(hosRevisit.getRevisitEndTime());
                    Date haveTime = DateUtil.parseDateTime(hosRevisit.getPayTime());
                    DateTime now = DateUtil.date();
                    long between = DateUtil.between(revisitEndTime, haveTime, DateUnit.SECOND);
                    //大于一天
                    if (between > cutSecond) {
                        //接单24小时后的时间
                        DateTime dateTime = DateUtil.offsetDay(haveTime, 1);
                        //当前时间在接单24小时后的时间之前为true
                        if (now.before(dateTime)) {
                            mills = String.valueOf(DateUtil.between(dateTime, now, DateUnit.SECOND));
                        } else {
                            mills = "0";
                        }
                    } else {
                        //当前在预约结束时间之前true
                        if (now.before(revisitEndTime)) {
                            mills = String.valueOf(DateUtil.between(revisitEndTime, now, DateUnit.SECOND));
                        } else {
                            mills = "0";
                        }
                    }
                }
            }
            hosOrder.setCountDown(mills);
        } else if (status.equals(StatusEnumUtil.HAVE)) {
            revisitRevisitStartTimeParse = DateUtil.parse(revisitRevisitHaveTime, "yyyy-MM-dd HH:mm:ss");
            Date revisitRevisitEndTimeParse = DateUtil.parse(revisitRevisitEndTime, "yyyy-MM-dd HH:mm:ss");
            //倒计时聊天时间
            String mills = "0";
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(hosRevisit.getHaveTime())) {
                if (StrUtil.isBlank(hosRevisit.getHaveTime()) || StrUtil.isBlank(hosRevisit.getRevisitEndTime())) {
                    mills = "0";
                } else {
                    Date revisitStartTime = DateUtil.parseDateTime(hosRevisit.getRevisitStartTime());
                    Date revisitEndTime = DateUtil.parseDateTime(hosRevisit.getRevisitEndTime());
                    DateTime now = DateUtil.date();
                    //当前在预约结束时间之前true
                    if (now.before(revisitStartTime)) {
                        hosOrder.setIsCanStart("0");
                        mills = String.valueOf(DateUtil.between(revisitStartTime, now, DateUnit.SECOND));
                    } else if (now.isIn(revisitStartTime, revisitEndTime)) {
                        hosOrder.setIsCanStart("1");
                        mills = String.valueOf(DateUtil.between(revisitEndTime, now, DateUnit.SECOND));
                    } else {
                        hosOrder.setIsCanStart("0");
                        mills = "0";
                    }
                }
            }
            hosOrder.setCountDown(mills);
        } else if (status.equals(StatusEnumUtil.START)) {
            //倒计时聊天时间
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(hosRevisit.getHaveTime())) {
                DateTime now = DateUtil.date();
                DateTime startTime = DateUtil.offsetDay(DateUtil.parseDateTime(hosRevisit.getStartTime()), 1);
                //当前在预约结束时间之前true
                if (now.before(startTime)) {
                    hosOrder.setCountDown(String.valueOf(DateUtil.between(startTime, now, DateUnit.SECOND)));

                } else {
                    hosOrder.setCountDown("0");
                }
            }
        } else if (status.equals(StatusEnumUtil.END)) {
            hosOrder.setEndTime(hosRevisit.getEndTime());
            hosOrder.setFiveStar(StringUtil.isBlank(hosRevisit.getFiveStar()) ? "" : hosRevisit.getFiveStar());
            Map prescriptionStatus = getPrescriptionStatus(map);
            if (prescriptionStatus == null) {
                hosOrder.setCheckStatus(null);//处方审核状态;
            } else {
                Object checkStatus = prescriptionStatus.get("status");
                hosOrder.setCheckStatus(checkStatus == null ? null : checkStatus.toString());//处方审核状态;
            }
        }
        if (DateUtil.parse(revisitRevisitStartTime, "yyyy-MM-dd HH:mm:ss").before(DateUtil.date()) && DateUtil.parse(revisitRevisitEndTime, "yyyy-MM-dd HH:mm:ss").after(DateUtil.date())) {
            hosOrder.setIsCanChat("1");
        } else if (StatusEnumUtil.START.equals(hosRevisit.getStatus()) && DateUtil.parse(hosRevisit.getRevisitStartTime(), "yyyy-MM-dd HH:mm:ss").before(DateUtil.date())) {
            hosOrder.setIsCanChat("1");
        } else {
            hosOrder.setIsCanChat("0");
        }
        return hosOrder;
    }

    /**
     * 复诊—》开处方-》疾病列表
     *
     * @param map
     * @return
     */
    @Override
    public List diseaseList(Map<String, Object> map) {
        int page = 1;
        int limit = 10;
        try {
            page = Integer.parseInt(String.valueOf(map.get("page")));
            limit = Integer.parseInt(String.valueOf(map.get("limit")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Page<Disease> ipage = new Page<>(page, limit);
        QueryWrapper<Disease> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", "0").orderByAsc("id");
        if (map.get("diseaseName") != null) {
            String diseaseName = String.valueOf(map.get("diseaseName"));
            wrapper.like("name", diseaseName);
        }
        IPage<Disease> diseaseIPage = diseaseMapper.selectPage(ipage, wrapper);
        List<Disease> records = diseaseIPage.getRecords();
        return records;
    }

    /**
     * 复诊—》开处方-》药品列表
     *
     * @param map page limit
     * @return
     */
    @Override
    public List drugList(Map<String, String> map) {
        int page = 1;
        int limit = 10;
        try {
            page = Integer.parseInt(String.valueOf(map.get("page")));
            limit = Integer.parseInt(String.valueOf(map.get("limit")));
        } catch (Exception e) {
            log.error("没有分页条件");
        }
        Page<Drug> ipage = new Page<>(page, limit);
        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "1").orderByAsc("id");
        String drugName = map.get("drugName");
        if (!StringUtils.isNullOrEmpty(drugName)) {
            wrapper.like("name", drugName);
        }
        IPage<Drug> diseaseIPage = drugMapper.selectPage(ipage, wrapper);
        List<Drug> records = diseaseIPage.getRecords();
        return records;
    }

    /**
     * 查询dist字典表list
     *
     * @param type 类型
     * @return List
     */
    @Override
    public List<Map<String, String>> dictList(String type) {
        return dictMapper.selectByType(type);
    }

    /**
     * 提交签名接口
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String sign(Map<String, Object> map) {
        String id = map.get("id").toString();
        HosPrescription send = hosPrescriptionMapper.selectById(id);
        if (null != send.getDoctorSignet() && !"".equals(send.getDoctorSignet())) {
            return send.getDoctorSignet();
        }
        HosPrescription hosPrescription = new HosPrescription();
        hosPrescription.setId(id);
        boolean sealUrl = getSealUrl(Objects.requireNonNull(doctorUserMapper.selectById(AuthUtil.getLoginUser().getId())).getPapersNumbers(), hosPrescription);
        if (sealUrl) {
//            getSign(AuthUtil.getLoginUser().getPapersNumbers(), send);
            send.setCheckStatus(StatusEnumUtil.SIGN);
            send.setDoctorSignet(hosPrescription.getDoctorSignet());
            hosPrescriptionMapper.updateById(send);
            return hosPrescription.getDoctorSignet();
        } else {
            return "ERROR";
        }
    }

    /**
     * 开处方接口
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String setPrescription(Map<String, Object> map) {
        //复诊订单查询
        String id = String.valueOf(map.get("id"));//id
        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        if (hosRevisit == null) {
            log.error("对应id无复诊订单");
            return null;
        }
        QueryWrapper<HosPrescription> hosPrescriptionQueryWrapper = new QueryWrapper<>();
        hosPrescriptionQueryWrapper.eq("visit_mdt_num", hosRevisit.getOrderNum());
        HosPrescription old = hosPrescriptionMapper.selectOne(hosPrescriptionQueryWrapper);
        if (old != null) {
            String checkStatus = old.getCheckStatus();
//        WAIT-等待审核 PASS-审核通过 REJECT-审核驳回 NOTSIGN-未签名 SIGN-已签名
            if (!"REJECT".equals(checkStatus)) {
                log.error("该复诊订单已存在未审核驳回的处方单，创建处方单失败");
                return null;
            }
        }
        //疾病列表查询
        Object disease = map.get("diseaseCodes");//疾病 ["O40.x00","N84.900","O07.900"]
        List<String> diseases = CastUtil.castList(disease, String.class);
        QueryWrapper<Disease> wrapper = new QueryWrapper<>();
        wrapper.in("code", diseases);
        List<Disease> diseaseList = diseaseMapper.selectList(wrapper);
        if (diseaseList == null || diseaseList.size() == 0) {
            log.error("疾病code错误，创建处方单失败");
            return null;
        }
        /**
         * 创建处方
         */
        HosPrescription hosPrescription = new HosPrescription();
        hosPrescription.setPatientUserId(hosRevisit.getPatientUserId());
        hosPrescription.setHosSickId(hosRevisit.getHosSickId());
        hosPrescription.setDoctorUserId(hosRevisit.getDoctorUserId());
        hosPrescription.setType("REVISIT");
        hosPrescription.setOtherId(id);
        String orderNum = hosRevisit.getOrderNum();
        hosPrescription.setVisitMdtNum(orderNum);
        hosPrescription.setOrderNum("P" + OrderNoUtil.getOrderNo());
        hosPrescription.setPrescriptionNum(OrderNoUtil.getOrderNo());
        hosPrescription.setFeeType("SELF");
        hosPrescription.setName(hosRevisit.getName());
        hosPrescription.setIdCard(hosRevisit.getIdCard());
        hosPrescription.setSex(hosRevisit.getSex());
        hosPrescription.setAge(hosRevisit.getAge());
        hosPrescription.setMobile(hosRevisit.getMobile());
        DoctorUser loginUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
        hosPrescription.setDepartCode(loginUser.getDepartCode());
        hosPrescription.setDepartName(hosRevisit.getDepartName());
        hosPrescription.setCheckStatus(StatusEnumUtil.NOTSIGN);
//        hosPrescription.setDoctorSignet(loginUser.getName());
        //药品查询[{"drugId":"7","freqMedCode":"Prn","routeAdmiCode":"922"}]
        List<Map> drugs = CastUtil.castList(map.get("drugs"), Map.class);
        double sum = 0d;
        for (Map drug : drugs) {
            String drugId = drug.get("drugId").toString();
            double number = Double.parseDouble(drug.get("number").toString());
            Drug selectDrug = drugMapper.selectById(drugId);
            if (selectDrug == null) {
                log.error("药品id错误，创建处方单失败");
                return null;
            }
            double price = Double.parseDouble(selectDrug.getPrice());
            sum += (price * number);
        }
        hosPrescription.setTotalPrice(String.valueOf(sum));
        if (null == old) {
            hosPrescriptionMapper.insert(hosPrescription);
        } else {
            String oldId = old.getId();
            hosPrescription.setId(oldId);
            hosPrescription.setCheckStatus(StatusEnumUtil.NOTSIGN);
            hosPrescription.setDoctorSignet("");
            hosPrescription.setCreateTime(DateUtil.now());
            hosPrescriptionMapper.updateById(hosPrescription);
            savePrescriptionError(hosPrescription, oldId);
        }
        String hosPrescriptionId = hosPrescription.getId();
        ArrayList<HosPrescriptionDisease> hosPrescriptionDiseases = new ArrayList<>();
        String medicalCertificate = "";
        for (Disease disease1 : diseaseList) {
            //处方绑定疾病
            HosPrescriptionDisease hosPrescriptionDisease = new HosPrescriptionDisease();
            hosPrescriptionDisease.setDiseaseCode(disease1.getCode());
            hosPrescriptionDisease.setDiseaseName(disease1.getName());
            hosPrescriptionDisease.setHosPrescriptionId(hosPrescriptionId);
            hosPrescriptionDiseases.add(hosPrescriptionDisease);
            if ("".equals(medicalCertificate)) {
                medicalCertificate += disease1.getName();
            } else {
                medicalCertificate += "," + disease1.getName();
            }
        }
        HosPrescription update = new HosPrescription();
        update.setMedicalCertificate(medicalCertificate);
        update.setId(hosPrescriptionId);
        hosPrescriptionMapper.updateById(update);
        prescriptionDiseaseService.saveBatch(hosPrescriptionDiseases);
        saveDrugs(drugs, hosPrescriptionId);
        logService.setPrescriptionLog(hosPrescription, StatusEnumUtil.NOTSIGN, loginUser.getName() + "医生为就诊人" + hosRevisit.getName() + "创建处方待认证");
        
        judgeService.judge(hosPrescription.getId());
        return hosPrescription.getId();
    }

    /**
     * 开处方保存药品
     *
     * @param drugs             药品列表
     * @param hosPrescriptionId 处方单id
     */
    private void saveDrugs(List<Map> drugs, String hosPrescriptionId) {
        List<HosPrescriptionDrug> hosPrescriptionDrugs = new ArrayList<>();
        for (Map drug : drugs) {
            String drugId = drug.get("drugId").toString();
            Drug selectDrug = drugMapper.selectById(drugId);
            HosPrescriptionDrug hosPrescriptionDrug = new HosPrescriptionDrug();
            hosPrescriptionDrug.setHosPrescriptionId(hosPrescriptionId);
            hosPrescriptionDrug.setDrugId(selectDrug.getId());
            hosPrescriptionDrug.setName(selectDrug.getName());
            hosPrescriptionDrug.setSize(selectDrug.getSize());
            hosPrescriptionDrug.setDrugDosCode(selectDrug.getDrugDosCode());
            hosPrescriptionDrug.setDrugDosName(selectDrug.getDrugDosName());
            hosPrescriptionDrug.setUnitMeasure(selectDrug.getUnitMeasure());
            hosPrescriptionDrug.setPackUnitCode(selectDrug.getPackUnitCode());
            hosPrescriptionDrug.setPackUnitName(selectDrug.getPackUnitName());
            hosPrescriptionDrug.setPrice(selectDrug.getPrice());
            hosPrescriptionDrug.setIsAntibiotics(selectDrug.getIsAntibiotics());
            hosPrescriptionDrug.setIsInjection(selectDrug.getIsInjection());
            hosPrescriptionDrug.setIsAnesthesia(selectDrug.getIsAnesthesia());
            hosPrescriptionDrug.setIsMonitor(selectDrug.getIsMonitor());
            hosPrescriptionDrug.setContent(selectDrug.getContent());
            String freqMedCode = drug.get("freqMedCode").toString();
            String routeAdmiCode = drug.get("routeAdmiCode").toString();
            String number = drug.get("number").toString();
            String useDay = drug.get("useDay").toString();
            String dosage = drug.get("dosage").toString();
            Dict freqMedDict = dictMapper.selectByCode(DictTypeEnum.YYPC.getType(), freqMedCode);
            Dict routeAdmiDict = dictMapper.selectByCode(DictTypeEnum.ROUTE_ADMI.getType(), routeAdmiCode);
            String freqMedName = freqMedDict.getName();
            String routeAdmiName = routeAdmiDict.getName();
            hosPrescriptionDrug.setNumber(number);//药品数量
            hosPrescriptionDrug.setUseDay(useDay);//用药天数
            hosPrescriptionDrug.setDosage(dosage);//用量
            hosPrescriptionDrug.setFreqMedCode(freqMedCode);//默认用药频次编码
            hosPrescriptionDrug.setFreqMedName(freqMedName);
            hosPrescriptionDrug.setRouteAdmiCode(routeAdmiCode);//默认用药途径编码
            hosPrescriptionDrug.setRouteAdmiName(routeAdmiName);
            hosPrescriptionDrug.setErp(selectDrug.getErp());
            hosPrescriptionDrug.setErpId(selectDrug.getErpId());
            hosPrescriptionDrugs.add(hosPrescriptionDrug);
        }
        prescriptionDrugService.saveBatch(hosPrescriptionDrugs);
    }

    /**
     * 重新提交处方时保存到驳回表
     *
     * @param hosPrescription
     * @param oldId
     */
    private void savePrescriptionError(HosPrescription hosPrescription, String oldId) {
        HosPrescriptionError hosPrescriptionError = new HosPrescriptionError();
        BeanUtil.copyProperties(hosPrescription, hosPrescriptionError, "id");
        LambdaQueryWrapper<HosPrescriptionDisease> lambda = new QueryWrapper<HosPrescriptionDisease>().lambda();
        lambda.eq(HosPrescriptionDisease::getHosPrescriptionId, oldId);
        List<HosPrescriptionDisease> list = prescriptionDiseaseService.list(lambda);
        ArrayList<HosPrescriptionDiseaseError> hosPrescriptionDiseaseErrors = new ArrayList<>();
        for (HosPrescriptionDisease disease1 : list) {
            HosPrescriptionDiseaseError hosPrescriptionDiseaseError = new HosPrescriptionDiseaseError();
            BeanUtil.copyProperties(disease1, hosPrescriptionDiseaseError, "id");
            hosPrescriptionDiseaseErrors.add(hosPrescriptionDiseaseError);
            prescriptionDiseaseService.removeById(disease1.getId());
        }
        LambdaQueryWrapper<HosPrescriptionDrug> lambda1 = new QueryWrapper<HosPrescriptionDrug>().lambda();
        lambda1.eq(HosPrescriptionDrug::getHosPrescriptionId, oldId);
        List<HosPrescriptionDrug> list1 = prescriptionDrugService.list(lambda1);
        ArrayList<HosPrescriptionDrugError> hosPrescriptionDrugErrors = new ArrayList<>();
        for (HosPrescriptionDrug drug : list1) {
            HosPrescriptionDrugError hosPrescriptionDrugError = new HosPrescriptionDrugError();
            BeanUtil.copyProperties(drug, hosPrescriptionDrugError, "id");
            hosPrescriptionDrugErrors.add(hosPrescriptionDrugError);
            prescriptionDrugService.removeById(drug.getId());
        }
        prescriptionDiseaseErrorService.saveBatch(hosPrescriptionDiseaseErrors);
        prescriptionDrugErrorService.saveBatch(hosPrescriptionDrugErrors);
    }

    /**
     * 查询处方审核状态
     *
     * @param map 复诊订单id
     * @return
     */
    @Override
    public Map getPrescriptionStatus(Map<String, Object> map) {
        String id = map.get("id").toString();
        QueryWrapper<HosPrescription> hosPrescriptionQueryWrapper = new QueryWrapper<>();
        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        if (hosRevisit == null) {
            log.error("复诊id未查询到复诊订单，查询处方单失败");
            return null;
        }
        hosPrescriptionQueryWrapper.eq("visit_mdt_num", hosRevisit.getOrderNum());
        HosPrescription hosPrescription = hosPrescriptionMapper.selectOne(hosPrescriptionQueryWrapper);
        Map result = new HashMap<>();
        result.put("content", "");
        result.put("status", "");
        result.put("statusName", "");
        result.put("doctorSignet", "");
        if (hosPrescription == null) {
            log.error("用接收到的处方ID未查询的相关的处方");
            return null;
        } else {
//             WAIT-等待审核 PASS-审核通过 REJECT-审核驳回
            String checkStatus = hosPrescription.getCheckStatus();
            result.put("status", checkStatus);
            result.put("statusName", StatusEnumUtil.getPrescriptionCheckStatus(checkStatus));
            if ("REJECT".equals(checkStatus)) {
                result.put("content", hosPrescription.getCheckContent());
                log.info("处方订单状态为审核驳回");
            } else if (StatusEnumUtil.SIGN.equals(checkStatus)) {
                result.put("doctorSignet", hosPrescription.getDoctorSignet());
            }
            return result;
        }
    }

    /**
     * 查看处方详情
     *
     * @param map
     * @return
     */
    @Override
    public Map prescription(Map<String, Object> map) {
        String id = map.get("id").toString();
        QueryWrapper<HosPrescription> hosPrescriptionQueryWrapper = new QueryWrapper<>();
        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        hosPrescriptionQueryWrapper.eq("visit_mdt_num", hosRevisit.getOrderNum()).orderByDesc("create_time");
        HosPrescription hosPrescription = hosPrescriptionMapper.selectOne(hosPrescriptionQueryWrapper);
        String prescriptionId = hosPrescription.getId();
        QueryWrapper<HosPrescriptionDrug> hosPrescriptionDrugQueryWrapper = new QueryWrapper<>();
        hosPrescriptionDrugQueryWrapper.eq("hos_prescription_id", prescriptionId);
        List<HosPrescriptionDrug> list = prescriptionDrugService.list(hosPrescriptionDrugQueryWrapper);
        List drugs = new ArrayList<>();
        for (HosPrescriptionDrug drug : list) {
            HashMap<String, Object> drugMap = new HashMap<>();
            drugMap.put("drugId", drug.getDrugId());
            drugMap.put("name", drug.getName());
            drugMap.put("size", drug.getSize());
            drugMap.put("packUnitName", drug.getPackUnitName());
            drugMap.put("routeAdmiCode", drug.getRouteAdmiCode());
            drugMap.put("routeAdmiName", drug.getRouteAdmiName());
            drugMap.put("freqMedCode", drug.getFreqMedCode());
            drugMap.put("freqMedName", drug.getFreqMedName());
            drugMap.put("useDay", drug.getUseDay());
            drugMap.put("dosage", drug.getDosage());
            drugMap.put("number", drug.getNumber());
            drugs.add(drugMap);
        }
        QueryWrapper<HosPrescriptionDisease> hosPrescriptionDiseaseQueryWrapper = new QueryWrapper<>();
        hosPrescriptionDiseaseQueryWrapper.eq("hos_prescription_id", prescriptionId);
        List<HosPrescriptionDisease> list1 = prescriptionDiseaseService.list(hosPrescriptionDiseaseQueryWrapper);
        List diseases = new ArrayList<>();
        for (HosPrescriptionDisease disease : list1) {
            HashMap<String, Object> diseaseMap = new HashMap<>();
            diseaseMap.put("diseaseCode", disease.getDiseaseCode());
            diseaseMap.put("diseaseName", disease.getDiseaseName());
            diseases.add(diseaseMap);
        }
        Map result = new HashMap<>();
        result.put("id", prescriptionId);
        result.put("checkStatus", hosPrescription.getCheckStatus());
        result.put("checkContent", hosPrescription.getCheckContent());
        result.put("doctorSignet", hosPrescription.getDoctorSignet());
        result.put("drugs", drugs);
        result.put("diseases", diseases);
        result.put("orderId", id);
        return result;
    }

    /**
     * 写医嘱
     *
     * @param map
     * @return
     */
    @Override
    public String writeRevisitResult(Map<String, String> map) {
        String id = map.get("id");//id
        String doctorAdvice = map.get("doctorAdvice");//医嘱
        HosRevisit revisit = hosRevisitMapper.selectById(id);
        HosRevisit hosRevisit = new HosRevisit();
        hosRevisit.setId(id);
        hosRevisit.setDoctorAdvice(doctorAdvice);
        hosRevisitMapper.updateById(hosRevisit);
        map = new HashMap<>();
        map.put("msgType", IMMsgType.RevisitResultCard);
        map.put("doctorAdvice", hosRevisit.getDoctorAdvice());
        map.put("businessID", "MSHCustomMsg");
        String doctorUserId = revisit.getDoctorUserId();
        imService.sendPostHttpRequest(DOCTOR + doctorUserId, PATIENT + revisit.getPatientUserId(), map, "诊断结果", "", "");
        return "写医嘱成功";
    }

    /**
     * 查询诊断结果
     *
     * @param map
     * @return
     */
    @Override
    public Map revisitResult(Map<String, String> map) {
        String id = map.get("id");//id
        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        HashMap<String, Object> result = new HashMap<>();
        result.put("sickName", logService.getSickName(hosRevisit.getHosSickId()));
        result.put("type", "复诊购药");
        result.put("date", hosRevisit.getStartTime().substring(0, 10));
        result.put("content", hosRevisit.getContent());
        result.put("doctorAdvice", hosRevisit.getDoctorAdvice());
        return result;
    }

    /**
     * 第二次提交审核（修改认证状态为已认证）
     *
     * @param id 处方id
     * @return
     */
    @Override
    public boolean commitPrescription(String id) {
        HosPrescription oldHosPrescription = hosPrescriptionMapper.selectById(id);
        if (oldHosPrescription == null) {
            return false;
        }
        String checkStatus = oldHosPrescription.getCheckStatus();
        if (!"SIGN".equals(checkStatus)) {
            return false;
        }
        HosPrescription hosPrescription = new HosPrescription();
        hosPrescription.setId(id);
        hosPrescription.setCheckStatus("WAIT");
        int i = hosPrescriptionMapper.updateById(hosPrescription);
        if (i == i) {
            try {
                judgeService.judge(hosPrescription.getId());
            } catch (Exception e) {
                log.error("审方出错：", e);
            }
            return true;
        }
        return false;
    }

    @Override
    public Drug getOneDrug(Map<String, String> map) {
        String id = map.get("id");
        return drugMapper.selectById(id);
    }

    @Override
    public Map<String, Object> getPrescription(Map<String, String> map) {
        String id = map.get("id");
        String type = map.get("type");
        String orderNum = "";
        if ("MDT".equals(type)) {
            MdtOrder mdtOrder = mdtOrderMapper.selectById(id);
            orderNum = mdtOrder.getOrderNum();
        } else if ("REVISIT".equals(type)) {
            HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
            orderNum = hosRevisit.getOrderNum();
        } else {
            throw new BizException("类型错误：MDT,REVISIT");
        }
        LambdaQueryWrapper<HosPrescription> hosPrescriptionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hosPrescriptionLambdaQueryWrapper.eq(HosPrescription::getVisitMdtNum, orderNum);
        hosPrescriptionLambdaQueryWrapper.orderByDesc(HosPrescription::getCreateTime);
        hosPrescriptionLambdaQueryWrapper.eq(HosPrescription::getType, type);
        HosPrescription hosPrescription = hosPrescriptionMapper.selectOne(hosPrescriptionLambdaQueryWrapper);
        String prescriptionId = hosPrescription.getId();
        LambdaQueryWrapper<HosPrescriptionDrug> hosPrescriptionDrugQueryWrapper = new LambdaQueryWrapper<>();
        hosPrescriptionDrugQueryWrapper.eq(HosPrescriptionDrug::getHosPrescriptionId, prescriptionId);
        List<HosPrescriptionDrug> list = prescriptionDrugService.list(hosPrescriptionDrugQueryWrapper);
        List<HashMap<String, Object>> drugs = new ArrayList<>();
        for (HosPrescriptionDrug drug : list) {
            HashMap<String, Object> drugMap = new HashMap<>();
            drugMap.put("drugId", drug.getDrugId());
            drugMap.put("name", drug.getName());
            drugMap.put("size", drug.getSize());
            drugMap.put("packUnitName", drug.getPackUnitName());
            drugMap.put("routeAdmiCode", drug.getRouteAdmiCode());
            drugMap.put("routeAdmiName", drug.getRouteAdmiName());
            drugMap.put("freqMedCode", drug.getFreqMedCode());
            drugMap.put("freqMedName", drug.getFreqMedName());
            drugMap.put("useDay", drug.getUseDay());
            drugMap.put("dosage", drug.getDosage());
            drugMap.put("number", drug.getNumber());
            drugs.add(drugMap);
        }
        QueryWrapper<HosPrescriptionDisease> hosPrescriptionDiseaseQueryWrapper = new QueryWrapper<>();
        hosPrescriptionDiseaseQueryWrapper.eq("hos_prescription_id", prescriptionId);
        List<HosPrescriptionDisease> list1 = prescriptionDiseaseService.list(hosPrescriptionDiseaseQueryWrapper);
        List<HashMap<String, Object>> diseases = new ArrayList<>();
        for (HosPrescriptionDisease disease : list1) {
            HashMap<String, Object> diseaseMap = new HashMap<>();
            diseaseMap.put("diseaseCode", disease.getDiseaseCode());
            diseaseMap.put("diseaseName", disease.getDiseaseName());
            diseases.add(diseaseMap);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("id", prescriptionId);
        result.put("checkStatus", hosPrescription.getCheckStatus());
        result.put("doctorSignet", hosPrescription.getDoctorSignet());
        result.put("drugs", drugs);
        result.put("diseases", diseases);
        result.put("orderId", id);

        //编号：A20200197355445
        //临床诊断：病情稳定

        result.put("createTime", hosPrescription.getCreateTime().substring(0, 10));
        result.put("departName", hosPrescription.getDepartName());
        result.put("sickName", hosPrescription.getName());
        result.put("age", hosPrescription.getAge());
        result.put("sex", hosPrescription.getSex().equals("0") ? "女" : "男");
        result.put("feeType", hosPrescription.getFeeType().equals("SELF") ? "自费" : "自费");
        result.put("mobile", hosPrescription.getMobile());
        result.put("medicalCertificate", hosPrescription.getMedicalCertificate());
        result.put("prescriptionNum", hosPrescription.getOrderNum());
        result.put("checkPharmaceutist", hosPrescription.getCheckPharmaceutist());

        return result;
    }

    /**
     * 患者处方记录 列表（审核中，审核成功，审核失败）
     *
     * @param map
     * @return
     */
    @Override
    public Object getPrescriptionList(Map<String, String> map) {
//        String sickId = map.get("id");
        int page = 1;
        int limit = 10;
        try {
            page = Integer.parseInt(String.valueOf(map.get("page")));
            limit = Integer.parseInt(String.valueOf(map.get("limit")));
        } catch (Exception e) {
            log.error("没有分页条件");
        }
        Page<HosPrescription> ipage = new Page<>(page, limit);
        String doctorId = Objects.requireNonNull(AuthUtil.getLoginUser()).getId();
        LambdaQueryWrapper<HosPrescription> lambda = new QueryWrapper<HosPrescription>().lambda();
        lambda.eq(HosPrescription::getDoctorUserId, doctorId);
        lambda.orderByDesc(HosPrescription::getCreateTime)/*.eq(HosPrescription::getHosSickId, sickId)*/;
        if (null != map.get("checkStatus") || "".equals(map.get("checkStatus"))) {
            String[] checkStatuses = map.get("checkStatus").split(",");
            List<String> collect = Arrays.stream(checkStatuses).collect(Collectors.toList());
            lambda.in(HosPrescription::getCheckStatus, collect);
        }
        if (null == map.get("type")) {
            lambda.eq(HosPrescription::getType, "REVISIT");
        } else {
            lambda.eq(HosPrescription::getType, map.get("type"));
        }
        List<HosPrescription> hosPrescriptions = hosPrescriptionMapper.selectPage(ipage, lambda).getRecords();
        for (HosPrescription hosPrescription : hosPrescriptions) {
            LambdaQueryWrapper<HosPrescriptionDrug> drugLambdaQueryWrapper = new LambdaQueryWrapper<>();
            drugLambdaQueryWrapper.eq(HosPrescriptionDrug::getHosPrescriptionId, hosPrescription.getId());
            List<HosPrescriptionDrug> drugs = hosPrescriptionDrugMapper.selectList(drugLambdaQueryWrapper);
            String drugName = drugs.stream().map(HosPrescriptionDrug::getName).collect(Collectors.joining("、"));
            hosPrescription.setDrugName(drugName);
        }
        return hosPrescriptions;
    }

    @Override
    public Object getArchives(String id) {
        HealthRecords healthRecords = new HealthRecords();
        healthRecords.setDeptCode(Objects.requireNonNull(doctorUserMapper.selectById(AuthUtil.getLoginUser().getId())).getDepartCode());
        healthRecords.setRevisitId(id);
        healthRecordsMapper.insert(healthRecords);
        return new HashMap<>();
    }

    @Override
    public Map<String, String> revisitOrderCount(Map<String, String> map) {
        String sickId = map.get("sickId");//就诊人id
        long success = this.selectRevisitCount(sickId, "SUCCESS");
        long wait = this.selectRevisitCount(sickId, "WAIT");
        Map<String, String> result = Maps.newHashMap();
        result.put("success", String.valueOf(success));
        result.put("wait", String.valueOf(wait));
        return result;
    }

    public Long selectRevisitCount(String sickId, String status) {
        if (status == null) {
            status = "";
        }
        DoctorUser loginUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
        assert loginUser != null;
        String id = loginUser.getId();
        LambdaQueryWrapper<HosRevisit> hosVisitQueryWrapper = new QueryWrapper<HosRevisit>().lambda();
        hosVisitQueryWrapper.select().eq(HosRevisit::getDoctorUserId, id);
        hosVisitQueryWrapper.orderByDesc(HosRevisit::getCreateTime);
        //状态 DECLINE 拒单 PAY 已付款|待接单 HAVE 已接单  START 复诊进行中 END 订单完成
        if (StrUtil.isNotBlank(sickId)) {
            hosVisitQueryWrapper.eq(HosRevisit::getHosSickId, sickId);
        }
        if (status.toUpperCase().equals("SUCCESS")) {
            hosVisitQueryWrapper.and(wrapper -> wrapper.eq(HosRevisit::getStatus, StatusEnumUtil.END));
        } else if (status.toUpperCase().equals("WAIT")) {
            hosVisitQueryWrapper.select().and(wrapper -> wrapper.eq(HosRevisit::getStatus, StatusEnumUtil.PAY).or()
                    .eq(HosRevisit::getStatus, StatusEnumUtil.HAVE).or().eq(HosRevisit::getStatus, StatusEnumUtil.START));
        }
        return hosRevisitMapper.selectCount(hosVisitQueryWrapper);
    }

    /**
     * 调用第三方接口获取签章并存入数据库
     *
     * @param mobile
     * @param hosPrescription
     * @return
     */
    public boolean getSealUrl(String mobile, HosPrescription hosPrescription) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", mobile);
            map.put("appId", caProperties.getAppId());
            String msg = "appId=" + caProperties.getAppId() + "&id=" + mobile;
            String signature = SecurityUtil.signature(caProperties.getPrivateStr(), msg);
            map.put("sign", signature);
            String response = HttpUtil.post(caProperties.getOpenUrl() + "v1/user/seal", map);
            String decode = URLDecoder.decode(response, "UTF-8");
            log.debug(decode);
            Map hashMap = JSON.parseObject(decode, HashMap.class);
            Map data = JSON.parseObject(hashMap.get("data").toString(), HashMap.class);
            String seal = data.get("seal").toString();

            byte[] bytes = Base64.decode(seal);
            String fileName = System.currentTimeMillis() + ".png";
            MultipartFile file = new MockMultipartFile(fileName, fileName, "", bytes);
            String url = OssFileUtils.uploadFile(file);
            System.out.println("签章URL:" + url);
            hosPrescription.setSeal(seal);
            hosPrescription.setDoctorSignet(url);
            return true;/* http://internet-hospital-prod.oss-accelerate.aliyuncs.com:80/files/2020102109260492b6da92450645d7b6ac4a4490ed49d4.png */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

/*    private String getSign(String mobile, HosPrescription hosPrescription) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appId", caProperties.getAppId());
            map.put("id", mobile);
            String message = "appId=" + caProperties.getAppId() + "&id=" + mobile;
            String signature = SecurityUtil.signature(caProperties.getPrivateStr(), message);
            map.put("sign", signature);
            String prescriptionNum = hosPrescription.getPrescriptionNum();
            map.put("bizSn", prescriptionNum);
            String msg = Base64.encode(SHA256Util.GetSHA256FormString(hosPrescription.getId()));
            map.put("msg", msg);
            map.put("url", URLEncoder.encode(caProperties.getCallBack() + "/api/v1/api/sign", "utf-8"));
            map.put("mode", "forward");
            String response = HttpUtil.post(caProperties.getOpenUrl() + "/v1/push/sign", map);
            String decode = URLDecoder.decode(response, "UTF-8");
            Map responseMap = JSON.parseObject(decode, Map.class);
            String ret = responseMap.get("ret").toString();
            if ("success".equals(ret)) {
                Map data = JSON.parseObject(responseMap.get("data").toString(), Map.class);
                try {
                    String cert = data.get("cert").toString();
                    log.info("推送成功");
                    return cert;
                } catch (Exception e) {
                    log.info("用户未设置免密推送");
                    return "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*public void getSignImg() throws Exception{
        String id = "15093707719";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("appId", SecurityUtil.APPID);
        String msg = "药品：appId=" + SecurityUtil.APPID + "&id=" + id;
        String signature = SecurityUtil.signature(SecurityUtil.PRIVATESTR, msg);
        map.put("bizSn", System.currentTimeMillis() + "");
        map.put("action", "sign");
        map.put("msg", SHA256Util.GetSHA256FormString(msg));
        map.put("sign", signature);
        String response = HttpUtil.post(SecurityUtil.OPENURL+"/v1/user/seal", map);
        String decode = URLDecoder.decode(response, "UTF-8");
        System.out.println("response = [" + decode + "]");
        Map hashMap = JSON.parseObject(decode, HashMap.class);
        Map data = JSON.parseObject(hashMap.get("data").toString(), HashMap.class);
        byte[] bytes = Base64.decode(data.get("seal").toString(), "utf-8");
        File file = new File("C:\\Users\\Lenovo\\Desktop\\dianying\\aaa.png");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes, 0, bytes.length);
        fos.flush();
        fos.close();
    }*/
}