package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接诊设置时间表
 */
@TableName("t_doctor_set_times")
@Data
public class DoctorSetTimes implements Serializable {
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
     * 星期 d_dict type=WEEK
     */
    private String weekCode;

    /**
     * 星期
     */
    private String weekName;

    /**
     * 开始时间 整点 例：01:00
     */
    private String startTime;

    /**
     * 结束时间 整点 例：13:00 结束时间大于开始时间
     */
    private String endTime;

    /**
     * 类型 VOICE VIDEO REVISIT
     */
    private String type;

}