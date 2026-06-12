package com.aihoo.api.doctor.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.im.model.mapper.PushMessageMapper;
import com.aihoo.domain.im.model.entity.PushMessage;
import com.aihoo.api.doctor.app.service.PushMessageService;
import com.aihoo.api.doctor.common.utils.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @program: aihoo-root
 * @description: 增加待推送消息
 * @author: Mr.Li
 * @create: 2020-10-16 15:08
 **/
@Service("doctorApiPushMessageServiceImpl")
public class PushMessageServiceImpl extends ServiceImpl<PushMessageMapper, PushMessage> implements PushMessageService {

    /**
     * 保存消息
     *
     * @param title       消息标题
     * @param intro       消息简介
     * @param messageType 消息类型 TEXT-文字公告 IMAGE_TEXT-图文公告 IMAGE-图文 VOICE-语音 VIDEO-视频 REVISIT-复诊
     * @param otherId     消息第三方ID 更加消息类型 不同的消息详情ID 【IMAGE VOICE VIDEO】t_hos_visit外键ID REVISIT t_hos_revisit外键ID
     * @param content     消息内容
     * @param isPush      是否已推送 1:已推送 0:未推送
     * @return
     */
    @Override
    public int insertOne(String title, String intro, String messageType, String otherId, String content, String isPush) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(Objects.requireNonNull(AuthUtil.getLoginUser()).getId());
        pushMessage.setType("DOCKER_PERSONAL");//PATIENT_PERSONAL-患者个人 DOCKER_PERSONAL-医生个人
        pushMessage.setPesronalId(AuthUtil.getLoginUser().getId());
        pushMessage.setTitle(title);//消息标题
        pushMessage.setIntro(intro);//消息简介
        pushMessage.setMessageType(messageType);//消息类型 TEXT-文字公告 IMAGE_TEXT-图文公告 IMAGE-图文 VOICE-语音 VIDEO-视频 REVISIT-复诊
        pushMessage.setOtherId(otherId);
        pushMessage.setContent(content);//消息内容
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setSetTime(DateUtil.now());
        return baseMapper.insert(pushMessage);
    }

    @Override
    public int insertDoctor(String title, String doctorId, String intro, String messageType, String otherId, String content, String isPush) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(AuthUtil.getLoginUserIds());
        pushMessage.setType("DOCKER_PERSONAL");//PATIENT_PERSONAL-患者个人 DOCKER_PERSONAL-医生个人
        pushMessage.setPesronalId(doctorId);
        pushMessage.setTitle(title);//消息标题
        pushMessage.setIntro(intro);//消息简介
        pushMessage.setMessageType(messageType);//消息类型 TEXT-文字公告 IMAGE_TEXT-图文公告 IMAGE-图文 VOICE-语音 VIDEO-视频 REVISIT-复诊
        pushMessage.setOtherId(otherId);
        pushMessage.setContent(content);//消息内容
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setSetTime(DateUtil.now());
        return baseMapper.insert(pushMessage);
    }

    @Override
    public int insertPatient(String title, String patientId, String intro, String messageType, String otherId, String content, String isPush) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(Objects.requireNonNull(AuthUtil.getLoginUser()).getId());
        pushMessage.setType("PATIENT_PERSONAL");//PATIENT_PERSONAL-患者个人 DOCKER_PERSONAL-医生个人
        pushMessage.setPesronalId(patientId);
        pushMessage.setTitle(title);//消息标题
        pushMessage.setIntro(intro);//消息简介
        pushMessage.setMessageType(messageType);//消息类型 TEXT-文字公告 IMAGE_TEXT-图文公告 IMAGE-图文 VOICE-语音 VIDEO-视频 REVISIT-复诊
        pushMessage.setOtherId(otherId);
        pushMessage.setContent(content);//消息内容
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setSetTime(DateUtil.now());
        return baseMapper.insert(pushMessage);
    }

    @Override
    public int insertPatient(String title, String patientId, String intro, String messageType, String otherId, String content, String isPush, String setTime) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(AuthUtil.getLoginUserIds());
        pushMessage.setType("PATIENT_PERSONAL");//PATIENT_PERSONAL-患者个人 DOCKER_PERSONAL-医生个人
        pushMessage.setPesronalId(patientId);
        pushMessage.setTitle(title);//消息标题
        pushMessage.setIntro(intro);//消息简介
        pushMessage.setMessageType(messageType);//消息类型 TEXT-文字公告 IMAGE_TEXT-图文公告 IMAGE-图文 VOICE-语音 VIDEO-视频 REVISIT-复诊
        pushMessage.setOtherId(otherId);
        pushMessage.setContent(content);//消息内容
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setSetTime(setTime);
        return baseMapper.insert(pushMessage);
    }

    @Override
    public int insertAdmin(String title, String adminId, String intro, String messageType, String otherId, String content, String isPush) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(AuthUtil.getLoginUserIds());
        pushMessage.setType("ADMIN_PERSONAL");//PATIENT_PERSONAL-患者个人 DOCKER_PERSONAL-医生个人
        pushMessage.setPesronalId(adminId);
        pushMessage.setTitle(title);//消息标题
        pushMessage.setIntro(intro);//消息简介
        pushMessage.setMessageType(messageType);//消息类型 TEXT-文字公告 IMAGE_TEXT-图文公告 IMAGE-图文 VOICE-语音 VIDEO-视频 REVISIT-复诊
        pushMessage.setOtherId(otherId);
        pushMessage.setImg(null);//消息图片
        pushMessage.setContent(content);//消息内容
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setPushTime(DateUtil.now());
        pushMessage.setSetTime(DateUtil.now());
        return baseMapper.insert(pushMessage);
    }
}
