package com.aihoo.domain.sys.model.entity;
import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 药品目录字典表
 * </p>
 *
 * @author lx
 * @since 2020-10-26
 */
@Data
@TableName("TB_DIC_MEDICINES")
public class DicMedicines implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 药品id
     */
    private String drugId;
    /**
     * 医疗机构代码  复合主键，医保的医院11位代码。
     */
    @ExcelColumn(value = "医疗机构代码",col = 1)
    private String yljgdm;

    /**
     * 项目标准代码  复合主键，医保统一要求的收费编码
     */
    @ExcelColumn(value = "项目标准代码",col = 2)
    private String ybmlbm;

    /**
     * 卫生机构（组织）代码
     */
    @ExcelColumn(value = "卫生机构（组织）代码",col = 3)
    private String wsjgdm;

    /**
     * 使用标志  1、使用中；0、停用。
     */
    @ExcelColumn(value = "使用标志",col = 4)
    private String sybz;

    /**
     * 药品注册通用名   国家食药监批文的注册通用名，或者是本院自行界定的通用名
     */
    @ExcelColumn(value = "药品注册通用名",col = 5)
    private String tymc;

    /**
     * 剂型代码   详见药物剂型代码CV08.50.002
     */
    @ExcelColumn(value = "剂型代码",col = 6)
    private String jxda;

    /**
     * 批准文号      （即国药准字）按照国家药监发布内容填写，若是医疗器材则填写注册证号。见说明（1）
     */
        @ExcelColumn(value = "批准文号",col = 7)
    private String yppzwh;

    /**
     * 剂型名称
     */
    @ExcelColumn(value = "剂型名称",col = 8)
    private String bzjx;

    /**
     * 院内制剂标志  0、非自制药品；1、自制药品。
     */
    @ExcelColumn(value = "院内制剂标志",col = 9)
    private String ynzjbz;

    /**
     * 基药标识  1、国基药；2、上海增补基药；0、非前述两种
     */
    @ExcelColumn(value = "基药标识",col = 10)
    private String jybz;

    /**
     * 抗生素标识   1、抗生素；0、非抗生素
     */
    @ExcelColumn(value = "抗生素标识",col = 11)
    private String kssbz;

    /**
     * 毒麻精放标识   1、是；0、否；
     */
    @ExcelColumn(value = "毒麻精放标识",col = 12)
    private String dmjfbs;

    /**
     * 备注说明   
     */
    @ExcelColumn(value = "备注说明",col = 13)
    private String bzsm;

    /**
     * 修改标志  1、正常；2.撤销。
     */
    @ExcelColumn(value = "修改标志",col = 14)
    private String xgbz;
}
