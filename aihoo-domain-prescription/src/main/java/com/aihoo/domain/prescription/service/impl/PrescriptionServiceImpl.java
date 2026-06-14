package com.aihoo.domain.prescription.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.aihoo.properties.CaProperties;
import com.aihoo.properties.TencentProperties;
import com.aihoo.util.CastUtil;
import com.aihoo.util.OssFileUtils;
import com.aihoo.util.SecurityUtil;
import com.aihoo.common.JsonResult;
import com.aihoo.domain.hospital.model.entity.Drugstore;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.prescription.dto.*;
import com.aihoo.domain.prescription.dto.RecentPreVo;
import com.aihoo.domain.consultation.model.mapper.MdtOrderMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDrugMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionMapper;
import com.aihoo.domain.visit.model.mapper.HosVisitMapper;
import com.aihoo.domain.visit.model.mapper.HosRevisitMapper;
import com.aihoo.domain.sys.model.mapper.DictMapper;
import com.aihoo.domain.sys.model.mapper.DiseaseMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionErrorMapper;

import com.aihoo.domain.prescription.dto.PrescriptionConsultationFeeDTO;
import com.aihoo.domain.prescription.dto.PrescriptionDrugDTO;
import com.aihoo.domain.prescription.dto.PrescriptionInstructionDTO;

import com.aihoo.enums.DictTypeEnum;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.security.AuthUtil;
import com.aihoo.util.OrderNoUtil;
import com.aihoo.util.TencentImGroupUtil;
import com.aihoo.domain.im.dto.ImSendGroupMsgRequest;
import com.aihoo.domain.im.dto.ImSendGroupMsgRespVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.aihoo.domain.hospital.model.mapper.DrugMapper;
import com.aihoo.domain.sys.model.mapper.TBaseMapper;
import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDisease;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrug;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionFee;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionInstruction;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionError;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDiseaseError;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrugError;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.consultation.model.entity.MdtOrder;
import com.aihoo.domain.sys.model.entity.Disease;
import com.aihoo.domain.sys.model.entity.Dict;
import com.aihoo.domain.sys.model.entity.TBase;
import com.aihoo.domain.prescription.service.PrescriptionDrugErrorService;
import com.aihoo.domain.hospital.service.DrugstoreService;
import com.aihoo.domain.prescription.service.PrescriptionDrugService;
import com.aihoo.domain.prescription.service.PrescriptionDiseaseErrorService;
import com.aihoo.domain.prescription.service.PrescriptionDiseaseService;
import com.aihoo.domain.prescription.service.PrescriptionFeeService;
import com.aihoo.domain.im.service.IMService;
import com.aihoo.domain.visit.service.LogService;
import com.aihoo.domain.prescription.service.PrescriptionInstructionService;
import com.aihoo.domain.payment.service.JudgeService;
import com.aihoo.domain.prescription.service.PrescriptionService;

@Service
@Slf4j
public class PrescriptionServiceImpl extends ServiceImpl<HosPrescriptionMapper, HosPrescription> implements PrescriptionService {
    @Resource
    private DictMapper dictMapper;
    @Resource
    private LogService logService;
    @Resource
    private DrugMapper drugMapper;
    @Resource
    private TBaseMapper tBaseMapper;
    @Resource
    private JudgeService judgeService;
    @Resource
    private CaProperties caProperties;
    @Resource
    private DiseaseMapper diseaseMapper;
    @Resource
    private MdtOrderMapper mdtOrderMapper;
    @Resource
    private HosVisitMapper hosVisitMapper;
    @Resource
    private HosRevisitMapper hosRevisitMapper;
    @Resource
    private DoctorUserMapper doctorUserMapper;
    @Resource
    private PrescriptionDrugService prescriptionDrugService;
    @Resource
    private HosPrescriptionDrugMapper hosPrescriptionDrugMapper;
    @Resource
    private PrescriptionDiseaseService prescriptionDiseaseService;
    @Resource
    private HosPrescriptionErrorMapper hosPrescriptionErrorMapper;
    @Resource
    private PrescriptionDrugErrorService prescriptionDrugErrorService;
    @Resource
    private PrescriptionDiseaseErrorService prescriptionDiseaseErrorService;
    @Resource
    private DrugstoreService drugstoreService;
    @Resource
    private PrescriptionFeeService prescriptionFeeService;
    @Resource
    private PrescriptionInstructionService prescriptionInstructionService;
    @Autowired
    private TencentImGroupUtil tencentImGroupUtil;
    @Autowired
    private IMService iMService;
    @Autowired
    private TencentProperties tencentProperties;

    @Override
    public List<Drug> drugList(Map<String, String> map) {
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
        return diseaseIPage.getRecords();
    }

    @Override
    public List<Disease> diseaseList(Map<String, Object> map) {
        int page = 1;
        int limit = 10;
        try {
            page = Integer.parseInt(String.valueOf(map.get("page")));
            limit = Integer.parseInt(String.valueOf(map.get("limit")));
        } catch (Exception e) {
            log.error("没有分页条件");
        }
        Page<Disease> ipage = new Page<>(page, limit);
        QueryWrapper<Disease> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", "0").orderByAsc("id");
        if (map.get("diseaseName") != null) {
            String diseaseName = String.valueOf(map.get("diseaseName"));
            wrapper.like("name", diseaseName);
        }
        IPage<Disease> diseaseIPage = diseaseMapper.selectPage(ipage, wrapper);
        return diseaseIPage.getRecords();
    }

    /**
     * 提交签名接口
     *
     * @param map preId 处方id
     * @return JsonResult
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult sign(Map<String, String> map) {
        String id = map.get("id");
        HosPrescription send = baseMapper.selectById(id);
        if (null != send.getDoctorSignet() && !"".equals(send.getDoctorSignet()) && StatusEnumUtil.SIGN.equals(send.getCheckStatus())) {
            return JsonResult.ok().put("data", send.getDoctorSignet());
        }
        HosPrescription hosPrescription = new HosPrescription();
        hosPrescription.setId(id);
        boolean sealUrl = getSealUrl(Objects.requireNonNull(doctorUserMapper.selectById(AuthUtil.getLoginUser().getId())).getPapersNumbers(), hosPrescription);
        if (sealUrl) {
            send.setCheckStatus(StatusEnumUtil.SIGN);
            send.setDoctorSignet(hosPrescription.getDoctorSignet());
            baseMapper.updateById(send);
            return JsonResult.ok().put("data", hosPrescription.getDoctorSignet());
        }
        return JsonResult.error("签名失败");
    }

    /**
     * preType仅支持：MDT、VISIT、REVISIT
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult savePrescription(Map<String, Object> map) {
        //订单查询
        String orderId = String.valueOf(map.get("orderId"));//id
        String type = map.get("type").toString();
        HosPrescription hosPrescription = new HosPrescription();
        String orderNum = null;
        String patientName = null;
        String loginDoctorUserId = Objects.requireNonNull(AuthUtil.getLoginUser()).getId();
        switch (type) {
            case "MDT":
                MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
                if (mdtOrder == null) {
                    log.error("对应id无会诊订单");
                    return JsonResult.error("对应id无会诊订单");
                }
                orderNum = mdtOrder.getOrderNum();
                patientName = mdtOrder.getName();
                hosPrescription.setVisitMdtNum(orderNum);
                hosPrescription.setPatientUserId(mdtOrder.getPatientUserId());
                hosPrescription.setHosSickId(mdtOrder.getHosSickId());
                hosPrescription.setDoctorUserId(loginDoctorUserId);
                hosPrescription.setName(mdtOrder.getName());
                hosPrescription.setIdCard(mdtOrder.getIdCard());
                hosPrescription.setSex(mdtOrder.getSex());
                hosPrescription.setAge(mdtOrder.getAge());
                hosPrescription.setMobile(mdtOrder.getMobile());
                List<DoctorUser> mdtDoctorUsers = doctorUserMapper.findTeamDoctorByMdtOrderNum("1", orderNum);
                String departName = null;
                if (null == mdtDoctorUsers || mdtDoctorUsers.size() == 0) {
                    List<DoctorUser> mdtDoctorUsersIsNotMain = doctorUserMapper.findTeamDoctorByMdtOrderNum("0", orderNum);
                    if (null == mdtDoctorUsersIsNotMain || mdtDoctorUsersIsNotMain.size() == 0) {
                        log.error("未查询到会诊医生");
                        return JsonResult.error("未查询到会诊医生");
                    } else {
                        departName = mdtDoctorUsersIsNotMain.get(0).getDepartName();
                    }
                } else {
                    departName = mdtDoctorUsers.get(0).getDepartName();
                }
                hosPrescription.setDepartName(departName);
                hosPrescription.setVisitMdtNum(mdtOrder.getOrderNum());
                break;
            case "VISIT":
                HosVisit hosVisit = hosVisitMapper.selectById(orderId);
                if (hosVisit == null) {
                    log.error("对应id无在线复诊订单");
                    return JsonResult.error("对应id无在线复诊订单");
                }
                orderNum = hosVisit.getOrderNum();
                patientName = hosVisit.getName();
                hosPrescription.setVisitMdtNum(orderNum);
                hosPrescription.setPatientUserId(hosVisit.getPatientUserId());
                hosPrescription.setHosSickId(hosVisit.getHosSickId());
                hosPrescription.setDoctorUserId(loginDoctorUserId);
                hosPrescription.setName(hosVisit.getName());
                hosPrescription.setIdCard(hosVisit.getIdCard());
                hosPrescription.setSex(hosVisit.getSex());
                hosPrescription.setAge(hosVisit.getAge());
                hosPrescription.setMobile(hosVisit.getMobile());
                DoctorUser visitDoctorUser = doctorUserMapper.selectById(hosVisit.getDoctorUserId());
                hosPrescription.setDepartName(visitDoctorUser.getDepartName());
                hosPrescription.setVisitMdtNum(hosVisit.getOrderNum());
                break;
            case "REVISIT":
                HosRevisit hosRevisit = hosRevisitMapper.selectById(orderId);
                if (hosRevisit == null) {
                    log.error("对应id无复诊订单");
                    return JsonResult.error("对应id无复诊订单");
                }
                orderNum = hosRevisit.getOrderNum();
                patientName = hosRevisit.getName();
                hosPrescription.setVisitMdtNum(orderNum);
                hosPrescription.setPatientUserId(hosRevisit.getPatientUserId());
                hosPrescription.setHosSickId(hosRevisit.getHosSickId());
                hosPrescription.setDoctorUserId(loginDoctorUserId);
                hosPrescription.setName(hosRevisit.getName());
                hosPrescription.setIdCard(hosRevisit.getIdCard());
                hosPrescription.setSex(hosRevisit.getSex());
                hosPrescription.setAge(hosRevisit.getAge());
                hosPrescription.setMobile(hosRevisit.getMobile());
                hosPrescription.setDepartName(hosRevisit.getDepartName());
                hosPrescription.setVisitMdtNum(hosRevisit.getOrderNum());
                break;
        }

        QueryWrapper<HosPrescription> hosPrescriptionQueryWrapper = new QueryWrapper<>();
        hosPrescriptionQueryWrapper.eq("visit_mdt_num", orderNum);
        HosPrescription old = baseMapper.selectOne(hosPrescriptionQueryWrapper);
        if (old != null) {
            String checkStatus = old.getCheckStatus();
//        WAIT-等待审核 PASS-审核通过 REJECT-审核驳回 NOTSIGN-未签名 SIGN-已签名
            if (!"REJECT".equals(checkStatus)) {
                log.error("该复诊订单已存在未审核驳回的处方单，创建处方单失败");
                return JsonResult.error("存在未驳回订单");
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
            return JsonResult.error("疾病code错误，创建处方单失败");
        }
        /*
         * 创建处方
         */
        //肾功能状况0-肝功能不全;2-严重肝功能不全
        String kidneyStatus = null == map.get("kidneyStatus") ? null : map.get("kidneyStatus").toString();
        String liverStatus = null == map.get("liverStatus") ? null : map.get("liverStatus").toString();
        String womanStatus = null == map.get("womanStatus") ? null : map.get("womanStatus").toString();
        String allegeName = null == map.get("allegeName") ? null : map.get("allegeName").toString();
        hosPrescription.setKidneyStatus(kidneyStatus);
        //肝功能状况0-肝功能不全;2-严重肝功能不全
        hosPrescription.setLiverStatus(liverStatus);
        //妊娠/哺乳	0-哺乳期;1-妊娠期;
        hosPrescription.setWomanStatus(womanStatus);
        //过敏源名称，如果有多个，用“；”隔开串起来。
        hosPrescription.setAllegeName(allegeName);

        hosPrescription.setType(type);
        hosPrescription.setOtherId(orderId);
        hosPrescription.setOrderNum("P" + OrderNoUtil.getOrderNo());
        hosPrescription.setPrescriptionNum(OrderNoUtil.getOrderNo());
        hosPrescription.setFeeType("SELF");
        DoctorUser loginUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
        assert loginUser != null;
        hosPrescription.setDepartCode(loginUser.getDepartCode());
        hosPrescription.setCheckStatus(StatusEnumUtil.NOTSIGN);
        hosPrescription.setDoctorSignet("");
        //药品查询[{"drugId":"7","freqMedCode":"Prn","routeAdmiCode":"922"}]
        List<Map> drugs = CastUtil.castList(map.get("drugs"), Map.class);
        double sum = 0d;
        for (Map drug : drugs) {
            String drugId = drug.get("drugId").toString();
            double number = Double.parseDouble(drug.get("number").toString());
            Drug selectDrug = drugMapper.selectById(drugId);
            if (selectDrug == null) {
                log.error("药品id错误，创建处方单失败");
                return JsonResult.error("药品id错误，创建处方单失败");
            }
            double price = Double.parseDouble(selectDrug.getPrice());
            sum += (price * number);
        }
        hosPrescription.setTotalPrice(String.valueOf(sum));
        if (null != old) {
            savePrescriptionError(old);
            hosPrescription.setId(old.getId());
            hosPrescription.setIsCanForce("0");
            baseMapper.updateById(hosPrescription);
        } else {
            baseMapper.insert(hosPrescription);
        }
        //获取处方订单id
        String hosPrescriptionId = hosPrescription.getId();
        //保存疾病
        saveDiseaseList(diseaseList, hosPrescriptionId);
        //保存药品
        saveDrugList(drugs, hosPrescriptionId);
        //保存日志
        logService.setPrescriptionLog(hosPrescription, StatusEnumUtil.NOTSIGN, loginUser.getName() + "医生为就诊人" + patientName + "创建处方待认证");
        return JsonResult.ok().putData(hosPrescription.getId());
    }


    /**
     * 第二次提交审核（修改认证状态为已认证）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult commitPrescription(Map<String, String> map) {
        String type = map.get("type");//id
        String id = map.get("id");//id
        String orderId = map.get("orderId");//id
        HosPrescription oldHosPrescription;
        if (StrUtil.isNotBlank(id)) {
            oldHosPrescription = baseMapper.selectById(id);
        } else if (StrUtil.isNotBlank(type) && StrUtil.isNotBlank(orderId)) {
            String orderNum = null;
            switch (type) {
                case "VISIT":
                    HosVisit hosVisit = hosVisitMapper.selectById(orderId);
                    if (null == hosVisit) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = hosVisit.getOrderNum();
                    break;
                case "REVISIT":
                    HosRevisit hosRevisit = hosRevisitMapper.selectById(orderId);
                    if (null == hosRevisit) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = hosRevisit.getOrderNum();
                    break;
                case "MDT":
                    MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
                    if (null == mdtOrder) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = mdtOrder.getOrderNum();
                    break;
            }
            LambdaQueryWrapper<HosPrescription> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(HosPrescription::getVisitMdtNum, orderNum);
            oldHosPrescription = baseMapper.selectOne(wrapper);
            id = oldHosPrescription.getId();
        } else {
            return JsonResult.error("参数错误");
        }
        if (oldHosPrescription == null) {
            return JsonResult.error("订单不存在");
        }
        String checkStatus = oldHosPrescription.getCheckStatus();
        if ("SIGN".equals(checkStatus)) {
            HosPrescription hosPrescription = new HosPrescription();
            hosPrescription.setId(id);
            hosPrescription.setCheckStatus("WAIT");
            int i = baseMapper.updateById(hosPrescription);
            if (i == i) {
                try {
                    judgeService.judge(hosPrescription.getId());
                } catch (Exception e) {
                    log.error("审方出错：", e);
                }
                return JsonResult.ok();
            }
        } else if ("REJECT".equals(checkStatus) && "0".equals(oldHosPrescription.getIsDisable())) {
            return JsonResult.error("存在禁忌药品，请重新开方");
        } else if ("REJECT".equals(checkStatus)) {
            try {
                HosPrescription update = new HosPrescription();
                update.setCheckStatus(StatusEnumUtil.WAIT);
                update.setId(id);
                update.setIsCanForce("1");
                baseMapper.updateById(update);
                judgeService.judge(id);
            } catch (Exception e) {
                log.error("审方出错：", e);
            }
            return JsonResult.ok("强制执行成功");
        } else {
            return JsonResult.error("订单状态错误");
        }
        return JsonResult.error("审方出错");
    }

    @Override
    public JsonResult prescriptionDetails(Map<String, String> map) {
        String type = map.get("type");//id
        String id = map.get("id");//id
        String orderId = map.get("orderId");//id
        HosPrescription hosPrescription;
        if (StrUtil.isNotBlank(id)) {
            hosPrescription = baseMapper.selectById(id);
        } else if (StrUtil.isNotBlank(type) && StrUtil.isNotBlank(orderId)) {
            String orderNum = null;
            switch (type) {
                case "VISIT":
                    HosVisit hosVisit = hosVisitMapper.selectById(orderId);
                    if (null == hosVisit) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = hosVisit.getOrderNum();
                    break;
                case "REVISIT":
                    HosRevisit hosRevisit = hosRevisitMapper.selectById(orderId);
                    if (null == hosRevisit) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = hosRevisit.getOrderNum();
                    break;
                case "MDT":
                    MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
                    if (null == mdtOrder) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = mdtOrder.getOrderNum();
                    break;
            }
            LambdaQueryWrapper<HosPrescription> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(HosPrescription::getVisitMdtNum, orderNum);
            hosPrescription = baseMapper.selectOne(wrapper);
        } else {
            return JsonResult.error("参数错误");
        }
        if (null == hosPrescription) {
            return JsonResult.error("未开处方");
        }
        String prescriptionId = hosPrescription.getId();
        QueryWrapper<HosPrescriptionDrug> hosPrescriptionDrugQueryWrapper = new QueryWrapper<>();
        hosPrescriptionDrugQueryWrapper.eq("hos_prescription_id", prescriptionId);
        List<HosPrescriptionDrug> list = prescriptionDrugService.list(hosPrescriptionDrugQueryWrapper);
        List<Object> drugs = new ArrayList<>();
        for (HosPrescriptionDrug drug : list) {
            HashMap<String, Object> drugMap = new HashMap<>();
            String drugId = drug.getDrugId();
            Drug selectById = drugMapper.selectById(drugId);
            drugMap.put("drugId", drugId);
            drugMap.put("name", drug.getName());
            drugMap.put("size", drug.getSize());
            drugMap.put("packUnitName", drug.getPackUnitName());
            drugMap.put("content", drug.getContent());
            drugMap.put("routeAdmiCode", drug.getRouteAdmiCode());
            drugMap.put("routeAdmiName", drug.getRouteAdmiName());
            drugMap.put("freqMedCode", drug.getFreqMedCode());
            drugMap.put("freqMedName", drug.getFreqMedName());
            drugMap.put("useDay", drug.getUseDay());
            drugMap.put("number", drug.getNumber());
            //单位
            String doseUnit = selectById.getDoseUnit();
            //用量
            drugMap.put("dosage", drug.getDosage().replaceAll(doseUnit, ""));
            drugMap.put("doseUnit", doseUnit);
            drugs.add(drugMap);
        }
        Map<Object, Object> result = new HashMap<>();
        result.put("id", prescriptionId);
        result.put("checkStatus", hosPrescription.getCheckStatus());
        result.put("checkStatusName", hosPrescription.getCheckStatusName());
        result.put("checkContent", hosPrescription.getCheckContent());
        result.put("manualCheckContent", hosPrescription.getManualCheckContent());
        result.put("doctorSignet", hosPrescription.getDoctorSignet());
        result.put("drugs", drugs);
        result.put("orderId", hosPrescription.getOtherId());
//        result.put("kidneyStatus", hosPrescription.getKidneyStatus());
//        result.put("liverStatus", hosPrescription.getLiverStatus());
//        result.put("womanStatus", hosPrescription.getWomanStatus());
//        result.put("allegeName", hosPrescription.getAllegeName());
        result.put("isDisable", hosPrescription.getIsDisable());
        result.put("taboos", hosPrescription.getTaboos());
        LambdaQueryWrapper<HosPrescriptionDisease> diseaseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        diseaseLambdaQueryWrapper.eq(HosPrescriptionDisease::getHosPrescriptionId, hosPrescription.getId());
        List<HosPrescriptionDisease> diseases = prescriptionDiseaseService.list(diseaseLambdaQueryWrapper);
        result.put("diseases", diseases);
        return JsonResult.ok().putData(result);
    }

    @Override
    public JsonResult getPrescriptionList(Map<String, String> map) {
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
        List<HosPrescription> hosPrescriptions = baseMapper.selectPage(ipage, lambda).getRecords();
        for (HosPrescription hosPrescription : hosPrescriptions) {
            LambdaQueryWrapper<HosPrescriptionDrug> drugLambdaQueryWrapper = new LambdaQueryWrapper<>();
            drugLambdaQueryWrapper.eq(HosPrescriptionDrug::getHosPrescriptionId, hosPrescription.getId());
            List<HosPrescriptionDrug> drugs = hosPrescriptionDrugMapper.selectList(drugLambdaQueryWrapper);
            String drugName = drugs.stream().map(HosPrescriptionDrug::getName).collect(Collectors.joining("、"));
            hosPrescription.setDrugName(drugName);
//            hosPrescription.setCheckStatusName(StatusEnumUtil.getPrescriptionCheckStatus(hosPrescription.getCheckStatus()));
        }
        return JsonResult.ok().putData(hosPrescriptions);
    }

    @Override
    public JsonResult taboo() {
        TBase prescription_taboo = tBaseMapper.selectByKey("PRESCRIPTION_TABOO");
        return JsonResult.ok().putData(JSON.parseObject(prescription_taboo.getContent(), List.class));
    }

    /**
     * 查询处方审核状态
     *
     * @param map 复诊订单id
     * @return
     */
    @Override
    public JsonResult getPrescriptionStatus(Map<String, String> map) {
        String type = map.get("type");//id
        String id = map.get("id");//id
        String orderId = map.get("orderId");//id
        HosPrescription hosPrescription;
        if (StrUtil.isNotBlank(id)) {
            hosPrescription = baseMapper.selectById(id);
        } else if (StrUtil.isNotBlank(type) && StrUtil.isNotBlank(orderId)) {
            String orderNum = null;
            switch (type) {
                case "VISIT":
                    HosVisit hosVisit = hosVisitMapper.selectById(orderId);
                    if (null == hosVisit) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = hosVisit.getOrderNum();
                    break;
                case "REVISIT":
                    HosRevisit hosRevisit = hosRevisitMapper.selectById(orderId);
                    if (null == hosRevisit) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = hosRevisit.getOrderNum();
                    break;
                case "MDT":
                    MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
                    if (null == mdtOrder) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = mdtOrder.getOrderNum();
                    break;
            }
            LambdaQueryWrapper<HosPrescription> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(HosPrescription::getVisitMdtNum, orderNum);
            hosPrescription = baseMapper.selectOne(wrapper);
        } else {
            return JsonResult.error("参数错误");
        }
        Map<String, String> result = new HashMap<>();
        result.put("content", "");
        result.put("status", "");
        result.put("statusName", "");
        result.put("doctorSignet", "");
        result.put("isDisable", "");
        if (hosPrescription == null) {
            log.error("用接收到的处方ID未查询的相关的处方");
            return JsonResult.ok();
        } else {
//             WAIT-等待审核 PASS-审核通过 REJECT-审核驳回
            String checkStatus = hosPrescription.getCheckStatus();
            result.put("status", checkStatus);
            result.put("statusName", hosPrescription.getCheckStatusName());
            result.put("prescribeId", hosPrescription.getId());
            if ("REJECT".equals(checkStatus)) {
                result.put("content", hosPrescription.getCheckContent());
                result.put("isDisable", hosPrescription.getIsDisable());
                log.info("处方订单状态为审核驳回");
            } else if (StatusEnumUtil.SIGN.equals(checkStatus)) {
                result.put("doctorSignet", hosPrescription.getDoctorSignet());
            }
            return JsonResult.ok().putData(result);
        }
    }


    @Override
    public Map<String, Object> getPrescription(Map<String, String> map) {
        String orderId = map.get("orderId");
        String id = map.get("id");//id
        String type = map.get("type");
        HosPrescription hosPrescription = null;
        if (StrUtil.isNotBlank(id)) {
            hosPrescription = baseMapper.selectById(id);
        } else if (StrUtil.isNotBlank(type) && StrUtil.isNotBlank(orderId)) {
            String orderNum = null;
            switch (type) {
                case "VISIT":
                    HosVisit hosVisit = hosVisitMapper.selectById(orderId);
                    if (null == hosVisit) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = hosVisit.getOrderNum();
                    break;
                case "REVISIT":
                    HosRevisit hosRevisit = hosRevisitMapper.selectById(orderId);
                    if (null == hosRevisit) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = hosRevisit.getOrderNum();
                    break;
                case "MDT":
                    MdtOrder mdtOrder = mdtOrderMapper.selectById(orderId);
                    if (null == mdtOrder) {
                        return JsonResult.error("订单不存在");
                    }
                    orderNum = mdtOrder.getOrderNum();
                    break;
            }
            LambdaQueryWrapper<HosPrescription> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(HosPrescription::getVisitMdtNum, orderNum);
            hosPrescription = baseMapper.selectOne(wrapper);
        } else {
            return JsonResult.error("参数错误");
        }
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
        result.put("orderId", orderId);

        //编号：A20200197355445
        //临床诊断：病情稳定

        result.put("createTime", hosPrescription.getCreateTime().substring(0, 10));
        result.put("departName", hosPrescription.getDepartName());
        result.put("sickName", hosPrescription.getName());
        result.put("age", hosPrescription.getAge());
        result.put("sex", hosPrescription.getSex().equals("0") ? "女" : "男");
//        result.put("feeType", hosPrescription.getFeeType().equals("SELF") ? "自费" : "自费");
        result.put("feeType", "自费");
        result.put("mobile", hosPrescription.getMobile());
        result.put("medicalCertificate", hosPrescription.getMedicalCertificate());
        result.put("prescriptionNum", hosPrescription.getOrderNum());
        result.put("checkPharmaceutist", hosPrescription.getCheckPharmaceutist());
        result.put("manualCheckPharmaceutist", hosPrescription.getManualCheckPharmaceutist());
        result.put("manualCheckPharmaceutistId", hosPrescription.getManualCheckPharmaceutistId());
        result.put("manualCheckTime", hosPrescription.getManualCheckTime());
        result.put("manualCheckContent", hosPrescription.getManualCheckContent());
        result.put("checkStatusName", hosPrescription.getCheckStatusName());
        result.put("isDisable", hosPrescription.getIsDisable());
        result.put("checkContent", hosPrescription.getCheckContent());
        return result;
    }

    private void saveDiseaseList(List<Disease> diseaseList, String hosPrescriptionId) {
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
        baseMapper.updateById(update);
        prescriptionDiseaseService.saveBatch(hosPrescriptionDiseases);
    }

    /**
     * 重新提交处方时保存到驳回表
     *
     * @param oldHosPrescription HosPrescription
     */
    private void savePrescriptionError(HosPrescription oldHosPrescription) {
        HosPrescriptionError hosPrescriptionError = new HosPrescriptionError();
        BeanUtil.copyProperties(oldHosPrescription, hosPrescriptionError, "id");
        LambdaQueryWrapper<HosPrescriptionDisease> lambda = new QueryWrapper<HosPrescriptionDisease>().lambda();
        String oldId = oldHosPrescription.getId();
        lambda.eq(HosPrescriptionDisease::getHosPrescriptionId, oldId);
        List<HosPrescriptionDisease> list = prescriptionDiseaseService.list(lambda);
        ArrayList<HosPrescriptionDiseaseError> hosPrescriptionDiseaseErrors = new ArrayList<>();
        for (HosPrescriptionDisease disease : list) {
            HosPrescriptionDiseaseError hosPrescriptionDiseaseError = new HosPrescriptionDiseaseError();
            BeanUtil.copyProperties(disease, hosPrescriptionDiseaseError, "id");
            hosPrescriptionDiseaseErrors.add(hosPrescriptionDiseaseError);
            prescriptionDiseaseService.removeById(disease.getId());
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
        hosPrescriptionErrorMapper.insert(hosPrescriptionError);
        prescriptionDiseaseErrorService.saveBatch(hosPrescriptionDiseaseErrors);
        prescriptionDrugErrorService.saveBatch(hosPrescriptionDrugErrors);
    }

    /**
     * 开处方保存药品
     *
     * @param drugs             药品列表
     * @param hosPrescriptionId 处方单id
     */
    private void saveDrugList(List<Map> drugs, String hosPrescriptionId) {
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
     * 调用第三方接口获取签章并存入数据库
     *
     * @param id              唯一键
     * @param hosPrescription 处方信息
     * @return 是否签名成功
     */
    public boolean getSealUrl(String id, HosPrescription hosPrescription) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("appId", caProperties.getAppId());
            String msg = "appId=" + caProperties.getAppId() + "&id=" + id;
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public long countByDoctorUserId(String doctorUserId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<HosPrescription>()
                .eq(HosPrescription::getDoctorUserId, doctorUserId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RecentPreVo savePrescription(SavePrescriptionRequest request) {
        String id = request.getId();
        HosPrescription hosPrescription = getById(id);
        if (hosPrescription == null) {
            request.setId(null);
            hosPrescription = new HosPrescription();
        }
        BeanUtil.copyProperties(request, hosPrescription);
        saveOrUpdate(hosPrescription);

        // 更新药品信息
        prescriptionDrugService.remove(new LambdaQueryWrapper<HosPrescriptionDrug>()
                .eq(HosPrescriptionDrug::getHosPrescriptionId, hosPrescription.getId()));
        List<PrescriptionDrugDTO> drugList = request.getDrugList();
        HosPrescription finalHosPrescription = hosPrescription;
        List<HosPrescriptionDrug> hosPrescriptionDrugList = drugList.stream().map(drug -> {
            HosPrescriptionDrug prescriptionDrug = new HosPrescriptionDrug();
            BeanUtil.copyProperties(drug, prescriptionDrug);
            prescriptionDrug.setHosPrescriptionId(finalHosPrescription.getId());
            return prescriptionDrug;
        }).toList();
        prescriptionDrugService.saveBatch(hosPrescriptionDrugList);

        // 更新用法
        PrescriptionInstructionDTO instruction = request.getInstruction();
        HosPrescriptionInstruction prescriptionInstruction = null;
        if (instruction != null) {
            prescriptionInstruction = prescriptionInstructionService.getOne(new LambdaQueryWrapper<HosPrescriptionInstruction>()
                    .eq(HosPrescriptionInstruction::getHosPrescriptionId, hosPrescription.getId()));
            if (prescriptionInstruction == null) prescriptionInstruction = new HosPrescriptionInstruction();
            BeanUtil.copyProperties(instruction, prescriptionInstruction);
            prescriptionInstruction.setHosPrescriptionId(hosPrescription.getId());
            prescriptionInstructionService.saveOrUpdate(prescriptionInstruction);
        }

        // 更新诊金
        PrescriptionConsultationFeeDTO consultationFee = request.getConsultationFee();
        HosPrescriptionFee prescriptionFee = null;
        if (consultationFee != null) {
            prescriptionFee = prescriptionFeeService.getOne(new LambdaQueryWrapper<HosPrescriptionFee>()
                    .eq(HosPrescriptionFee::getHosPrescriptionId, hosPrescription.getId()));
            if (prescriptionFee == null) prescriptionFee = new HosPrescriptionFee();
            BeanUtil.copyProperties(consultationFee, prescriptionFee);
            prescriptionFee.setHosPrescriptionId(hosPrescription.getId());
            prescriptionFeeService.saveOrUpdate(prescriptionFee);
        }
        try {
            log.info("处方创建成功，开始发送消息");
            //先发送系统消息
            ImSendGroupMsgRequest systemMsgRequest = buildSystemMsg(request);
            boolean systemMsg = iMService.sendGroupMsg(systemMsgRequest);
            log.info("系统消息消息发送结果:{},参数值:{}", systemMsg, JSONObject.toJSONString(systemMsgRequest));
            //在发送支付小卡片消息
            ImSendGroupMsgRequest msgRequest = buildSavePrescriptionMsg(request, hosPrescription);
            boolean msg = iMService.sendGroupMsg(msgRequest);
            log.info("小卡片消息发送结果:{},参数值:{}", msg, JSONObject.toJSONString(msgRequest));
        } catch (Exception e) {
            log.info("异常:", e);
        }

        return convert2Vo(hosPrescription, drugList, prescriptionInstruction, prescriptionFee);
    }

    private ImSendGroupMsgRequest buildSystemMsg(SavePrescriptionRequest request) {
        ImSendGroupMsgRequest msgRequest = new ImSendGroupMsgRequest();
        msgRequest.setGroupId("GROUP_" + request.getVisitMdtNum());
        msgRequest.setFromAccount(tencentProperties.getAdminidentifier());
        msgRequest.setToAccount("PATIENT_" + request.getPatientUserId());
        msgRequest.setVisitNo(request.getVisitMdtNum());
        List<ImSendGroupMsgRequest.MessageBody> bodys = Lists.newArrayList();
        ImSendGroupMsgRequest.MessageBody body = new ImSendGroupMsgRequest.MessageBody();
        body.setMsgType("TIMTextElem");
        ImSendGroupMsgRequest.MsgParam msgParam = new ImSendGroupMsgRequest.MsgParam();
        msgParam.setText("系统消息：点击卡片查看处方详情并进行药品购买，建议根据处方按时吃药");
        body.setMsgContent(msgParam);
        bodys.add(body);
        msgRequest.setMsgBody(bodys);
        msgRequest.setMsgType(2);
        return msgRequest;
    }

    private ImSendGroupMsgRequest buildSavePrescriptionMsg(SavePrescriptionRequest request, HosPrescription hosPrescription) {
        ImSendGroupMsgRequest msgRequest = new ImSendGroupMsgRequest();
        msgRequest.setGroupId("GROUP_" + request.getVisitMdtNum());
        msgRequest.setFromAccount("DOCTOR_" + request.getDoctorUserId());
        msgRequest.setVisitNo(request.getVisitMdtNum());
        List<ImSendGroupMsgRequest.MessageBody> bodys = Lists.newArrayList();
        ImSendGroupMsgRequest.MessageBody body = new ImSendGroupMsgRequest.MessageBody();
        body.setMsgType("TIMCustomElem");
        ImSendGroupMsgRequest.MsgParam msgParam = new ImSendGroupMsgRequest.MsgParam();
        Map<String, Object> data = Maps.newHashMap();
        data.put("medicineStatusCode", switch (request.getMedicineStatusCode()) {
            case "1" -> "中药饮片-自煎";
            case "2" -> "中药饮片-代煎";
            case "3" -> "颗粒";
            default -> "";
        });
        data.put("type", "savePrescription");
        data.put("createTime", hosPrescription.getCreateTime());
        data.put("sickName", hosPrescription.getName());
        data.put("sex", hosPrescription.getSex());
        data.put("age", hosPrescription.getAge());
        data.put("disease", hosPrescription.getDisease());
        data.put("syndrome", hosPrescription.getSyndrome());
        data.put("hosPrescriptionId", hosPrescription.getId());


        Map<String, Object> bigBody = Maps.newHashMap();
        bigBody.put("type", "savePrescription");
        bigBody.put("desc", "处方小卡片");
        bigBody.put("data", data);

        msgParam.setData(JSONObject.toJSONString(bigBody));
        body.setMsgContent(msgParam);
        bodys.add(body);
        msgRequest.setMsgBody(bodys);
        msgRequest.setLoadParam(1);
        msgRequest.setMsgType(1);
        return msgRequest;
    }

    @Override
    public RecentPreVo getRecentPre(SearchRecentPreRequest request) {
        HosPrescription hosPrescription = getOne(new LambdaQueryWrapper<HosPrescription>()
                .eq(HosPrescription::getHosSickId, request.getHosSickId())
                .eq(HosPrescription::getDoctorUserId, request.getDoctorUserId())
                .orderByDesc(HosPrescription::getCreateTime)
                .last("limit 1"));
        return getRecentPreByEntity(hosPrescription);
    }

    @Override
    public IPage<HosPrescription> getHosPrescriptionList(PrescriptionQueryRequest request) {
        return this.baseMapper.selectPage(new Page<>(request.getPage(), request.getLimit()),
                new LambdaQueryWrapper<HosPrescription>()
                        .eq(HosPrescription::getDoctorUserId, AuthUtil.getLoginUserId())
                        .orderByDesc(HosPrescription::getCreateTime));
    }

    @Override
    public RecentPreVo getRecentPreById(Long id) {
        HosPrescription hosPrescription = getById(id);
        return getRecentPreByEntity(hosPrescription);
    }

    @Override
    public Boolean withdrawPrescription(WithdrawPrescriptionRequest req) {
        HosPrescription prescription = getById(req.getPrescriptionId());
        if (prescription == null) {
            log.info("没有查询到该处方,参数:{}", req.getPrescriptionId());
            return Boolean.FALSE;
        }

        if (!Integer.valueOf(prescription.getDoctorUserId()).equals(AuthUtil.getLoginUserId())) {
            log.info("该处方此医生没有操作权限,参数:{},医生:{}", req.getPrescriptionId(), AuthUtil.getLoginUserId());
            return Boolean.FALSE;
        }
        prescription.setStatus("WITHDRAW");
        return updateById(prescription);
    }

    public RecentPreVo getRecentPreByEntity(HosPrescription hosPrescription) {
        if (hosPrescription == null) {
            return null;
        }
        List<PrescriptionDrugDTO> drugList = prescriptionDrugService.list(new LambdaQueryWrapper<HosPrescriptionDrug>()
                        .eq(HosPrescriptionDrug::getHosPrescriptionId, hosPrescription.getId())
                )
                .stream()
                .map(prescriptionDrug -> {
                    PrescriptionDrugDTO dto = new PrescriptionDrugDTO();
                    BeanUtil.copyProperties(prescriptionDrug, dto);
                    return dto;
                })
                .toList();

        HosPrescriptionInstruction prescriptionInstruction = prescriptionInstructionService.getOne(new LambdaQueryWrapper<HosPrescriptionInstruction>()
                .eq(HosPrescriptionInstruction::getHosPrescriptionId, hosPrescription.getId()));
        HosPrescriptionFee prescriptionFee = prescriptionFeeService.getOne(new LambdaQueryWrapper<HosPrescriptionFee>()
                .eq(HosPrescriptionFee::getHosPrescriptionId, hosPrescription.getId()));
        return convert2Vo(hosPrescription, drugList, prescriptionInstruction, prescriptionFee);
    }

    private RecentPreVo convert2Vo(HosPrescription hosPrescription, List<PrescriptionDrugDTO> drugList,
                                   HosPrescriptionInstruction prescriptionInstruction, HosPrescriptionFee prescriptionFee) {
        RecentPreVo recentPreVo = new RecentPreVo();
        BeanUtil.copyProperties(hosPrescription, recentPreVo);
        if (AuthUtil.getLoginUser() != null) {
            recentPreVo.setDoctorUserName(AuthUtil.getLoginUser().getName());
        }

        Drugstore drugstore = drugstoreService.getById(hosPrescription.getDrugstoreId());
        if (drugstore != null) {
//            recentPreVo.setDrugstoreProvincesCode(drugstore.getProvincesCode());
//            recentPreVo.setDrugstoreCityCode(drugstore.getCityCode());
            recentPreVo.setDrugList(drugList);
            recentPreVo.setDrugstoreName(drugstore.getName());
            recentPreVo.setDrugstoreImage(drugstore.getImage());
        }

        if (prescriptionInstruction != null) {
            PrescriptionInstructionDTO instruction = new PrescriptionInstructionDTO();
            BeanUtil.copyProperties(prescriptionInstruction, instruction);
            recentPreVo.setInstruction(instruction);
        }

        if (prescriptionFee != null) {
            PrescriptionConsultationFeeDTO consultationFee = new PrescriptionConsultationFeeDTO();
            BeanUtil.copyProperties(prescriptionFee, consultationFee);
            recentPreVo.setConsultationFee(consultationFee);
        }

        return recentPreVo;
    }
}
