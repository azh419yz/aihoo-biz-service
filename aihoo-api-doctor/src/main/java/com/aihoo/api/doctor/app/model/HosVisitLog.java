package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 问诊日志表
 */
@Data
@TableName("t_hos_visit_log")
public class HosVisitLog implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 在线问诊信息id
     */
    private String hosVisitId;

    /**
     * 操作人类型 PATIENT-患者 DOCKER-医生
     */
    private String type;

    /**
     * 状态 DONE订单关闭 CANCEL取消订单 DECLINE拒单 WAIT待付款 PAY已付款|待接单 HAVE已接单  START复诊进行中 END订单完成
     */
    private String status;

    /**
     * 患者操作人id
     */
    private String patientUserId;

    /**
     * 医生操作人id
     */
    private String doctorUserId;

    /**
     * 日志
     */
    private String remark;

    /**
     * 就诊人id
     */
    private String sickId;
}