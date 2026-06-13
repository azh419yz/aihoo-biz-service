package com.aihoo.domain.hospital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_drug")
public class Drug implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String createUserId;
    private String skuCode;
    private String mshId;
    private String healthCode;
    private String name;
    private String size;
    private String drugDosCode;
    private String drugDosName;
    private String unitMeasure;
    private String doseUnit;
    private String doseUnitCode;
    private String manufacturers;
    private String approvalNumber;
    private String packUnitCode;
    private String packUnitName;
    private String price;
    private String freqMedCode;
    private String freqMedName;
    private String routeAdmiCode;
    private String routeAdmiName;
    private String basicMedicineCode;
    private String isBasicMedicine;
    private String basicMedicine;
    private String isAntibiotics;
    private String isInjection;
    private String isAnesthesia;
    private String isMonitor;
    private String psychotropicDrug;
    private String hospitalPreparations;
    private String content;
    private String status;
    private String erp;
    private String erpId;
    private String supplier;
    private String drugstoreId;
    private String method;
    private String pinyinInitial;
}
