package com.aihoo.domain.im.service;

import com.aihoo.domain.im.dto.ImSendGroupMsgRequest;
import com.aihoo.domain.im.dto.ImWithdrawMsgRequest;

public interface IMService {

    <T> boolean sendPostHttpRequest(String From_Account, String To_Account, T orderDetails, String desc, String ext, String sound);

    <T> boolean sendImSystemMsg(String From_Account, String To_Account, String contentData, String msgType, String desc, String ext, String sound);

    boolean sendGroupMsg(ImSendGroupMsgRequest req);

    boolean withdrawMsg(ImWithdrawMsgRequest req);
}
