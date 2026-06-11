package com.aihoo.domain.hospital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 药品信息表
 * </p>
 *
 * @author mcp
 * @since 2020-09-19
 */
@Data
@TableName("t_drug")
public class Drug implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 创建人id
     */
    private String createUserId;

    /**
     * 药品编号
     */
    private String skuCode;

    /**
     * 合偶平方ID
     */
    private String mshId;

    /**
     * 阳光码
     */
    private String healthCode;

    /**
     * 药品名称
     */
    private String name;

    /**
     * 药品规格
     */
    private String size;

    /**
     * 药物剂型编码 d_dict type=DRUG_DOS
     */
    private String drugDosCode;

    /**
     * 药物剂型
     */
    private String drugDosName;

    /**
     * 单位剂量
     */
    private String unitMeasure;

    /**
     * 剂量单位
     */
    private String doseUnit;

    /**
     * 剂量单位编码
     */
    private String doseUnitCode;

    /**
     * 厂家
     */
    private String manufacturers;

    /**
     * 批准文号
     */
    private String approvalNumber;

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
     * 基药标识编码 d_dict type=BASIC_MEDICINE
     */
    private String basicMedicineCode;

    /**
     * 是否基药
     */
    private String isBasicMedicine;

    /**
     * 基药标识（国基药/增补基药/飞前述两种）
     */
    private String basicMedicine;

    /**
     * 是否抗生素 0 否 1 是
     */
    private String isAntibiotics;

    /**
     * 是否注射 0 否 1 是
     */
    private String isInjection;

    /**
     * 是否麻醉药  0 否 1 是
     */
    private String isAnesthesia;

    /**
     * 是否监控药物 0 否 1 是
     */
    private String isMonitor;

    /**
     * 精神药物级别 0 无 1 有
     */
    private String psychotropicDrug;

    /**
     * 是否院内制剂 0否 1是
     */
    private String hospitalPreparations;

    /**
     * 药品说明书
     */
    private String content;

    /**
     * 状态(是否启用 1:启用 0:停用)
     */
    private String status;

    /**
     * ERP码
     */
    private String erp;

    /**
     * ERPID
     */
    private String erpId;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 药房ID
     */
    private String drugstoreId;

    /**
     * 煎药方式
     */
    private String method;

    private String pinyinInitial;
}
