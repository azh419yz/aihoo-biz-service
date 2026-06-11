package com.aihoo.domain.prescription.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: aihoo-root
 * @description: 处方流转平台配置
 * @author: Mr.Li
 * @create: 2021-01-11 15:27
 **/
@Data
@Component
@ConfigurationProperties(prefix = "prescription")
public class PrescriptionProperties {
    private String appId;
    private String appsecret;
    private String reqUrl;
    private String reqsUrl;
}
