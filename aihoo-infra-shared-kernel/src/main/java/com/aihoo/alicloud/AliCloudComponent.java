package com.aihoo.alicloud;

import org.springframework.stereotype.Component;

@Component
public class AliCloudComponent {
    public boolean sendSms(String phone, String templateParam) {
        return true;
    }
}
