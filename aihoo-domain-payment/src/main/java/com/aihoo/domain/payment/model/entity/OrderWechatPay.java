package com.aihoo.domain.payment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信支付信息表
 */
@TableName("t_order_wechat_pay")
@Data
public class OrderWechatPay implements Serializable {
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
     * t_order外键
     */
    private String orderId;

    /**
     * 类型
     */
    private String tradeType;

    /**
     * t_order 支付单号
     */
    private String outTradeNo;

    /**
     * 微信支付生成prepayid
     */
    private String prepayid;

    /**
     * 付款金额
     */
    private String totalFee;

    /**
     * 支付手续费
     */
    private String paymentFee;

    /**
     * 支付状态
     */
    private String tradeStatus;

    /**
     * 支付账户的openid
     */
    private String openid;

    /**
     * 通知时间
     */
    private String notifyTime;

    /**
     * 微信返回信息time_end
     */
    private String timeEnd;

    /**
     * 微信返回信息transaction_id
     */
    private String transactionId;

}