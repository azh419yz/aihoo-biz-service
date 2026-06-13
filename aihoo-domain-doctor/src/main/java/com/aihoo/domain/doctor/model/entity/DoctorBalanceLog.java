package com.aihoo.domain.doctor.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_doctor_balance_log")
public class DoctorBalanceLog implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    private String doctorUserId;

    private String type;

    private String changeAmount;

    private String availableAmount;

    private String lockAmount;

    private String remark;

    private String otherId;

    private static final long serialVersionUID = 1L;
}
