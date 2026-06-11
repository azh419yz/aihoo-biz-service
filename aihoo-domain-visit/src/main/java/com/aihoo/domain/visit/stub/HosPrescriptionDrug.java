package com.aihoo.domain.visit.stub;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 占位实体：原 t_hos_prescription_drug 表。
 * 字段集合取自 RevisitOrderServiceImpl.prescriptionDurgBulkExport 的使用面。
 * 待 prescription 域统一后改用 com.aihoo.domain.prescription.model.entity.HosPrescriptionDrug。
 */
@Data
@TableName("t_hos_prescription_drug")
public class HosPrescriptionDrug implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String hosPrescriptionId;
    private String name;
    private String price;
    private String number;
}
