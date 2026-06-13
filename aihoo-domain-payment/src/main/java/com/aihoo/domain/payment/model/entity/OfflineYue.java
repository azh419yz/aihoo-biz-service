package com.aihoo.domain.payment.model.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 客户预约
 **/
@Data
@TableName("t_offline_yue")
public class OfflineYue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 身份证
     */
    private String customerNo;
    /**
     * 证件类型
     */
    private String customerType;
    /**
     * 姓名
     */
    private String customerName;
    /**
     * 手机号
     */
    private String customerTel;
    /**
     *是否选择就诊卡1线上支付 2线下支付
     */
    private String customer;
    /**
     *就诊卡号
     */
    private String customerId;
    /**
     * 1指定日期 2不指定
     */
    private String date;
    /**
     *0默认发送用户 1发送用户 2发送合偶平方'
     */
    private String message;
    /**
     *排版时间
     */
    private String schedulingDate;
    /**
     *1上午 2下午
     */
    private String schedulingType;
    /**
     *排版编号
     */
    private String schedulingId;
    /**
     *时间段标识时间段标志／具体时间段／可预约标志（1有名额／0没有名额/-1该时间段不预约',
     */
    private String bookingTime;
    /**
     * 状态 1已预约，2已挂号
     */
    private String status;
    /**
     *  时段id
     */
    private String periodId;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 空余标识
     */
    private String freeFlag;
    /**
     * 号源
     */
    private String freeAmount;
    /**
     * 医生id
     */
    private String districtId;
    /**
     * 医院
     */
    private String districtName;
    /**
     * 挂号id
     */
    private String groupId;
    /**
     * 科室id
     */
    private String codeId;
    /**
     * 医生id
     */
    private String staffId;
    /**
     *
     */
    private String createTime;
    /**
     *
     */
    private String updateTime;
}