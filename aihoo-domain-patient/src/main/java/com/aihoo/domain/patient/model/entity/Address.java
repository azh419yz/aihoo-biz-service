package com.aihoo.domain.patient.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 收货地址表
 * </p>
 */
@Data
@TableName("t_address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String patientUserId;
    private String name;
    private String mobile;
    private String provinceCode;
    private String cityCode;
    private String districtCode;
    private String province;
    private String city;
    private String district;
    private String address;
    private String isDefault;
    private String isDelete;
}
