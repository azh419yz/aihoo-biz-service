package com.aihoo.domain.im.service.impl;

import cn.hutool.core.date.DateUtil;
import com.aihoo.domain.im.model.entity.PushMessage;
import com.aihoo.domain.im.model.mapper.PushMessageMapper;
import com.aihoo.domain.im.service.PushMessageService;
import com.aihoo.security.AuthUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Mr.Li
 * @since 2020-10-16
 */
@Service
public class PushMessageServiceImpl extends ServiceImpl<PushMessageMapper, PushMessage> implements PushMessageService {

    @Override
    public int insertOne(String title, String intro, String messageType, String otherId, String content, String isPush) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(Objects.requireNonNull(AuthUtil.getLoginUser()).getId());
        pushMessage.setType("DOCKER_PERSONAL");
        pushMessage.setPesronalId(AuthUtil.getLoginUser().getId());
        pushMessage.setTitle(title);
        pushMessage.setIntro(intro);
        pushMessage.setMessageType(messageType);
        pushMessage.setOtherId(otherId);
        pushMessage.setContent(content);
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setSetTime(DateUtil.now());
        return baseMapper.insert(pushMessage);
    }

    @Override
    public int insertDoctor(String title, String doctorId, String intro, String messageType, String otherId, String content, String isPush) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(AuthUtil.getLoginUserId());
        pushMessage.setType("DOCKER_PERSONAL");
        pushMessage.setPesronalId(doctorId);
        pushMessage.setTitle(title);
        pushMessage.setIntro(intro);
        pushMessage.setMessageType(messageType);
        pushMessage.setOtherId(otherId);
        pushMessage.setContent(content);
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setSetTime(DateUtil.now());
        return baseMapper.insert(pushMessage);
    }

    @Override
    public int insertPatient(String title, String patientId, String intro, String messageType, String otherId, String content, String isPush) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(Objects.requireNonNull(AuthUtil.getLoginUser()).getId());
        pushMessage.setType("PATIENT_PERSONAL");
        pushMessage.setPesronalId(patientId);
        pushMessage.setTitle(title);
        pushMessage.setIntro(intro);
        pushMessage.setMessageType(messageType);
        pushMessage.setOtherId(otherId);
        pushMessage.setContent(content);
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setSetTime(DateUtil.now());
        return baseMapper.insert(pushMessage);
    }

    @Override
    public int insertPatient(String title, String patientId, String intro, String messageType, String otherId, String content, String isPush, String setTime) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(AuthUtil.getLoginUserId());
        pushMessage.setType("PATIENT_PERSONAL");
        pushMessage.setPesronalId(patientId);
        pushMessage.setTitle(title);
        pushMessage.setIntro(intro);
        pushMessage.setMessageType(messageType);
        pushMessage.setOtherId(otherId);
        pushMessage.setContent(content);
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setSetTime(setTime);
        return baseMapper.insert(pushMessage);
    }

    @Override
    public int insertAdmin(String title, String adminId, String intro, String messageType, String otherId, String content, String isPush) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(AuthUtil.getLoginUserId());
        pushMessage.setType("ADMIN_PERSONAL");
        pushMessage.setPesronalId(adminId);
        pushMessage.setTitle(title);
        pushMessage.setIntro(intro);
        pushMessage.setMessageType(messageType);
        pushMessage.setOtherId(otherId);
        pushMessage.setImg(null);
        pushMessage.setContent(content);
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setPushTime(DateUtil.now());
        pushMessage.setSetTime(DateUtil.now());
        return baseMapper.insert(pushMessage);
    }
}
