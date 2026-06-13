package com.aihoo.domain.im.service;

import com.aihoo.domain.im.model.entity.PushMessage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PushMessageService extends IService<PushMessage> {
    int insertOne(String title, String intro, String messageType, String otherId, String content, String isPush);

    int insertDoctor(String title, String doctorId, String intro, String messageType, String otherId, String content, String isPush);

    int insertPatient(String title, String patientId, String intro, String messageType, String otherId, String content, String isPush);

    int insertPatient(String title, String patientId, String intro, String messageType, String otherId, String content, String isPush, String setTime);

    int insertAdmin(String title, String adminId, String intro, String messageType, String otherId, String content, String isPush);
}
