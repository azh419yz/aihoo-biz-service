package com.aihoo.domain.payment.model.vo;

import lombok.Data;

/**
 * 华东医院挂号后返回的信息
 **/
@Data
public class OfflineConfirmBooking {

    private String status;
    private String code;

    /**
     * 预约号
     */
    private String bookingId;
    /**
     * 科室名称
     */
    private String nodeName;
    /**
     * 预约班次id
     */
    private String classId;
    /**
     *  专家id
     */
    private String entityId;
    /**
     * 专家姓名
     */
    private String entityName;
    /**
     * 排班日期
     */
    private String classDate;
    /**
     * 上下午标识
     */
    private String dayType;
    /**
     * 预约的时段id
     */
    private String periodId;
    /**
     * 时段
     */
    private String period;
    /**
     * 诊室
     */
    private String postion;
    /**
     * 诊室具体位置
     */
    private String address;
    /**
     * 挂号类型
     */
    private String serviceType;
    /**
     * 挂号价格
     */
    private String price;
    /**
     * 信息提示
     */
    private String message;
}