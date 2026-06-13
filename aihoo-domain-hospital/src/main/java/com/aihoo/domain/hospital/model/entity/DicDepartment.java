package com.aihoo.domain.hospital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("TB_DIC_Department")
public class DicDepartment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "departmentId", type = IdType.INPUT)
    private String departmentId;

    private String yljgdm;
    private String wsjdm;
    private String wsjgdm;
    private String kstybz;
    private String ksjb;
    private String sfkzyy;
    private String xgbz;
}
