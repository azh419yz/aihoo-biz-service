package com.aihoo.domain.doctor.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname DoctorBalance
 * @Description hf
 * @Date 2020/9/30 16:18
 * @Created by ad
 */
@Data
@TableName("t_doctor_balance")
public class DoctorBalance implements Serializable {
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
     * 医生id
     */
    private String doctorUserId;

    /**
     * 可用余额
     */
    private String availableAmount;

    /**
     * 锁定余额
     */
    private String lockAmount;

    /**
     * 问诊账单总金额
     */
    private String visitAmount;

    /**
     * 复诊账单总金额
     */
    private String revisitAmount;

    private static final long serialVersionUID = 1L;

}
