package com.aihoo.api.doctor.app.controller;


import com.aihoo.common.JsonResult;
import com.alibaba.fastjson2.JSONObject;
import com.aihoo.api.doctor.app.controller.vo.ChangeBalance;
import com.aihoo.api.doctor.app.service.WorkService;
import com.ibm.icu.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @param
 * @Description: 工作台内容
 * @Date: 2020/9/17
 * @Return:
 **/
@RestController
@RequestMapping("/api/v1/work")
public class WorkController {

    @Autowired
    private WorkService workService;

    /**
     * @Description: 工作台详细信息
     * @Return: com.aihoo.common.JsonResult
     */
    @ResponseBody
    @PostMapping("/workbenchtest")
    public JsonResult workbenchtest() {
        try {
            Map<String, Object> workbench = workService.getWorkbenchTest();
            return JsonResult.ok().put("data", workbench);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * @Description: 工作台详细信息
     * @Return: com.aihoo.common.JsonResult
     */
    @ResponseBody
    @PostMapping("/workbench")
    public JsonResult getWorkbench() {
        try {
            Map<String, Object> workbench = workService.getWorkbench();
            return JsonResult.ok().put("data", workbench);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * @Description: 医生工作台专家咨询订单
     * @Return: com.aihoo.common.JsonResult
     */
    @ResponseBody
    @PostMapping("/visits")
    public JsonResult visitOrderList() {
        try {
            List result = workService.visitOrderList();
            return JsonResult.ok().put("data", result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * @Description: 医生工作台复诊订单
     * @Return: com.aihoo.common.JsonResult
     */
    @ResponseBody
    @PostMapping("/revisits")
    public JsonResult revisitOrderList() {
        try {
            List result = workService.revisitOrderList();
            return JsonResult.ok().put("data", result);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * @Description: 医查询生个人信息
     * @Date: 2020/9/28
     * @Return: com.aihoo.common.JsonResult
     **/
    @ResponseBody
    @PostMapping("/doctorMessage")
    @CrossOrigin
    public JsonResult doctorMessage() {
        try {
            JSONObject jsonObject = workService.doctorMessage();
            return JsonResult.ok().put("data", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * @Description: 保存医擅长和简介
     * @Date: 2020/9/28
     * @Return: com.aihoo.common.JsonResult
     **/
    @ResponseBody
    @PostMapping("/saveContent")
    public JsonResult saveContent(@RequestBody Map<String, String> map) {
        try {
            JSONObject jsonObject = workService.saveContent(map);
            if ("error".equals(jsonObject.get("is_succ"))) {
                return JsonResult.error("修改失败");
            }
            return JsonResult.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 医生账单
     *
     * @param map page limit
     * @return com.aihoo.common.JsonResult
     */
    @ResponseBody
    @PostMapping("/balanceLog")
    public JsonResult balanceLog(@RequestBody Map<String, String> map) {
        try {
            List<ChangeBalance> list = workService.balanceLog(map);
            Double add = list.stream().filter(x->x.getType().contains("ADD"))
                .mapToDouble(x->Double.valueOf(x.getChangeAmount().substring(1))).sum();
            Double lose = list.stream().filter(x->x.getType().contains("LOSE"))
                .mapToDouble(x->Double.valueOf(x.getChangeAmount().substring(1))).sum();
            String count = String.valueOf(new BigDecimal(add.toString()).subtract(new BigDecimal(lose.toString())));
            // return JsonResult.ok().put("data", list).put("count",count);
            // 2022-06-02 APP端要求 list 和count 都放到count中
            JSONObject json = new JSONObject();
            json.put("list", list);
            json.put("count",count);
            return JsonResult.ok().put("data",json);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    @ResponseBody
    @PostMapping("/balanceLogType")
    public JsonResult balanceLogType(@RequestBody Map<String, String> map) {
        try {
            List list = workService.balanceLogType(map);
            return JsonResult.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 医生接诊设置
     *
     * @return com.aihoo.common.JsonResult
     */
    @ResponseBody
    @PostMapping("/doctorSet")
    public JsonResult doctorSet() {
        try {
            JSONObject jsonObject = workService.doctorSet();
            return JsonResult.ok().put("data", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 医生某个接诊手段的排班时间
     *
     * @param map page limit
     * @Return: com.aihoo.common.JsonResult
     **/
    @ResponseBody
    @PostMapping("/doctorSetTimes")
    public JsonResult doctorSetTimes(@RequestBody Map<String, String> map) {
        //VOICE-语音问诊 VIDEO-视频问诊 REVISIT-复诊
        String type = map.get("type");
        if (type == null || !(type.equals("VOICE") || type.equals("VIDEO") || type.equals("REVISIT"))) {
            return JsonResult.error("type为空或类型有误");
        }
        try {
            List list = workService.doctorSetTimes(map);
            return JsonResult.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 登出
     */
    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult logout(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        boolean b = workService.logout(request);
        if (b) {
            return JsonResult.ok("退出成功");
        }
        return JsonResult.error("退出错误");
    }


    /**
     * @Author: 14891
     * @Description: 获取用户注销验证码
     * @Date: 2020/9/15
     * @Return: com.aihoo.common.JsonResult
     **/
    @PostMapping("/sendCancelCode")
    @ResponseBody
    public JsonResult sendCancelCode(@RequestBody Map<String, Object> map) {
        try {
            JSONObject jsonObject = workService.sendCancelCode(map);
            if ((int) jsonObject.get("is_succ") == 0) {
                return JsonResult.error(jsonObject.get("msg").toString());
            }
            jsonObject.remove("is_succ");
            return JsonResult.ok().put("data", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResult.error();
        }
        return JsonResult.ok();
    }

    /**
     * @Author: 14891
     * @Description: 用户注销
     * @Date: 2020/9/15
     * @Return: com.aihoo.common.JsonResult
     **/
    @PostMapping("/doctorUserCancel")
    @ResponseBody
    public JsonResult doctorUserCancel(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            if (null == map.get("code") || "".equals(map.get("code"))) {
                return JsonResult.error("请输入验证码");
            }
            if (null == map.get("remark") || "".equals(map.get("remark"))) {
                return JsonResult.error("请输入申请理由");
            }
            JSONObject jsonObject = workService.doctorUserCancel(map,request);
            if ((int) jsonObject.get("is_succ") == 0) {
                return JsonResult.error(jsonObject.get("msg").toString());
            }
            jsonObject.remove("is_succ");
            return JsonResult.ok().put("data", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }

    }

}
