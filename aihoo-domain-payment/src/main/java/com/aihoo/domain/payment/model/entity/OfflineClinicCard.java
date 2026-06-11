package com.aihoo.domain.payment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *  医院就诊卡
 **/
@Data
@TableName("t_offline_clinic_card")
public class OfflineClinicCard implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**
     * 医院id
     */
    private String hospitalId;
    /**
     * 医院
     */
    private String hospitalName;
    /**
     * 用户名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 证件类型
     */
    private String certificatesType;
    /**
     * 证件号码
     */
    private String certificatesNumber;
    /**
     * 生日
     */
    private String birth;
    /**
     * 地址
     */
    private String coordinate;
    /**
     * 就诊卡号
     */
    private String clinicName;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 是否删除
     */
    private String isDelete;
}