package com.aihoo.domain.im.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aihoo.constant.PushMessageType;
import com.aihoo.util.WeekUtil;
import com.aihoo.common.BizResultCode;
import com.aihoo.exception.BizException;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aihoo.domain.im.dto.VisitChatVo;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.visit.model.mapper.HosVisitMapper;
import com.aihoo.domain.visit.model.mapper.HosRevisitMapper;
import com.aihoo.domain.visit.model.mapper.HosRevisitImgMapper;


import com.aihoo.util.StatusEnumUtil;
import com.aihoo.security.AuthUtil;
import com.aihoo.domain.im.enums.IMMsgType;
import com.aihoo.util.TimeUtil;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aihoo.domain.sys.model.mapper.CommonLanguageMapper;
import com.aihoo.domain.sys.model.entity.CommonLanguage;
import com.aihoo.domain.im.model.mapper.PushMessageMapper;
import com.aihoo.domain.sys.model.mapper.TBaseMapper;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.visit.model.entity.HosRevisitImg;
import com.aihoo.domain.im.model.entity.PushMessage;
import com.aihoo.domain.sys.model.entity.TBase;
import com.aihoo.domain.im.service.ChatService;
import com.aihoo.domain.im.service.IMService;
import com.aihoo.domain.visit.service.LogService;
import com.aihoo.domain.visit.service.HosVisitService;
import com.aihoo.domain.visit.service.HosRevisitService;

import java.util.*;

/**
 * @program: aihoo-root
 * @description: 消息
 * @author: Mr.Li
 * @create: 2020-09-26 18:48
 **/
@Service
public class ChatServiceImpl implements ChatService {
    private Log log = LogFactory.get();
    /**
     * 复诊
     */
    @Resource
    private PushMessageMapper pushMessageMapper;
    /**
     * 复诊
     */
    @Resource
    private HosRevisitMapper hosRevisitMapper;
    /**
     * 问诊
     */
    @Resource
    private HosVisitMapper hosVisitMapper;
    /**
     * 日志
     */
    @Resource
    private LogService logService;
    /**
     * 患者
     */
    @Resource
    private TBaseMapper tBaseMapper;
    /**
     * 常用语
     */
    @Resource
    private CommonLanguageMapper commonLanguageMapper;
    /**
     * 常用语
     */
    @Resource
    private HosRevisitService hosRevisitService;
    /**
     * 常用语
     */
    @Resource
    private HosVisitService hosVisitService;

    @Resource
    private IMService imService;
    @Resource
    private com.aihoo.domain.im.service.impl.PushMessageServiceImpl pushMessageServiceImpl;
    @Resource
    private DoctorUserMapper doctorUserMapper;
    @Resource
    private HosRevisitImgMapper hosRevisitImgMapper;//复诊图片
    // 患者
    private static final String PATIENT = "PATIENT_";

    // 医生
    private static final String DOCTOR = "DOCTOR_";

    /**
     * 开始问诊
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> startVistChat(String id) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("msg", "订单状态错误");
        resultMap.put("is_succ", "ERROR");
        resultMap.put("orderStatus", "");
        resultMap.put("cutDown", "0");

        HosVisit hosVisit = hosVisitMapper.selectById(id);
        String status = hosVisit.getStatus();
        if (status.equals(StatusEnumUtil.HAVE)) {
            logService.setVisitLog(hosVisit, StatusEnumUtil.START, logService.getDoctorUserName() + "医生开始与患者" + logService.getSickName(hosVisit.getHosSickId()) + "的在线复诊聊天。");
            HosVisit updHosVisit = new HosVisit();
            updHosVisit.setId(id);
            updHosVisit.setStatus(StatusEnumUtil.START);
            updHosVisit.setStartTime(DateUtil.now());
            int update = hosVisitMapper.updateById(updHosVisit);
            if (update > 0) {
                serviceStartVistChat(hosVisit);
            }
            resultMap.put("msg", "开始在线复诊");
            resultMap.put("is_succ", "success");
            Map<String, Object> map = new HashMap<>();
            map.put("msgType", IMMsgType.DToPFirstText);
            map.put("msg", "医生已接单，您可以进行提问");
            map.put("businessID", "MSHCustomMsg");
            sendStartVisitIm(hosVisit.getOrderNum());
        } else if (status.equals(StatusEnumUtil.START)) {
            resultMap.put("msg", "在线复诊已开始");
            resultMap.put("is_succ", "success");
        }
        if (resultMap.get("is_succ").equals("ERROR")) {
            return resultMap;
        }
        String haveTime = hosVisit.getHaveTime();
        Date haveTimeParse = DateUtil.parse(haveTime, "yyyy-MM-dd HH:mm:ss");
        TBase tBase = tBaseMapper.selectByKey("DOCTOR_VISIT_TIMES");
        String content = tBase.getContent();
        String betweenTime = TimeUtil.getBetweenTime(DateUtil.date(), haveTimeParse, Integer.parseInt(content));
        resultMap.put("cutDown", betweenTime);
        resultMap.put("orderStatus", hosVisit.getStatus());
        return resultMap;
    }

    @Async(value = "asyncExecutor")
    public void sendStartVisitIm(String msg) {
        LambdaQueryWrapper<HosVisit> lambda = new QueryWrapper<HosVisit>().lambda();
        lambda.eq(HosVisit::getOrderNum, msg);
        HosVisit hosVisit = hosVisitMapper.selectOne(lambda);
        String doctorUserId = hosVisit.getDoctorUserId();
        imService.sendImSystemMsg(PATIENT + hosVisit.getPatientUserId(), DOCTOR + doctorUserId, "在线复诊已开始，本次在线复诊可持续24小时", IMMsgType.PToDFirstText, "在线复诊已开始，本次在线复诊可持续24小时", "", "");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        imService.sendImSystemMsg(PATIENT + hosVisit.getPatientUserId(), DOCTOR + doctorUserId, "温馨提示：患者购买了您的在线复诊，请在24小时内回复患者，如在在线复诊结束前，您未回复患者，患者可要求全额退款。", IMMsgType.PToDSecondText, "温馨提示：患者购买了您的在线复诊，请在24小时内回复患者，如在在线复诊结束前，您未回复患者，患者可要求全额退款。", "", "");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("msgType", IMMsgType.DToPFirstText);
        map.put("msg", "医生已接单，您可以进行提问");
        map.put("businessID", "MSHCustomMsg");
        imService.sendPostHttpRequest(DOCTOR + doctorUserId, PATIENT + hosVisit.getPatientUserId(), map, "医生已接单，您可以进行提问", "", "");
    }

    /**
     * 医生接问诊服务提醒
     *
     * @param hosVisit
     * @return
     */
    private boolean serviceStartVistChat(HosVisit hosVisit) {
        try {
            DoctorUser doctorUser = doctorUserMapper.selectOne(new QueryWrapper<DoctorUser>().eq("id", hosVisit.getDoctorUserId()));
            if (null == doctorUser) {
                log.error("服务提醒：进入诊室，在线复诊通道建立，没有查到医生信息");
                return true;
            }
            String name = doctorUser.getName();
            pushMessageServiceImpl.insertPatient("在线复诊订单 ：", hosVisit.getPatientUserId(), name + "医生已经接受您的在线复诊邀请，请尽快前往查看与医生交流病情。",
                    PushMessageType.messageType_IMAGE, hosVisit.getId(), name + "医生已经接受您的在线复诊邀请，请尽快前往查看与医生交流病情。", "0", null);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    private void serviceStartVisitChat(HosVisit hosVisit) {
        DoctorUser doctorUser = doctorUserMapper.selectOne(new QueryWrapper<DoctorUser>().eq("id", hosVisit.getDoctorUserId()));
        if (null == doctorUser) {
            log.error("服务提醒：进入诊室，在线复诊通道建立，没有查到医生信息");
            return;
        }
        String name = doctorUser.getName();
        pushMessageServiceImpl.insertPatient("在线复诊订单 ：", hosVisit.getPatientUserId(), name + "医生已经接受您的在线复诊邀请，请尽快前往查看与医生交流病情。",
                PushMessageType.messageType_IMAGE, hosVisit.getId(), name + "医生已经接受您的在线复诊邀请，请尽快前往查看与医生交流病情。", "0", null);
    }

    /**
     * 开始复诊
     * 设置开始时间并修改状态为（START）开始复诊
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> startRevisitChat(String id) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("msg", "订单状态错误");
        resultMap.put("is_succ", "ERROR");
        resultMap.put("orderStatus", "");
        resultMap.put("cutDown", "0");

        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        String status = hosRevisit.getStatus();
        if (status.equals(StatusEnumUtil.START)) {
            resultMap.put("msg", "复诊已开始");
            resultMap.put("isCanChat", "1");
            resultMap.put("is_succ", "success");
        } else if (status.equals(StatusEnumUtil.HAVE)) {
            Date revisitStartTime = DateUtil.parse(hosRevisit.getRevisitStartTime(), "yyyy-MM-dd HH:mm:ss");
            Date revisitEndTime = DateUtil.parse(hosRevisit.getRevisitEndTime(), "yyyy-MM-dd HH:mm:ss");

            Date createTime = DateUtil.parse(hosRevisit.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
            if (WeekUtil.inTime(DateUtil.date(), revisitStartTime, revisitEndTime)) {
                logService.setRevisitLog(hosRevisit, StatusEnumUtil.START, logService.getDoctorUserName() + "医生开始与患者" + logService.getSickName(hosRevisit.getHosSickId()) + "的复诊聊天。");
                HosRevisit updHosRevisit = new HosRevisit();
                updHosRevisit.setId(id);
                updHosRevisit.setStatus(StatusEnumUtil.START);
                updHosRevisit.setStartTime(DateUtil.now());
                int update = hosRevisitMapper.updateById(updHosRevisit);

                resultMap.put("msg", "开始复诊");
                resultMap.put("is_succ", "success");

                /********本来这里是接单发的，现在进入诊室再发**********/
                QueryWrapper<HosRevisitImg> hosRevisitImgQueryWrapper = new QueryWrapper<>();
                hosRevisitImgQueryWrapper.eq("hos_revisit_id", id);
                List<HosRevisitImg> hosRevisitImgs = hosRevisitImgMapper.selectList(hosRevisitImgQueryWrapper);
                List<String> imgList = new ArrayList<>();
                for (HosRevisitImg hosRevisitImg : hosRevisitImgs) {
                    imgList.add(hosRevisitImg.getImg());
                }
                hosRevisit.setImgList(imgList);
                hosRevisit.setMsgType(IMMsgType.RevisitOrderCard);
                hosRevisit.setBusinessID("MSHCustomMsg");
                String desc = hosRevisit.getName() + "," + (org.apache.commons.lang3.StringUtils.isNotEmpty(hosRevisit.getSex()) ? ("0".equals(hosRevisit.getSex()) ? "女" : "1".equals(hosRevisit.getStatus()) ? "男" : "未知") : "未知") + "," + hosRevisit.getAge() + "岁";
                /********本来这里是接单发的，现在进入诊室再发**********/
                Map<String, Object> map = new HashMap<>();
                map.put("msgType", IMMsgType.DToPFirstText);
                map.put("msg", "医生已接单，您可以进行提问");
                map.put("businessID", "MSHCustomMsg");
                resultMap.put("isCanChat", "1");
                sendStartRevisitIm(hosRevisit.getOrderNum());
            } else {
                resultMap.put("msg", "不在复诊预约时间");
                resultMap.put("isCanChat", "0");
                resultMap.put("is_succ", "ERROR");
            }
        }
        if (resultMap.get("is_succ").equals("ERROR")) {
            return resultMap;
        }
        DateTime now = DateUtil.date();
        hosRevisit = hosRevisitMapper.selectById(id);
        if (StrUtil.isNotBlank(hosRevisit.getStartTime())) {
            DateTime startTime = DateUtil.offsetDay(DateUtil.parseDateTime(hosRevisit.getStartTime()), 1);
            //当前在预约结束时间之前true
            if (now.before(startTime)) {
                resultMap.put("cutDown", String.valueOf(DateUtil.between(startTime, now, DateUnit.SECOND)));
            } else {
                resultMap.put("cutDown", "0");
            }
        } else {
            resultMap.put("cutDown", "0");
        }
        resultMap.put("orderStatus", hosRevisit.getStatus());
        return resultMap;
    }

    @Async(value = "asyncExecutor")
    public void sendStartRevisitIm(String msg) {
        LambdaQueryWrapper<HosRevisit> lambda = new QueryWrapper<HosRevisit>().lambda();
        lambda.eq(HosRevisit::getOrderNum, msg);
        HosRevisit hosRevisit = hosRevisitMapper.selectOne(lambda);
        /********本来这里是接单发的，现在进入诊室再发**********/
        QueryWrapper<HosRevisitImg> hosRevisitImgQueryWrapper = new QueryWrapper<>();
        hosRevisitImgQueryWrapper.eq("hos_revisit_id", hosRevisit.getId());
        List<HosRevisitImg> hosRevisitImgs = hosRevisitImgMapper.selectList(hosRevisitImgQueryWrapper);
        List<String> imgList = new ArrayList<>();
        for (HosRevisitImg hosRevisitImg : hosRevisitImgs) {
            imgList.add(hosRevisitImg.getImg());
        }
        hosRevisit.setImgList(imgList);
        hosRevisit.setMsgType(IMMsgType.RevisitOrderCard);
        hosRevisit.setBusinessID("MSHCustomMsg");
        String desc = hosRevisit.getName() + "," + (org.apache.commons.lang3.StringUtils.isNotEmpty(hosRevisit.getSex()) ? ("0".equals(hosRevisit.getSex()) ? "女" : "1".equals(hosRevisit.getStatus()) ? "男" : "未知") : "未知") + "," + hosRevisit.getAge() + "岁";
        String doctorUserId = hosRevisit.getDoctorUserId();
        imService.sendPostHttpRequest(PATIENT + hosRevisit.getPatientUserId(), DOCTOR + doctorUserId, hosRevisit, desc, "", "");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /********本来这里是接单发的，现在进入诊室再发**********/
        imService.sendImSystemMsg(PATIENT + hosRevisit.getPatientUserId(), DOCTOR + doctorUserId, "复诊已开始，本次复诊可持续24小时", IMMsgType.PToDFirstText, "复诊已开始，本次复诊可持续24小时", "", "");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        imService.sendImSystemMsg(PATIENT + hosRevisit.getPatientUserId(), DOCTOR + doctorUserId, "温馨提示：系统分配了一例复诊，请在24小时内回复患者，如在复诊结束前，您未回复患者，患者可要求全额退款。", IMMsgType.PToDSecondText, "温馨提示：系统分配了一例复诊，请在24小时内回复患者，如在复诊结束前，您未回复患者，患者可要求全额退款。", "", "");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("msgType", IMMsgType.DToPFirstText);
        map.put("msg", "医生已接单，您可以进行提问");
        map.put("businessID", "MSHCustomMsg");
        imService.sendPostHttpRequest(DOCTOR + doctorUserId, PATIENT + hosRevisit.getPatientUserId(), map, "医生已接单，您可以进行提问", "", "");

    }

    /**
     * 结束问诊
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> stopVistChat(Map<String, String> map) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("msg", "订单状态错误");
        resultMap.put("is_succ", "ERROR");
        String id = map.get("id");
        HosVisit hosVisit = hosVisitMapper.selectById(id);
        String status = hosVisit.getStatus();
        if (status.equals("HAVE") || status.equals("START")) {
            logService.setVisitLog(hosVisit, StatusEnumUtil.END, logService.getDoctorUserName() + "医生结束与患者" + logService.getSickName(hosVisit.getHosSickId()) + "的在线复诊聊天。");
            HosVisit updHosVisit = new HosVisit();
            updHosVisit.setId(id);
            updHosVisit.setEndTime(DateUtil.now());
            updHosVisit.setStatus(StatusEnumUtil.END);
            hosVisitMapper.updateById(updHosVisit);
            resultMap.put("msg", "在线复诊结束成功");
            resultMap.put("is_succ", "success");
            Map<String, Object> mapIm = new HashMap<>();
            mapIm.put("msgType", IMMsgType.ServiceAppraiseCard);
            mapIm.put("orderNum", hosVisit.getOrderNum());
            mapIm.put("orderId", hosVisit.getId());
            mapIm.put("orderType", "VISIT");
            mapIm.put("businessID", "MSHCustomMsg");
            boolean sendImSystemMsg = imService.sendImSystemMsg(PATIENT + hosVisit.getPatientUserId(), DOCTOR + hosVisit.getDoctorUserId(), "本次在线复诊已结束", IMMsgType.EndText, "本次在线复诊已结束", "", "");
            if (sendImSystemMsg) {
                imService.sendPostHttpRequest(DOCTOR + hosVisit.getDoctorUserId(), PATIENT + hosVisit.getPatientUserId(), mapIm, "在线复诊评价", "", "");
            }
        }
        return resultMap;
    }

    /**
     * 结束复诊
     * 设置结束时间并修改状态为（END）完成复诊
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> stopRevisitChat(Map<String, String> map) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("msg", "订单状态错误");
        resultMap.put("is_succ", "ERROR");
        String id = map.get("id");
        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        logService.setRevisitLog(hosRevisit, StatusEnumUtil.END, logService.getDoctorUserName() + "医生结束与患者" + logService.getSickName(hosRevisit.getHosSickId()) + "的复诊聊天。");
        if (hosRevisit.getStatus().equals("START")) {
            HosRevisit updHosRevisit = new HosRevisit();
            updHosRevisit.setId(id);
            updHosRevisit.setStatus(StatusEnumUtil.END);
            updHosRevisit.setEndTime(DateUtil.now());
            hosRevisitMapper.updateById(updHosRevisit);
            resultMap.put("msg", "复诊结束成功");
            resultMap.put("is_succ", "success");
            Map<String, Object> mapIm = new HashMap<>();
            mapIm.put("msgType", IMMsgType.ServiceAppraiseCard);
            mapIm.put("orderNum", hosRevisit.getOrderNum());
            mapIm.put("orderId", hosRevisit.getId());
            mapIm.put("orderType", "REVISIT");
            mapIm.put("businessID", "MSHCustomMsg");
            boolean sendImSystemMsg = imService.sendImSystemMsg(PATIENT + hosRevisit.getPatientUserId(), DOCTOR + hosRevisit.getDoctorUserId(), "本次在线复诊已结束", IMMsgType.EndText, "本次在线复诊已结束", "", "");
            if (sendImSystemMsg) {
                imService.sendPostHttpRequest(DOCTOR + hosRevisit.getDoctorUserId(), PATIENT + hosRevisit.getPatientUserId(), mapIm, "复诊评价", "", "");
            }
        }
        return resultMap;
    }

    /**
     * 常用语查询
     *
     * @return
     */
    @Override
    public List chatWords(Map<String, Object> map) {
        QueryWrapper<CommonLanguage> wrapper = new QueryWrapper<>();
        wrapper.eq("doctor_id", AuthUtil.getLoginUser().getId()).eq("type", "DOCKER").eq("is_delete", "0").orderByDesc("update_time");
        List<CommonLanguage> commonLanguages = commonLanguageMapper.selectList(wrapper);
        List jsonObject = JSON.parseObject(JSON.toJSONString(commonLanguages), List.class);
        return jsonObject;
    }

    /**
     * 常用语修改和添加
     *
     * @param map
     * @return
     */
    @Override
    public String updateChatWords(Map<String, Object> map) {
        if (map.get("id") != null && commonLanguageMapper.selectById(map.get("id").toString()) != null) {
            String id = map.get("id").toString();
            String content = map.get("content").toString();
            CommonLanguage commonLanguage = new CommonLanguage();
            commonLanguage.setId(id);
            commonLanguage.setContent(content);
            commonLanguageMapper.updateById(commonLanguage);
            return "修改成功";
        } else {
            String doctorId = AuthUtil.getLoginUser().getId();
            String content = map.get("content").toString();
            QueryWrapper<CommonLanguage> wrapper = new QueryWrapper<>();
            wrapper.eq("content", content).eq("doctor_id", doctorId);
            CommonLanguage one = commonLanguageMapper.selectOne(wrapper);
            if (one != null) {
                String isDelete = one.getIsDelete();
                if (isDelete.equals("0")) {
                    return "已存在";
                }
                CommonLanguage commonLanguage = new CommonLanguage();
                commonLanguage.setId(one.getId());
                commonLanguage.setIsDelete("0");
                commonLanguage.setCreateTime(DateUtil.now());
                commonLanguageMapper.updateById(commonLanguage);
                return "添加成功";
            }
            CommonLanguage commonLanguage = new CommonLanguage();
            commonLanguage.setDoctorId(doctorId);
            commonLanguage.setContent(content);
            commonLanguage.setType("DOCKER");
            commonLanguage.setIsDelete("0");
            commonLanguageMapper.insert(commonLanguage);
            return "添加成功";
        }
    }

    /**
     * 常用语删除
     *
     * @param map
     * @return
     */
    @Override
    public String deleteChatWords(Map<String, Object> map) {
        String id = map.get("id").toString();
        CommonLanguage commonLanguage = new CommonLanguage();
        commonLanguage.setId(id);
        commonLanguage.setIsDelete("1");
        commonLanguageMapper.updateById(commonLanguage);
        return "删除成功";
    }

    /**
     * 系统公告
     *
     * @param map
     * @return
     */
    @Override
    public Object pushMessage(Map<String, Object> map) {
        Object id = map.get("id");//消息id
        if (id != null) {
            return pushMessageMapper.selectById(id.toString());
        }
        Integer page = 1;
        Integer limit = 10;
        try {
            page = Integer.parseInt(map.get("page").toString());
            limit = Integer.parseInt(map.get("limit").toString());
        } catch (Exception e) {
            log.error("复诊列表无分页条件，默认展示前十条");
        }
        IPage<PushMessage> ipage = new Page<>(page, limit);

        Object noticeType = map.get("noticeType");//通知类型 ALL-全部通知 SYSTEM-系统公告 SERVICE-服务提醒
        LambdaQueryWrapper<PushMessage> pushMessageLambdaQueryWrapper = new QueryWrapper().lambda();
        if (noticeType.equals("ALL")) {
            pushMessageLambdaQueryWrapper.select().and(wrapper -> wrapper.eq(PushMessage::getNoticeType, "SYSTEM").or().eq(PushMessage::getNoticeType, "SERVICE"));
        } else {
            pushMessageLambdaQueryWrapper.select().eq(PushMessage::getNoticeType, noticeType.toString());
        }
        pushMessageLambdaQueryWrapper.eq(PushMessage::getIsDelete, "0").lt(PushMessage::getSetTime, DateUtil.now())
                .and(wrapper -> wrapper.and(wrapper2 -> wrapper2.eq(PushMessage::getType, "DOCKER").or().eq(PushMessage::getType, "ALL"))
                        .or(wrapper3 -> wrapper3.eq(PushMessage::getType, "DOCKER_PERSONAL").eq(PushMessage::getPesronalId, AuthUtil.getLoginUser().getId())))
                .orderByDesc(PushMessage::getSetTime);
        IPage<PushMessage> pushMessageIPage = pushMessageMapper.selectPage(ipage, pushMessageLambdaQueryWrapper);
        List<PushMessage> pushMessages = pushMessageIPage.getRecords();
        return pushMessages;
    }

    @Override
    public Map getChatMsg(String doctorId, String patientId) {
        LambdaQueryWrapper<HosVisit> visitLambdaQueryWrapper = new QueryWrapper<HosVisit>().lambda();
        visitLambdaQueryWrapper.eq(HosVisit::getPatientUserId, patientId).eq(HosVisit::getDoctorUserId, doctorId).and(wapper -> wapper.eq(HosVisit::getStatus, "START").or().eq(HosVisit::getStatus, "HAVE"));
        long count = hosVisitService.count(visitLambdaQueryWrapper);
        Map map = null;
        if (count == 1) {
            HosVisit hosVisit = hosVisitService.getOne(visitLambdaQueryWrapper);
            String hosVisitId = hosVisit.getId();
            Map<String, String> vistChat = startVistChat(hosVisitId);
            map = vistChat;
            map.put("type", "VISIT");
            map.put("orderId", hosVisitId);

        } else {
            LambdaQueryWrapper<HosRevisit> revisitLambdaQueryWrapper = new QueryWrapper<HosRevisit>().lambda();
            revisitLambdaQueryWrapper.eq(HosRevisit::getPatientUserId, patientId)
                    .eq(HosRevisit::getDoctorUserId, doctorId)
                    .and(wrapper ->
                            wrapper.eq(HosRevisit::getStatus, "START")
                                    .or()
                                    .eq(HosRevisit::getStatus, "HAVE")
                    );
            HosRevisit hosRevisit = hosRevisitService.getOne(revisitLambdaQueryWrapper);
            if (hosRevisit == null) {
                return null;
            }
            String hosRevisitId = hosRevisit.getId();
            Map<String, String> revisitChat = startRevisitChat(hosRevisitId);
            map = revisitChat;
            map.put("type", "REVISIT");
            map.put("orderId", hosRevisitId);
        }
        map.remove("is_succ");
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitChatVo startVisitChatV2(String id) {

        HosVisit hosVisit = hosVisitMapper.selectById(id);
        String status = hosVisit.getStatus();
        // 资料已提交状态 才可以接单
        if (!status.equals(StatusEnumUtil.SUBMITTED)) {
            throw new BizException(BizResultCode.DOCTOR_VISIT_CHAT_STATUS_ERROR);
        }
        VisitChatVo visitChatVo = new VisitChatVo();
        logService.setVisitLog(hosVisit, StatusEnumUtil.STARTED,
                logService.getDoctorUserName() + "医生开始与患者" + logService.getSickName(hosVisit.getHosSickId()) + "的在线复诊聊天。");

        hosVisit.setStatus(StatusEnumUtil.STARTED);
        String now = DateUtil.now();
        hosVisit.setStartTime(now);
        hosVisit.setHaveTime(now);

        int update = hosVisitMapper.updateById(hosVisit);
        if (update > 0) {
            serviceStartVisitChat(hosVisit);
        }
        visitChatVo.setMsg("开始在线复诊");
        sendStartVisitIm(hosVisit.getOrderNum());


        String haveTime = hosVisit.getHaveTime();
        Date haveTimeParse = DateUtil.parse(haveTime, "yyyy-MM-dd HH:mm:ss");
        TBase tBase = tBaseMapper.selectByKey("DOCTOR_VISIT_TIMES");
        String content = tBase.getContent();
        String betweenTime = TimeUtil.getBetweenTime(DateUtil.date(), haveTimeParse, Integer.parseInt(content));
        visitChatVo.setCutDown(betweenTime);
        visitChatVo.setOrderStatus(hosVisit.getStatus());
        return visitChatVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitChatVo stopVisitChatV2(String id) {
        HosVisit hosVisit = hosVisitMapper.selectById(id);
        String status = hosVisit.getStatus();
        if (!status.equals(StatusEnumUtil.STARTED)) {
            throw new BizException(BizResultCode.DOCTOR_VISIT_CHAT_STATUS_ERROR);
        }
        VisitChatVo visitChatVo = new VisitChatVo();
        visitChatVo.setMsg("在线复诊结束成功");

        logService.setVisitLog(hosVisit, StatusEnumUtil.END,
                logService.getDoctorUserName() + "医生结束与患者" + logService.getSickName(hosVisit.getHosSickId()) + "的在线复诊聊天。");

        hosVisit.setEndTime(DateUtil.now());
        hosVisit.setStatus(StatusEnumUtil.ENDED);
        hosVisitMapper.updateById(hosVisit);

        Map<String, Object> mapIm = new HashMap<>();
        mapIm.put("msgType", IMMsgType.ServiceAppraiseCard);
        mapIm.put("orderNum", hosVisit.getOrderNum());
        mapIm.put("orderId", hosVisit.getId());
        mapIm.put("orderType", "VISIT");
        mapIm.put("businessID", "MSHCustomMsg");
        boolean sendImSystemMsg = imService.sendImSystemMsg(PATIENT + hosVisit.getPatientUserId(),
                DOCTOR + hosVisit.getDoctorUserId(), "本次在线复诊已结束", IMMsgType.EndText,
                "本次在线复诊已结束", "", "");
        if (sendImSystemMsg) {
            imService.sendPostHttpRequest(DOCTOR + hosVisit.getDoctorUserId(),
                    PATIENT + hosVisit.getPatientUserId(), mapIm, "在线复诊评价", "", "");
        }
        return visitChatVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitChatVo startRevisitChatV2(String id) {
        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        String status = hosRevisit.getStatus();
        if (!(status.equals(StatusEnumUtil.START) || status.equals(StatusEnumUtil.HAVE))) {
            throw new BizException(BizResultCode.DOCTOR_VISIT_CHAT_STATUS_ERROR);
        }
        VisitChatVo visitChatVo = new VisitChatVo();
        visitChatVo.setIsCanChat("1");
        if (status.equals(StatusEnumUtil.START)) {
            visitChatVo.setMsg("复诊已开始");
        } else {
            Date revisitStartTime = DateUtil.parse(hosRevisit.getRevisitStartTime(), "yyyy-MM-dd HH:mm:ss");
            Date revisitEndTime = DateUtil.parse(hosRevisit.getRevisitEndTime(), "yyyy-MM-dd HH:mm:ss");

            if (WeekUtil.inTime(DateUtil.date(), revisitStartTime, revisitEndTime)) {
                logService.setRevisitLog(hosRevisit, StatusEnumUtil.START,
                        logService.getDoctorUserName() + "医生开始与患者" + logService.getSickName(hosRevisit.getHosSickId()) + "的复诊聊天。");
                HosRevisit updHosRevisit = new HosRevisit();
                updHosRevisit.setId(id);
                updHosRevisit.setStatus(StatusEnumUtil.START);
                updHosRevisit.setStartTime(DateUtil.now());
                hosRevisitMapper.updateById(updHosRevisit);
                visitChatVo.setMsg("开始复诊");

                QueryWrapper<HosRevisitImg> hosRevisitImgQueryWrapper = new QueryWrapper<>();
                hosRevisitImgQueryWrapper.eq("hos_revisit_id", id);
                List<HosRevisitImg> hosRevisitImgs = hosRevisitImgMapper.selectList(hosRevisitImgQueryWrapper);
                List<String> imgList = new ArrayList<>();
                for (HosRevisitImg hosRevisitImg : hosRevisitImgs) {
                    imgList.add(hosRevisitImg.getImg());
                }
                hosRevisit.setImgList(imgList);
                hosRevisit.setMsgType(IMMsgType.RevisitOrderCard);
                hosRevisit.setBusinessID("MSHCustomMsg");

                sendStartRevisitIm(hosRevisit.getOrderNum());
            } else {
                visitChatVo.setMsg("不在复诊预约时间");
                visitChatVo.setIsCanChat("0");
            }
        }
        visitChatVo.setCutDown("0");
        DateTime now = DateUtil.date();
        hosRevisit = hosRevisitMapper.selectById(id);
        if (StrUtil.isNotBlank(hosRevisit.getStartTime())) {
            DateTime startTime = DateUtil.offsetDay(DateUtil.parseDateTime(hosRevisit.getStartTime()), 1);
            //当前在预约结束时间之前true
            if (now.before(startTime)) {
                visitChatVo.setCutDown(String.valueOf(DateUtil.between(startTime, now, DateUnit.SECOND)));
            }
        }
        visitChatVo.setOrderStatus(hosRevisit.getStatus());
        return visitChatVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitChatVo stopRevisitChatV2(String id) {

        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        logService.setRevisitLog(hosRevisit, StatusEnumUtil.END,
                logService.getDoctorUserName() + "医生结束与患者" + logService.getSickName(hosRevisit.getHosSickId()) + "的复诊聊天。");
        if (!hosRevisit.getStatus().equals("START")) {
            throw new BizException(BizResultCode.DOCTOR_VISIT_CHAT_STATUS_ERROR);
        }
        VisitChatVo visitChatVo = new VisitChatVo();
        visitChatVo.setMsg("复诊结束成功");

        HosRevisit updHosRevisit = new HosRevisit();
        updHosRevisit.setId(id);
        updHosRevisit.setStatus(StatusEnumUtil.END);
        updHosRevisit.setEndTime(DateUtil.now());
        hosRevisitMapper.updateById(updHosRevisit);

        Map<String, Object> mapIm = new HashMap<>();
        mapIm.put("msgType", IMMsgType.ServiceAppraiseCard);
        mapIm.put("orderNum", hosRevisit.getOrderNum());
        mapIm.put("orderId", hosRevisit.getId());
        mapIm.put("orderType", "REVISIT");
        mapIm.put("businessID", "MSHCustomMsg");
        boolean sendImSystemMsg = imService.sendImSystemMsg(PATIENT + hosRevisit.getPatientUserId(),
                DOCTOR + hosRevisit.getDoctorUserId(), "本次在线复诊已结束", IMMsgType.EndText,
                "本次在线复诊已结束", "", "");
        if (sendImSystemMsg) {
            imService.sendPostHttpRequest(DOCTOR + hosRevisit.getDoctorUserId(),
                    PATIENT + hosRevisit.getPatientUserId(), mapIm, "复诊评价", "", "");
        }
        return visitChatVo;
    }
}
