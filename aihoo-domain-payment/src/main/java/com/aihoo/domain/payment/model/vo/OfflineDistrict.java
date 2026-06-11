package com.aihoo.domain.payment.model.vo;

import lombok.Data;

/**
 * 医院名称
 */
@Data
public class OfflineDistrict {

    /**
     * id
     */
    private String id;
    /**
     * 医院id
     */
    private String districtId;
    /**
     * 医院名称
     */
    private String districtName;
    /**
     * 医院地址
     */
    private String districtAddress;

}