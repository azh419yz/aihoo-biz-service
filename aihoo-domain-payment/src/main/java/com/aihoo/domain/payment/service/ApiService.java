package com.aihoo.domain.payment.service;

import java.util.Map;

public interface ApiService {
    boolean getPushSign(String mobile, String bizSn, String cert);
    boolean getPrescriptionLogistics(String prescripNo, String prescripStatus, String dealDate);
    boolean judgeResult(String certificate, String userID, String code, Map<String, Object> map) throws Exception;
}
