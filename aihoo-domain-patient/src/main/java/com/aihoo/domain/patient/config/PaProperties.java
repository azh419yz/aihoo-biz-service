package com.aihoo.domain.patient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 患者实名认证（多源身份认证）外部接口配置。
 *
 * <p>原 admin 模块 com.aihoo.common.config.properties.PaProperties，
 * 因仅 PatientUserServiceImpl.patientApprove 使用，归入 patient 域。</p>
 *
 * @author lsl
 * @date 2021-01-07
 */
@Data
@Component
@ConfigurationProperties(prefix = "approve")
public class PaProperties {
    private String url;
    private String tokenUrl;
    private String appId;
    private String appSecret;
}
