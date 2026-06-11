package com.aihoo.api.doctor.app.service;

import com.aihoo.api.doctor.app.controller.request.ImSendGroupMsgRequest;
import com.aihoo.api.doctor.app.controller.request.ImWithdrawMsgRequest;

public interface IMService {

    <T> boolean sendPostHttpRequest(String From_Account, String To_Account, T orderDetails, String desc, String ext, String sound);

    <T> boolean sendImSystemMsg(String From_Account, String To_Account, String contentData, String msgType, String desc, String ext, String sound);

    boolean sendGroupMsg(ImSendGroupMsgRequest req);

    boolean withdrawMsg(ImWithdrawMsgRequest req);
}
