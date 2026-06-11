package com.aihoo.domain.visit.model.excel;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;


/**
 * @Classname HosVisitEntity
 * @Description hf
 * @Date 2020/10/26 14:05
 * @Created by ad
 */
@Data
public class HosVisitEntity {


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
     * 患者用户姓名
     */
    @ExcelColumn(value = "患者用户姓名",col = 3)
    private String patientUserName;

    /**
     * 就诊人id
     */
    @ExcelColumn(value = "就诊人id",col = 4)
    private String hosSickId;

    /**
     * 姓名
     */
    @ExcelColumn(value = "姓名",col = 5)
    private String name;

    /**
     * 身份证
     */
    @ExcelColumn(value = "身份证",col = 6)
    private String idCard;

    /**
     * 性别 0-女 1-男
     */
    @ExcelColumn(value = "性别",col = 7)
    private String sex;

    /**
     * 年龄
     */
    @ExcelColumn(value = "年龄",col = 8)
    private String age;

    /**
     * 手机号
     */
    @ExcelColumn(value = "手机号",col = 9)
    private String mobile;

    /**
     * 病情描述（主诉）
     */
    @ExcelColumn(value = "病情描述",col = 10)
    private String content;

    /**
     * 问诊类型 图文IMAGE、语音VOICE、视频VIDEO
     */
    @ExcelColumn(value = "问诊类型",col = 11)
    private String type;

    /**
     * 支付金额
     */
    @ExcelColumn(value = "支付金额",col = 12)
    private String totalPrice;

    /**
     * 支付类型 支付宝ALIPAY 微信WECHAT
     */
    @ExcelColumn(value = "支付类型",col = 13)
    private String payType;

    /**
     * 付款时间
     */
    @ExcelColumn(value = "付款时间",col = 14)
    private String payTime;

    /**
     * 状态 DONE订单关闭 ;CANCEL取消订单; WAIT待付款 PAY已付款|待接单 ; (START|HAVE) 已接单 ;   END订单完成  ;REFUNDWAIT退款进行中 ;REFUNDSUCCESS退款成功 ;CHANGE退款异常  ;REFUNDCLOSE退款关闭
     */
    @ExcelColumn(value = "支付状态",col = 15)
    private String status;

    /**
     * 取消订单与拒单信息
     */
    @ExcelColumn(value = "取消订单与拒单信息",col = 16)
    private String msg;

    /**
     * 订单编号-首字母为V
     */
    @ExcelColumn(value = "订单编号",col = 17)
    private String orderNum;

    /**
     * 五星好评 最低一星 最高五星
     */
    @ExcelColumn(value = "五星好评",col = 18)
    private String fiveStar;

    /**
     * 医生id
     */
    @ExcelColumn(value = "医生id",col = 19)
    private String doctorUserId;

    /**
     * 医嘱
     */
    @ExcelColumn(value = "医嘱",col = 20)
    private String doctorAdvice;

    /**
     * 初诊结果（拟诊）
     */
    @ExcelColumn(value = "初诊结果",col = 21)
    private String firstVisit;

    /**
     * 接单时间
     */
    @ExcelColumn(value = "接单时间",col = 22)
    private String haveTime;

    /**
     * 问诊实际开始时间
     */
    @ExcelColumn(value = "问诊实际开始时间",col = 23)
    private String startTime;

    /**
     * 问诊实际结束时间
     */
    @ExcelColumn(value = "问诊实际结束时间",col = 24)
    private String endTime;

    /**
     * 是否已拉取IM聊天消息 0 已拉取  1未拉取
     */
    @ExcelColumn(value = "是否已拉取IM聊天消息",col = 25)
    private String isReadIm;

    /**
     * 支付状态 0已支付，1未支付
     */
    @ExcelColumn(value = "支付状态",col = 26)
    private String isPay;

}
