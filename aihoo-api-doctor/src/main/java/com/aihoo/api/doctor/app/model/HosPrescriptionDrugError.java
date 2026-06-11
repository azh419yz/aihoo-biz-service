package com.aihoo.api.doctor.app.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
    * 处方药品表
    */
@Data
@TableName("t_hos_prescription_drug_error")
public class HosPrescriptionDrugError implements Serializable {
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
     * 药品id
     */
    private String drugId;

    /**
     * 药品名称
     */
    private String name;

    /**
     * 药品规格
     */
    private String size;

    /**
     * 药品剂型编码 d_dict type=DRUG_DOS
     */
    private String drugDosCode;

    /**
     * 药品剂型
     */
    private String drugDosName;

    /**
     * 单位计量
     */
    private String unitMeasure;

    /**
     * 包装单位编码 d_dict type=PACK_UNIT
     */
    private String packUnitCode;

    /**
     * 包装单位
     */
    private String packUnitName;

    /**
     * 药品单价
     */
    private String price;

    /**
     * 默认用药频次编码 d_dict type=FREQ_MED
     */
    private String freqMedCode;

    /**
     * 默认用药频次
     */
    private String freqMedName;

    /**
     * 默认用药途径编码 d_dict type=ROUTE_ADMI
     */
    private String routeAdmiCode;

    /**
     * 默认用药途径
     */
    private String routeAdmiName;

    /**
     * 是否抗生素
     */
    private String isAntibiotics;

    /**
     * 是否注射
     */
    private String isInjection;

    /**
     * 是否麻醉药
     */
    private String isAnesthesia;

    /**
     * 是否监控药物
     */
    private String isMonitor;

    /**
     * 药品说明书
     */
    private String content;

    /**
     * 药品数量
     */
    private String number;

    /**
     * 用药天数
     */
    private String useDay;

    /**
     * 药品用量
     */
    private String dosage;

    /**
     * ERP码
     */
    private String erp;

    /**
     * ERPID
     */
    private String erpId;

    private static final long serialVersionUID = 1L;
}

