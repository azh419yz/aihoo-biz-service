package com.aihoo.domain.visit.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.sys.model.entity.Dict;
import com.aihoo.domain.sys.model.mapper.DiceMapper;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.excel.HosVisitEntity;
import com.aihoo.domain.visit.model.mapper.VisitOrderMapper;
import com.aihoo.domain.visit.model.vo.ImMsgContentVo;
import com.aihoo.domain.visit.model.vo.ImMsgVo;
import com.aihoo.domain.visit.service.VisitOrderService;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.im.model.entity.ImMsg;
import com.aihoo.domain.im.model.entity.ImMsgContent;
import com.aihoo.domain.im.model.mapper.ImMsgContentMapper;
import com.aihoo.domain.im.model.mapper.ImMsgMapper;
import com.aihoo.domain.patient.model.entity.PatientUser;
import com.aihoo.domain.patient.model.mapper.PatientUserMapper;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Classname OrderServiceImpl
 * @Description hf
 * @Date 2020/9/22 16:42
 * @Created by ad
 */
@Service
public class VisitOrderServiceImpl extends ServiceImpl<VisitOrderMapper, HosVisit> implements VisitOrderService {

    @Resource
    private DoctorUserMapper doctorUserMapper;

    @Resource
    private VisitOrderMapper visitOrderMapper;

    @Resource
    private PatientUserMapper patientUserMapper;

    @Resource
    private ImMsgMapper imMsgMapper;

    @Resource
    private ImMsgContentMapper imMsgContentMapper;

    @Resource
    private DiceMapper diceMapper;

    // 患者
    private static final String PATIENT = "PATIENT_";

    // 医生
    private static final String DOCTOR = "DOCTOR_";


    @Override
    public PageResult<HosVisit> page(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<HosVisit> wrapper = new QueryWrapper<>();
        if (null != map.get("orderNum") && !"".equals(map.get("orderNum"))) {
            wrapper.eq("order_num", map.get("orderNum"));
        }
        if (null != map.get("status") && !"".equals(map.get("status"))) {
            if ("START".equals(map.get("status"))) {
                wrapper.in("status", Stream.of("START", "HAVE").collect(Collectors.toList()));
            } else {
                wrapper.eq("status", map.get("status"));
            }
        }
        if (null != map.get("mobile") && !"".equals(map.get("mobile"))) {
            wrapper.eq("mobile", map.get("mobile"));
        }
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            wrapper.like("name", map.get("name"));
        }
        if (null != map.get("doctorUserName") && !"".equals(map.get("doctorUserName"))) {
            List<DoctorUser> doctorUsers = this.doctorUserMapper.
                    selectList(new QueryWrapper<DoctorUser>().like("name", map.get("doctorUserName")));
            if (!CollectionUtils.isEmpty(doctorUsers)) {
                List<String> doctorUserIds = doctorUsers.stream().map(DoctorUser::getId).collect(Collectors.toList());
                wrapper.in("doctor_user_id", doctorUserIds);
            } else {
                return new PageResult<>(Lists.newArrayList(), 0);
            }
        }
        if (null != map.get("type") && !"".equals(map.get("type"))) {
            wrapper.eq("type", map.get("type"));
        }
        wrapper.orderByDesc("create_time");
        IPage<HosVisit> res = visitOrderMapper.selectPage(new Page<>(page, limit), wrapper);
        List<HosVisit> resp = res.getRecords();
        if (CollectionUtils.isEmpty(resp)) {
            return new PageResult<>(res.getRecords(), res.getTotal());
        }
        // 查询关联数据
        List<String> doctorUserIds = resp.stream().map(HosVisit::getDoctorUserId).collect(Collectors.toList());
        List<DoctorUser> doctorUsers = doctorUserMapper.selectBatchIds(doctorUserIds);
        if (CollectionUtils.isEmpty(doctorUsers)) {
            return new PageResult<>("根据医生id没有查询到医生信息 id ： " + doctorUserIds.toString());
        }
        // doctorUsers.forEach(x->x.setName(x.getName() + ("1".equals(x.getIsCancel())?"(旧)":"")));
        Map<String, DoctorUser> doctorUserMap = doctorUsers.stream().collect(Collectors.toMap(DoctorUser::getId, doctorUser -> doctorUser));

        List<String> patientUserIds = resp.stream().map(HosVisit::getPatientUserId).collect(Collectors.toList());
        List<PatientUser> patientUsers = this.patientUserMapper.selectBatchIds(patientUserIds);
        if (CollectionUtils.isEmpty(patientUsers)) {
            return new PageResult<>("根据患者id没有查询到患者信息 id ： " + patientUserIds.toString());
        }
        Map<String, PatientUser> patientUserMap = patientUsers.stream().collect(Collectors.toMap(PatientUser::getId, patientUser -> patientUser));
        List<Dict> dicts = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", "PAY_TYPE"));
        Map<String, String> typeMap = dicts.stream().collect(Collectors.toMap(Dict::getCode, Dict::getName));

        resp.forEach(r -> {
            r.setDoctorName(doctorUserMap.get(r.getDoctorUserId()).getName());
            r.setDepartName(doctorUserMap.get(r.getDoctorUserId()).getDepartName());
            r.setHospitalName(doctorUserMap.get(r.getDoctorUserId()).getHospitalName());
            r.setUserMobile(patientUserMap.get(r.getPatientUserId()).getMobile());
            if (!StringUtils.isEmpty(r.getPayType())) {
                r.setPayType(typeMap.get(r.getPayType()));
            }
        });
        return new PageResult<>(resp, res.getTotal());
    }

    @Override
    public JSONArray getDoctorAll() {
        JSONArray jsonArray = new JSONArray();
        List<DoctorUser> doctorUsers = this.doctorUserMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(doctorUsers)) {
            return jsonArray;
        }
        doctorUsers.forEach(doctorUser -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", doctorUser.getId());
            jsonObject.put("name", doctorUser.getName());
            jsonArray.add(jsonObject);
        });
        return jsonArray;
    }

    @Override
    public HosVisit getInquiryDetails(String id) {
        HosVisit hosVisit = visitOrderMapper.getInquiryDetails(id);
        List<Dict> dicts = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", "PAY_TYPE"));
        Map<String, String> typeMap = dicts.stream().collect(Collectors.toMap(Dict::getCode, Dict::getName));
        if (null != hosVisit) {
            DoctorUser doctorUser = this.doctorUserMapper.selectById(hosVisit.getDoctorUserId());
            if (null != doctorUser) {
                hosVisit.setDoctorName(doctorUser.getName());
                hosVisit.setHospitalName(doctorUser.getHospitalName());
                hosVisit.setDepartName(doctorUser.getDepartName());
                PatientUser patientUser = this.patientUserMapper.selectById(hosVisit.getPatientUserId());
                if (null != patientUser) {
                    hosVisit.setPatientUserName(patientUser.getName());
                    hosVisit.setUserMobile(patientUser.getMobile());
                }
            }
            if (!StringUtils.isEmpty(hosVisit.getPayType())) {
                hosVisit.setPayType(typeMap.get(hosVisit.getPayType()));
            }
        }
        return hosVisit;
    }

    @Override
    public void visitBulkExport(String orderNum, String status, String mobile, String name, String doctorUserName,
                                String type, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<HosVisit> wrapper = new QueryWrapper<>();
        if (null != orderNum && !"".equals(orderNum)) {
            wrapper.eq("order_num", orderNum);
        }
        if (null != status && !"".equals(status)) {
            if ("START".equals(status)) {
                wrapper.in("status", Stream.of("START", "HAVE").collect(Collectors.toList()));
            } else {
                wrapper.eq("status", status);
            }
        }
        if (null != mobile && !"".equals(mobile)) {
            wrapper.eq("mobile", mobile);
        }
        if (null != name && !"".equals(name)) {
            wrapper.like("name", name);
        }
        if (null != doctorUserName && !"".equals(doctorUserName)) {
            List<DoctorUser> doctorUsers = this.doctorUserMapper.
                    selectList(new QueryWrapper<DoctorUser>().like("name", doctorUserName));
            if (!CollectionUtils.isEmpty(doctorUsers)) {
                List<String> doctorUserIds = doctorUsers.stream().map(DoctorUser::getId).collect(Collectors.toList());
                wrapper.in("doctor_user_id", doctorUserIds);
            } else {
                throw new BizException("当前条件无数据");
            }
        }
        if (null != type && !"".equals(type)) {
            wrapper.like("type", type);
        }
        List<HosVisit> hosVisits = this.visitOrderMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(hosVisits)) {
            throw new BizException("当前条件无数据");
        }
        List<String> PatientUserIds = hosVisits.stream().map(HosVisit::getPatientUserId).collect(Collectors.toList());
        List<PatientUser> patientUsers = this.patientUserMapper.selectBatchIds(PatientUserIds);
        if (CollectionUtils.isEmpty(patientUsers)) {
            throw new BizException("根据用户id没有查询到用户数据" + PatientUserIds);
        }
        Map<String, PatientUser> patientUserMap = patientUsers.stream().collect(Collectors.toMap(PatientUser::getId, s -> s));

        List<HosVisitEntity> list = new ArrayList<>();
        hosVisits.forEach(s -> {
            if ("ALIPAY".equals(s.getPayType())) {
                s.setPayType("支付宝");
            } else {
                s.setPayType("微信");
            }
            if ("1".equals(s.getSex())) {
                s.setSex("男");
            } else {
                s.setSex("女");
            }

            // 问诊类型 图文IMAGE、语音VOICE、视频VIDEO
            switch (s.getType()) {
                case "IMAGE":
                    s.setType("图文问诊");
                    break;
                case "VOICE":
                    s.setType("语音问诊");
                    break;
                case "VIDEO":
                    s.setType("视频问诊");
                    break;
            }

            //状态 DONE订单关闭 ;CANCEL取消订单; WAIT待付款 PAY已付款|待接单 ; (START|HAVE) 已接单 ;
            // END订单完成  ;REFUNDWAIT退款进行中 ;REFUNDSUCCESS退款成功 ;CHANGE退款异常  ;REFUNDCLOSE退款关闭'
            switch (s.getStatus()) {
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
                case "START":
                    s.setStatus("已接单");
                    break;
                case "HAVE":
                    s.setStatus("已接单");
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
            if ("1".equals(s.getIsReadIm())) {
                s.setIsReadIm("否");
            } else {
                s.setIsReadIm("是");
            }
            if ("1".equals(s.getIsPay())) {
                s.setIsPay("未支付");
            } else {
                s.setIsPay("已支付");
            }
            HosVisitEntity entity = new HosVisitEntity();
            BeanUtils.copyProperties(s, entity);
            entity.setPatientUserName(patientUserMap.get(s.getPatientUserId()).getName());
            list.add(entity);
        });
        ExcelUtils.writeExcel(request, response, list, HosVisitEntity.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + "问诊订单表格.xlsx");
    }

    @Override
    public List<ImMsgVo> imList(Map<String, Object> map) throws Exception {
        List<ImMsgVo> sendImMsgVos = Lists.newArrayList();
        //接收者消息记录
        List<ImMsgVo> recipientImMsg = getImMsg(map, DOCTOR + map.get("doctorUserId"), PATIENT + map.get("patientUserId"), map.get("startTime").toString(), map.get("endTime").toString());
        //发送者消息记录
        List<ImMsgVo> sendImMsg = getImMsg(map, PATIENT + map.get("patientUserId"), DOCTOR + map.get("doctorUserId"), map.get("startTime").toString(), map.get("endTime").toString());
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(recipientImMsg)) {
            sendImMsgVos.addAll(recipientImMsg);
        }
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(sendImMsg)) {
            sendImMsgVos.addAll(sendImMsg);
        }
        Collections.sort(sendImMsgVos, (o1, o2) -> {
            //按照学生的姓名进行升序排列
            if (o1.getMsgTime().compareTo(o2.getMsgTime()) == 0) {
                return 1;
            } else {
                return o1.getMsgTime().compareTo(o2.getMsgTime());
            }
        });
        return sendImMsgVos;
    }

    //获取接收者消息
    private List<ImMsgVo> getImMsg(Map<String, Object> map, String sendId, String recipientId, String imStartTime, String imEndTime) throws Exception {
        PatientUser patientUser = null;
        DoctorUser doctorUser = null;
        //查询当前医生和患者的图像
        if (sendId.startsWith("PATIENT_")) {
            String id = sendId.split("_")[1];
            patientUser = this.patientUserMapper.selectById(id);
        } else {
            String id = recipientId.split("_")[1];
            patientUser = this.patientUserMapper.selectById(id);
        }
        if (sendId.startsWith("DOCTOR_")) {
            String id = sendId.split("_")[1];
            doctorUser = this.doctorUserMapper.selectById(id);
            sendId = DOCTOR + id;
        } else {
            String id = recipientId.split("_")[1];
            doctorUser = this.doctorUserMapper.selectById(id);
            recipientId = DOCTOR + id;
        }
        List<ImMsgVo> recipientImMsg = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get("startTime").toString()));
        //开始时间取秒
        long startTimeMillis = calendar.getTimeInMillis() / 1000;
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get("endTime").toString()));
        //结束时间取秒
        long endTimeMillis = calendar.getTimeInMillis() / 1000;
        //查询聊天消息
        QueryWrapper<ImMsg> wrapper = new QueryWrapper<>();
        wrapper.eq("from_account", sendId);
        wrapper.eq("to_account", recipientId);
        wrapper.gt("msg_time", startTimeMillis);
        wrapper.lt("msg_time", endTimeMillis);
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty(imStartTime)) {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(imStartTime));
            //IM聊天开始时间取秒
            long imStartTimeMillis = calendar.getTimeInMillis() / 1000;
            wrapper.and(infoWrapper -> infoWrapper.gt("msg_time", imStartTimeMillis));
        }
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty(imEndTime)) {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(imEndTime));
            //IM聊天结束时间取秒
            long imEndTimeMillis = calendar.getTimeInMillis() / 1000;
            wrapper.and(infoWrapper -> infoWrapper.lt("msg_time", imEndTimeMillis));
        }
        //获取聊天消息
        List<ImMsg> imMsgs = this.imMsgMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(imMsgs)) {
            return recipientImMsg;
        }

        for (ImMsg imMsg : imMsgs) {
            ImMsgVo imMsgVo = new ImMsgVo();
            BeanUtils.copyProperties(imMsg, imMsgVo);
            List<ImMsgContent> imMsgContentList = imMsgContentMapper.selectList(new QueryWrapper<ImMsgContent>().eq("im_msg_id", imMsg.getId()));
            if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(imMsgContentList)) {
                List<ImMsgContentVo> imMsgContentVos = Lists.newArrayList();
                for (ImMsgContent imMsgContent : imMsgContentList) {
                    ImMsgContentVo imMsgContentVo = new ImMsgContentVo();
                    BeanUtils.copyProperties(imMsgContent, imMsgContentVo);
                    imMsgContentVo.setMsgContent(imMsgContent.getMsgContent());
                    imMsgContentVo.setData(imMsgContent.getMsgContent());
                    imMsgContentVos.add(imMsgContentVo);
                }
                imMsgVo.setPayloads(imMsgContentVos);
                imMsgVo.setPayload(imMsgContentVos.get(0));
                imMsgVo.setTime(imMsgVo.getMsgTime());
                imMsgVo.setType(imMsgContentVos.get(0).getMsgType());
                imMsgVo.setFrom(imMsgVo.getFromAccount());
                imMsgVo.setTo(imMsgVo.getToAccount());
                if (imMsgVo.getFromAccount().startsWith("PATIENT_")) {
                    // 发送者为患者设置为患者图像
                    imMsgVo.setAvatar(patientUser.getHeadImg());
                } else {
                    // 发送者为医生时设置为医生的图像
                    imMsgVo.setAvatar(doctorUser.getHeadImg());
                }
            }
            recipientImMsg.add(imMsgVo);
        }
        return recipientImMsg;
    }
}
