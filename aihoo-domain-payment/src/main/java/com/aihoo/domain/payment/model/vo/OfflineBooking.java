package com.aihoo.domain.payment.model.vo;

import lombok.Data;

/**
 * 挂号类型
 **/
@Data
public class OfflineBooking {

    /**
     * 院区id
     */
    private String districtId;
    /**
     * 挂号类型id
     */
    private String bookingId;
    /**
     * 挂号类型
     */
    private String bookingName;
}