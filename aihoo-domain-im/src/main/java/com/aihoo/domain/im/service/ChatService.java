package com.aihoo.domain.im.service;

import com.aihoo.domain.im.dto.VisitChatVo;

import java.util.List;
import java.util.Map;

public interface ChatService {
    Map<String, String> startVistChat(String id);

    Map<String, String> stopVistChat(Map<String, String> map);

    Map<String, String> startRevisitChat(String id);

    Map<String, String> stopRevisitChat(Map<String, String> map);

    List chatWords(Map<String, Object> map);

    String updateChatWords(Map<String, Object> map);

    String deleteChatWords(Map<String, Object> map);

    Object pushMessage(Map<String, Object> map);

    Map getChatMsg(String doctorId, String patientId);


    VisitChatVo startVisitChatV2(String id);

    VisitChatVo stopVisitChatV2(String id);

    VisitChatVo startRevisitChatV2(String id);

    VisitChatVo stopRevisitChatV2(String id);
}
