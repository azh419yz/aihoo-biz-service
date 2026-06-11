package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单退款表
 */
@Data
@TableName("t_order_back_pay")
public class OrderBackPay implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 类型：申请退款（RETURN_GOODS），取消订单（CANCEL_ORDER）
     */
    private String type;

    /**
     * 付款类型（WECHAT、ALIPAY）
     */
    private String payType;

    /**
     * t_order外键
     */
    private String orderId;

    /**
     * 原t_order支付单号
     */
    private String outTradeNo;

    /**
     * 退款编号
     */
    private String outRefundNo;

    /**
     * 第三方编号
     */
    private String thridOutTradeNo;

    /**
     * 订单支付金额
     */
    private String totalPrice;

    /**
     * 退款金额
     */
    private String amount;

    /**
     * 状态（WAIT进行中，PASS成功）
     */
    private String status;
}