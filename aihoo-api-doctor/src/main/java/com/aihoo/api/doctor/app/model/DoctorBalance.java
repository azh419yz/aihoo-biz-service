package com.aihoo.api.doctor.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
    * 余额额表
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
}