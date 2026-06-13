package com.aihoo.domain.hospital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("t_hospital")
public class Hospital implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String hospitalNo;
    private String createTime;
    private String updateTime;
    private String createUserId;
    private String hosName;
    private String hosMobile;
    private String hosGradeCode;
    private String hosGradeName;
    private String hosLevelCode;
    private String hosLevelName;
    private String hosCateCode;
    private String hosCateName;
    private String hosAttCode;
    private String hosAttName;
    private String provinceCode;
    private String cityCode;
    private String districtCode;
    private String province;
    private String city;
    private String district;
    private String address;
    private String imgs;
    private String status;
    private String content;
    private Integer isDelete;

    @TableField(exist = false)
    private List<Object> hospitalDepartmentVos;
}
