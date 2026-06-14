package com.aihoo.api.doctor.app.controller;

import com.aihoo.common.JsonResult;
import com.aihoo.domain.im.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: aihoo-root
 * @description: 消息开始，结束
 * @author: Mr.Li
 * @create: 2020-09-26 18:27
 **/
@RestController
@RequestMapping("/api/v1/doctorChat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    /**
     * 开始问诊
     *
     * @param map id-订单id
     * @return
     */
    @ResponseBody
    @PostMapping("/startVistChat")
    public JsonResult startVistChat(@RequestBody Map<String, String> map) {
        if (map.get("id") == null) {
            return JsonResult.error("请传复诊订单id");
        }
        try {
            Map<String, String> stringStringMap = chatService.startVistChat(map.get("id").toString());
            String isSucc = stringStringMap.get("is_succ");
            if (isSucc.equals("ERROR")) {
                return JsonResult.error(stringStringMap.get("msg"));
            }
            stringStringMap.remove("is_succ");
            return JsonResult.ok().put("data", stringStringMap);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 结束问诊
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/stopVistChat")
    public JsonResult stopVistChat(@RequestBody Map<String, String> map) {
        if (map.get("id") == null) {
            return JsonResult.error("未传参");
        }
        try {
            Map<String, String> stringStringMap = chatService.stopVistChat(map);
            String isSucc = stringStringMap.get("is_succ");
            if (isSucc.equals("ERROR")) {
                return JsonResult.error(stringStringMap.get("msg"));
            }
            stringStringMap.remove("is_succ");
            return JsonResult.ok().put("data", stringStringMap);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 开始复诊
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/startRevisitChat")
    public JsonResult startRevisitChat(@RequestBody Map<String, String> map) {
        if (map.get("id") == null) {
            return JsonResult.error("未传参");
        }
        try {
            Map<String, String> stringStringMap = chatService.startRevisitChat(map.get("id"));
            String isSucc = stringStringMap.get("is_succ");
            if (isSucc.equals("ERROR")) {
                return JsonResult.error(stringStringMap.get("msg"));
            }
            stringStringMap.remove("is_succ");
            return JsonResult.ok().put("data", stringStringMap);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 结束复诊
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/stopRevisitChat")
    public JsonResult stopRevisitChat(@RequestBody Map<String, String> map) {
        if (map.get("id") == null) {
            return JsonResult.error("未传参");
        }
        try {
            Map<String, String> stringStringMap = chatService.stopRevisitChat(map);
            String isSucc = stringStringMap.get("is_succ");
            if (isSucc.equals("ERROR")) {
                return JsonResult.error(stringStringMap.get("msg"));
            }
            stringStringMap.remove("is_succ");
            return JsonResult.ok().put("data", stringStringMap);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 常用语查询
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/commonWords")
    public JsonResult chatWords(@RequestBody Map map) {
        try {
            List result = chatService.chatWords(map);
            return JsonResult.ok().put("data", result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("查询异常");
        }
    }

    /**
     * 常用语修改或添加
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/updateChatWords")
    public JsonResult updateChatWords(@RequestBody Map<String, Object> map) {
        Object content = map.get("content");
        if (content == null) {
            return JsonResult.error("未传入添加或修改内容");
        }
        try {
            String result = chatService.updateChatWords(map);
            return JsonResult.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("修改或添加失败");
        }
    }

    /**
     * 常用语删除
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/deleteChatWords")
    public JsonResult deleteChatWords(@RequestBody Map<String, Object> map) {
        Object id = map.get("id");
        if (id == null) {
            return JsonResult.error("未传入常用语id");
        }
        try {
            String result = chatService.deleteChatWords(map);
            return JsonResult.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("修改或添加失败");
        }
    }

    /**
     * 系统公告
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/pushMessage")
    public JsonResult pushMessage(@RequestBody Map<String, Object> map) {
        Object noticeType = map.get("noticeType");//通知类型 ALL-全部通知 SYSTEM-系统公告 SERVICE-服务提醒
        if (noticeType == null) {
            return JsonResult.error("未传入通知类型：ALL-全部通知，SYSTEM-系统公告，SERVICE-服务提醒");
        }
        try {
            Object result = chatService.pushMessage(map);
            return JsonResult.ok().put("data", result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("推送信息查询失败失败");
        }
    }

    /**
     * 从消息列表开始聊天
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/startChat")
    public JsonResult getChatMsg(@RequestBody Map<String, Object> map) {
        /*2021-1-27 修改为身份证*/
        if (map.get("doctorId") == null) {
            return JsonResult.error("医生id不能为空");
        }
        if (map.get("patientId") == null) {
            return JsonResult.error("患者id不能为空");
        }
        String doctorId = map.get("doctorId").toString();
        String patientId = map.get("patientId").toString();
        try {
            Map result = chatService.getChatMsg(doctorId, patientId);
            if (null == result) {
                return JsonResult.ok();
            }
            return JsonResult.ok().put("data", result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("推送信息查询失败失败");
        }
    }
}
