package com.aihoo.domain.prescription.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
    * 处方临床诊断表
    */
@Data
@TableName("t_hos_prescription_disease_error")
public class HosPrescriptionDiseaseError implements Serializable{
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 处方id
     */
    private String hosPrescriptionId;

    /**
     * 疾病编码
     */
    private String diseaseCode;

    /**
     * 疾病名称
     */
    private String diseaseName;
}

