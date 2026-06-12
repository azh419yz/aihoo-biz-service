package com.aihoo.domain.prescription.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 处方诊金表
 */
@Data
@TableName("t_hos_prescription_fee")
public class HosPrescriptionFee implements Serializable {
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
     * 诊金
     */
    private String visitAmount;

    /**
     * 是否诊后服务 1:是 0:否
     */
    private String afterService;

    /**
     * 是否可见处方 1:是 0:否
     */
    private String seePrescription;

    /**
     * 药费
     */
    private String drugAmount;

    /**
     * 代煎费
     */
    private String decoctionAmount;

    /**
     * 诊后服务费
     */
    private String serviceAmount;

    /**
     * 总费
     */
    private String amount;
}