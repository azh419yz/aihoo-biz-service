package com.aihoo.domain.consultation.model.vo;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;

@Data
public class MdtOrderTradeInfoVo {
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
     * 疾病名称
     */
    @ExcelColumn(value = "MDT项目",col = 7)
    private String mdtName;

    /**
     * 支付金额
     */
    @ExcelColumn(value = "交易金额",col = 8)
    private String totalPrice;


    /**
     * 支付类型 支付宝ALIPAY 微信WECHAT
     */
    @ExcelColumn(value = "支付方式",col = 9)
    private String payType;

    /**
     * 状态
     */
    @ExcelColumn(value = "交易状态",col = 10)
    private String status;

    /**
     * 创建时间
     */
    @ExcelColumn(value = "交易时间",col = 11)
    private String createTime;
}