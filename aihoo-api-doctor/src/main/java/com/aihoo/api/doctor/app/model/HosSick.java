package com.aihoo.api.doctor.app.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
    * 就诊人信息表
    */
@Data
@TableName("t_hos_sick")
public class HosSick implements Serializable {
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
    * 患者用户id
    */
    private String patientUserId;

    /**
    * 姓名
    */
    private String name;

    /**
    * 身份证
    */
    private String idCard;

    /**
    * 性别 0-女 1-男
    */
    private String sex;

    /**
    * 年龄
    */
    private String age;

    /**
    * 手机号
    */
    private String mobile;

    /**
    * 是否默认就诊人 1:是 0:不是
    */
    private String isDefault;

    /**
    * 是否删除 1:已删除 0:可用
    */
    private String isDelete;

    /**
    * 就诊人地址
    */
    private String address;
}