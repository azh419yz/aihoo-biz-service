package com.aihoo.domain.visit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 就诊人信息表
 */
@Data
@TableName("t_hos_sick")
public class HosSick implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String patientUserId;
    private String name;
    private String idCard;
    private String sex;
    private String age;
    private String mobile;
    private String isDefault;
    private String isDelete;
    private String address;
}
