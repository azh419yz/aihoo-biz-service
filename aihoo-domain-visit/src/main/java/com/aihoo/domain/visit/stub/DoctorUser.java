package com.aihoo.domain.visit.stub;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 占位实体：原 t_doctor_user 表。
 * 来自 admin 的 DoctorUser 实体。字段集合取自 VisitOrderServiceImpl / RevisitOrderServiceImpl 的使用面。
 * 待 visit 域业务重构完成后再统一改用 com.aihoo.domain.doctor.model.entity.DoctorUser。
 */
@Data
@TableName("t_doctor_user")
public class DoctorUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String name;
    private String hospitalId;
    private String hospitalName;
    private String departId;
    private String departName;
    private String headImg;
}
