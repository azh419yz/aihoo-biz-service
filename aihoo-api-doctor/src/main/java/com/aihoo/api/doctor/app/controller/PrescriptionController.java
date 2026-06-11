package com.aihoo.api.doctor.app.controller;

import cn.hutool.core.util.StrUtil;
import com.aihoo.common.JsonResult;
import com.aihoo.api.doctor.app.model.Disease;
import com.aihoo.api.doctor.app.model.Drug;
import com.aihoo.api.doctor.app.service.PrescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 开处方接口
 */
@Controller
@RequestMapping("/api/v1/pre")
public class PrescriptionController {
    @Resource
    private PrescriptionService prescriptionService;

    /**
     * 药品列表
     *
     * @param map page limit
     * @return JsonResult
     */
    @ResponseBody
    @PostMapping("/drugList")
    public JsonResult drugList(@RequestBody Map<String, String> map) {
        try {
            List<Drug> list = prescriptionService.drugList(map);
            return JsonResult.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 疾病列表
     *
     * @param map page limit
     * @return JsonResult
     */
    @ResponseBody
    @PostMapping("/diseaseList")
    public JsonResult diseaseList(@RequestBody Map<String, Object> map) {
        try {
            List<Disease> list = prescriptionService.diseaseList(map);
            return JsonResult.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 提交签名接口
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/sign")
    public JsonResult sign(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        if (StrUtil.isBlank(id)) {
            return JsonResult.error("处方id为不能为空");
        }
        try {
            return prescriptionService.sign(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 开处方接口
     * <p>
     * {
     * "id":"1",
     * "diseaseCodes":["O40.x00","N84.900","O07.900"],
     * "drugs":[{"drugId":"7","freqMedCode":"Prn","routeAdmiCode":"922","medicalCertificate":"","number":"2","useDay":"3","dosage":"1.5克"}]
     * }
     *
     * @param map id复诊订单id diseaseCode疾病list drugCode药品list（drugId药品id，freqMedCode用法，routeAdmiCode频次）
     * @return
     */
    @ResponseBody
    @PostMapping("/create")
    public JsonResult createPrescription(@RequestBody Map<String, Object> map) {
        Object orderId = map.get("orderId");//id
        Object type = map.get("type");//id
        Object diseaseCodes = map.get("diseaseCodes");//疾病
        Object drugs = map.get("drugs");//药品
        if (null == orderId) {
            return JsonResult.error("orderId为null");
        }
        if (null == diseaseCodes) {
            return JsonResult.error("diseaseCodes为null");
        }
        if (null == drugs) {
            return JsonResult.error("drugs为null");
        }
        if (null == type) {
            return JsonResult.error("type为null");
        }
        if (!"MDT".equals(type) && !"VISIT".equals(type) && !"REVISIT".equals(type)) {
            return JsonResult.error("type仅支持：MDT、VISIT、REVISIT");
        }
        try {
            return prescriptionService.savePrescription(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 第二次提交审核（修改认证状态为已认证）
     *
     * @param map 处方id
     * @return
     */
    @ResponseBody
    @PostMapping("/commit")
    public JsonResult commitPrescription(@RequestBody Map<String, String> map) {
        try {
            return prescriptionService.commitPrescription(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 处方查询接口
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/details")
    public JsonResult prescriptionDetails(@RequestBody Map<String, String> map) {
        try {
            return prescriptionService.prescriptionDetails(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 患者处方记录 列表（审核中，审核成功，人工审核中,审核失败）
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/list")
    public JsonResult getPrescriptionList(@RequestBody Map<String, String> map) {
        try {
            return prescriptionService.getPrescriptionList(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("未查询到数据");
        }
    }

    /**
     * 查询禁忌
     *
     * @return JsonResult
     */
    @ResponseBody
    @PostMapping("/taboo")
    public JsonResult taboo() {
        try {
            return prescriptionService.taboo();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("未查询到数据");
        }
    }

    /**
     * 查询处方审核状态
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("status")
    public JsonResult getPrescriptionStatus(@RequestBody Map<String, String> map) {
        try {
            return prescriptionService.getPrescriptionStatus(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 处方笺
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("getPrescription")
    public JsonResult getPrescription(@RequestBody Map<String, String> map) {
        try {
            Map<String, Object> data = prescriptionService.getPrescription(map);
            return JsonResult.ok().put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("未查询到数据");
        }
    }

}
