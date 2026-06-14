package com.aihoo.api.doctor.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpUtil;
import com.aihoo.util.OssFileUtils;
import com.aihoo.util.SecurityUtil;
import com.aihoo.common.JsonResult;
import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.common.utils.StringUtils;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.aihoo.domain.visit.service.HosRevisitService;
import com.aihoo.properties.CaProperties;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/22 18:59
 * @description：复诊订单
 */
@Controller
@RequestMapping("/api/v1/hosRevisit")
public class HosRevisitController {
    @Autowired
    private HosRevisitService hosRevisitService;

    /**
     * 订单统计
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/count")
    public JsonResult VisitOrderCount(@RequestBody Map<String, String> map) {
        try {
            Map<String, String> selectCount = hosRevisitService.revisitOrderCount(map);
            return JsonResult.ok().put("data", selectCount);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 复诊接单
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/haveRevisit")
    public JsonResult haveRevisit(@RequestBody Map<String, String> map) {
        if (map.get("id") == null) {
            return JsonResult.error("未传复诊订单id");
        }
        try {
            String s = hosRevisitService.haveRevisit(map);
            if (s.equals("ERROR")) {
                return JsonResult.error("接单失败");
            }
            return JsonResult.ok(s);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 展示复诊订单
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/revisitOrderList")
    public JsonResult revisitOrderList(@RequestBody Map<String, String> map) {
        try {
            List list = hosRevisitService.revisitOrderList(map);
            return JsonResult.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 复诊-》订单-》转单
     *
     * @param map id：订单id     msg：转单说明
     * @return
     */
/*    @ResponseBody
    @PostMapping("/revisitTransfer")
    public JsonResult revisitTransfer(@RequestBody Map<String, String> map) {
        try {
            String msg = hosRevisitService.revisitTransfer(map);
            if (msg.equals("ok")) {
                return JsonResult.ok("转单成功");
            } else {
                return JsonResult.error(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }*/

    /**
     * 医生-》复诊-》详情
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/revisitData")
    public JsonResult revisitData(@RequestBody Map<String, Object> map) {
        Object id = map.get("id");
        if (id == null) {
            return JsonResult.error("为传参复诊id");
        }
        try {
            Object status = hosRevisitService.revisitData(map);
            return JsonResult.ok().put("data", status);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 复诊—》开c-》疾病列表
     *
     * @param map page   limit
     * @return
     */
    @ResponseBody
    @PostMapping("/diseaseList")
    public JsonResult diseaseList(@RequestBody Map<String, Object> map) {
        try {
            List list = hosRevisitService.diseaseList(map);
            return JsonResult.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 复诊—》开处方-》药品列表
     *
     * @param map page   limit
     * @return
     */
    @ResponseBody
    @PostMapping("/drugList")
    public JsonResult drugList(@RequestBody Map<String, String> map) {
        try {
            List list = hosRevisitService.drugList(map);
            return JsonResult.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 查询dist字典表list
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/dictList")
    public JsonResult dictList(@RequestBody Map<String, String> map) {
        if (StringUtils.isNullOrEmpty(map.get("type"))) {
            return JsonResult.error("请传入type类型");
        }
        try {
            return JsonResult.ok().putData(hosRevisitService.dictList(map.get("type")));
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
    public JsonResult sign(@RequestBody Map<String, Object> map) {
        Object id = map.get("id");
        if (id == null) {
            return JsonResult.error("处方id为NULL");
        }
        try {
            String data = hosRevisitService.sign(map);
            if ("ERROR".equals(data)) {
                return JsonResult.error("请确认是否签名");
            }
            return JsonResult.ok().put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    @Autowired
    private CaProperties caProperties;

    /**
     * 测试签章接口
     *
     * @return
     */
    @SneakyThrows
//    @ResponseBody
//    @PostMapping("/signTest")
    public JsonResult sign() {
        String idcard = "33052219781214694X";
        Map<String, Object> map = new HashMap<>();
        map.put("id", idcard);
        map.put("appId", caProperties.getAppId());
        String msg = "appId=" + caProperties.getAppId() + "&id=" + idcard;
        String signature = SecurityUtil.signature(caProperties.getPrivateStr(), msg);
        map.put("sign", signature);
        String response = HttpUtil.post(caProperties.getOpenUrl() + "v1/user/seal", map);
        String decode = URLDecoder.decode(response, "UTF-8");
        System.out.println(decode);
        Map hashMap = JSON.parseObject(decode, HashMap.class);
        Map data = JSON.parseObject(hashMap.get("data").toString(), HashMap.class);
        String seal = data.get("seal").toString();

        byte[] bytes = Base64.decode(seal);
        String fileName = System.currentTimeMillis() + ".png";
        MultipartFile file = new MockMultipartFile(fileName, fileName, "", bytes);
        String url = OssFileUtils.uploadFile(file);
        System.out.println("签章URL:" + url);
        return null;
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
    @PostMapping("setPrescription")
    public JsonResult setPrescription(@RequestBody Map<String, Object> map) {
        Object id = map.get("id");//id
        Object diseaseCodes = map.get("diseaseCodes");//疾病
        Object drugs = map.get("drugs");//药品
        if (null == id) {
            return JsonResult.error("id为null");
        } else if (null == diseaseCodes) {
            return JsonResult.error("diseaseCodes为null");
        } else if (null == drugs) {
            return JsonResult.error("drugs为null");
        }
        try {
            String data = hosRevisitService.setPrescription(map);
            if (data == null) {
                return JsonResult.error();
            }
            return JsonResult.ok().put("data", data);
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
    @PostMapping("commitPrescription")
    public JsonResult commitPrescription(@RequestBody Map<String, Object> map) {
        Object id = map.get("id");//id
        if (id == null) {
            return JsonResult.error("id不能为空");
        }
        try {
            boolean data = hosRevisitService.commitPrescription(map.get("id").toString());
            if (data) {
                return JsonResult.ok();
            }
            return JsonResult.error();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 查询处方审核状态
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("getPrescriptionStatus")
    public JsonResult getPrescriptionStatus(@RequestBody Map<String, Object> map) {
        Object id = map.get("id");//id
        if (id == null) {
            return JsonResult.error("参数不能为空");
        }
        try {
            Map data = hosRevisitService.getPrescriptionStatus(map);
            if (null == data) {
                return JsonResult.ok();
            }
            return JsonResult.ok().put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 查看处方详情
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("prescription")
    public JsonResult prescription(@RequestBody Map<String, Object> map) {
        Object id = map.get("id");//id
        if (id == null) {
            return JsonResult.error("参数不能为空");
        }
        try {
            Map data = hosRevisitService.prescription(map);
            return JsonResult.ok().put("data", data);
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
    @PostMapping("writeRevisitResult")
    public JsonResult writeReVisitResult(@RequestBody Map<String, String> map) {
        String id = map.get("id");//id
        String doctorAdvice = map.get("doctorAdvice");//医嘱
        if (id == null) {
            return JsonResult.error("复诊订单id不能为空");
        }
        if (doctorAdvice == null) {
            return JsonResult.error("医嘱不能为空");
        }
        try {
            String data = hosRevisitService.writeRevisitResult(map);
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
    @PostMapping("revisitResult")
    public JsonResult revisitResult(@RequestBody Map<String, String> map) {
        String id = map.get("id");//id
        if (id == null) {
            return JsonResult.error("复诊订单id不能为空");
        }
        try {
            Map data = hosRevisitService.revisitResult(map);
            return JsonResult.ok().put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("未查询到数据");
        }
    }

    /**
     * 查询单个药品详情
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("getOneDrug")
    public JsonResult getOneDrug(@RequestBody Map<String, String> map) {
        String id = map.get("id");//id
        if (id == null) {
            return JsonResult.error("药品id不能为null");
        }
        try {
            Drug data = hosRevisitService.getOneDrug(map);
            return JsonResult.ok().put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("未查询到数据");
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
        String id = map.get("id");//id
        if (id == null) {
            return JsonResult.error("订单id不能为null");
        }
        String type = map.get("type");
        if (type == null) {
            return JsonResult.error("请传入类型");
        }
        try {
            Map<String, Object> data = hosRevisitService.getPrescription(map);
            return JsonResult.ok().put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("未查询到数据");
        }
    }

    /**
     * 患者处方记录 列表（审核中，审核成功，审核失败）
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("getPrescriptionList")
    public JsonResult getPrescriptionList(@RequestBody Map<String, String> map) {
//        String id = map.get("id");//id
//        if (id == null) {
//            return JsonResult.error("就诊人id不能为null");
//        }
        try {
            Object data = hosRevisitService.getPrescriptionList(map);
            return JsonResult.ok().put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("未查询到数据");
        }
    }

    /**
     * 健康档案调阅+1
     *
     * @param map 复诊订单id
     * @return
     */
    @ResponseBody
    @PostMapping("getArchives")
    public JsonResult getArchives(@RequestBody Map<String, String> map) {
        String id = map.get("id");//id
        if (id == null) {
            return JsonResult.error("复诊订单id不能为null");
        }
        try {
            Object data = hosRevisitService.getArchives(id);
            return JsonResult.ok("调阅成功").put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("未查询到数据");
        }
    }


}