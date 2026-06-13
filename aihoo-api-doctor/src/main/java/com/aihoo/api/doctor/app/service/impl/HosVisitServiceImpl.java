package com.aihoo.api.doctor.app.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aihoo.oss.OssComponent;
import com.aihoo.util.SmsModelSendUtil;
import com.aihoo.util.StringUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.visit.model.mapper.HosVisitVoMapper;
import com.aihoo.domain.visit.model.mapper.HosVisitMapper;
import com.aihoo.domain.visit.model.mapper.HosVisitImgMapper;
import com.aihoo.domain.payment.model.mapper.OrderBackPayMapper;
import com.aihoo.domain.payment.model.mapper.OrderMapper;

import com.aihoo.domain.visit.dto.HosVisitBaseInfoDTO;
import com.aihoo.api.doctor.app.service.HosSickHealthRecordsService;
import com.aihoo.api.doctor.app.service.HosVisitService;
import com.aihoo.api.doctor.app.service.IMService;
import com.aihoo.api.doctor.app.service.LogService;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.api.doctor.common.utils.AuthUtil;
import com.aihoo.api.doctor.common.utils.IMMsgType;
import com.aihoo.util.TimeUtil;
import com.aihoo.api.doctor.common.utils.VisitStatusUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aihoo.domain.visit.model.mapper.HosSickMapper;
import com.aihoo.domain.im.model.mapper.ImMsgMapper;
import com.aihoo.domain.patient.model.mapper.PatientUserMapper;
import com.aihoo.domain.sys.model.mapper.TBaseMapper;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.entity.HosVisitImg;
import com.aihoo.domain.visit.model.entity.HosSickHealthRecords;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.visit.model.vo.HosVisitSelectVo;
import com.aihoo.domain.patient.model.entity.PatientUser;
import com.aihoo.domain.im.model.entity.ImMsg;
import com.aihoo.domain.sys.model.entity.TBase;
import com.aihoo.domain.payment.model.entity.Order;
import com.aihoo.domain.payment.model.entity.OrderBackPay;
import com.aihoo.api.doctor.app.controller.vo.HosOrder;
import com.aihoo.api.doctor.app.controller.vo.OrderResult;
import com.aihoo.api.doctor.app.controller.vo.HosVisitHealthInfoVo;
import com.aihoo.api.doctor.app.controller.vo.HosVisitBaseInfoVo;

import java.util.*;

/**
 * <p>
 * 在线问诊信息表 服务实现类
 * </p>
 *
 * @author mcp
 * @since 2020-09-18
 */
@Service("doctorApiHosVisitServiceImpl")
public class HosVisitServiceImpl extends ServiceImpl<HosVisitMapper, HosVisit> implements HosVisitService {
    @Resource
    private SmsModelSendUtil smsModelSendUtil;
    @Resource
    private HosVisitMapper hosVisitMapper;
    @Resource
    private HosVisitVoMapper hosVisitVoMapper;
    @Resource
    private OrderBackPayMapper orderBackPayMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private HosVisitImgMapper hosVisitImgMapper;
    @Resource
    private PatientUserMapper patientUserMapper;
    @Resource
    private TBaseMapper tBaseMapper;
    @Resource
    private LogService logService;
    @Resource
    private HosSickMapper hosSickMapper;
    @Resource
    private IMService imService;
    @Resource
    private DoctorUserMapper doctorUserMapper;

    // 患者
    private static final String PATIENT = "PATIENT_";

    // 医生
    private static final String DOCTOR = "DOCTOR_";
    @Autowired
    private HosSickHealthRecordsService hosSickHealthRecordsService;
    @Autowired
    private OssComponent ossComponent;
    @Autowired
    private ImMsgMapper imMsgMapper;

    /**
     * 医生-》问诊-》拒单
     *
     * @param map id：订单id     msg：取消说明
     * @return
     */
/*    @Override
    @Transactional(rollbackFor = Exception.class)
    public String refuseVisit(Map<String, String> map) {
        String id = map.get("id");
        String msg = map.get("msg");
        HosVisit hosVisit = hosVisitMapper.selectById(id);
        hosVisit.setStatus(StatusEnumUtil.DECLINE);
        hosVisit.setMsg(msg);
        hosVisitMapper.updateById(hosVisit);
        logService.setVisitLog(hosVisit, StatusEnumUtil.DECLINE, logService.getDoctorUserName() + "医生拒单，被拒单患者：" + logService.getSickName(hosVisit.getHosSickId()));
        String orderNum = hosVisit.getOrderNum();
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_num", orderNum);
        Order order = orderMapper.selectOne(orderQueryWrapper);
        OrderBackPay orderBackPay = new OrderBackPay();
        orderBackPay.setType("DECLINE_ORDER");//类型：申请退款（RETURN_GOODS），取消订单（CANCEL_ORDER），拒绝订单（DECLINE_ORDER）
        orderBackPay.setPayType(order.getPayType());
        orderBackPay.setOrderId(order.getId());
        orderBackPay.setOutTradeNo(order.getPayOrderNum());
        orderBackPay.setOutRefundNo(ToolUtils.getUuid());
        orderBackPay.setTotalPrice(order.getTotalPrice());
        orderBackPay.setAmount(order.getTotalPrice());
        orderBackPay.setStatus("WAIT");//状态（WAIT进行中，PASS成功）
        orderBackPayMapper.insert(orderBackPay);//无第三方编号
        return "拒单成功";
    }*/

    /**
     * 订单列表
     *
     * @return
     */
    @Override
    public List visitOrderList(Map<String, String> map) {
        String status = map.get("status");
        String sickId = map.get("sickId");//就诊人id
        if (status == null) {
            status = "";
        }
        List<OrderResult> returnVisitArrayList = new ArrayList<>();
        DoctorUser loginUser = AuthUtil.getLoginUser();
        String id = loginUser.getId();
        LambdaQueryWrapper<HosVisit> hosVisitQueryWrapper = new QueryWrapper<HosVisit>().lambda();
        hosVisitQueryWrapper.select().eq(HosVisit::getDoctorUserId, id).orderByDesc(HosVisit::getCreateTime);
        if (StrUtil.isNotBlank(sickId)) {
            hosVisitQueryWrapper.eq(HosVisit::getHosSickId, sickId);
        }
        if ("SUCCESS".equals(status.toUpperCase())) {
            hosVisitQueryWrapper.select().and(wrapper -> wrapper.eq(HosVisit::getStatus, StatusEnumUtil.END));
            /*.eq("status", "DECLINE")*/
            ;
        } else if ("WAIT".equals(status.toUpperCase())) {
            hosVisitQueryWrapper.select().and(wrapper -> wrapper.eq(HosVisit::getStatus, StatusEnumUtil.PAY).or()
                    .eq(HosVisit::getStatus, StatusEnumUtil.HAVE).or().eq(HosVisit::getStatus, StatusEnumUtil.START));
        } else {
            hosVisitQueryWrapper.select().and(wrapper -> wrapper.eq(HosVisit::getStatus, StatusEnumUtil.PAY).or()
                    .eq(HosVisit::getStatus, StatusEnumUtil.HAVE).or().eq(HosVisit::getStatus, StatusEnumUtil.END).or()
                    .eq(HosVisit::getStatus, StatusEnumUtil.START));
            /*.eq("status", "DECLINE")*/
            ;
        }
        Integer page;
        Integer limit;
        try {
            page = Integer.parseInt(map.get("page"));
            limit = Integer.parseInt(map.get("limit"));
        } catch (Exception e) {
            page = 1;
            limit = 10;
            e.printStackTrace();
        }
        Page<HosVisit> hosVisitPage = new Page<>(page, limit);
        IPage<HosVisit> hosVisitIPage = hosVisitMapper.selectPage(hosVisitPage, hosVisitQueryWrapper);
        List<HosVisit> records = hosVisitIPage.getRecords();
        for (HosVisit hosVisit : records) {
            OrderResult orderResult = new OrderResult();
            orderResult.setOrderId(hosVisit.getId());
            orderResult.setDate(hosVisit.getCreateTime());
            orderResult.setName(hosVisit.getName());
            orderResult.setType(hosVisit.getType());
            orderResult.setContent(hosVisit.getContent());
            orderResult.setStatus(hosVisit.getStatus());
            orderResult.setStatusName("在线复诊" + StatusEnumUtil.getStatus(hosVisit.getStatus()));
//            orderResult.setStatusName("专家咨询" + StatusEnumUtil.getStatus(hosVisit.getStatus()));
            orderResult.setTotalPrice(hosVisit.getTotalPrice());
            orderResult.makeVisitbtnJson();
            orderResult.setPatientId(hosVisit.getPatientUserId());
            orderResult.setCreateTime(hosVisit.getCreateTime());
            orderResult.setEndTime(hosVisit.getEndTime());
            returnVisitArrayList.add(orderResult);
        }
        return returnVisitArrayList;
    }

    /**
     * 问诊详情
     *
     * @param map
     * @return
     */
    @Override
    public HosOrder visitData(String id) {
        HosOrder hosOrder = new HosOrder();
        HosVisit hosVisit = hosVisitMapper.selectById(id);
        if (StringUtils.isNotEmpty(hosVisit.getHosSickId())) {
            HosSickHealthRecords record = hosSickHealthRecordsService.getOne(new LambdaQueryWrapper<HosSickHealthRecords>()
                    .eq(HosSickHealthRecords::getHosSickId, hosVisit.getHosSickId())
                    .last("limit 1"));
            if (record != null) {
                hosOrder.setAreaCode(record.getArea());
                hosOrder.setAreaName(record.getAreaName());
            }
            hosOrder.setHosSickId(hosVisit.getHosSickId());
        }

        hosOrder.setDoctorId(hosVisit.getDoctorUserId());
        hosOrder.setSex(hosVisit.getSex());
        hosOrder.setAge(hosVisit.getAge());
        hosOrder.setName(hosVisit.getName());
        String payTime = hosVisit.getPayTime();
        hosOrder.setPayTime(payTime);
        hosOrder.setContent(hosVisit.getContent());
        hosOrder.setOrderNum(hosVisit.getOrderNum());
        hosOrder.setCreateTime(hosVisit.getCreateTime());
        hosOrder.setPatientId(hosVisit.getPatientUserId());
        hosOrder.setType(hosVisit.getType());
        hosOrder.setFirstVisit(hosVisit.getFirstVisit());
        hosOrder.setDoctorAdvice(hosVisit.getDoctorAdvice());
        hosOrder.setOrderId(hosVisit.getId());
        //获取患者获取头像
        PatientUser patientUser = patientUserMapper.selectById(hosVisit.getPatientUserId());
        //获取附件图片
        hosOrder.setHeadImg(patientUser.getHeadImg());
        List<HosVisitImg> hosVisitList = hosVisitImgMapper.selectByHosVisitId(id);
        List<Object> imgList = new ArrayList<>();
        for (HosVisitImg hosVisitImg : hosVisitList) {
            imgList.add(hosVisitImg.getImg());
        }
        hosOrder.setImgList(imgList);
//        hosOrder.setStatusName(StatusEnumUtil.getStatus(hosVisit.getStatus()));
//        hosOrder.setStatusName("问诊" + hosOrder.getStatusName());
        //状态
        String status = hosVisit.getStatus();
        hosOrder.setStatus(status);
        hosOrder.setStatusName("在线复诊" + StatusEnumUtil.getStatus(status));
//        hosOrder.setStatusName("专家咨询" + StatusEnumUtil.getStatus(status));
        // PAY 未接单（根据支付时间保持24小时）：剩余21小时15分自动结束结束复诊
        // HAVE 已接单，未到预约时间：（NULL）
        // START 已接单，到预约时间（根据接单时间往后推24小时关闭）：剩余接诊时间：21小时5分
        // END 已完成：患者评价：5.0
        //支付时间payTime
        //预约时间yyyy-MM-dd HH:mm:ss
        Date date = new Date();
        if (status.equals(StatusEnumUtil.PAY)) {
            Date payTimeParse = DateUtil.parse(payTime, "yyyy-MM-dd HH:mm:ss");
            TBase tBase = tBaseMapper.selectByKey("VISIT_TIMES");
            String content = tBase.getContent();
            String betweenTime = TimeUtil.getBetweenTime(date, payTimeParse, Integer.parseInt(content));
            hosOrder.setCountDown(betweenTime);
        } else if (status.equals(StatusEnumUtil.HAVE) || status.equals(StatusEnumUtil.START)) {
            String haveTime = hosVisit.getHaveTime();
            Date haveTimeParse = DateUtil.parse(haveTime, "yyyy-MM-dd HH:mm:ss");
            TBase tBase = tBaseMapper.selectByKey("DOCTOR_VISIT_TIMES");
            String content = tBase.getContent();
            String betweenTime = TimeUtil.getBetweenTime(date, haveTimeParse, Integer.parseInt(content));
            hosOrder.setCountDown(betweenTime);
        } else if (status.equals(StatusEnumUtil.END)) {
            hosOrder.setEndTime(hosVisit.getEndTime());
            hosOrder.setFiveStar(StringUtil.isBlank(hosVisit.getFiveStar()) ? "" : hosVisit.getFiveStar());
        }
        hosOrder.makeVisitbtnJson();
        return hosOrder;
    }


    /**
     * 问诊接单，不记录开始聊天时间
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String haveVisit(Map<String, String> map) {
        String id = map.get("id");
        HosVisit visit = hosVisitMapper.selectById(id);
        if (visit.getStatus().equals(StatusEnumUtil.PAY)) {
            LambdaQueryWrapper<HosVisit> lambda = new QueryWrapper<HosVisit>().lambda();
            HosVisit hosVisit = new HosVisit();
            lambda.eq(HosVisit::getId, id);
            hosVisit.setHaveTime(DateUtil.now());
            hosVisit.setStatus(StatusEnumUtil.HAVE);
            int i = hosVisitMapper.update(hosVisit, lambda);
            if (i == 0) {
                return "ERROR";
            }
            logService.setVisitLog(visit, StatusEnumUtil.HAVE, "医生" + logService.getDoctorUserName() + "接单患者" + logService.getSickName(visit.getHosSickId()) + "的复诊订单");
            QueryWrapper<HosVisitImg> hosVisitImgQueryWrapper = new QueryWrapper<>();
            hosVisitImgQueryWrapper.eq("hos_visit_id", id);
            List<HosVisitImg> hosVisitImgs = hosVisitImgMapper.selectList(hosVisitImgQueryWrapper);
            List<String> imgList = new ArrayList<>();
            for (HosVisitImg hosVisitImg : hosVisitImgs) {
                imgList.add(hosVisitImg.getImg());
            }
            visit.setImgList(imgList);
            visit.setMsgType(IMMsgType.VisitOrderCard);
            visit.setBusinessID("MSHCustomMsg");
            String desc = visit.getName() + "," + (org.apache.commons.lang3.StringUtils.isNotEmpty(visit.getSex()) ? ("0".equals(visit.getSex()) ? "女" : "1".equals(visit.getStatus()) ? "男" : "未知") : "未知") + "," + visit.getAge() + "岁";
            String doctorUserId = visit.getDoctorUserId();
            imService.sendPostHttpRequest(PATIENT + visit.getPatientUserId(), DOCTOR + doctorUserId, visit, desc, "", "");
            DoctorUser doctorUser = doctorUserMapper.selectById(doctorUserId);
            smsModelSendUtil.visitStartRemindPatient(visit.getMobile(), doctorUser.getName() + "医生");
            return "接单成功";
        }
        return "ERROR";
    }

    @Override
    public String writeVisitResult(Map<String, String> map) {
        String id = map.get("id");//id
//        String firstVisit = map.get("firstVisit");//初步诊断
        String doctorAdvice = map.get("doctorAdvice");//医嘱
        HosVisit hosVisit = new HosVisit();
        hosVisit.setId(id);
//        hosVisit.setFirstVisit(firstVisit);
        hosVisit.setDoctorAdvice(doctorAdvice);
        hosVisitMapper.updateById(hosVisit);
        HosVisit visit = hosVisitMapper.selectById(id);
        map.put("msgType", IMMsgType.VisitResultCard);
        map.put("businessID", "MSHCustomMsg");
        imService.sendPostHttpRequest(DOCTOR + visit.getDoctorUserId(), PATIENT + visit.getPatientUserId(), map, "诊断结果", "", "");
        return "提交成功";
    }

    @Override
    public Map visitResult(Map<String, String> map) {
        String id = map.get("id");
        HosVisit hosVisit = hosVisitMapper.selectById(id);
        HashMap<String, Object> result = new HashMap<>();
        result.put("sickName", logService.getSickName(hosVisit.getHosSickId()));
        result.put("type", StatusEnumUtil.getVisitTypeName(hosVisit.getType()));
        result.put("date", hosVisit.getStartTime().substring(0, 10));
        result.put("content", hosVisit.getContent());
        result.put("firstVisit", hosVisit.getFirstVisit());
        result.put("doctorAdvice", hosVisit.getDoctorAdvice());
        return result;
    }

    @Override
    public Map<String, String> visitOrderCount(Map<String, String> map) {
        String sickId = map.get("sickId");//就诊人id
        long success = this.selectVisitCount(sickId, "SUCCESS");
        long wait = this.selectVisitCount(sickId, "WAIT");
        Map result = Maps.newHashMap();
        result.put("success", String.valueOf(success));
        result.put("wait", String.valueOf(wait));
        return result;
    }

    public Long selectVisitCount(String sickId, String status) {
        DoctorUser loginUser = AuthUtil.getLoginUser();
        String id = loginUser.getId();
        LambdaQueryWrapper<HosVisit> hosVisitQueryWrapper = new QueryWrapper<HosVisit>().lambda();
        hosVisitQueryWrapper.select().eq(HosVisit::getDoctorUserId, id).orderByDesc(HosVisit::getCreateTime);
        if (StrUtil.isNotBlank(sickId)) {
            hosVisitQueryWrapper.eq(HosVisit::getHosSickId, sickId);
        }
        if ("SUCCESS".equals(status.toUpperCase())) {
            hosVisitQueryWrapper.select().and(wrapper -> wrapper.eq(HosVisit::getStatus, StatusEnumUtil.END));
        } else if ("WAIT".equals(status.toUpperCase())) {
            hosVisitQueryWrapper.select().and(wrapper -> wrapper.eq(HosVisit::getStatus, StatusEnumUtil.PAY).or()
                    .eq(HosVisit::getStatus, StatusEnumUtil.HAVE).or().eq(HosVisit::getStatus, StatusEnumUtil.START));
        }
        long selectCount = hosVisitMapper.selectCount(hosVisitQueryWrapper);
        return selectCount;
    }

    @Override
    public Long countByDoctorUserId(String doctorUserId) {
        return hosVisitMapper.selectCount(new LambdaQueryWrapper<HosVisit>()
                .eq(HosVisit::getDoctorUserId, doctorUserId));
    }

    @Override
    public HosVisitHealthInfoVo getHealthInfo(String hosVisitId) {
        HosVisitHealthInfoVo healthInfoVo = new HosVisitHealthInfoVo();
        healthInfoVo.setHosVisitId(hosVisitId);
        HosVisit hosVisit = hosVisitMapper.selectById(hosVisitId);
        if (StringUtil.isNotBlank(hosVisit.getHealthInfo())) {
            healthInfoVo.setHealthInfo(hosVisit.getHealthInfo());
        }
        healthInfoVo.setCreateTime(hosVisit.getCreateTime());
        return healthInfoVo;
    }

    @Override
    public HosVisitBaseInfoVo getBaseInfo(String hosVisitId) {
        HosVisitBaseInfoVo baseInfoVo = new HosVisitBaseInfoVo();
        baseInfoVo.setHosVisitId(hosVisitId);
        HosVisit hosVisit = hosVisitMapper.selectById(hosVisitId);
        HosVisitBaseInfoDTO baseInfo = new HosVisitBaseInfoDTO();
        baseInfoVo.setBaseInfo(baseInfo);

        HosSickHealthRecords healthRecords = hosSickHealthRecordsService.getOne(new LambdaQueryWrapper<HosSickHealthRecords>()
                .eq(HosSickHealthRecords::getHosSickId, hosVisit.getHosSickId()));
        if (healthRecords != null) {
            baseInfo.setArea(healthRecords.getArea());
            baseInfo.setAreaName(healthRecords.getAreaName());
            baseInfo.setHeight(healthRecords.getHeight());
            baseInfo.setWeight(healthRecords.getWeight());
            baseInfo.setAllergyHistory(healthRecords.getAllergyHistory());
            baseInfo.setMedicalHistory(healthRecords.getPastHistory());
            if (StringUtils.isNotEmpty(healthRecords.getFaceImages()))
                baseInfo.setFaceImages(Lists.newArrayList(healthRecords.getFaceImages().split(",")));
            if (StringUtils.isNotEmpty(healthRecords.getTongueImages()))
                baseInfo.setTongueImages(Lists.newArrayList(healthRecords.getTongueImages().split(",")));
            if (StringUtils.isNotEmpty(healthRecords.getMedicalRecordImages()))
                baseInfo.setMedicalRecordImages(Lists.newArrayList(healthRecords.getMedicalRecordImages().split(",")));
        }
        baseInfoVo.setCreateTime(hosVisit.getCreateTime());
        return baseInfoVo;
    }

    @Override
    public JSONArray patientList(Map<String, Object> map) {
        map.put("doctorUserId", AuthUtil.getLoginUserId());
        List<HosVisitSelectVo> hosVisitVoList = hosVisitVoMapper.patientList(map);
        if (CollectionUtils.isEmpty(hosVisitVoList)) {
            return new JSONArray();
        }
        JSONArray jSONArray = new JSONArray();

        for (HosVisitSelectVo hosVisitVo : hosVisitVoList) {
            JSONObject jsonObject = new JSONObject();

            //DONE订单关闭 WAIT待付款 资料未提交UNSUBMITTED 资料已提交SUBMITTED 对话已开始STARTED 问诊已结束ENDED 退款中REFUND_PROCESSING 已退款REFUNDED
            String status = hosVisitVo.getStatus();

            //图文IMAGE、语音VOICE、视频VIDEO
            String type = hosVisitVo.getType();

            String doctorUserId = hosVisitVo.getDoctorUserId();
            jsonObject.put("doctorId", doctorUserId);
            jsonObject.put("fiveStar", hosVisitVo.getFiveStar());
            jsonObject.put("doctorName", hosVisitVo.getDoctorName());
            jsonObject.put("doctorHeadImg", hosVisitVo.getDoctorHeadImg());
            jsonObject.put("age", hosVisitVo.getAge());
            jsonObject.put("hospitalName", hosVisitVo.getHospitalName());
            jsonObject.put("officeHolderName", hosVisitVo.getOfficeHolderName());
            jsonObject.put("departName", hosVisitVo.getDepartName());
            jsonObject.put("totalPrice", hosVisitVo.getTotalPrice());
            jsonObject.put("orderNum", hosVisitVo.getOrderNum());
            jsonObject.put("type", type);
            jsonObject.put("status", status);
            jsonObject.put("patientId", hosVisitVo.getPatientUserId());
            jsonObject.put("sickId", hosVisitVo.getHosSickId());
            jsonObject.put("sickName", hosVisitVo.getSickName());
            jsonObject.put("createTime", hosVisitVo.getCreateTime());
            jsonObject.put("startTime", hosVisitVo.getStartTime());
            jsonObject.put("sex", hosVisitVo.getSex());
            jsonObject.put("id", hosVisitVo.getId());
            jsonObject.put("content", hosVisitVo.getContent());
            jsonObject.put("msg", hosVisitVo.getMsg());
            jsonObject.put("imUserId", hosVisitVo.getImUserId());
            jsonObject.put("imUserSig", hosVisitVo.getImUserSig());
            jsonObject.put("avatar", ossComponent.getUrl(getAvatarPath(hosVisitVo.getSex(), hosVisitVo.getAge())));
            jsonObject.put("imGroupId", "GROUP_" + hosVisitVo.getOrderNum());
            jsonObject.put("visitStatus", JSONObject.toJSONString(VisitStatusUtil.getStatusFlow(status)));
            QueryWrapper<ImMsg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_num", hosVisitVo.getOrderNum());
            queryWrapper.orderByDesc("create_time");
            List<ImMsg> msgList = imMsgMapper.selectList(queryWrapper);
            Integer count = msgList.stream().filter(msg -> msg.getDoctorPeerReadStatus() == 0).toList().size();
            jsonObject.put("msgPeerReadCount", count);
            jSONArray.add(jsonObject);
        }
        return jSONArray;
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
