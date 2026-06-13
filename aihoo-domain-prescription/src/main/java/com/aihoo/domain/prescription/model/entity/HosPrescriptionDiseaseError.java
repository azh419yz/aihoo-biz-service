package com.aihoo.domain.prescription.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 处方临床诊断错误表
 */
@Data
public class HosPrescriptionDiseaseError implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String createTime;
    private String updateTime;
    private String hosPrescriptionId;
    private String diseaseCode;
    private String diseaseName;
}
