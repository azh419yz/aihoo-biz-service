package com.aihoo.domain.visit.model.excel;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;


/**
 * @Classname HosRevisitEntity
 * @Description hf
 * @Date 2020/10/26 11:34
 * @Created by ad
 */
@Data
public class HosRevisitEntity {

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
     * 患者用户id
     */
    @ExcelColumn(value = "患者用户id",col = 3)
    private String patientUserId;

    /**
     * 就诊人id
     */
    @ExcelColumn(value = "就诊人id",col = 4)
    private String hosSickId;

    /**
     * 医生id
     */
    @ExcelColumn(value = "医生id",col = 5)
    private String doctorUserId;

    /**
     * 姓名
     */
    @ExcelColumn(value = "姓名",col = 6)
    private String name;

    /**
     * 身份证
     */
    @ExcelColumn(value = "身份证",col = 7)
    private String idCard;

    /**
     * 性别 0-女 1-男
     */
    @ExcelColumn(value = "性别",col = 8)
    private String sex;

    /**
     * 年龄
     */
    @ExcelColumn(value = "年龄",col = 9)
    private String age;

    /**
     * 手机号
     */
    @ExcelColumn(value = "手机号",col = 10)
    private String mobile;

    /**
     * 就诊医院
     */
    @ExcelColumn(value = "就诊医院",col = 11)
    private String hospitalName;

    /**
     * 就诊科室
     */
    @ExcelColumn(value = "就诊科室",col = 12)
    private String departName;

    /**
     * 疾病名称 (生成处方单之后对应的疾病列表)
     */
    @ExcelColumn(value = "疾病名称",col = 13)
    private String revisitDiseaseName;

    /**
     * 复诊预约开始时间
     */
    @ExcelColumn(value = "复诊预约开始时间",col = 14)
    private String revisitStartTime;

    /**
     * 复诊预约结束时间
     */
    @ExcelColumn(value = "复诊预约结束时间",col = 15)
    private String revisitEndTime;

    /**
     * 复诊时间 日期
     */
    @ExcelColumn(value = "复诊时间",col = 16)
    private String revisitDateStr;

    /**
     * 复诊时间 时间区间
     */
    @ExcelColumn(value = "复诊时间",col = 17)
    private String revisitTimeStr;

    /**
     * 病情主诉
     */
    @ExcelColumn(value = "病情主诉",col = 18)
    private String content;

    /**
     * 支付金额
     */
    @ExcelColumn(value = "支付金额",col = 19)
    private String totalPrice;

    /**
     * 支付类型 支付宝ALIPAY 微信WECHAT
     */
    @ExcelColumn(value = "支付类型",col = 20)
    private String payType;

    /**
     * 付款时间
     */
    @ExcelColumn(value = "付款时间",col = 21)
    private String payTime;

    /**
     * 状态 DONE订单关闭 CANCEL取消订单 DECLINE拒单 WAIT待付款 PAY已付款|待接单 HAVE已接单 START复诊进行中 END订单完成  REFUNDWAIT退款进行中 REFUNDSUCCESS退款成功 CHANGE退款异常  REFUNDCLOSE退款关闭
     */
    @ExcelColumn(value = "订单状态",col = 22)
    private String status;

    /**
     * 取消订单与拒单信息
     */
    @ExcelColumn(value = "取消订单与拒单信息",col = 23)
    private String msg;

    /**
     * 订单编号-首字母为R
     */
    @ExcelColumn(value = "订单编号",col = 24)
    private String orderNum;

    /**
     * 五星好评 最低一星 最高五星
     */
    @ExcelColumn(value = "五星好评",col = 25)
    private String fiveStar;

    /**
     * 医嘱
     */
    @ExcelColumn(value = "医嘱",col = 26)
    private String doctorAdvice;

    /**
     * 接单时间
     */
    @ExcelColumn(value = "接单时间",col = 27)
    private String haveTime;

    /**
     * 问诊开始时间
     */
    @ExcelColumn(value = "问诊开始时间",col = 28)
    private String startTime;

    /**
     * 问诊结束时间
     */
    @ExcelColumn(value = "问诊结束时间",col = 29)
    private String endTime;

    /**
     * 是否已拉取IM聊天消息 0 是 1否
     */
    @ExcelColumn(value = "是否已拉取IM聊天消息",col = 30)
    private String isReadIm;

    /**
     * 支付状态 0已支付，1未支付
     */
    @ExcelColumn(value = "支付状态",col = 31)
    private String isPay;


}
