package com.aihoo.api.doctor.app.controller;

import com.aihoo.common.JsonResult;
import com.aihoo.domain.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description: 患者
 * @author: Mr.Li
 * @create: 2020-09-29 13:54
 **/
@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    /**
     * @Description: 就诊人列表
     * @Return: com.aihoo.common.JsonResult
     */
    @ResponseBody
    @PostMapping("/patientList")
    public JsonResult patientList(@RequestBody Map<String, String> map) {
        try {
            List<Map> list = patientService.patientList(map);
            return JsonResult.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * @Description: 患者详情
     * @Return: com.aihoo.common.JsonResult
     */
    @ResponseBody
    @PostMapping("/patientMsg")
    public JsonResult patientMsg(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        if (id == null) {
            return JsonResult.error("传个患者id吧");
        }
        try {
            Map jsonObject = patientService.patientMsg(map);
            return JsonResult.ok().put("data", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }
}
