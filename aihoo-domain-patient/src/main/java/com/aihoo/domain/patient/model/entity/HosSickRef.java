package com.aihoo.domain.patient.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 就诊人引用（PatientUser XML 中 collection 引用的简化 DTO）。
 *
 * <p>原 admin 域下 HosSick 实体属于就诊人域，跨域引用时不直接依赖对方实体，
 * 在 patient 域内以最小字段集占位，后续就诊人域迁移完成后再做正式映射。</p>
 */
@Data
@TableName("t_hos_sick")
public class HosSickRef implements Serializable {

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
    private String address;
    private String isDefault;
    private String isDelete;
}
