package com.aihoo.api.doctor.app.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 医生操作表
 */
@Data
@TableName("t_doctor_user_log")
public class DoctorUserLog implements Serializable {
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
     * 操作类型 LOGIN登录 LOGOUT登出 CANCEL注销  REGISTER 注册 CASIGN CA认证
     */
    private String actionType;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 操作说明
     */
    private String remark;
    /**
     * 操作说明
     */
    private String city;
}