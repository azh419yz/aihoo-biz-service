package com.aihoo.domain.payment.model.entity;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * <p>
 * 互联网服务收费明细表
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
@Data
@TableName("TB_YL_HLWYY_FWSFMX")
public class YlHlwyyFwsfmx implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医疗机构代码
     */
    @ExcelColumn(value = "医疗机构代码",col = 1)
    private String yljgdm;

    /**
     * 收费明细ID  复合主键，见说明（1）
     */
    @ExcelColumn(value = "收费明细ID",col = 2)
    private String sfmxid;

    /**
     * 退费标志   复合主键。1、收费；2、退费；
     */
    @ExcelColumn(value = "退费标志",col = 3)
    private String stfbz;

    /**
     * 卫生机构（组织）代码
     */
    @ExcelColumn(value = "卫生机构（组织）代码",col = 4)
    private String wsjgdm;

    /**
     * 复诊流水号    互联网医院复诊诊疗系统中产生的唯一复诊编码。
     */
    @ExcelColumn(value = "复诊流水号",col = 5)
    private String fzlsh;

    /**
     * 收/退费编号   用于与“互联网服务收费表”关联
     */
    @ExcelColumn(value = "收/退费编号",col = 6)
    private String stfbh;

    /**
     * 收/退费时间   格式：YYYYMMDD HH:MM:SS
     */
    @ExcelColumn(value = "收/退费时间",col = 7)
    // private LocalDateTime stfsj;
    private String stfsj;

    /**
     * 医嘱编码    医嘱项目类型为“01”时填报处方号；医嘱项目类型为“02”或“03”时填写检查或检验申请单号
     */
    @ExcelColumn(value = "医嘱编码",col = 8)
    private String yzbm;

    /**
     * 明细费用类别
     */
    @ExcelColumn(value = "明细费用类别",col = 9)
    private String mxfylb;

    /**
     * 项目标准代码   医保统一要求的收费编码
     */
    @ExcelColumn(value = "项目标准代码",col = 10)
    private String mxxmbmyb;

    /**
     * 明细项目单位
     */
    @ExcelColumn(value = "明细项目单位",col = 11)
    private String mxxmdw;

    /**
     * 明细项目单价   金额；>=0。
     */
    @ExcelColumn(value = "明细项目单价",col = 12)
    private BigDecimal mxxmdj;

    /**
     * 明细项目数量
     */
    @ExcelColumn(value = "明细项目数量",col = 13)
    private BigDecimal mxxmsl;

    /**
     * 明细项目金额  金额；>=0。
     */
    @ExcelColumn(value = "明细项目金额",col = 14)
    private BigDecimal mxxmje;

    /**
     * 修改标志  1、正常；2.撤销。
     */
    @ExcelColumn(value = "修改标志",col = 15)
    private String xgbz;
}