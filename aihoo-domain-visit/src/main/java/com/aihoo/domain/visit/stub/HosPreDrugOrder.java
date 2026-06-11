package com.aihoo.domain.visit.stub;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 占位实体：原 t_hos_pre_drug_order 表。
 * 字段集合取自 RevisitOrderServiceImpl.prescriptionDurgBulkExport 的使用面。
 * 待 prescription 域统一后改用 com.aihoo.domain.prescription.model.entity.HosPreDrugOrder。
 */
@Data
@TableName("t_hos_pre_drug_order")
public class HosPreDrugOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String name;
    private String mobile;
    private String type;
    private String orderNum;
    private String status;
    private String hosPrescriptionId;
    private String province;
    private String city;
    private String district;
    private String address;
}
