package com.aihoo.domain.payment.model.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 *  华东医院的 预约表
 **/
@Data
@TableName("t_offline_dong_yue")
public class OfflineDongYue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     *身份证
     */
    private String no;
    /**
     *证件类型
     */
    private String type;
    /**
     *就诊人
     */
    private String name;
    /**
     *手机号
     */
    private String tel;
    /**
     *1线上支付 2线下支付
     */
    private String customer;
    /**
     *1 指定日期  2不指定日期
     */
    private String date;
    /**
     *排版id
     */
    private String schedulingId;
    /**
     *排版时间
     */
    private String schedulingDate;
    /**
     *1 上午 2下午
     */
    private String schedulingType;
    /**
     *时段id
     */
    private String periodId;
    /**
     *开始时间
     */
    private String startTime;
    /**
     *结束时间
     */
    private String endTime;
    /**
     *空余标识
     */
    private String freeFlag;
    /**
     *号源数量
     */
    private String freeAmount;
    /**
     *装填 1已预约 2一挂号
     */
    private String status;
    /**
     *创建时间
     */
    private String createTime;
    /**
     *结束时间
     */
    private String updateTime;

    private String poolId;
}