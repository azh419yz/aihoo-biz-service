package com.aihoo.domain.visit.stub;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 占位实体：原 t_patient_user 表。
 * 来自 admin 的 PatientUser 实体。该域已迁移到 patient 域，但本域不依赖对方以避免循环依赖。
 * 字段集合取自 HosSickServiceImpl / VisitOrderServiceImpl 的使用面。
 * 待 visit 域业务重构完成后再统一改用 com.aihoo.domain.patient.model.entity.PatientUser。
 */
@Data
@TableName("t_patient_user")
public class PatientUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String name;
    private String mobile;
    private String headImg;
}
