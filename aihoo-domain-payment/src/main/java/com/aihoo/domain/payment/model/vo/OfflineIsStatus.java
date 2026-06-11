package com.aihoo.domain.payment.model.vo;

import lombok.Data;

/**
 * 返回状态的数量
 **/
@Data
public class OfflineIsStatus {

    /**
     * 总数量
     */
    private String sum;
    /**
     * 已预约
     */
    private String reserved;
    /**
     *  待支付
     */
    private String paid;
    /**
     *  挂号成功
     */
    private String success;
    /**
     *  挂号失败
     */
    private String fail;
    /**
     * 停诊
     */
    private String close;
    /**
     *  已取消
     */
    private String invalid;
    /**
     *  退款中
     */
    private String progress;
    /**
     *  退款成功
     */
    private String refund;
    /**
     * 已就诊
     */
    private String visited;
}