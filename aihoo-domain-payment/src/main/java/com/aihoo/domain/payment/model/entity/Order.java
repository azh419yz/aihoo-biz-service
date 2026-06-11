package com.aihoo.domain.payment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 支付订单表
 * </p>
 *
 * @author mcp
 * @since 2020-09-22
 */
@Data
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 订单类型 VISIT在线问诊支付 REVISIT复诊支付 RECIPE处方支付
     */
    private String orderType;

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 支付类型 WECHAT微信 ALIPAY支付宝 UNIONPAY银联
     */
    private String payType;

    /**
     * 支付单号[微信|支付宝]
     */
    private String payOrderNum;

    /**
     * 患者用户id
     */
    private String patientUserId;

    /**
     * VISIT-t_hos_visit主键 REVISIT-t_hos_revisit主键
     */
    private String otherId;

    /**
     * 总金额
     */
    private String totalPrice;

    /**
     * 付款时间
     */
    private String payTime;

    /**
     * 状态 DONE订单关闭 WAIT待付款 PAY已付款
     */
    private String payStatus;


}