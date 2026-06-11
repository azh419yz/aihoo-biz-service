package com.aihoo.domain.visit.model.excel;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;


/**
 * @Classname HosPrescriptionEntity
 * @Description hf
 * @Date 2020/10/26 11:34
 * @Created by ad
 */
@Data
public class HosPrescriptionEntity {

    /**
     * 创建时间
     */
    @ExcelColumn(value = "创建时间",col = 1)
    private String createTime;

    /**
     * 更新时间
     */
    @ExcelColumn(value = "更新时间",col = 2)
    private String updateTime;

    /**
     * 医生id
     */
    @ExcelColumn(value = "医生姓名",col = 3)
    private String doctorUserId;

    /**
     * 就诊人姓名
     */
    @ExcelColumn(value = "就诊人姓名",col = 4)
    private String name;

    /**
     * 身份证
     */
    @ExcelColumn(value = "身份证",col = 5)
    private String idCard;

    /**
     * 性别 0-女 1-男
     */
    @ExcelColumn(value = "性别",col = 6)
    private String sex;

    /**
     * 年龄
     */
    @ExcelColumn(value = "年龄",col = 7)
    private String age;

    /**
     * 手机号
     */
    @ExcelColumn(value = "手机号",col = 8)
    private String mobile;

    /**
     * 订单类型
     */
    @ExcelColumn(value = "订单类型",col = 9)
    private String type;

    /**
     * 关联复诊/会诊订单编号
     */
    @ExcelColumn(value = "关联复诊/会诊订单编号",col = 10)
    private String visitMdtNum;

    /**
     * 处方订单编号
     */
    @ExcelColumn(value = "处方订单编号",col = 11)
    private String orderNum;

    /**
     * 科室
     */
    @ExcelColumn(value = "科室",col = 12)
    private String departName;

    /**
     * 处方笺
     */
    @ExcelColumn(value = "处方笺",col = 13)
    private String img;

    /**
     * 审核处方状态
     */
    @ExcelColumn(value = "审核处方状态",col = 14)
    private String checkStatus;

    /**
     * 审方药师
     */
    @ExcelColumn(value = "审方药师",col = 15)
    private String checkPharmaceutist;

    /**
     * 临床诊断
     */
    @ExcelColumn(value = "临床诊断",col = 16)
    private String medicalCertificate;

    /**
     * 支付金额
     */
    @ExcelColumn(value = "支付金额",col = 17)
    private String totalPrice;

    /**
     * 支付类型 支付宝ALIPAY 微信WECHAT
     */
    @ExcelColumn(value = "支付类型",col = 18)
    private String payType;

    /**
     * 付款时间
     */
    @ExcelColumn(value = "付款时间",col = 19)
    private String payTime;

    /**
     * 状态 DONE订单关闭 CANCEL取消订单 WAIT待付款 PAY已付款 END订单完成 REFUNDWAIT退款进行中 REFUNDSUCCESS退款成功 CHANGE退款异常  REFUNDCLOSE退款关闭
     */
    @ExcelColumn(value = "订单状态",col = 20)
    private String status;

    /**
     * 取消订单与拒单信息
     */
    @ExcelColumn(value = "取消订单与拒单信息",col = 21)
    private String msg;
    /**
     * 支付状态 0已支付，1未支付
     */
    @ExcelColumn(value = "支付状态",col = 22)
    private String isPay;

}
