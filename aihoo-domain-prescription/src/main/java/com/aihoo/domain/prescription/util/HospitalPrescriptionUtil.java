package com.aihoo.domain.prescription.util;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Component;

/**
 * @program: aihoo-root
 * @description: 处方流转平台工具（查询处方物流）
 * @author: Mr.Li
 * @create: 2020-10-20 13:22
 * <p>
 * 当前状态：占位实现，仅保留与旧代码一致的方法签名。具体 HTTP 逻辑待 shared-kernel 引入 AppSiganatureUtils/Request 后回填。
 * </p>
 **/
@Component
public class HospitalPrescriptionUtil {
    private Log log = LogFactory.get();

    private long TIMESTAMP = System.currentTimeMillis();

    /**
     * 查询处方物流
     *
     * @param REQS_URL       物流查询接口地址
     * @param ACCESS_APPID   应用ID
     * @param APP_SECRET     应用密钥
     * @param prescripNo     处方单号
     * @param prescribeDate  处方日期
     * @param hospitalName   医院名称
     * @return 物流查询结果
     */
    public JSONObject prescriptionLogistics(String REQS_URL, String ACCESS_APPID, String APP_SECRET,
                                            String prescripNo, String prescribeDate, String hospitalName) throws Exception {
        log.info("查询处方物流开始-- 订单号" + prescripNo);
        TIMESTAMP = System.currentTimeMillis();
        log.info("\n{}\n{}\n{}\n{}\n{}\n{}", REQS_URL, ACCESS_APPID, APP_SECRET, prescripNo, hospitalName, TIMESTAMP);
        // TODO: 真实实现需调用 REQS_URL 物流查询接口，并使用 AppSiganatureUtils.createSiganature 签名。
        // 当前为占位实现，待 shared-kernel 提供签名工具后回填。
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prescripNo", prescripNo);
        jsonObject.put("prescribeDate", prescribeDate);
        jsonObject.put("hospitalName", hospitalName);
        jsonObject.put("result", new com.alibaba.fastjson2.JSONArray());
        log.info("查询处方物流结束-- 订单号" + prescripNo);
        return jsonObject;
    }
}
