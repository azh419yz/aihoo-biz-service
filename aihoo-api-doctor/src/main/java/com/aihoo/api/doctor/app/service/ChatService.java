package com.aihoo.api.doctor.app.service;

import com.aihoo.api.doctor.app.controller.vo.VisitChatVo;

import java.util.List;
import java.util.Map;

/**
 * @program: aihoo-root
 * @description: 消息
 * @author: Mr.Li
 * @create: 2020-09-26 18:47
 **/
public interface ChatService {
    /**
     * 开始问诊
     *
     * @param id
     * @return
     */
    Map<String, String> startVistChat(String id);

    /**
     * 结束问诊
     *
     * @param map
     * @return
     */
    Map<String, String> stopVistChat(Map<String, String> map);

    /**
     * 开始复诊
     *
     * @param id
     * @return
     */
    Map<String, String> startRevisitChat(String id);

    /**
     * 结束复诊
     *
     * @param map
     * @return
     */
    Map<String, String> stopRevisitChat(Map<String, String> map);

    /**
     * 常用语查询
     *
     * @return
     */
    List chatWords(Map<String, Object> map);

    /**
     * 常用语修改或添加
     *
     * @param map
     * @return
     */
    String updateChatWords(Map<String, Object> map);

    /**
     * 常用语删除
     *
     * @param map
     * @return
     */
    String deleteChatWords(Map<String, Object> map);

    /**
     * 系统公告
     *
     * @param map
     * @return
     */
    Object pushMessage(Map<String, Object> map);

    /**
     * 从消息进入聊天室
     * @param doctorId
     * @param patientId
     * @return
     */
    Map getChatMsg(String doctorId, String patientId);


    VisitChatVo startVisitChatV2(String id);

    VisitChatVo stopVisitChatV2(String id);

    VisitChatVo startRevisitChatV2(String id);

    VisitChatVo stopRevisitChatV2(String id);
}
