package com.aihoo.domain.patient.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_patient_user_log")
public class PatientUserLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String patientUserId;
    private String actionType;
    private String osName;
    private String ipAddress;
    private String remark;
    private String city;
}
