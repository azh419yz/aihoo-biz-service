package com.aihoo.domain.visit.model.vo;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;

/**
 * 问诊订单交易记录vo
 */
@Data
public class VisitOrderTradeInfoVo {
    @ExcelColumn(value = "序号",col = 1)
    private String id;

    /**
     * 订单编号-首字母为V
     */
    @ExcelColumn(value = "订单编号",col = 2)
    private String orderNum;


    /**
     * 支付单号[微信|支付宝]
     */
    @ExcelColumn(value = "交易流水号",col = 3)
    private String payOrderNum;


    /**
     * 就诊人姓名
     */
    @ExcelColumn(value = "就诊人姓名",col = 4)
    private String hosSickName;


    /**
     * 医生所属医院
     */
    @ExcelColumn(value = "医院名称",col = 5)
    private String hospitalName;


    /**
     * 医生姓名
     */
    @ExcelColumn(value = "医生姓名",col = 6)
    private String doctorName;

    /**
     * 支付金额
     */
    @ExcelColumn(value = "交易金额",col = 7)
    private String totalPrice;

    /**
     * 支付类型 支付宝ALIPAY 微信WECHAT
     */
    @ExcelColumn(value = "支付方式",col = 8)
    private String payType;

    /**
     * 状态 DONE订单关闭 ;CANCEL取消订单; WAIT待付款 PAY已付款|待接单 ; (START|HAVE) 已接单 ;   END订单完成  ;REFUNDWAIT退款进行中 ;REFUNDSUCCESS退款成功 ;CHANGE退款异常  ;REFUNDCLOSE退款关闭
     */
    @ExcelColumn(value = "交易状态",col = 9)
    private String status;

    /**
     * 创建时间
     */
    @ExcelColumn(value = "交易时间",col = 10)
    private String createTime;
}
