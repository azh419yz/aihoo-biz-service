package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/5 15:48
 */
@TableName("t_doctor_directory")
@Data
public class DoctorDirectory implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 医生id
     */
    @TableField("doctor_id")
    private Long doctorId;
    /**
     * 患者id
     */
    @TableField("sick_id")
    private Long sickId;
    /**
     * 患者名称
     */
    @TableField("sick_name")
    private String sickName;
    /**
     * 来源 1:扫码 2:购买问诊卡
     */
    @TableField("source")
    private Integer source;
    /**
     * 用户id
     */
    @TableField("patient_user_id")
    private Long patientUserId;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
}
