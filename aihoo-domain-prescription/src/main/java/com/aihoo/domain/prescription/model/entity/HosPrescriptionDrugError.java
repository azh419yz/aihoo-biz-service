package com.aihoo.domain.prescription.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 处方药品错误表
 */
@Data
public class HosPrescriptionDrugError implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String createTime;
    private String updateTime;
    private String hosPrescriptionId;
    private String drugId;
    private String name;
    private String size;
    private String drugDosCode;
    private String drugDosName;
    private String unitMeasure;
    private String packUnitCode;
    private String packUnitName;
    private String price;
    private String freqMedCode;
    private String freqMedName;
    private String routeAdmiCode;
    private String routeAdmiName;
    private String isAntibiotics;
    private String isInjection;
    private String isAnesthesia;
    private String isMonitor;
    private String content;
    private String number;
    private String useDay;
    private String dosage;
    private String erp;
    private String erpId;
}
