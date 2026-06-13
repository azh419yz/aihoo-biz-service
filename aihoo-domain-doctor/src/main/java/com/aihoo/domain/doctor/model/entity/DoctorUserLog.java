package com.aihoo.domain.doctor.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_doctor_user_log")
public class DoctorUserLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String createTime;
    private String updateTime;
    private String doctorUserId;
    private String actionType;
    private String osName;
    private String ipAddress;
    private String remark;
    private String city;
}
