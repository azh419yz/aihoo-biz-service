package com.aihoo.domain.payment.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *  查询科室排版信息
 **/
@Data
public class OfflineAppointment implements Serializable {
    /**
     * ID
     */
    private String id;
    /**
     * 挂号id
     */
    private String groupId;
    /**
     * 挂号类型
     */
    private String groupName;
    /**
     * 医生工号
     */
    private String staffCode;
    /**
     * 医生姓名
     */
    private String staffName;
    /**
     * 科室id
     */
    private String codeId;
    /**
     * 科室名称
     */
    private String codeName;
    /**
     * 排版时间
     */
    private String schedulingDate;
    /**
     * 1上午 2下午
     */
    private String schedulingType;
    /**
     * 楼层
     */
    private String floor;
    /**
     * 院区
     */
    private String district;
    /**
     * 排版id
     */
    private String schedulingId;
    /**
     * 1可预约 0 不可预约
     */
    private String bookingNum;

    private String bookingTime1;
    private String bookingTime2;
    private String bookingTime3;
    private String bookingTime4;
    private String bookingTime5;
    private String bookingTime6;
    private String bookingTime7;
    private String bookingTime8;
    private String bookingTime9;
    private String bookingTime10;
    private String bookingTime11;
    private String bookingTime12;
    //价钱
    private String price;
    private String data;

    /**
     *结束时间
     */
    private String endTime;
    /**
     *开始时间
     */
    private String startTime;
    /**
     *诊室
     */
    private String postion;
    /**
     * 挂号列表（主任，辅主任）
     */
    private String serviceType;

    private String findGroupId;
    private String findDistrictId;
}