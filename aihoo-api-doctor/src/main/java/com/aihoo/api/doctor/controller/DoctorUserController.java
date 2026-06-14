package com.aihoo.api.doctor.controller;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aihoo.common.JsonResult;
import com.alibaba.fastjson2.JSONObject;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 医生用户表 前端控制器
 * </p>
 *
 * @since 2020-09-15
 */
@RestController
@RequestMapping("/api/v1/doctorUser")
public class DoctorUserController {

    Log log = LogFactory.get();

    @Autowired
    private DoctorUserService doctorUserService;

    /***
     * @Description: 关于我们，隐私保护政策，法律申明
     * @Date: 2020/9/18
     * @Return: com.aihoo.common.JsonResult
     **/
    @RequestMapping("/all")
    public JsonResult PrivacyPolicyAll() {
        JSONObject data = new JSONObject();
        try {
            data.put("aboutUs", "https://www.baidu.com");
            data.put("privacyPolicy", "https://www.baidu.com");
            data.put("legal", "https://www.baidu.com");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
        return JsonResult.ok().put("data", data);
    }

    /**
     * @param
     * @Description: 隐私保护政策
     * @Date: 2020/9/15
     * @Return:
     **/
    @ResponseBody
    @PostMapping("/privacyPolicy")
    public JsonResult PrivacyPolicy() {
        JSONObject data = new JSONObject();
        try {
            data.put("privacyPolicyUrl", "https://www.baidu.com");
            data.put("legalStatementyUrl", "https://www.baidu.com");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
        return JsonResult.ok().put("data", data);
    }

    /**
     * @param
     * @Description: 获取验证码
     * @Date: 2020/9/15
     * @Return: com.aihoo.common.JsonResult
     **/
    @ResponseBody
    @PostMapping("/sendCode")
    public JsonResult sendCode(@RequestBody Map<String, Object> map) {
        try {
            //把手机号转成字符串方便向数据库传值
            String mobile = (String) map.get("mobile");
            //请输入手机号
            if (null == map.get("mobile") || "".equals(map.get("mobile"))) {
                return JsonResult.error("请输入手机号");
            }
            //数据库中查询不到相对应的手机号码
            DoctorUser doctorUser = doctorUserService.selectMobile(mobile);
            if (null == doctorUser || !"PASS".equals(doctorUser.getIsAuth())) {
                return JsonResult.error("手机号码暂未绑定");
            }
            JSONObject jsonObject = doctorUserService.sendCode(map);
            //现在先注释用到的时候再开启,方便测试
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
     * @Description: 医生页面登录
     * @Date: 2020/9/15
     * @Return: com.aihoo.common.JsonResult
     **/
    @ResponseBody
    @PostMapping("/phoneLogin")
    public JsonResult login(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            if (null == map.get("mobile") || "".equals(map.get("mobile"))) {
                return JsonResult.error("请输入手机号");
            }
            if (null == map.get("code") || "".equals(map.get("code"))) {
                return JsonResult.error("请输入验证码");
            }
            JSONObject jsonObject = doctorUserService.phoneLogin(map, request);
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

    /**
     * 版本更新
     *
     * @return
     */

    @PostMapping("/versionsUpdate")
    @ResponseBody
    public JsonResult versionsUpdate(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("appType") || "".equals(map.get("appType"))) {
                return JsonResult.error("请选择版本类型");
            }
            if (null == map.get("number") || "".equals(map.get("number"))) {
                return JsonResult.error("请选择版本号");
            }
            JSONObject jsonObject = doctorUserService.versionsUpdate(map);
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
