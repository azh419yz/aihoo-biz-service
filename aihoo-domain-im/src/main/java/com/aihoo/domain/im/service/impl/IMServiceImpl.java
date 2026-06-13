package com.aihoo.domain.im.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aihoo.util.UUIDUtil;
import com.aihoo.common.BizResult;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.aihoo.domain.im.dto.ImSendGroupMsgRequest;
import com.aihoo.domain.im.dto.ImWithdrawMsgRequest;
import com.aihoo.domain.im.dto.ImSendGroupMsgRespVo;
import com.aihoo.domain.im.model.entity.ImMsg;
import com.aihoo.domain.im.model.entity.ImMsgContent;
import com.aihoo.domain.im.service.IMService;
import com.aihoo.domain.im.service.ImMsgContentService;
import com.aihoo.domain.im.service.ImMsgService;
import com.aihoo.util.JacksonHelper;
import com.aihoo.util.TencentImGroupUtil;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
public class IMServiceImpl implements IMService {

    private final TencentImGroupUtil tencentImGroupUtil;
    private final ImMsgService imMsgService;
    private final ImMsgContentService imMsgContentService;
    Log log = LogFactory.get();

    public IMServiceImpl(TencentImGroupUtil tencentImGroupUtil, ImMsgService imMsgService, ImMsgContentService imMsgContentService) {
        this.tencentImGroupUtil = tencentImGroupUtil;
        this.imMsgService = imMsgService;
        this.imMsgContentService = imMsgContentService;
    }

    /**
     * 发送IM聊天信息
     *
     * @param From_Account 发送者(就诊人ID) 格式: PATIENT+就诊人ID
     * @param To_Account   接收者（医生ID） 格式: DOCTOR+医生ID
     * @param orderDetails 订单详情
     */
    @Override
//    @Async(value = "asyncExecutor")
    public <T> boolean sendPostHttpRequest(String From_Account, String To_Account, T orderDetails, String desc, String ext, String sound) {
        HashMap<Object, Object> map = Maps.newHashMap();
        map.clear();
        try {
            Integer MsgRandom = RandomUtil.randomInt(2000000000);
            long MsgTimeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
            String sdkappid = "1400400517";
            String identifier = "administrator";
            String usersig = "eJw1jlELgjAcxL-LnsPmplsKPfQSQQphOuhxsJn-Sl1zmBB999TsuJe74wf3Rnly9vRgwGoU*yFnGzxpNfe9tihGxMPolzt1l8aAGks-wHh06PNlA6UbByXMiFQ1NNA5K11r-zBcx0WloT2pXpJinZeXQgonomofpY8da7ObOUo2iIPJnkn12i6gg3r6xgilmPOAfr7AODXk";
            String random = UUIDUtil.randomUUID32();
            String host = "https://console.tim.qq.com/v4/openim/sendmsg?sdkappid=" + sdkappid + "&identifier=" + identifier + "&usersig=" + usersig + "&random=" + random + "&contenttype=json";
            Map<String, Object> sendImMap = new HashMap<>();
            sendImMap.put("SyncOtherMachine", 1);
            sendImMap.put("From_Account", From_Account);
            sendImMap.put("To_Account", To_Account);
            sendImMap.put("MsgRandom", MsgRandom);
            sendImMap.put("MsgTimeStamp", MsgTimeStamp);
            JSONArray MsgBody = new JSONArray();
            //自定义消息
            JSONObject MsgBodyDetails = new JSONObject();
            MsgBodyDetails.put("MsgType", "TIMCustomElem");
            JSONObject msgContent = new JSONObject();
            msgContent.put("Data", JSON.toJSONString(orderDetails));
            msgContent.put("Desc", desc);
            msgContent.put("Ext", "");
            msgContent.put("Sound", "");
            MsgBodyDetails.put("MsgContent", msgContent);
            MsgBody.add(MsgBodyDetails);
            sendImMap.put("MsgBody", MsgBody);

            JSONObject OfflinePushInfo = new JSONObject();
            OfflinePushInfo.put("PushFlag", 0);
            OfflinePushInfo.put("Title", desc);
            OfflinePushInfo.put("Desc", "");
            OfflinePushInfo.put("Ext", "");
            sendImMap.put("OfflinePushInfo", OfflinePushInfo);
//            log.info(MsgBodyDetails.get("MsgContent").toString());
            String result = HttpRequest.post(host)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(JSON.toJSONString(sendImMap))
                    .execute().body();
            Map<String, Object> imReturnMap = JacksonHelper.jsonToMap(result);
            if ("OK".equals(imReturnMap.get("ActionStatus"))) {
                //保存消息 todo  看IM官方文档，了解服务端发送消息后，会不会走回调接口
            }
            log.info("发送者:" + From_Account + "," + "接收者:" + To_Account + "," + "消息:" + JSON.toJSONString(sendImMap));
            log.info(imReturnMap.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("发送消息失败:" + "发送者:" + From_Account + "," + "接收者:" + To_Account + "异常信息:" + e);
            return false;
        }
    }

    /**
     * 发送IM聊天信息
     *
     * @param To_Account 接收者（医生ID） 格式: DOCTOR+医生ID
     */
    @Override
//    @Async(value = "asyncExecutor")
    public <T> boolean sendImSystemMsg(String From_Account, String To_Account, String contentData, String msgType, String desc, String ext, String sound) {
        try {
            Integer MsgRandom = RandomUtil.randomInt(2000000000);
            long MsgTimeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
            String sdkappid = "1400400517";
            String identifier = "administrator";
            String usersig = "eJw1jlELgjAcxL-LnsPmplsKPfQSQQphOuhxsJn-Sl1zmBB999TsuJe74wf3Rnly9vRgwGoU*yFnGzxpNfe9tihGxMPolzt1l8aAGks-wHh06PNlA6UbByXMiFQ1NNA5K11r-zBcx0WloT2pXpJinZeXQgonomofpY8da7ObOUo2iIPJnkn12i6gg3r6xgilmPOAfr7AODXk";
            String random = UUIDUtil.randomUUID32();
            String host = "https://console.tim.qq.com/v4/openim/sendmsg?sdkappid=" + sdkappid + "&identifier=" + identifier + "&usersig=" + usersig + "&random=" + random + "&contenttype=json";
            Map<String, Object> sendImMap = new HashMap<>();
            sendImMap.put("SyncOtherMachine", 1);
            sendImMap.put("From_Account", From_Account);
            sendImMap.put("To_Account", To_Account);
            sendImMap.put("MsgRandom", MsgRandom);
            sendImMap.put("MsgTimeStamp", MsgTimeStamp);
            JSONArray MsgBody = new JSONArray();
            //自定义消息1
            JSONObject MsgBodyDetailsText1 = new JSONObject();
            MsgBodyDetailsText1.put("MsgType", "TIMCustomElem");
            JSONObject msgContent1 = new JSONObject();
            JSONObject msgContentDetails1 = new JSONObject();
            msgContentDetails1.put("msg", contentData);
            msgContentDetails1.put("msgType", msgType);
            msgContentDetails1.put("businessID", "MSHCustomMsg");
            msgContent1.put("Data", JSON.toJSONString(msgContentDetails1));
            msgContent1.put("Desc", desc);
            msgContent1.put("Ext", ext);
            msgContent1.put("Sound", sound);
            MsgBodyDetailsText1.put("MsgContent", msgContent1);
            MsgBody.add(MsgBodyDetailsText1);
            JSONObject OfflinePushInfo = new JSONObject();
            OfflinePushInfo.put("PushFlag", 0);
            OfflinePushInfo.put("Title", desc);
            OfflinePushInfo.put("Desc", "");
            OfflinePushInfo.put("Ext", "");
            sendImMap.put("OfflinePushInfo", OfflinePushInfo);
            sendImMap.put("MsgBody", MsgBody);
            String result = HttpRequest.post(host)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(JSON.toJSONString(sendImMap))
                    .execute().body();
            Map<String, Object> imReturnMap = JacksonHelper.jsonToMap(result);
            log.info("发送者:" + From_Account + "," + "接收者:" + To_Account + "," + "消息:" + JSON.toJSONString(sendImMap));
            log.info(imReturnMap.toString());
            if ("OK".equals(imReturnMap.get("ActionStatus"))) {
                return true;
                //保存消息 todo  看IM官方文档，了解服务端发送消息后，会不会走回调接口
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            log.info("发送消息失败:" + "接收者:" + To_Account + "异常信息:" + e);
            return false;
        }
    }

    @Override
    public boolean sendGroupMsg(ImSendGroupMsgRequest req) {
        ImSendGroupMsgRespVo resp = tencentImGroupUtil.sendGroupMessage(req);
        if (resp.isSuccess()) {
            try {
                ImMsg imMsg = new ImMsg();
                imMsg.setOrderNum(req.getVisitNo());
                imMsg.setPeerReadStatus("0");
                imMsg.setMsgRandom(req.getRandom().toString());
                imMsg.setFromAccount(req.getFromAccount());
                imMsg.setToAccount(req.getToAccount());
                imMsg.setErrorInfo(resp.getErrorInfo());
                imMsg.setSendMsgResult("0");
                imMsg.setLoadParam(req.getLoadParam() != null ? req.getLoadParam().toString() : null);
                imMsg.setMsgSeq(resp.getMsgSeq().toString());
                imMsg.setMsgType(String.valueOf(req.getMsgType()));
                imMsg.setMsgTime(resp.getMsgTime().toString());
                imMsg.setCreateTimeStr(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) + "");
                boolean msg = imMsgService.save(imMsg);
                if (msg) {
                    String msgContent = null;
                    for (int i = 0; i < req.getMsgBody().size(); i++) {
                        ImMsgContent imMsgContent = new ImMsgContent();
                        imMsgContent.setImMsgId(imMsg.getId());
                        ImSendGroupMsgRequest.MessageBody reqMsgContent = req.getMsgBody().get(i);
                        String msgType = req.getMsgBody().get(i).getMsgType();
                        imMsgContent.setMsgType(msgType);
                        if ("TIMTextElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("文本消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMLocationElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("位置消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMFaceElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("表情消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMCustomElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("自定义消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMSoundElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("语音消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMImageElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("图像消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMFileElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("文件消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMVideoFileElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("视频消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        }
                        imMsgContent.setMsgContent(msgContent);
                        imMsgContent.setCreateTimeStr(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) + "");
                        imMsgContentService.save(imMsgContent);
                    }
                }
            } catch (Exception e) {
                log.info("异常:", e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean withdrawMsg(ImWithdrawMsgRequest req) {
        String visitNo = req.getMsgReq().split("_")[1];
        ImMsg msg = imMsgService.getOne(new LambdaQueryWrapper<ImMsg>()
                .eq(ImMsg::getMsgSeq, req.getMsgReq())
                .eq(ImMsg::getOrderNum, visitNo)
                .last("limit 1"));
        if (msg != null) {
            msg.setMsgStatus("WITHDRAW");
            return imMsgService.updateById(msg);
        }
        return false;
    }
}
