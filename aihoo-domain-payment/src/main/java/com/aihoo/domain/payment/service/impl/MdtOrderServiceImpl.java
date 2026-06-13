package com.aihoo.domain.payment.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aihoo.enums.PushMessageType;
import com.aihoo.common.JsonResult;
import com.aihoo.exception.BizException;
import com.aihoo.util.*;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aihoo.domain.payment.dto.MdtOrderReportVo;
import com.aihoo.domain.consultation.model.mapper.MdtOrderMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDrugMapper;
import com.aihoo.domain.sys.model.mapper.DictMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorBalanceLogMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionMapper;

import com.aihoo.domain.payment.service.JudgeService;
import com.aihoo.domain.payment.service.MdtOrderService;
import com.aihoo.domain.prescription.service.PrescriptionDrugService;
import com.aihoo.domain.im.service.PushMessageService;
import com.aihoo.properties.CaProperties;
import com.aihoo.constant.DictTypeEnum;
import com.aihoo.constant.MdtTypeEnum;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.constant.UserRoleEnum;
import com.aihoo.security.AuthUtil;
import com.aihoo.util.OrderNoUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.aihoo.domain.consultation.model.mapper.MdtMapper;
import com.aihoo.domain.hospital.model.mapper.DrugMapper;
import com.aihoo.domain.consultation.model.mapper.MdtOrderDoctorMapper;
import com.aihoo.domain.consultation.model.mapper.MdtOrderDicomStudyMapper;
import com.aihoo.domain.consultation.model.mapper.MdtOrderReportMapper;
import com.aihoo.domain.consultation.model.mapper.MdtOrderFileMapper;
import com.aihoo.domain.sys.model.mapper.SysUserMapper;
import com.aihoo.domain.consultation.model.mapper.MdtOrderReportAuditMapper;
import com.aihoo.domain.consultation.model.mapper.MdtTeamMapper;
import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrug;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.entity.DoctorBalanceLog;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.aihoo.domain.consultation.model.entity.MdtOrder;
import com.aihoo.domain.consultation.model.entity.Mdt;
import com.aihoo.domain.consultation.model.entity.MdtOrderDoctor;
import com.aihoo.domain.consultation.model.entity.MdtOrderDicomStudy;
import com.aihoo.domain.consultation.model.entity.MdtTeam;
import com.aihoo.domain.consultation.model.entity.MdtOrderFile;
import com.aihoo.domain.consultation.model.entity.MdtOrderReport;
import com.aihoo.domain.consultation.model.entity.MdtOrderReportAudit;
import com.aihoo.domain.sys.model.entity.Dict;
import com.aihoo.domain.sys.model.entity.SysUser;

/**
 * @program: aihoo-root
 * @description: mdtorder业务
 * @author: Mr.Li
 * @create: 2021-01-07 17:04
 **/
@Service("doctorApiMdtOrderServiceImpl")
public class MdtOrderServiceImpl implements MdtOrderService {
    Log log = LogFactory.get();
    @Resource
    private SmsModelSendUtil smsModelSendUtil;
    @Resource
    private DrugMapper drugMapper;
    @Resource
    private DictMapper dictMapper;
    @Resource
    private MdtOrderMapper mdtOrderMapper;
    @Resource
    private MdtOrderDoctorMapper mdtOrderDoctorMapper;
    @Resource
    private DoctorBalanceLogMapper doctorBalanceLogMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private MdtOrderFileMapper mdtOrderFileMapper;
    @Resource
    private MdtOrderReportMapper mdtOrderReportMapper;
    @Resource
    private MdtOrderReportAuditMapper mdtOrderReportAuditMapper;
    @Resource
    private CaProperties caProperties;
    @Resource
    private MdtOrderDicomStudyMapper mdtOrderDicomStudyMapper;
    @Resource
    private HosPrescriptionMapper hosPrescriptionMapper;
    @Resource
    private HosPrescriptionDrugMapper hosPrescriptionDrugMapper;
    @Resource
    private DoctorUserMapper doctorUserMapper;
    @Resource
    private PrescriptionDrugService prescriptionDrugService;
    @Resource
    private MdtTeamMapper mdtTeamMapper;
    @Resource
    private MdtMapper mdtMapper;
    @Resource
    private PushMessageService pushMessageService;
    @Lazy
    @Resource
    private JudgeService judgeService;
    @Override
    public List<Map<String, String>> bill(Map<String, String> map) {
        String date = map.get("data");
        //一月的开始
        Date beginOfMonth;
        //一月的结束
        Date endOfMonth;
        try {
            if (StrUtil.isBlank(date)) {
                beginOfMonth = DateUtil.beginOfMonth(DateUtil.date());
                endOfMonth = DateUtil.endOfMonth(DateUtil.date());
            } else {
                DateTime dateTime = DateUtil.parse(date, "yyyy-MM");
                beginOfMonth = DateUtil.beginOfMonth(dateTime);
                endOfMonth = DateUtil.endOfMonth(dateTime);
            }
        } catch (Exception e) {
            beginOfMonth = DateUtil.beginOfMonth(DateUtil.date());
            endOfMonth = DateUtil.endOfMonth(DateUtil.date());
        }
        String pageStr = map.get("page");
        String limitStr = map.get("limit");
        int page = 1;
        int limit = 10;
        if (NumberUtil.isNumber(pageStr) && NumberUtil.isNumber(limitStr)) {
            page = NumberUtil.parseInt(pageStr);
            limit = NumberUtil.parseInt(limitStr);
        } else {
            log.debug("会诊我的账单接口：分页条件有误，默认展示前十条");
        }
        IPage<DoctorBalanceLog> iPage = new Page<>(page, limit);
        IPage<DoctorBalanceLog> resultPage = doctorBalanceLogMapper.selectByDoctorId(Objects.requireNonNull(AuthUtil.getLoginUser()).getId(), "MDT%", beginOfMonth, endOfMonth, iPage);
        List<DoctorBalanceLog> doctorBalanceLogList = resultPage.getRecords();
        List<Map<String, String>> resultList = Lists.newArrayList();
        for (DoctorBalanceLog doctorBalanceLog : doctorBalanceLogList) {
            Map<String, String> resultMap = Maps.newHashMap();
            MdtOrder mdtOrder = mdtOrderMapper.selectById(doctorBalanceLog.getOtherId());
            LambdaQueryWrapper<MdtOrderDoctor> lambdaQueryWrapper = new QueryWrapper<MdtOrderDoctor>().lambda();
            lambdaQueryWrapper.select(MdtOrderDoctor::getDoctorName).eq(MdtOrderDoctor::getMdtOrderId, mdtOrder.getId()).eq(MdtOrderDoctor::getDoctorType, "CONSULTANT");
            List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(lambdaQueryWrapper);
            String doctorNames = mdtOrderDoctorList.stream().map(MdtOrderDoctor::getDoctorName).collect(Collectors.joining(","));
            resultMap.put("createTime", doctorBalanceLog.getCreateTime());
            resultMap.put("changeAmount", doctorBalanceLog.getChangeAmount());
            resultMap.put("appointmentTime", mdtOrder.getMdtAppointmentTime());
            resultMap.put("doctorNames", doctorNames);
            resultList.add(resultMap);
        }
        return resultList;
    }

    @Override
    public JsonResult count(Map<String, String> map) {
        JSONObject jsonObject = new JSONObject();
        DoctorUser loginUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
        assert loginUser != null;
        String doctorUserId = loginUser.getId();
        ArrayList<String> statusList = Lists.newArrayList(
                StatusEnumUtil.ILLNESS_EXAMINE,
                StatusEnumUtil.ILLNESS_EXAMINE_PASS,
                StatusEnumUtil.CONSULTATION_CONFIRM,
                StatusEnumUtil.CONSULTATION,
                StatusEnumUtil.CONSULTATION_END,
                StatusEnumUtil.CANCEL,
                StatusEnumUtil.DONE
        );
        List<MdtOrder> mdtOrders = mdtOrderMapper.selectListByDoctorIdCard(doctorUserId, statusList, null).getRecords();

        if (null != mdtOrders && mdtOrders.size() > 0) {
            //PAY HAVE ILLNESS_EXAMINE_PASS CONSULTATION_CONFIRM CONSULTATION CONSULTATION_END CANCEL DONE
            Long haveListSize = mdtOrders.stream()
                    .filter(s -> {
                                boolean equals = StatusEnumUtil.ILLNESS_EXAMINE.equals(s.getStatus());
                                LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorQueryWrapper = new LambdaQueryWrapper<>();
                                mdtOrderDoctorQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, s.getId());
                                List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(mdtOrderDoctorQueryWrapper);
                                int count = 0;
                                boolean thisDoctorIsHave = false;
                                for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctorList) {
                                    String isHave = mdtOrderDoctor.getIsHave();
                                    if (!isHave.equals("1")) {
                                        count++;
                                    }
                                    if (doctorUserId.equals(mdtOrderDoctor.getDoctorUserId()) && isHave.equals("1")) {
                                        thisDoctorIsHave = true;
                                    }
                                }
                                return equals && count != 0 && !thisDoctorIsHave;
                            }
                    )
                    .count();
            Long agreeListSize = mdtOrders.stream()
                    .filter(s -> {
                        boolean allEquals = StatusEnumUtil.ILLNESS_EXAMINE_PASS.equals(s.getStatus())
                                || StatusEnumUtil.CONSULTATION_CONFIRM.equals(s.getStatus())
                                || StatusEnumUtil.CONSULTATION.equals(s.getStatus());
                        boolean equals = StatusEnumUtil.ILLNESS_EXAMINE.equals(s.getStatus());
                        LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorQueryWrapper = new LambdaQueryWrapper<>();
                        mdtOrderDoctorQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, s.getId());
                        List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(mdtOrderDoctorQueryWrapper);
                        int count = 0;
                        boolean thisDoctorIsHave = false;
                        for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctorList) {
                            String isHave = mdtOrderDoctor.getIsHave();
                            if (!isHave.equals("1")) {
                                count++;
                            }
                            if (doctorUserId.equals(mdtOrderDoctor.getDoctorUserId()) && isHave.equals("1")) {
                                thisDoctorIsHave = true;
                            }
                        }
                        return allEquals || (thisDoctorIsHave && equals);
                    })
                    .count();
            Long oldListSize = mdtOrders.stream()
                    .filter(s -> StatusEnumUtil.CONSULTATION_END.equals(s.getStatus())
                            || StatusEnumUtil.CANCEL.equals(s.getStatus())
                            || StatusEnumUtil.DONE.equals(s.getStatus()))
                    .count();
            jsonObject.put("haveListSize", haveListSize);
            jsonObject.put("agreeListSize", agreeListSize);
            jsonObject.put("oldListSize", oldListSize);
        }
        return JsonResult.ok().putData(jsonObject);
    }

    private Function<MdtOrder, JSONObject> getMdtOrderJSONObjectFunction(DoctorUser loginDoctorUser) {
        return s -> {
            JSONObject object = new JSONObject();
            String idCard = loginDoctorUser.getPapersNumbers();
            String loginDoctorUserId = loginDoctorUser.getId();
            String mdtOrderId = s.getId();
            String reportDoctorId = s.getReportDoctorId();
            object.put("id", mdtOrderId);
            object.put("patientName", s.getName());
            String mdtAppointmentTime = s.getMdtAppointmentTime();
            object.put("mdtAppointmentTime", null == mdtAppointmentTime ? "时间待确认" : mdtAppointmentTime);
            object.put("doctors", s.getConsultationDoctorName());
            String status = s.getStatus();
            object.put("status", status);
            object.put("statusName", StatusEnumUtil.getMdtOrderStatus(status));
            object.put("price", s.getTotalPrice());
            object.put("adminId", s.getAdminId());
            object.put("patientAge", s.getAge());
            object.put("patientSex", s.getSex());

            MdtOrderDoctor relMdtOrderDoctor = mdtOrderDoctorMapper.selectById(reportDoctorId);
            object.put("isReportDoctor", "0");
            if (null != relMdtOrderDoctor && relMdtOrderDoctor.getDoctorUserId().equals(loginDoctorUserId)) {
                object.put("isReportDoctor", "1");
            }


            LambdaQueryWrapper<HosPrescription> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(HosPrescription::getType, "MDT");
            lambdaQueryWrapper.eq(HosPrescription::getOtherId, mdtOrderId);
            lambdaQueryWrapper.eq(HosPrescription::getIsCancel, "0");
            HosPrescription oldHosPrescription = hosPrescriptionMapper.selectOne(lambdaQueryWrapper);
            object.put("isHavePrescription", "0");
            if (null != oldHosPrescription) {
                object.put("isHavePrescription", "1");
            }

            object.put("isPrescriptionDoctor", "0");
            MdtOrderDoctor relMdtPrescriptionDoctor = mdtOrderDoctorMapper.selectById(s.getPrescriptionDoctorId());
            if (null != relMdtPrescriptionDoctor && relMdtPrescriptionDoctor.getDoctorUserId().equals(loginDoctorUserId)) {
                object.put("isPrescriptionDoctor", "1");
            }

            object.put("mdtRoomId", s.getMdtRoomId());
            object.put("isCanChat", s.getIsCanChat());

            object.put("isCanSign", "0");
            object.put("isWriteReport", "0");
            LambdaQueryWrapper<MdtOrderReport> mdtOrderReportLambdaQueryWrapper = new LambdaQueryWrapper<>();
            mdtOrderReportLambdaQueryWrapper.eq(MdtOrderReport::getOrderNum, s.getOrderNum());
            mdtOrderReportLambdaQueryWrapper.orderByDesc(MdtOrderReport::getCreateTime);
            List<MdtOrderReport> mdtOrderReports = mdtOrderReportMapper.selectList(mdtOrderReportLambdaQueryWrapper);
            if (null != mdtOrderReports && mdtOrderReports.size() > 0) {
                MdtOrderReport mdtOrderReport = mdtOrderReports.get(0);
                object.put("isWriteReport", "1");
                String reportId = mdtOrderReport.getId();
                LambdaQueryWrapper<MdtOrderReportAudit> mdtOrderReportAuditLambdaQueryWrapper = new LambdaQueryWrapper<MdtOrderReportAudit>();
                mdtOrderReportAuditLambdaQueryWrapper.eq(MdtOrderReportAudit::getReportId, reportId);
                mdtOrderReportAuditLambdaQueryWrapper.eq(MdtOrderReportAudit::getAuditDoctorIdCard, idCard);
                mdtOrderReportAuditLambdaQueryWrapper.orderByDesc(MdtOrderReportAudit::getCreateTime);
                List<MdtOrderReportAudit> mdtOrderReportAudits = mdtOrderReportAuditMapper.selectList(mdtOrderReportAuditLambdaQueryWrapper);
                if (mdtOrderReportAudits.size() > 0) {
                    object.put("isCanSign", "1");
                }
            }

            LambdaQueryWrapper<MdtOrderDoctor> lambda = new LambdaQueryWrapper<>();
            lambda.eq(MdtOrderDoctor::getMdtOrderId, mdtOrderId);
            lambda.eq(MdtOrderDoctor::getDoctorUserId, loginDoctorUserId);
            List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(lambda);

            object.put("userRole", UserRoleEnum.ASSISTANT.getCode());
            for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctorList) {
                //病史资料审核
                object.put("auditResult", mdtOrderDoctor.getAuditResult());
                //是否接单
                object.put("isHave", mdtOrderDoctor.getIsHave());
                //是否时间确认
                object.put("isAgree", mdtOrderDoctor.getIsAgree());
                if (UserRoleEnum.CONSULTANT.getCode().equals(mdtOrderDoctor.getDoctorType())) {
                    object.put("userRole", UserRoleEnum.CONSULTANT.getCode());
                    break;
                }
            }
            //查询影像资料
            LambdaQueryWrapper<MdtOrderDicomStudy> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MdtOrderDicomStudy::getOrderId, mdtOrderId);
            wrapper.eq(MdtOrderDicomStudy::getIsCancel, "0");
            wrapper.eq(MdtOrderDicomStudy::getIsUploadMeiqing, "1");
            wrapper.eq(MdtOrderDicomStudy::getIsUnzip, "1");
            wrapper.orderByDesc(MdtOrderDicomStudy::getUpdateTime);
            wrapper.last("limit 1");
            MdtOrderDicomStudy mdtOrderDicomStudy = mdtOrderDicomStudyMapper.selectOne(wrapper);

            if (null != mdtOrderDicomStudy) {
                //检查名称
                object.put("examName", mdtOrderDicomStudy.getExamName());
                //检查模式
                object.put("studyModality", mdtOrderDicomStudy.getStudyModality());
                //影像链接
                object.put("meiqingUrl", mdtOrderDicomStudy.getMeiqingUrl());
                //备注
                object.put("remark", mdtOrderDicomStudy.getRemark());
                //健康报告
                object.put("reportUrl", mdtOrderDicomStudy.getReportUrl());
                //创建时间
                object.put("createTime", mdtOrderDicomStudy.getCreateTime());
                /*强行设置一个类型*/
                object.put("type", "DICOM_FILE");
            }


            object.put("isAllSignReport", "0");
            if (mdtOrderReports.size() > 0) {
                //查出所有已签名医生
                MdtOrderReport mdtOrderReport = mdtOrderReports.get(0);
                LambdaQueryWrapper<MdtOrderReportAudit> mdtOrderReportAuditLambdaQueryWrapper = new LambdaQueryWrapper<>();
                mdtOrderReportAuditLambdaQueryWrapper.eq(MdtOrderReportAudit::getReportId, mdtOrderReport.getId());
                List<MdtOrderReportAudit> mdtOrderReportAudits = mdtOrderReportAuditMapper.selectList(mdtOrderReportAuditLambdaQueryWrapper);
                //查出所有要签名的医生
                LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
                mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, s.getId());
                mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getDoctorType, UserRoleEnum.CONSULTANT.getCode());
                List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
                //比较已签名医生和未签名医生是否一致
                for (MdtOrderReportAudit mdtOrderReportAudit : mdtOrderReportAudits) {
                    mdtOrderDoctors.removeIf(mdtOrderDoctor -> mdtOrderReportAudit.getAuditDoctorIdCard().equals(mdtOrderDoctor.getDoctorIdCard()));
                }
                if (mdtOrderDoctors.size() > 0) {
                    object.put("isAllSignReport", "0");
                } else {
                    object.put("isAllSignReport", "1");
                }

            }
            return object;
        };
    }

    @Override
    public JsonResult list(Map<String, String> map) {
        DoctorUser loginUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
        assert loginUser != null;
        String doctorUserId = loginUser.getId();
        ArrayList<String> statusList = null;
        String type = map.get("type").toUpperCase(Locale.ROOT);
        switch (type) {
            case "HAVE":
                statusList = Lists.newArrayList(
                        StatusEnumUtil.ILLNESS_EXAMINE
                );
                break;
            case "AGREE":
                statusList = Lists.newArrayList(
                        StatusEnumUtil.ILLNESS_EXAMINE,
                        StatusEnumUtil.ILLNESS_EXAMINE_PASS,
                        StatusEnumUtil.CONSULTATION_CONFIRM,
                        StatusEnumUtil.CONSULTATION
                );
                break;
            case "OLD":
                statusList = Lists.newArrayList(
                        StatusEnumUtil.CONSULTATION_END,
                        StatusEnumUtil.CANCEL,
                        StatusEnumUtil.DONE
                );
                break;
        }
        String pageStr = map.get("page");
        String limitStr = map.get("limit");
        int page = 1;
        int limit = 10;
        if (NumberUtil.isNumber(pageStr) && NumberUtil.isNumber(limitStr)) {
            page = NumberUtil.parseInt(pageStr);
            limit = NumberUtil.parseInt(limitStr);
        }
        Page<MdtOrder> mdtOrderPage = new Page<>(page, limit);
        IPage<MdtOrder> resultPage = mdtOrderMapper.selectListByDoctorIdCard(doctorUserId, statusList, mdtOrderPage);
        List<MdtOrder> mdtOrders = resultPage.getRecords();
        if (statusList.contains(StatusEnumUtil.ILLNESS_EXAMINE)) {
            if ("HAVE".equals(type)) {
                mdtOrders = mdtOrders.stream()
                        .filter(s -> {
                                    boolean equals = StatusEnumUtil.ILLNESS_EXAMINE.equals(s.getStatus());
                                    LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorQueryWrapper = new LambdaQueryWrapper<>();
                                    mdtOrderDoctorQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, s.getId());
                                    List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(mdtOrderDoctorQueryWrapper);
                                    int count = 0;
                                    boolean thisDoctorIsHave = false;
                                    for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctorList) {
                                        String isHave = mdtOrderDoctor.getIsHave();
                                        if (!isHave.equals("1")) {
                                            count++;
                                        }
                                        if (doctorUserId.equals(mdtOrderDoctor.getDoctorUserId()) && isHave.equals("1")) {
                                            thisDoctorIsHave = true;
                                        }
                                    }
                                    return !thisDoctorIsHave && equals && count != 0;
                                }
                        )
                        .collect(Collectors.toList());
            } else if ("AGREE".equals(type)) {
                mdtOrders = mdtOrders.stream()
                        .filter(s -> {
                            boolean allEquals = StatusEnumUtil.ILLNESS_EXAMINE_PASS.equals(s.getStatus())
                                    || StatusEnumUtil.CONSULTATION_CONFIRM.equals(s.getStatus())
                                    || StatusEnumUtil.CONSULTATION.equals(s.getStatus());
                            boolean equals = StatusEnumUtil.ILLNESS_EXAMINE.equals(s.getStatus());
                            LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorQueryWrapper = new LambdaQueryWrapper<>();
                            mdtOrderDoctorQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, s.getId());
                            List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(mdtOrderDoctorQueryWrapper);
                            int count = 0;
                            boolean thisDoctorIsHave = false;
                            for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctorList) {
                                String isHave = mdtOrderDoctor.getIsHave();
                                if (!isHave.equals("1")) {
                                    count++;
                                }
                                if (doctorUserId.equals(mdtOrderDoctor.getDoctorUserId()) && isHave.equals("1")) {
                                    thisDoctorIsHave = true;
                                }
                            }
                            return allEquals || (thisDoctorIsHave && equals);
                        })
                        .collect(Collectors.toList());
            }
        }

        List<JSONObject> orderList = mdtOrders.stream().map(getMdtOrderJSONObjectFunction(loginUser)).collect(Collectors.toList());
        return JsonResult.ok().putData(orderList);
    }

    @Override
    public JsonResult details(Map<String, String> map) {
        DoctorUser loginUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
        assert loginUser != null;
        String idCard = loginUser.getPapersNumbers();
        String loginDoctorUserId = loginUser.getId();
        String orderId = map.get("orderId");
        MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
        if (null == mdtOrder) {
            return JsonResult.error("会诊订单不存在");
        }
        JSONObject jsonObject = new JSONObject();
        /*患者信息*/
        jsonObject.put("patientName", mdtOrder.getName());
        jsonObject.put("patientSex", "1".equals(mdtOrder.getSex()) ? "男" : "女");
        jsonObject.put("patientAge", mdtOrder.getAge());
        jsonObject.put("patientIdCard", CodeUtils.idCardMask(mdtOrder.getIdCard()));
        jsonObject.put("mobile", CodeUtils.phoneMask(mdtOrder.getMobile()));

        /*会诊信息*/
        String mdtAppointmentTime = mdtOrder.getMdtAppointmentTime();
        jsonObject.put("mdtAppointmentTime", null == mdtAppointmentTime ? "时间待确认" : mdtAppointmentTime);
        LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
        List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
        String consultantDoctors = mdtOrderDoctors.stream().filter(s -> UserRoleEnum.CONSULTANT.getCode().equals(s.getDoctorType())).map(MdtOrderDoctor::getDoctorName).collect(Collectors.joining(","));
        String assistantDoctors = mdtOrderDoctors.stream().filter(s -> UserRoleEnum.ASSISTANT.getCode().equals(s.getDoctorType())).map(MdtOrderDoctor::getDoctorName).collect(Collectors.joining(","));
        jsonObject.put("consultantDoctors", consultantDoctors);
        jsonObject.put("assistantDoctors", assistantDoctors);
        String adminId = mdtOrder.getAdminId();
        SysUser sysUser = sysUserMapper.selectById(adminId);
        if (null == sysUser) {
            return JsonResult.error("医疗管家不存在");
        }
        jsonObject.put("adminName", sysUser.getNickName());

        /*订单信息*/
        jsonObject.put("orderNum", mdtOrder.getOrderNum());
        jsonObject.put("createTime", mdtOrder.getCreateTime());
        jsonObject.put("payTime", mdtOrder.getPayTime());
        jsonObject.put("price", mdtOrder.getTotalPrice());

        /*其他信息*/
        jsonObject.put("id", orderId);
        jsonObject.put("adminId", adminId);
        String status = mdtOrder.getStatus();
        jsonObject.put("status", status);
        jsonObject.put("statusName", StatusEnumUtil.getMdtOrderStatus(status));

        jsonObject.put("isCanSign", "0");
        jsonObject.put("isWriteReport", "0");
        LambdaQueryWrapper<MdtOrderReport> mdtOrderReportLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mdtOrderReportLambdaQueryWrapper.eq(MdtOrderReport::getOrderNum, mdtOrder.getOrderNum());
        mdtOrderReportLambdaQueryWrapper.orderByDesc(MdtOrderReport::getCreateTime);
        List<MdtOrderReport> mdtOrderReports = mdtOrderReportMapper.selectList(mdtOrderReportLambdaQueryWrapper);
        if (null != mdtOrderReports && mdtOrderReports.size() > 0) {
            MdtOrderReport mdtOrderReport = mdtOrderReports.get(0);
            jsonObject.put("isWriteReport", "1");
            String reportId = mdtOrderReport.getId();
            MdtOrderReportAudit mdtOrderReportAudits = mdtOrderReportAuditMapper.selectOne(new LambdaQueryWrapper<MdtOrderReportAudit>().eq(MdtOrderReportAudit::getReportId, reportId).eq(MdtOrderReportAudit::getAuditDoctorIdCard, idCard));
            if (null != mdtOrderReportAudits) {
                jsonObject.put("isCanSign", "1");
            }
        }

        jsonObject.put("userRole", UserRoleEnum.ASSISTANT.getCode());
        LambdaQueryWrapper<MdtOrderDoctor> lambda = new LambdaQueryWrapper<>();
        lambda.eq(MdtOrderDoctor::getMdtOrderId, mdtOrder.getId());
        lambda.eq(MdtOrderDoctor::getDoctorUserId, loginDoctorUserId);
        List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(lambda);
        for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctorList) {
            //病史资料审核
            jsonObject.put("auditResult", mdtOrderDoctor.getAuditResult());
            //是否接单
            jsonObject.put("isHave", mdtOrderDoctor.getIsHave());
            //是否时间确认
            jsonObject.put("isAgree", mdtOrderDoctor.getIsAgree());
            if (UserRoleEnum.CONSULTANT.getCode().equals(mdtOrderDoctor.getDoctorType())) {
                jsonObject.put("userRole", UserRoleEnum.CONSULTANT.getCode());
                break;
            }
        }
        MdtOrderDoctor relMdtOrderDoctor = mdtOrderDoctorMapper.selectById(mdtOrder.getReportDoctorId());
        jsonObject.put("isReportDoctor", "0");
        if (null != relMdtOrderDoctor && relMdtOrderDoctor.getDoctorUserId().equals(loginDoctorUserId)) {
            jsonObject.put("isReportDoctor", "1");
        }

        LambdaQueryWrapper<HosPrescription> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HosPrescription::getType, "MDT");
        lambdaQueryWrapper.eq(HosPrescription::getOtherId, mdtOrder.getId());
        lambdaQueryWrapper.eq(HosPrescription::getIsCancel, "0");
        HosPrescription oldHosPrescription = hosPrescriptionMapper.selectOne(lambdaQueryWrapper);
        jsonObject.put("isHavePrescription", "0");
        if (null != oldHosPrescription) {
            jsonObject.put("isHavePrescription", "1");
        }

        jsonObject.put("isPrescriptionDoctor", "0");
        MdtOrderDoctor relMdtPrescriptionDoctor = mdtOrderDoctorMapper.selectById(mdtOrder.getPrescriptionDoctorId());
        if (null != relMdtPrescriptionDoctor && relMdtPrescriptionDoctor.getDoctorUserId().equals(loginDoctorUserId)) {
            jsonObject.put("isPrescriptionDoctor", "1");
        }

        jsonObject.put("mdtRoomId", mdtOrder.getMdtRoomId());
        jsonObject.put("isCanChat", mdtOrder.getIsCanChat());


        //查询影像资料
        LambdaQueryWrapper<MdtOrderDicomStudy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdtOrderDicomStudy::getOrderId, orderId);
        wrapper.eq(MdtOrderDicomStudy::getIsCancel, "0");
        wrapper.eq(MdtOrderDicomStudy::getIsUploadMeiqing, "1");
        wrapper.eq(MdtOrderDicomStudy::getIsUnzip, "1");
        wrapper.orderByDesc(MdtOrderDicomStudy::getUpdateTime);
        wrapper.last("limit 1");
        MdtOrderDicomStudy mdtOrderDicomStudy = mdtOrderDicomStudyMapper.selectOne(wrapper);
        if (null != mdtOrderDicomStudy) {
            //检查名称
            jsonObject.put("examName", mdtOrderDicomStudy.getExamName());
            //检查模式
            jsonObject.put("studyModality", mdtOrderDicomStudy.getStudyModality());
            //影像链接
            jsonObject.put("meiqingUrl", mdtOrderDicomStudy.getMeiqingUrl());
            //备注
            jsonObject.put("remark", mdtOrderDicomStudy.getRemark());
            //健康报告
            jsonObject.put("reportUrl", mdtOrderDicomStudy.getReportUrl());
            //创建时间
            jsonObject.put("createTime", mdtOrderDicomStudy.getCreateTime());
            /*强行设置一个类型*/
            jsonObject.put("type", "DICOM_FILE");
        }
        jsonObject.put("isAllSignReport", "0");
        if (mdtOrderReports.size() > 0) {
            //查出所有已签名医生
            MdtOrderReport mdtOrderReport = mdtOrderReports.get(0);
            LambdaQueryWrapper<MdtOrderReportAudit> mdtOrderReportAuditLambdaQueryWrapper = new LambdaQueryWrapper<>();
            mdtOrderReportAuditLambdaQueryWrapper.eq(MdtOrderReportAudit::getReportId, mdtOrderReport.getId());
            List<MdtOrderReportAudit> mdtOrderReportAudits = mdtOrderReportAuditMapper.selectList(mdtOrderReportAuditLambdaQueryWrapper);
            //查出所有要签名的医生
            List<MdtOrderDoctor> orderDoctors = mdtOrderDoctors.stream().filter(s -> UserRoleEnum.CONSULTANT.getCode().equals(s.getDoctorType())).collect(Collectors.toList());
            //比较已签名医生和未签名医生是否一致
            for (MdtOrderReportAudit mdtOrderReportAudit : mdtOrderReportAudits) {
                orderDoctors.removeIf(mdtOrderDoctor -> mdtOrderReportAudit.getAuditDoctorIdCard().equals(mdtOrderDoctor.getDoctorIdCard()));
            }
            if (orderDoctors.size() > 0) {
                jsonObject.put("isAllSignReport", "0");
            } else {
                jsonObject.put("isAllSignReport", "1");
            }

        }
        return JsonResult.ok().putData(jsonObject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult receivingOrders(Map<String, String> map) {
        String orderId = map.get("orderId");
        MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
        if (null == mdtOrder) {
            return JsonResult.error("订单id不存在");
        }
        String status = mdtOrder.getStatus();
        if (!status.equals(StatusEnumUtil.ILLNESS_EXAMINE)) {
            return JsonResult.error("订单状态不支持操作");
        }
        boolean isTeam = mdtOrder.getMdtType().equals(MdtTypeEnum.TEAM.getCode());
        boolean isPersonal = mdtOrder.getMdtType().equals(MdtTypeEnum.PERSONAL.getCode());
        boolean isCombination = mdtOrder.getMdtType().equals(MdtTypeEnum.COMBINATION.getCode());
        String idCard = Objects.requireNonNull(doctorUserMapper.selectById(AuthUtil.getLoginUser().getId())).getPapersNumbers();
        if (isPersonal) {
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setIsHave("1");
            int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId));
            if (update == 0) {
                return JsonResult.error("操作失败");
            }
        } else if (isTeam) {
            //查询出当前订单所有医生
            LambdaQueryWrapper<MdtOrderDoctor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
            List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(lambdaQueryWrapper);
            //当前登陆人在当前订单中的医生信息
            List<MdtOrderDoctor> loginMdtOrderDoctors = mdtOrderDoctors.stream().filter(s -> s.getDoctorIdCard().equals(idCard)).collect(Collectors.toList());
            //过滤：查询出是否是助理医生
            long assistantCount = loginMdtOrderDoctors.stream().filter(mdtOrderDoctor -> mdtOrderDoctor.getDoctorType().equals(UserRoleEnum.ASSISTANT.getCode())).count();
            //当前人是助理医生,否则不是
            if (assistantCount != 0) {
                MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
                updateMdtOrderDoctor.setIsHave("1");
                int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId));
                if (update == 0) {
                    throw new BizException("接单失败");
                }
            } else {
                MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
                updateMdtOrderDoctor.setIsHave("1");
                int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId).eq(MdtOrderDoctor::getDoctorIdCard, idCard));
                if (update == 0) {
                    return JsonResult.error("操作失败");
                }
            }
        } else if (isCombination) {
            LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getDoctorIdCard, idCard);
            mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
            List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
            List<String> mdtOrderDoctorsIds = new ArrayList<>();
            for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctors) {
                mdtOrderDoctorsIds.add(mdtOrderDoctor.getId());
                if (mdtOrderDoctor.getDoctorType().equals(UserRoleEnum.ASSISTANT.getCode())) {
                    String consultantId = mdtOrderDoctor.getConsultantId();
                    mdtOrderDoctorsIds.add(consultantId);
                } else {
                    LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                    mdtOrderDoctorLambdaQueryWrapper1.eq(MdtOrderDoctor::getMdtOrderId, orderId);
                    mdtOrderDoctorLambdaQueryWrapper1.eq(MdtOrderDoctor::getConsultantId, mdtOrderDoctor.getId());
                    MdtOrderDoctor mdtOrderDoctor1 = mdtOrderDoctorMapper.selectOne(mdtOrderDoctorLambdaQueryWrapper1);
                    if (null != mdtOrderDoctor1) {
                        mdtOrderDoctorsIds.add(mdtOrderDoctor1.getId());
                    }
                }
            }
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setIsHave("1");
            int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId).in(MdtOrderDoctor::getId, mdtOrderDoctorsIds));
            if (update == 0) {
                return JsonResult.error("操作失败");
            }
        }
        //查询所有的会诊医生是否接单，如果已经接单判断是否有助理医生未接单，如果没有接单自动帮他接单
        LambdaQueryWrapper<MdtOrderDoctor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
        List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(lambdaQueryWrapper);
        List<MdtOrderDoctor> consultantDoctorCount = mdtOrderDoctorList.stream().filter(s -> UserRoleEnum.CONSULTANT.getCode().equals(s.getDoctorType())).collect(Collectors.toList());
        //会诊医生未接单的医生数量
        long isHaveCount = consultantDoctorCount.stream().filter(s -> !"1".equals(s.getIsHave())).count();
        //会诊医生未审核资料的医生数量
        long auditResultCount = consultantDoctorCount.stream().filter(s -> !"1".equals(s.getAuditResult())).count();
        if (isHaveCount == 0) {
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setIsHave("1");
            mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId).eq(MdtOrderDoctor::getDoctorType, UserRoleEnum.ASSISTANT.getCode()));
        }
        if (auditResultCount == 0) {
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setAuditResult("1");
            updateMdtOrderDoctor.setAuditOpinion("");
            updateMdtOrderDoctor.setAuditTime(cn.hutool.core.date.DateUtil.now());
            mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId).eq(MdtOrderDoctor::getDoctorType, UserRoleEnum.ASSISTANT.getCode()));
        }
        if (isHaveCount == 0 && auditResultCount == 0) {
            MdtOrder updateMdtOrder = new MdtOrder();
            updateMdtOrder.setStatus(StatusEnumUtil.ILLNESS_EXAMINE_PASS);
            LambdaQueryWrapper<MdtOrder> updateMdtOrderWrapper = new LambdaQueryWrapper<>();
            updateMdtOrderWrapper.eq(MdtOrder::getId, orderId);
            updateMdtOrderWrapper.eq(MdtOrder::getStatus, StatusEnumUtil.ILLNESS_EXAMINE);
            int updateById = mdtOrderMapper.update(updateMdtOrder, updateMdtOrderWrapper);
            if (updateById == 0) {
                throw new BizException("订单状态修改失败");
            }
        }
        return JsonResult.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult doctorAgree(Map<String, Object> map) {
        String orderId = map.get("orderId").toString();
        MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
        if (null == mdtOrder) {
            return JsonResult.error("订单id不存在");
        }
        boolean isTeam = mdtOrder.getMdtType().equals(MdtTypeEnum.TEAM.getCode());
        boolean isPersonal = mdtOrder.getMdtType().equals(MdtTypeEnum.PERSONAL.getCode());
        boolean isCombination = mdtOrder.getMdtType().equals(MdtTypeEnum.COMBINATION.getCode());
        String idCard = Objects.requireNonNull(doctorUserMapper.selectById(AuthUtil.getLoginUser().getId())).getPapersNumbers();
        String isAgree = map.get("isAgree").toString();
        if (isPersonal) {
            try {
                LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
                mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, mdtOrder.getId());
                mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getIsAgree, "0");
                List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
                for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctors) {
                    if (isAgree.equals("1")) {
                        pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), mdtOrderDoctor.getDoctorName() + "已确认会诊时间，请尽快查看并处理。",
                                PushMessageType.messageType_MDT, mdtOrder.getId(), mdtOrderDoctor.getDoctorName() + "已确认会诊时间，请尽快查看并处理。", "1");
                    } else {
                        pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), mdtOrderDoctor.getDoctorName() + "会诊时间未确认，请尽快与医生和患者协调统一时间。",
                                PushMessageType.messageType_MDT, mdtOrder.getId(), mdtOrderDoctor.getDoctorName() + "会诊时间未确认，请尽快与医生和患者协调统一时间。", "1");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setIsAgree(isAgree);
            LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
            mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getIsAgree, "0");
            int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, mdtOrderDoctorLambdaQueryWrapper);
            if (update == 0) {
                return JsonResult.error("操作失败");
            }
        } else if (isTeam) {
            //查询出当前订单所有医生
            LambdaQueryWrapper<MdtOrderDoctor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
            List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(lambdaQueryWrapper);
            //当前登陆人在当前订单中的医生信息
            List<MdtOrderDoctor> loginMdtOrderDoctors = mdtOrderDoctors.stream().filter(s -> s.getDoctorIdCard().equals(idCard)).collect(Collectors.toList());
            //过滤：查询出是否是助理医生
            long assistantCount = loginMdtOrderDoctors.stream().filter(mdtOrderDoctor -> mdtOrderDoctor.getDoctorType().equals(UserRoleEnum.ASSISTANT.getCode())).count();
            //当前人是助理医生,否则不是
            if (assistantCount != 0) {
                try {
                    LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, mdtOrder.getId());
                    mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getIsAgree, "0");
                    List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
                    for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctorList) {
                        if (isAgree.equals("1")) {
                            pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), mdtOrderDoctor.getDoctorName() + "已确认会诊时间，请尽快查看并处理。",
                                    PushMessageType.messageType_MDT, mdtOrder.getId(), mdtOrderDoctor.getDoctorName() + "已确认会诊时间，请尽快查看并处理。", "1");
                        } else {
                            pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), mdtOrderDoctor.getDoctorName() + "会诊时间未确认，请尽快与医生和患者协调统一时间。",
                                    PushMessageType.messageType_MDT, mdtOrder.getId(), mdtOrderDoctor.getDoctorName() + "会诊时间未确认，请尽快与医生和患者协调统一时间。", "1");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
                updateMdtOrderDoctor.setIsAgree(isAgree);
                LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
                mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getIsAgree, "0");
                mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
                int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, mdtOrderDoctorLambdaQueryWrapper);
                if (update == 0) {
                    throw new BizException("接单失败");
                }
            } else {
                try {
                    LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, mdtOrder.getId());
                    mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getIsAgree, "0");
                    mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getDoctorIdCard, idCard);
                    List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
                    String doctorName = mdtOrderDoctorList.get(0).getDoctorName();
                    if (isAgree.equals("1")) {
                        pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), doctorName + "已确认会诊时间，请尽快查看并处理。",
                                PushMessageType.messageType_MDT, mdtOrder.getId(), doctorName + "已确认会诊时间，请尽快查看并处理。", "1");
                    } else {
                        pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), doctorName + "会诊时间未确认，请尽快与医生和患者协调统一时间。",
                                PushMessageType.messageType_MDT, mdtOrder.getId(), doctorName + "会诊时间未确认，请尽快与医生和患者协调统一时间。", "1");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
                updateMdtOrderDoctor.setIsAgree(isAgree);
                LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
                mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getIsAgree, "0");
                mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
                mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getDoctorIdCard, idCard);
                int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, mdtOrderDoctorLambdaQueryWrapper);
                if (update == 0) {
                    return JsonResult.error("操作失败");
                }
            }
        } else if (isCombination) {
            LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getDoctorIdCard, idCard);
            mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
            mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getIsAgree, "0");
            List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
            List<String> mdtOrderDoctorsIds = new ArrayList<>();
            for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctors) {
                mdtOrderDoctorsIds.add(mdtOrderDoctor.getId());
                if (mdtOrderDoctor.getDoctorType().equals(UserRoleEnum.ASSISTANT.getCode())) {
                    String consultantId = mdtOrderDoctor.getConsultantId();
                    mdtOrderDoctorsIds.add(consultantId);
                } else {
                    LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                    mdtOrderDoctorLambdaQueryWrapper1.eq(MdtOrderDoctor::getMdtOrderId, orderId);
                    mdtOrderDoctorLambdaQueryWrapper1.eq(MdtOrderDoctor::getConsultantId, mdtOrderDoctor.getId());
                    MdtOrderDoctor mdtOrderDoctor1 = mdtOrderDoctorMapper.selectOne(mdtOrderDoctorLambdaQueryWrapper1);
                    if (null != mdtOrderDoctor1) {
                        mdtOrderDoctorsIds.add(mdtOrderDoctor1.getId());
                    }
                }
            }
            try {
                LambdaQueryWrapper<MdtOrderDoctor> in = new LambdaQueryWrapper<>();
                in.eq(MdtOrderDoctor::getMdtOrderId, orderId);
                in.eq(MdtOrderDoctor::getIsAgree, "0");
                in.in(MdtOrderDoctor::getId, mdtOrderDoctorsIds);
                List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(in);
                for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctorList) {
                    if (isAgree.equals("1")) {
                        pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), mdtOrderDoctor.getDoctorName() + "已确认会诊时间，请尽快查看并处理。",
                                PushMessageType.messageType_MDT, mdtOrder.getId(), mdtOrderDoctor.getDoctorName() + "已确认会诊时间，请尽快查看并处理。", "1");
                    } else {
                        pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), mdtOrderDoctor.getDoctorName() + "会诊时间未确认，请尽快与医生和患者协调统一时间。",
                                PushMessageType.messageType_MDT, mdtOrder.getId(), mdtOrderDoctor.getDoctorName() + "会诊时间未确认，请尽快与医生和患者协调统一时间。", "1");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setIsAgree(isAgree);
            int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId).in(MdtOrderDoctor::getId, mdtOrderDoctorsIds));
            if (update == 0) {
                return JsonResult.error("操作失败");
            }
        }
        //查询所有的会诊医生是否接单，如果已经接单判断是否有助理医生未接单，如果没有接单自动帮他接单
        LambdaQueryWrapper<MdtOrderDoctor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
        List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(lambdaQueryWrapper);
        List<MdtOrderDoctor> consultantDoctorCount = mdtOrderDoctorList.stream().filter(s -> UserRoleEnum.CONSULTANT.getCode().equals(s.getDoctorType())).collect(Collectors.toList());
        //会诊医生未接单的医生数量
        long isAgreeCount = consultantDoctorCount.stream().filter(s -> !"1".equals(s.getIsAgree())).count();
        long isNotAgreeCount = consultantDoctorCount.stream().filter(s -> !"2".equals(s.getIsAgree())).count();
        if (isAgreeCount == 0) {
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setIsAgree("1");
            mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId).eq(MdtOrderDoctor::getDoctorType, UserRoleEnum.ASSISTANT.getCode()));
        }
        if (isNotAgreeCount == 0) {
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setIsAgree("2");
            mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId).eq(MdtOrderDoctor::getDoctorType, UserRoleEnum.ASSISTANT.getCode()));
        }
        return JsonResult.ok();
    }

    @Override
    public List<Map<String, Object>> medicalHistory(String orderId) {
        String OUT_FILE = "OUT_FILE";
        String CHECK_FILE = "CHECK_FILE";
        String DICOM_FILE = "DICOM_FILE";
        LambdaQueryWrapper<MdtOrderFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MdtOrderFile::getMdtOrderId, orderId);
        queryWrapper.orderByDesc(MdtOrderFile::getCreateTime);
        List<MdtOrderFile> mdtOrderFiles = mdtOrderFileMapper.selectList(queryWrapper);
        //查询影像资料
        LambdaQueryWrapper<MdtOrderDicomStudy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdtOrderDicomStudy::getOrderId, orderId);
        wrapper.eq(MdtOrderDicomStudy::getIsCancel, "0");
        wrapper.eq(MdtOrderDicomStudy::getIsUploadMeiqing, "1");
        wrapper.eq(MdtOrderDicomStudy::getIsUnzip, "1");
        List<MdtOrderDicomStudy> mdtOrderDicomStudies = mdtOrderDicomStudyMapper.selectList(wrapper);
        List<Map<String, String>> resultList = mdtOrderDicomStudies
                .stream()
                .map(s -> {
                    Map<String, String> jsonObject = new HashMap<>();
                    //检查名称
                    jsonObject.put("examName", s.getExamName());
                    //检查模式
                    jsonObject.put("studyModality", s.getStudyModality());
                    //影像链接
                    jsonObject.put("meiqingUrl", s.getMeiqingUrl());
                    //备注
                    jsonObject.put("remark", s.getRemark());
                    //健康报告
                    jsonObject.put("reportUrl", s.getReportUrl());
                    //创建时间
                    jsonObject.put("createTime", s.getCreateTime());
                    /*强行设置一个类型*/
                    jsonObject.put("type", "DICOM_FILE");
                    return jsonObject;
                })
                .collect(Collectors.toList());
        for (MdtOrderFile mdtOrderFile : mdtOrderFiles) {
            Map<String, String> jsonObject = new HashMap<>();
            jsonObject.put("id", mdtOrderFile.getId());
            jsonObject.put("createTime", mdtOrderFile.getCreateTime());
            jsonObject.put("updateTime", mdtOrderFile.getUpdateTime());
            jsonObject.put("mdtOrderId", mdtOrderFile.getMdtOrderId());
            jsonObject.put("type", mdtOrderFile.getType());
            jsonObject.put("fileUrl", mdtOrderFile.getFileUrl());
            jsonObject.put("remark", mdtOrderFile.getRemark());
            resultList.add(jsonObject);
        }
        //数据处理=>筛选时间相同的数据
        HashMap<String, List<Map<String, String>>> hashMap = new HashMap<>();
        for (Map<String, String> jsonObject : resultList) {
            String createTime = jsonObject.get("createTime");
            if (hashMap.containsKey(createTime)) {
                List<Map<String, String>> jsonObjects = hashMap.get(createTime);
                jsonObjects.add(jsonObject);
            } else {
                List<Map<String, String>> list = new ArrayList<>();
                list.add(jsonObject);
                hashMap.put(createTime, list);
            }
        }
        //把时间相同的数据的key放提出来，并把数据重新分成三类
        Set<Map.Entry<String, List<Map<String, String>>>> entrySet = hashMap.entrySet();
        List<Object> anythingData = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, String>>> stringListEntry : entrySet) {
            Map<String, Object> stringMapHashMap = new HashMap<>();
            String key = stringListEntry.getKey();
            stringMapHashMap.put("createTime", key);
            List<Map<String, String>> valueList = stringListEntry.getValue();
            List<Map<String, String>> outFile = new ArrayList<>();
            List<Map<String, String>> checkFile = new ArrayList<>();
            List<Map<String, String>> dicomFile = new ArrayList<>();
            for (Map<String, String> stringStringMap : valueList) {
                String type = stringStringMap.get("type");
                if (DICOM_FILE.equals(type)) {
                    dicomFile.add(stringStringMap);
                } else if (CHECK_FILE.equals(type)) {
                    checkFile.add(stringStringMap);
                } else if (OUT_FILE.equals(type)) {
                    outFile.add(stringStringMap);
                }
            }
            Map<Object, Object> relOutFile = new HashMap<>();
            List<String> outFileUrlsList = new ArrayList<>();
            relOutFile.put("remark", "");
            for (Map<String, String> stringStringMap : outFile) {
                outFileUrlsList.add(stringStringMap.get("fileUrl"));
                String fileRemark = stringStringMap.get("remark");
                if (StrUtil.isNotBlank(fileRemark)) {
                    relOutFile.put("remark", fileRemark);
                }
            }
            relOutFile.put("fileUrls", outFileUrlsList);

            Map<Object, Object> relCheckFile = new HashMap<>();
            List<String> checkFileUrlsList = new ArrayList<>();
            relCheckFile.put("remark", "");
            for (Map<String, String> stringStringMap : checkFile) {
                checkFileUrlsList.add(stringStringMap.get("fileUrl"));
                String fileRemark = stringStringMap.get("remark");
                if (StrUtil.isNotBlank(fileRemark)) {
                    relCheckFile.put("remark", fileRemark);
                }
            }
            relCheckFile.put("fileUrls", checkFileUrlsList);

            Map<String, Object> allFiles = new HashMap<>();
            allFiles.put(OUT_FILE, relOutFile);
            allFiles.put(CHECK_FILE, relCheckFile);
            allFiles.put(DICOM_FILE, dicomFile);
            stringMapHashMap.put("value", allFiles);
            anythingData.add(stringMapHashMap);
        }
        Map<String, Object> createTimeMap = new HashMap<>();
        for (Object anythingDatum : anythingData) {
            Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) anythingDatum;
            String createTime = String.valueOf(map.get("createTime")).substring(0, 10);
            if (createTimeMap.containsKey(createTime)) {
                Map<String, Object> oldValueMap = (Map<String, Object>) createTimeMap.get(createTime);
                List<Object> oldDicomFiles = (List<Object>) oldValueMap.get(DICOM_FILE);
                List<Object> oldCheckFiles = (List<Object>) oldValueMap.get(CHECK_FILE);
                List<Object> oldOutFiles = (List<Object>) oldValueMap.get(OUT_FILE);

                Map<String, Object> valueMapList = new HashMap<>();
                Map<String, Object> value = map.get("value");
                List<Object> dicomFile = (List<Object>) value.get(DICOM_FILE);
                Object checkFile = value.get(CHECK_FILE);
                Object outFile = value.get(OUT_FILE);
                oldDicomFiles.addAll(dicomFile);
                oldCheckFiles.add(checkFile);
                oldOutFiles.add(outFile);
                valueMapList.put(DICOM_FILE, oldDicomFiles);
                valueMapList.put(CHECK_FILE, oldCheckFiles);
                valueMapList.put(OUT_FILE, oldOutFiles);
                createTimeMap.put(createTime, valueMapList);
            } else {
                Map<String, Object> valueMapList = new HashMap<>();
                Map<String, Object> value = map.get("value");
                Object checkFile = value.get(CHECK_FILE);
                Object outFile = value.get(OUT_FILE);
                List<Object> checkFiles = new ArrayList<>();
                checkFiles.add(checkFile);
                List<Object> outFiles = new ArrayList<>();
                outFiles.add(outFile);
                valueMapList.put(DICOM_FILE, value.get(DICOM_FILE));
                valueMapList.put(CHECK_FILE, checkFiles);
                valueMapList.put(OUT_FILE, outFiles);
                createTimeMap.put(createTime, valueMapList);
            }
        }
        List<Map<String, Object>> data = new ArrayList<>();
        for (Map.Entry<String, Object> obj : createTimeMap.entrySet()) {
            String key = obj.getKey();
            Object value = obj.getValue();
            Map<String, Object> newMap = new HashMap<>();
            newMap.put("createTime", key);
            newMap.put("value", value);
            data.add(newMap);
        }
        data.sort((o1, o2) -> {
            DateTime createTime1 = DateUtil.parse(o1.get("createTime").toString());
            DateTime createTime2 = DateUtil.parse(o2.get("createTime").toString());
            return createTime2.compareTo(createTime1);
        });
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult auditReview(Map<String, Object> map) {
        String orderId = map.get("orderId").toString();
        MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
        if (null == mdtOrder) {
            return JsonResult.error("订单id不存在");
        }
        boolean isTeam = mdtOrder.getMdtType().equals(MdtTypeEnum.TEAM.getCode());
        boolean isPersonal = mdtOrder.getMdtType().equals(MdtTypeEnum.PERSONAL.getCode());
        boolean isCombination = mdtOrder.getMdtType().equals(MdtTypeEnum.COMBINATION.getCode());
        String idCard = Objects.requireNonNull(doctorUserMapper.selectById(AuthUtil.getLoginUser().getId())).getPapersNumbers();
        String auditResult = map.get("auditResult").toString();
        String auditOpinion;
        if (null == map.get("auditOpinion") || StrUtil.isBlank(map.get("auditOpinion").toString())) {
            auditOpinion = "";
        } else {
            auditOpinion = map.get("auditOpinion").toString();
        }
        if (isPersonal) {
            try {
                LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
                mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, mdtOrder.getId());
                mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getAuditResult, "0");
                List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
                for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctors) {
                    pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), mdtOrder.getName() + "的病史材料" + mdtOrderDoctor.getDoctorName() + "已审核通过，请尽快查看并处理。",
                            PushMessageType.messageType_MDT, mdtOrder.getId(), mdtOrder.getName() + "的病史材料" + mdtOrderDoctor.getDoctorName() + "已审核通过，请尽快查看并处理。", "1");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setAuditResult(auditResult);
            updateMdtOrderDoctor.setAuditOpinion(auditOpinion);
            updateMdtOrderDoctor.setAuditTime(DateTime.now().toString());
            int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId));
            if (update == 0) {
                return JsonResult.error("操作失败");
            }
        } else if (isTeam) {
            //查询出当前订单所有医生
            LambdaQueryWrapper<MdtOrderDoctor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
            List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(lambdaQueryWrapper);
            //当前登陆人在当前订单中的医生信息
            List<MdtOrderDoctor> loginMdtOrderDoctors = mdtOrderDoctors.stream().filter(s -> s.getDoctorIdCard().equals(idCard)).collect(Collectors.toList());
            //过滤：查询出是否是助理医生
            long assistantCount = loginMdtOrderDoctors.stream().filter(mdtOrderDoctor -> mdtOrderDoctor.getDoctorType().equals(UserRoleEnum.ASSISTANT.getCode())).count();
            //当前人是助理医生,否则不是
            if (assistantCount != 0) {
                try {
                    LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, mdtOrder.getId());
                    mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getAuditResult, "0");
                    List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
                    for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctorList) {
                        pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), mdtOrder.getName() + "的病史材料" + mdtOrderDoctor.getDoctorName() + "已审核通过，请尽快查看并处理。",
                                PushMessageType.messageType_MDT, mdtOrder.getId(), mdtOrder.getName() + "的病史材料" + mdtOrderDoctor.getDoctorName() + "已审核通过，请尽快查看并处理。", "1");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
                updateMdtOrderDoctor.setAuditResult(auditResult);
                updateMdtOrderDoctor.setAuditOpinion(auditOpinion);
                updateMdtOrderDoctor.setAuditTime(DateTime.now().toString());
                int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId));
                if (update == 0) {
                    throw new BizException("接单失败");
                }
            } else {
                try {
                    LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, mdtOrder.getId());
                    mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getAuditResult, "0");
                    mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getDoctorIdCard, idCard);
                    List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
                    String doctorName = mdtOrderDoctorList.get(0).getDoctorName();
                    pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), mdtOrder.getName() + "的病史材料" + doctorName + "已审核通过，请尽快查看并处理。",
                            PushMessageType.messageType_MDT, mdtOrder.getId(), mdtOrder.getName() + "的病史材料" + doctorName + "已审核通过，请尽快查看并处理。", "1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
                updateMdtOrderDoctor.setAuditResult(auditResult);
                updateMdtOrderDoctor.setAuditOpinion(auditOpinion);
                updateMdtOrderDoctor.setAuditTime(DateTime.now().toString());
                int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId).eq(MdtOrderDoctor::getDoctorIdCard, idCard));
                if (update == 0) {
                    return JsonResult.error("操作失败");
                }
            }
        } else if (isCombination) {
            LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getDoctorIdCard, idCard);
            mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
            List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
            List<String> mdtOrderDoctorsIds = new ArrayList<>();
            for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctors) {
                mdtOrderDoctorsIds.add(mdtOrderDoctor.getId());
                if (mdtOrderDoctor.getDoctorType().equals(UserRoleEnum.ASSISTANT.getCode())) {
                    String consultantId = mdtOrderDoctor.getConsultantId();
                    mdtOrderDoctorsIds.add(consultantId);
                } else {
                    LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                    mdtOrderDoctorLambdaQueryWrapper1.eq(MdtOrderDoctor::getMdtOrderId, orderId);
                    mdtOrderDoctorLambdaQueryWrapper1.eq(MdtOrderDoctor::getConsultantId, mdtOrderDoctor.getId());
                    MdtOrderDoctor mdtOrderDoctor1 = mdtOrderDoctorMapper.selectOne(mdtOrderDoctorLambdaQueryWrapper1);
                    if (null != mdtOrderDoctor1) {
                        mdtOrderDoctorsIds.add(mdtOrderDoctor1.getId());
                    }
                }
            }
            try {
                LambdaQueryWrapper<MdtOrderDoctor> in = new LambdaQueryWrapper<>();
                in.eq(MdtOrderDoctor::getMdtOrderId, orderId);
                in.eq(MdtOrderDoctor::getAuditResult, "0");
                in.in(MdtOrderDoctor::getId, mdtOrderDoctorsIds);
                List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(in);
                for (MdtOrderDoctor mdtOrderDoctor : mdtOrderDoctorList) {
                    pushMessageService.insertAdmin("会诊订单", mdtOrder.getAdminId(), mdtOrder.getName() + "的病史材料" + mdtOrderDoctor.getDoctorName() + "已审核通过，请尽快查看并处理。",
                            PushMessageType.messageType_MDT, mdtOrder.getId(), mdtOrder.getName() + "的病史材料" + mdtOrderDoctor.getDoctorName() + "已审核通过，请尽快查看并处理。", "1");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setAuditResult(auditResult);
            updateMdtOrderDoctor.setAuditOpinion(auditOpinion);
            updateMdtOrderDoctor.setAuditTime(DateTime.now().toString());
            int update = mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId).in(MdtOrderDoctor::getId, mdtOrderDoctorsIds));
            if (update == 0) {
                return JsonResult.error("操作失败");
            }
        }
        //查询所有的会诊医生是否接单，如果已经接单判断是否有助理医生未接单，如果没有接单自动帮他接单
        LambdaQueryWrapper<MdtOrderDoctor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
        List<MdtOrderDoctor> mdtOrderDoctorList = mdtOrderDoctorMapper.selectList(lambdaQueryWrapper);
        List<MdtOrderDoctor> consultantDoctorCount = mdtOrderDoctorList.stream().filter(s -> UserRoleEnum.CONSULTANT.getCode().equals(s.getDoctorType())).collect(Collectors.toList());
        //会诊医生未接单的医生数量
        long isHaveCount = consultantDoctorCount.stream().filter(s -> !"1".equals(s.getIsHave())).count();
        //会诊医生未审核资料的医生数量
        long auditResultCount = consultantDoctorCount.stream().filter(s -> !"1".equals(s.getAuditResult())).count();
        if (isHaveCount == 0) {
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setIsHave("1");
            mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId).eq(MdtOrderDoctor::getDoctorType, UserRoleEnum.ASSISTANT.getCode()));
        }
        if (auditResultCount == 0) {
            MdtOrderDoctor updateMdtOrderDoctor = new MdtOrderDoctor();
            updateMdtOrderDoctor.setAuditResult("1");
            updateMdtOrderDoctor.setAuditOpinion("");
            updateMdtOrderDoctor.setAuditTime(cn.hutool.core.date.DateUtil.now());
            mdtOrderDoctorMapper.update(updateMdtOrderDoctor, new LambdaQueryWrapper<MdtOrderDoctor>().eq(MdtOrderDoctor::getMdtOrderId, orderId).eq(MdtOrderDoctor::getDoctorType, UserRoleEnum.ASSISTANT.getCode()));
        }
        if (isHaveCount == 0 && auditResultCount == 0) {
            MdtOrder updateMdtOrder = new MdtOrder();
            updateMdtOrder.setStatus(StatusEnumUtil.ILLNESS_EXAMINE_PASS);
            LambdaQueryWrapper<MdtOrder> updateMdtOrderWrapper = new LambdaQueryWrapper<>();
            updateMdtOrderWrapper.eq(MdtOrder::getId, orderId);
            updateMdtOrderWrapper.eq(MdtOrder::getStatus, StatusEnumUtil.ILLNESS_EXAMINE);
            int updateById = mdtOrderMapper.update(updateMdtOrder, updateMdtOrderWrapper);
            if (updateById == 0) {
                throw new BizException("订单状态修改失败");
            }
        }
        return JsonResult.ok();
    }

    /**
     * 保存会诊报告
     *
     * @param medicalHistorySummary
     * @param consultationSummary
     * @param diagnosisResults
     * @param treatmentPlan
     * @param imgPath
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult mdtReport(String orderId, String medicalHistorySummary, String consultationSummary, String diagnosisResults, String treatmentPlan,
                                String imgPath, String demand, String checkup) {
        MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
        if (null == mdtOrder) {
            return JsonResult.error(500, "MDT订单编码错误!");
        }
        DoctorUser loginUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
        assert loginUser != null;
        String loginUserId = loginUser.getId();
        String reportDoctorId = mdtOrder.getReportDoctorId();
        MdtOrderDoctor mdtOrderDoctor = mdtOrderDoctorMapper.selectById(reportDoctorId);
        if (!mdtOrderDoctor.getDoctorUserId().equals(loginUserId)) {
            return JsonResult.error("登录人不是报告编写人");
        }
        List<MdtOrderReport> mdtOrderReports = mdtOrderReportMapper.selectList(new LambdaQueryWrapper<MdtOrderReport>().eq(MdtOrderReport::getOrderNum, mdtOrder.getOrderNum()));
        if (null != mdtOrderReports && mdtOrderReports.size() > 0) {
            if (mdtOrderReports.size() == 1) {
                MdtOrderReport mdtOrderReport = mdtOrderReports.get(0);
                List<MdtOrderReportAudit> mdtOrderReportAudits = mdtOrderReportAuditMapper.selectList(new LambdaQueryWrapper<MdtOrderReportAudit>().eq(MdtOrderReportAudit::getReportId, mdtOrderReport.getId()));
                if (null != mdtOrderReportAudits && mdtOrderReportAudits.size() > 0) {
                    return JsonResult.error("已存在签章过的会诊报告");
                }
            }
            List<String> mdtOrderReportIds = mdtOrderReports.stream().map(MdtOrderReport::getId).collect(Collectors.toList());
            mdtOrderReportAuditMapper.delete(new LambdaQueryWrapper<MdtOrderReportAudit>().in(MdtOrderReportAudit::getReportId, mdtOrderReportIds));
            mdtOrderReportMapper.delete(new LambdaQueryWrapper<MdtOrderReport>().in(MdtOrderReport::getId, mdtOrderReportIds));
        }
        MdtOrderReport report = new MdtOrderReport();
        report.setOrderNum(mdtOrder.getOrderNum());
        if (StrUtil.isNotBlank(medicalHistorySummary)) {
            report.setMedicalHistorySummary(medicalHistorySummary);
        }
        if (StrUtil.isNotBlank(consultationSummary)) {
            report.setConsultationSummary(consultationSummary);
        }
        if (StrUtil.isNotBlank(diagnosisResults)) {
            report.setDiagnosisResults(diagnosisResults);
        }
        if (StrUtil.isNotBlank(treatmentPlan)) {
            report.setTreatmentPlan(treatmentPlan);
        }
        if (StrUtil.isNotBlank(imgPath)) {
            report.setImgPath(imgPath);
        }
        if (StrUtil.isNotBlank(demand)) {
            report.setDemand(demand);
        }
        if (StrUtil.isNotBlank(checkup)) {
            report.setCheckup(checkup);
        }
        report.setOrderDoctorId(mdtOrderDoctor.getId());
        report.setDoctorName(mdtOrderDoctor.getDoctorName());
        int insert = mdtOrderReportMapper.insert(report);
        if (insert == 0) {
            return JsonResult.error("保存失败");
        }
        LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, mdtOrder.getId());
        List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
        List<String> doctorUserIds = mdtOrderDoctors.stream().map(MdtOrderDoctor::getDoctorUserId).distinct().collect(Collectors.toList());
        List<DoctorUser> doctorUsers = doctorUserMapper.selectList(new LambdaQueryWrapper<DoctorUser>().in(DoctorUser::getId, doctorUserIds));
        String mobiles = doctorUsers.stream().map(DoctorUser::getMobile).collect(Collectors.joining(";"));
        smsModelSendUtil.mdtReportAutographRemindDoctor(mobiles, mdtOrder.getName());
        return JsonResult.ok(200, "操作成功!");
    }

    /**
     * 提交签名接口
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult sign(@RequestBody Map<String, String> param) {
        String orderId = param.get("orderId");
        //获取到会诊订单
        MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
        String orderNum = mdtOrder.getOrderNum();
        //获取到报告
        LambdaQueryWrapper<MdtOrderReport> mdtOrderReportLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mdtOrderReportLambdaQueryWrapper.eq(MdtOrderReport::getOrderNum, orderNum);
        mdtOrderReportLambdaQueryWrapper.orderByDesc(MdtOrderReport::getCreateTime);
        List<MdtOrderReport> mdtOrderReports = mdtOrderReportMapper.selectList(mdtOrderReportLambdaQueryWrapper);
        if (null == mdtOrderReports || mdtOrderReports.size() == 0) {
            return JsonResult.error("报告不存在");
        }
        MdtOrderReport mdtOrderReport = mdtOrderReports.get(0);
        String reportId = mdtOrderReport.getId();
        //获取当前登录医生的身份证号
        DoctorUser doctorUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
        LambdaQueryWrapper<MdtOrderDoctor> mdtOrderDoctorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        assert doctorUser != null;
        String idCard = doctorUser.getPapersNumbers();
        mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getDoctorIdCard, idCard);
        mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getMdtOrderId, orderId);
        mdtOrderDoctorLambdaQueryWrapper.eq(MdtOrderDoctor::getDoctorType, UserRoleEnum.CONSULTANT.getCode());
        //拿到当前登录是否是会诊医生
        List<MdtOrderDoctor> mdtOrderDoctors = mdtOrderDoctorMapper.selectList(mdtOrderDoctorLambdaQueryWrapper);
        if (mdtOrderDoctors.size() != 1) {
            return JsonResult.error("当前登陆人在这个订单对应的会诊医生要么没有要么有多个");
        }
        //判断是否已经获取过当前医生的签名
        MdtOrderDoctor mdtOrderDoctor = mdtOrderDoctors.get(0);
        String doctorIdCard = mdtOrderDoctor.getDoctorIdCard();
        LambdaQueryWrapper<MdtOrderReportAudit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdtOrderReportAudit::getAuditDoctorIdCard, doctorIdCard);
        wrapper.eq(MdtOrderReportAudit::getReportId, reportId);
        List<MdtOrderReportAudit> orderReportAuditList = mdtOrderReportAuditMapper.selectList(wrapper);
        if (null != orderReportAuditList && orderReportAuditList.size() > 0) {
            return JsonResult.ok("签章成功");
        }
        JSONObject sealUrl = getSealUrl(doctorIdCard);
        Object code = sealUrl.get("code");
        Object msg = sealUrl.get("msg");
        if (null == code || !"1".equals(code.toString()) || null == msg || StrUtil.isBlank(msg.toString())) {
            return JsonResult.error("获取签章失败：" + msg);
        }
        //保存签名
        MdtOrderReportAudit audit = new MdtOrderReportAudit();
        audit.setAuditHospitalName(doctorUser.getHospitalName());
        audit.setAuditDepartmentName(doctorUser.getDepartName());
        audit.setAuditDoctorName(doctorUser.getName());
        audit.setAuditDoctorIdCard(idCard);
        audit.setAuditDoctorOfficeHolderName(doctorUser.getOfficeHolderName());
        audit.setAuditDoctorHeadImg(doctorUser.getHeadImg());
        audit.setAuditResult("签章获取成功");
        audit.setAuditTime(cn.hutool.core.date.DateUtil.now());
        audit.setReportId(reportId);
        audit.setIsMain(mdtOrderDoctor.getIsMain());
        audit.setDoctorType(mdtOrderDoctor.getDoctorType());
        audit.setAuditOpinion(msg.toString());
        LambdaQueryWrapper<MdtOrderReportAudit> mdtOrderReportAuditLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mdtOrderReportAuditLambdaQueryWrapper.eq(MdtOrderReportAudit::getReportId, reportId);
        mdtOrderReportAuditLambdaQueryWrapper.eq(MdtOrderReportAudit::getAuditDoctorIdCard, idCard);
        List<MdtOrderReportAudit> mdtOrderReportAudits = mdtOrderReportAuditMapper.selectList(mdtOrderReportAuditLambdaQueryWrapper);
        if (mdtOrderReportAudits.size() == 0) {
            int insert = mdtOrderReportAuditMapper.insert(audit);
            if (insert == 0) {
                return JsonResult.error("保存签章医生失败");
            }
        }
        try {
            String intro = "您的" + mdtOrder.getMdtName() + "会诊报告已发送，请前往查看。";
            pushMessageService.insertPatient("会诊订单" , mdtOrder.getPatientUserId(), intro,
                    PushMessageType.messageType_MDT, mdtOrder.getId(), intro, "0");
                    
            intro = mdtOrder.getName() + "的会诊报告已审核通过，请前往查看并发送给患者。";
            pushMessageService.insertDoctor("会诊订单" , mdtOrderDoctor.getDoctorUserId(), intro,
                            PushMessageType.messageType_MDT, mdtOrder.getId(), intro, "0");
                    
        } catch (Exception e) {
            log.error("推送失败", e);
        }
        return JsonResult.ok("签章成功");
    }

    /**
     * 调用第三方接口获取签章
     *
     * @param idCard
     * @return code 0失败 1成功
     * 成功数据或失败提示
     */
    public JSONObject getSealUrl(String idCard) {
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", idCard);
            map.put("appId", caProperties.getAppId());
            String msg = "appId=" + caProperties.getAppId() + "&id=" + idCard;
            String signature = SecurityUtil.signature(caProperties.getPrivateStr(), msg);
            map.put("sign", signature);
            String response = HttpUtil.post(caProperties.getOpenUrl() + "v1/user/seal", map);
            String decode = URLDecoder.decode(response, "UTF-8");
            log.debug(decode);
            HashMap hashMap = JSON.parseObject(decode, HashMap.class);
            HashMap data = JSON.parseObject(hashMap.get("data").toString(), HashMap.class);
            String seal = data.get("seal").toString();
            byte[] bytes = Base64.decode(seal);
            String fileName = cn.hutool.core.date.DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT) + RandomUtil.randomString(4) + ".png";
            MultipartFile file = new MockMultipartFile(fileName, ".png", null, bytes);
            String url = OssFileUtils.uploadFile(file);
            System.out.println("签章URL:" + url);
            jsonObject.put("code", "1");
            jsonObject.put("msg", url);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("code", "0");
        jsonObject.put("msg", "请检查云医签是否设置签名");
        return jsonObject;
    }

    /**
     * 查看会诊报告
     *
     * @param orderId
     * @return
     */
    @Override
    public MdtOrderReportVo orderReport(String orderId) {
        MdtOrderReportVo mdtOrderReportVo = new MdtOrderReportVo();
        LambdaQueryWrapper<MdtOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdtOrder::getId, orderId);
        MdtOrder mdtOrder = mdtOrderMapper.selectOne(wrapper);
        if (null == mdtOrder) {
            throw new BizException("订单号不存在!");
        }
        String orderNum = mdtOrder.getOrderNum();
        //查询会诊报告
        LambdaQueryWrapper<MdtOrderReport> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MdtOrderReport::getOrderNum, orderNum);
        queryWrapper.orderByDesc(MdtOrderReport::getCreateTime);
        List<MdtOrderReport> report = mdtOrderReportMapper.selectList(queryWrapper);
        if (null == report || report.size() == 0) {
            return null;
        }

        mdtOrderReportVo.setOrderNum(orderNum);
        mdtOrderReportVo.setMdtStartTime(mdtOrder.getMdtStartTime());
        mdtOrderReportVo.setMdtEndTime(mdtOrder.getMdtEndTime());
        mdtOrderReportVo.setMdtAppointmentTime(StringUtils.isNotBlank(mdtOrder.getMdtAppointmentTime()) ? mdtOrder.getMdtAppointmentTime() : "");
        mdtOrderReportVo.setSickName(mdtOrder.getName());
        mdtOrderReportVo.setSex(mdtOrder.getSex());
        mdtOrderReportVo.setSexName(StringUtils.isNotBlank(mdtOrder.getSex()) ? ("0".equals(mdtOrder.getSex()) ? "女" : "1".equals(mdtOrder.getSex()) ? "男" : "未知") : "未知");
        mdtOrderReportVo.setAge(mdtOrder.getAge());
        mdtOrderReportVo.setConsultationDoctor(mdtOrder.getConsultationDoctorName());
        String reportDoctorId = mdtOrder.getReportDoctorId();
        if (StrUtil.isBlank(reportDoctorId)) {
            mdtOrderReportVo.setReportAuthor("未指定");
        } else {
            MdtOrderDoctor mdtOrderDoctor = mdtOrderDoctorMapper.selectById(reportDoctorId);
            DoctorUser doctorUser = doctorUserMapper.selectById(mdtOrderDoctor.getDoctorUserId());
            mdtOrderReportVo.setReportAuthor(null == doctorUser ? "合偶平方医生" : doctorUser.getName());
        }
        if (StrUtil.isNotBlank(report.get(0).getImgPath())) {
            mdtOrderReportVo.setIsImg("1");
            mdtOrderReportVo.setReportUrl(StrUtil.isBlank(report.get(0).getOssImgPath()) ? report.get(0).getImgPath() : report.get(0).getOssImgPath());
        } else {
            mdtOrderReportVo.setIsImg("0");
            mdtOrderReportVo.setMedicalHistorySummary(report.get(0).getMedicalHistorySummary());
            mdtOrderReportVo.setConsultationSummary(report.get(0).getConsultationSummary());
            mdtOrderReportVo.setDiagnosisResults(report.get(0).getDiagnosisResults());
            mdtOrderReportVo.setTreatmentPlan(report.get(0).getTreatmentPlan());
            mdtOrderReportVo.setDemand(report.get(0).getDemand());
            mdtOrderReportVo.setCheckup(report.get(0).getCheckup());
        }
        List<MdtOrderReportAudit> mdtOrderReportAudits = mdtOrderReportAuditMapper.selectList(new QueryWrapper<MdtOrderReportAudit>().lambda().eq(MdtOrderReportAudit::getReportId, report.get(0).getId()));
        List<String> signList = Lists.newArrayList();
        mdtOrderReportAudits.forEach(audits -> {
            signList.add(audits.getAuditOpinion());
        });
        mdtOrderReportVo.setSignList(signList);
        return mdtOrderReportVo;
    }

    @Override
    public JsonResult prescriptionSign(Map<String, String> map) {
        String orderId = String.valueOf(map.get("orderId"));//id
        MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
        if (mdtOrder == null) {
            return JsonResult.error("对应id无会诊订单");
        }
        LambdaQueryWrapper<HosPrescription> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HosPrescription::getType, "MDT");
        lambdaQueryWrapper.eq(HosPrescription::getOtherId, orderId);
        lambdaQueryWrapper.eq(HosPrescription::getIsCancel, "0");
        HosPrescription send = hosPrescriptionMapper.selectOne(lambdaQueryWrapper);
        if (null == send) {
            return JsonResult.error("处方单不存在");
        }
        String doctorSignet = send.getDoctorSignet();
        if (null == doctorSignet || "".equals(doctorSignet)) {
            HosPrescription hosPrescription = new HosPrescription();
            hosPrescription.setId(send.getId());
            DoctorUser doctorUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
            assert doctorUser != null;
            String idCard = doctorUser.getPapersNumbers();
            JSONObject sealUrl = getSealUrl(idCard);
            Object code = sealUrl.get("code");
            Object msg = sealUrl.get("msg");
            if (null == code || !"1".equals(code.toString()) || null == msg || StrUtil.isBlank(msg.toString())) {
                return JsonResult.error("请检查云医签是否设置签名");
            }
            hosPrescription.setCheckStatus(StatusEnumUtil.SIGN);
            hosPrescription.setDoctorSignet(msg.toString());
            int i = hosPrescriptionMapper.updateById(hosPrescription);
            if (i == 0) {
                return JsonResult.error("保存失败");
            }
            return JsonResult.ok().putData(msg.toString());
        } else {
            return JsonResult.ok().putData(doctorSignet);
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
    public JsonResult setPrescription(Map<String, Object> map) {
        //复诊订单查询
        String orderId = String.valueOf(map.get("orderId"));//id
        MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
        if (mdtOrder == null) {
            return JsonResult.error("对应id无会诊订单");
        }
        LambdaQueryWrapper<HosPrescription> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HosPrescription::getType, "MDT");
        lambdaQueryWrapper.eq(HosPrescription::getOtherId, orderId);
        lambdaQueryWrapper.eq(HosPrescription::getIsCancel, "0");
        HosPrescription oldHosPrescription = hosPrescriptionMapper.selectOne(lambdaQueryWrapper);
        if (null != oldHosPrescription) {
            return JsonResult.error("已开过处方");
        }
        DoctorUser doctorUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
        assert doctorUser != null;
        /**
         * 创建处方
         */
        HosPrescription hosPrescription = new HosPrescription();
        hosPrescription.setPatientUserId(mdtOrder.getPatientUserId());
        hosPrescription.setHosSickId(mdtOrder.getHosSickId());
        hosPrescription.setDoctorUserId(doctorUser.getId());
        hosPrescription.setType("MDT");
        hosPrescription.setOtherId(orderId);
        String orderNum = mdtOrder.getOrderNum();
        hosPrescription.setVisitMdtNum(orderNum);
        hosPrescription.setOrderNum("P" + OrderNoUtil.getOrderNo());
        hosPrescription.setPrescriptionNum(OrderNoUtil.getOrderNo());
        hosPrescription.setFeeType("SELF");
        hosPrescription.setName(mdtOrder.getName());
        hosPrescription.setIdCard(mdtOrder.getIdCard());
        hosPrescription.setSex(mdtOrder.getSex());
        hosPrescription.setAge(mdtOrder.getAge());
        hosPrescription.setMobile(mdtOrder.getMobile());
        hosPrescription.setDepartCode(doctorUser.getDepartCode());
        hosPrescription.setDepartName(doctorUser.getDepartName());
        hosPrescription.setCheckStatus(StatusEnumUtil.NOTSIGN);
        MdtTeam mdtTeam = mdtTeamMapper.selectById(mdtOrder.getMdtTeamId());
        Mdt mdt = mdtMapper.selectById(mdtTeam.getMdtId());
        hosPrescription.setMedicalCertificate(mdt.getName());
        //药品查询[{"drugId":"7","freqMedCode":"Prn","routeAdmiCode":"922"}]
        List<Map> drugs = CastUtil.castList(map.get("drugs"), Map.class);
        double sum = 0d;
        for (Map drug : drugs) {
            String drugId = drug.get("drugId").toString();
            double number = Double.parseDouble(drug.get("number").toString());
            Drug selectDrug = drugMapper.selectById(drugId);
            if (selectDrug == null) {
                throw new BizException("药品id错误，创建处方单失败");
            }
            double price = Double.parseDouble(selectDrug.getPrice());
            sum += (price * number);
        }
        hosPrescription.setTotalPrice(String.valueOf(sum));
        int i = hosPrescriptionMapper.insert(hosPrescription);
        if (i == 0) {
            throw new BizException("处方保存失败");
        }
        /*JSONObject sealUrl = getSealUrl(idCard);
        Object code = sealUrl.get("code");
        Object msg = sealUrl.get("msg");
        if (null == code || !"1".equals(code.toString()) || null == msg || StrUtil.isBlank(msg.toString())) {
            throw new BizException("请检查云医签是否设置签名");
        }*/
        String hosPrescriptionId = hosPrescription.getId();
        /*HosPrescription update = new HosPrescription();
        update.setId(hosPrescriptionId);
        update.setDoctorSignet(msg.toString());
        int updateById = hosPrescriptionMapper.updateById(update);
        if (updateById == 0) {
            throw new BizException("签章保存失败");
        }*/
        saveDrugs(drugs, hosPrescriptionId);

        // 2022-03-01 ios医生端测试版本提交正常处方审核长时间无响应。
        judgeService.judge(hosPrescription.getId());
        return JsonResult.ok("开方成功");
    }

    /**
     * 开处方保存药品
     *
     * @param drugs
     * @param hosPrescriptionId
     */
    private void saveDrugs(List<Map> drugs, String hosPrescriptionId) {
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
            hosPrescriptionDrug.setRouteAdmiName(routeAdmiName);//默认用药途径
            hosPrescriptionDrug.setErp(selectDrug.getErp());
            hosPrescriptionDrug.setErpId(selectDrug.getErpId());
            int insert = hosPrescriptionDrugMapper.insert(hosPrescriptionDrug);
            if (insert == 0) {
                throw new BizException("药品保存失败");
            }
        }
    }

    @Override
    public JsonResult prescriptionDetails(Map<String, String> map) {
        String orderId = String.valueOf(map.get("orderId"));//id
        MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
        if (mdtOrder == null) {
            return JsonResult.error("对应id无会诊订单");
        }
        LambdaQueryWrapper<HosPrescription> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HosPrescription::getType, "MDT");
        lambdaQueryWrapper.eq(HosPrescription::getOtherId, orderId);
        lambdaQueryWrapper.eq(HosPrescription::getIsCancel, "0");
        HosPrescription hosPrescription = hosPrescriptionMapper.selectOne(lambdaQueryWrapper);
        if (null == hosPrescription) {
            return JsonResult.error("未开处方");
        }
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
        Map result = new HashMap<>();
        result.put("id", prescriptionId);
        result.put("checkStatus", hosPrescription.getCheckStatus());
        result.put("checkContent", hosPrescription.getCheckContent());
        result.put("doctorSignet", hosPrescription.getDoctorSignet());
        result.put("drugs", drugs);
        result.put("orderId", orderId);
        return JsonResult.ok().putData(result);
    }

    /**
     * 第二次提交审核（修改认证状态为已认证）
     *
     * @param map 订单id
     * @return
     */
    @Override
    public JsonResult commitPrescription(Map<String, String> map) {
        String orderId = String.valueOf(map.get("orderId"));//id
        MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
        if (mdtOrder == null) {
            return JsonResult.error("对应id无会诊订单");
        }
        LambdaQueryWrapper<HosPrescription> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HosPrescription::getType, "MDT");
        lambdaQueryWrapper.eq(HosPrescription::getOtherId, orderId);
        lambdaQueryWrapper.eq(HosPrescription::getIsCancel, "0");
        HosPrescription oldHosPrescription = hosPrescriptionMapper.selectOne(lambdaQueryWrapper);
        if (null == oldHosPrescription) {
            return JsonResult.error("处方单不存在");
        }
        String checkStatus = oldHosPrescription.getCheckStatus();
        if (!"SIGN".equals(checkStatus)) {
            return JsonResult.error("处方未签名");
        }
        HosPrescription hosPrescription = new HosPrescription();
        hosPrescription.setId(oldHosPrescription.getId());
        hosPrescription.setCheckStatus(StatusEnumUtil.WAIT);
        int i = hosPrescriptionMapper.updateById(hosPrescription);
        judgeService.judge(oldHosPrescription.getId());
        if (i != 0) {
            return JsonResult.ok("开处方成功");
        }
        return JsonResult.error("开处方失败");
    }

    @Override
    public JsonResult prescriptionStatus(Map<String, String> map) {
        String orderId = String.valueOf(map.get("orderId"));//id
        MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
        if (mdtOrder == null) {
            return JsonResult.error("对应id无会诊订单");
        }
        LambdaQueryWrapper<HosPrescription> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HosPrescription::getType, "MDT");
        lambdaQueryWrapper.eq(HosPrescription::getOtherId, orderId);
        lambdaQueryWrapper.eq(HosPrescription::getIsCancel, "0");
        HosPrescription oldHosPrescription = hosPrescriptionMapper.selectOne(lambdaQueryWrapper);
        JSONObject jsonObject = new JSONObject();
        if (null != oldHosPrescription) {
            jsonObject.put("checkStatus", oldHosPrescription.getCheckStatus());
            jsonObject.put("doctorSignet", oldHosPrescription.getDoctorSignet());
            return JsonResult.ok().putData(jsonObject);
        }
        return JsonResult.ok("处方单不存在").putData(jsonObject);
    }

}
