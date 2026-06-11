package com.aihoo.domain.payment.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 离线预约 SMS 工具 - Stub
 *
 * <p>原 com.aihoo.common.utils.SmsUtils 尚未迁入 shared-kernel；这里提供一个占位实现，
 * 以满足 OfflineHuaEastServiceImpl / OfflineAppointmentServiceImpl 的 import 需求。
 * 等 common 域迁入后删除此 stub。</p>
 */
@Slf4j
public class SmsUtils {

    /**
     * 发送短信（占位）
     */
    public static String offlineSend(Map<String, Object> map) {
        log.warn("SmsUtils.offlineSend 暂未实现，map={}", map);
        return "STUB";
    }
}