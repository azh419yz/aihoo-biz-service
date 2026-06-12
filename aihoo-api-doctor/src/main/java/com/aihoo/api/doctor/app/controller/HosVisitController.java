package com.aihoo.api.doctor.app.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aihoo.common.JsonResult;
import com.alibaba.fastjson2.JSON;
import com.aliyuncs.utils.StringUtils;
import com.aihoo.api.doctor.app.controller.vo.HosOrder;
import com.aihoo.domain.visit.model.mapper.HosVisitMapper;
import com.aihoo.api.doctor.app.model.HosVisit;
import com.aihoo.api.doctor.app.service.HosVisitService;
import com.aihoo.api.doctor.app.service.IMService;
import com.aihoo.api.doctor.common.utils.JacksonHelper;

import io.swagger.v3.oas.annotations.Hidden;

/**
 * <p>
 * 在线问诊信息表 前端控制器
 * </p>
 *
 * @author updateStatusHavemcp
 * @since 2020-09-18
 */
@Controller
@RequestMapping("/api/v1/hosVisit")
public class HosVisitController {

    @Autowired
    private HosVisitService hosVisitService;

    @Autowired
    private IMService imService;

    /**
     * 展示问诊订单
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/count")
    public JsonResult VisitOrderCount(@RequestBody Map<String, String> map) {
        try {
            Map<String, String> selectCount = hosVisitService.visitOrderCount(map);
            return JsonResult.ok().put("data", selectCount);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 展示问诊订单
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/visitOrderList")
    public JsonResult VisitOrderList(@RequestBody Map<String, String> map) {
        try {
            List list = hosVisitService.visitOrderList(map);
            return JsonResult.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 问诊订单接单
     *
     * @param map id：订单id     msg：取消说明
     * @return
     */
    @ResponseBody
    @PostMapping("/haveVisit")
    public JsonResult haveVisit(@RequestBody Map<String, String> map) {
        if (map.get("id") == null) {
            return JsonResult.error("未传订单id");
        }
        try {
            String s = hosVisitService.haveVisit(map);
            if (s.equals("ERROR")) {
                return JsonResult.error("已接单或接单失败");
            }
            return JsonResult.ok(s);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 医生-》问诊-》拒单
     *
     * @param map
     * @return
     */
    /*@ResponseBody
    @PostMapping("/refuseVisit")
    public JsonResult refuseVisit(@RequestBody Map<String, String> map) {
        if (map.get("id") == null) {
            return JsonResult.error("未传入参数id");
        }
        try {
            String status = hosVisitService.refuseVisit(map);
            return JsonResult.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }*/

    /**
     * 医生-》问诊-》详情
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/visitData")
    public JsonResult visitData(String id) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(id)) {
            return JsonResult.error("未传入参数id");
        }
        try {
            HosOrder status = hosVisitService.visitData(id);
            return JsonResult.ok().put("data", status);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 写医嘱
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("writeVisitResult")
    public JsonResult writeVisitResult(@RequestBody Map<String, String> map) {
        String id = map.get("id");//id
//        String firstVisit = map.get("firstVisit");//医嘱
        String doctorAdvice = map.get("doctorAdvice");//医嘱
        if (StringUtils.isEmpty(id)) {
            return JsonResult.error("订单id不能为空");
        }
//        if (StringUtils.isEmpty(firstVisit)) {
//            return JsonResult.error("初步诊断不能为空");
//        }
        if (StringUtils.isEmpty(doctorAdvice)) {
            return JsonResult.error("医嘱不能为空");
        }
        try {
            String data = hosVisitService.writeVisitResult(map);
            return JsonResult.ok().put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 诊断结果页面
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("visitResult")
    public JsonResult visitResult(@RequestBody Map<String, String> map) {
        String id = map.get("id");//id
        if (id == null) {
            return JsonResult.error("订单id不能为空");
        }
        try {
            Map data = hosVisitService.visitResult(map);
            return JsonResult.ok().put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("未查询到数据");
        }
    }

    @Autowired
    private HosVisitMapper hosVisitMapper;

    /**
     * IM发送消息测试接口
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/imSend")
    @Hidden
    public JsonResult imSend() {
        try {
            HosVisit hosVisit = hosVisitMapper.selectById("49");
            String toJSONString = JSON.toJSONString(hosVisit);
            System.out.println(toJSONString);
            Map<String, Object> stringObjectMap = JacksonHelper.jsonToMap("{\"Desc\":\"\",\"Data\":\"{\"businessID\":\"notify\",\"title\":\"通知消息名称\",\"linkTitle\":\"详情\",\"version\":4}\",\"Ext\":\"\",\"Sound\":\"\"}");
            System.out.println(stringObjectMap);
            String data = stringObjectMap.get("Data").toString();

            imService.sendPostHttpRequest("DOCTOR_27", "DOCTOR_12", toJSONString, null, "", "");
            return JsonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }
}
