package com.aihoo.domain.payment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_order_back_pay")
public class OrderBackPay implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    private String type;

    private String payType;

    private String orderId;

    private String outTradeNo;

    private String outRefundNo;

    private String thridOutTradeNo;

    private String totalPrice;

    private String amount;

    private String status;
}
