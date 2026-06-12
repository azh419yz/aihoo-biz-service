package com.aihoo.domain.prescription.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 处方日志表
 */
@Data
@TableName("t_hos_prescription_log")
public class HosPrescriptionLog implements Serializable {
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
     * 处方id
     */
    private String hosPrescriptionId;

    /**
     * 操作人类型 PATIENT-患者 DOCKER-医生
     */
    private String type;

    /**
     * 患者用户id
     */
    private String patientUserId;

    /**
     * 医生id
     */
    private String doctorUserId;

    /**
     * 状态 DONE订单关闭 CANCEL取消订单 WAIT待付款 PAY已付款 END订单完成
     */
    private String status;

    /**
     * 日志
     */
    private String remark;

    /**
     * 就诊人id
     */
    private String sickId;
}