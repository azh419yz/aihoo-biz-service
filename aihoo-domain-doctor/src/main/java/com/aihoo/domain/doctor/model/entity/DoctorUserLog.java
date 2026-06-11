package com.aihoo.domain.doctor.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Classname DoctorUserLog
 * @Description hf
 * @Date 2020/10/20 13:50
 * @Created by ad
 */
@Data
@TableName("t_doctor_user_log")
public class DoctorUserLog {

    private static final long serialVersionUID = 1L;

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
     * 操作类型 LOGIN登录 LOGOUT登出 CANCEL注销
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

}
