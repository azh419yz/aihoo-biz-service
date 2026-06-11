package com.aihoo.domain.hospital.model.excel;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;

/**
 * @Classname DrugEntity
 * @Description hf
 * @Date 2020/10/14 16:27
 * @Created by ad
 */
@Data
public class DrugEntity {

    /**
     * 创建人姓名
     */
    //@ExcelColumn(value = "创建人姓名",col = 1)
    private String createUserName;

    /**
     * 药品编号
     */
    /*@ExcelColumn(value = "药品编号",col = 2)*/
   /* private String skuCode;*/

    /**
     * 合偶平方ID
     */
    @ExcelColumn(value = "合偶平方ID",col = 3)
    private String mshId;

    /**
     * 药品代码（医保代码）阳光码
     */
    @ExcelColumn(value = "阳光码",col = 4)
    private String healthCode;

    /**
     * 药品名称
     */
    @ExcelColumn(value = "药品名称",col = 5)
    private String name;

    /**
     * 药品规格
     */
    @ExcelColumn(value = "药品规格",col = 6)
    private String size;

    /**
     * 药物剂型编码 d_dict type=DRUG_DOS
     */
//    @ExcelColumn(value = "药物剂型编码",col = 7)
    private String drugDosCode;

    /**
     * 药物剂型
     */
    @ExcelColumn(value = "药物剂型",col = 8)
    private String drugDosName;

    /**
     * 单位剂量
     */
    @ExcelColumn(value = "单位剂量",col = 9)
    private String unitMeasure;

    /**
     * 剂量单位
     */
    @ExcelColumn(value = "剂量单位",col = 10)
    private String doseUnit;
    /**
     * 剂量单位编码
     */
    private String doseUnitCode;

    /**
     * 厂家
     */
    @ExcelColumn(value = "厂家",col = 11)
    private String manufacturers;

    /**
     * 批准文号
     */
    @ExcelColumn(value = "批准文号",col = 12)
    private String approvalNumber;

    /**
     * 包装单位编码 d_dict type=PACK_UNIT
     */
//    @ExcelColumn(value = "包装单位编码",col = 13)
    private String packUnitCode;

    /**
     * 包装单位
     */
    @ExcelColumn(value = "包装单位",col = 14)
    private String packUnitName;

    /**
     * 药品单价
     */
    @ExcelColumn(value = "药品单价",col = 15)
    private String price;

    /**
     * 默认用药频次编码 d_dict type=FREQ_MED
     */
//    @ExcelColumn(value = "默认用药频次编码",col = 16)
    private String freqMedCode;

    /**
     * 默认用药频次
     */
    @ExcelColumn(value = "默认用药频次",col = 17)
    private String freqMedName;

    /**
     * 默认用药途径编码 d_dict type=ROUTE_ADMI
     */
//    @ExcelColumn(value = "默认用药途径编码",col = 18)
    private String routeAdmiCode;

    /**
     * 默认用药途径
     */
    @ExcelColumn(value = "默认用药途径",col = 19)
    private String routeAdmiName;

    /**
     * 是否基药
     */
    @ExcelColumn(value = "是否基药",col = 20)
    private String isBasicMedicine;

    /**
     * 基药标识编码 d_dict type=BASIC_MEDICINE
     */
//    @ExcelColumn(value = "基药标识编码",col = 21)
    private String basicMedicineCode;

    /**
     * 基药标识（国基药/增补基药/飞前述两种）
     */
    @ExcelColumn(value = "基药标识",col = 22)
    private String basicMedicine;

    /**
     * 是否抗生素 0 否 1 是
     */
    @ExcelColumn(value = "是否抗生素",col = 23)
    private String isAntibiotics;

    /**
     *  是否注射 0 否 1 是
     */
    @ExcelColumn(value = "是否注射",col = 24)
    private String isInjection;

    /**
     * 是否麻醉药  0 否 1 是
     */
    @ExcelColumn(value = "是否麻醉药",col = 25)
    private String isAnesthesia;

    /**
     * 是否监控药物 0 否 1 是
     */
    @ExcelColumn(value = "是否监控药物",col = 26)
    private String isMonitor;

    /**
     * '精神药物级别 0 无 1 有'
     */
    @ExcelColumn(value = "精神药物级别",col = 27)
    private String psychotropicDrug;

    /**
     * 是否院内制剂 0否 1是
     */
    @ExcelColumn(value = "是否院内制剂",col = 28)
    private String hospitalPreparations;

    /**
     * 药品说明书
     */
    @ExcelColumn(value = "药品说明书",col = 29)
    private String content;


    /**
     * ERP码
     */
    @ExcelColumn(value = "ERP码",col = 30)
    private String erp;

    /**
     * ERPID
     */
    @ExcelColumn(value = "ERPID",col = 31)
    private String erpId;

    /**
     * 供应商
     */
    @ExcelColumn(value = "供应商",col = 32)
    private String supplier;
}
