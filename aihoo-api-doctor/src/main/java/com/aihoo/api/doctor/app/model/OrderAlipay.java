package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付宝支付信息表
 */
@Data
@TableName("t_order_alipay")
public class OrderAlipay implements Serializable {
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
     * t_batch_payment 支付批次号
     */
    private String outTradeNo;

    /**
     * 加密类型
     */
    private String signType;

    /**
     * 支付宝客户端支付参数
     */
    private String sign;

    /**
     * 支付金额
     */
    private String totalFee;

    /**
     * 支付手续费
     */
    private String paymentFee;

    /**
     * 回调通知方式-#SYNC同步 ASYN异步
     */
    private String notifyType;

    /**
     * 回调通知状态
     */
    private String tradeStatus;

    /**
     * 支付宝订单号
     */
    private String tradeNo;

    /**
     * 通知ID
     */
    private String notifyId;

    /**
     * 通知时间
     */
    private String notifyTime;

    /**
     * 买家支付宝id
     */
    private String buyerId;

    /**
     * 商户支付宝id
     */
    private String sellerId;

    /**
     * 回调参数
     */
    private String retSign;

    /**
     * 回调信息
     */
    private String subject;

    /**
     * 支付时间
     */
    private String paidTime;

}