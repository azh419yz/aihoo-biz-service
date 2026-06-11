package com.aihoo.domain.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 华山/华东医院接口配置 Stub
 *
 * <p>原 com.aihoo.common.config.properties.HospitalProperties 尚未迁入 shared-kernel；
 * 这里提供一个本地副本以满足 OfflineHuaEastServiceImpl / OfflineOderServiceImpl 编译需要。
 * 等 common 域迁入后改为引用 shared-kernel 路径。</p>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "hospital")
public class HospitalProperties {
    /** 华山医院预约接口地址 */
    private String appointmentUrl;
    /** 华山医院支付接口地址 */
    private String paymentUrl;
    /** 平台公钥 */
    private String publicKey;
    /** 识别代码 */
    private String distinguishKey;
    /** 域名 */
    private String url;
}