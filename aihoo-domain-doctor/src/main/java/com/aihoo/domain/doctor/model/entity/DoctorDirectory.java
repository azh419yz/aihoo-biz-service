package com.aihoo.domain.doctor.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("t_doctor_directory")
@Data
public class DoctorDirectory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("doctor_id")
    private Long doctorId;
    @TableField("sick_id")
    private Long sickId;
    @TableField("sick_name")
    private String sickName;
    @TableField("source")
    private Integer source;
    @TableField("patient_user_id")
    private Long patientUserId;
    @TableField("create_time")
    private String createTime;
    @TableField("update_time")
    private String updateTime;
}
