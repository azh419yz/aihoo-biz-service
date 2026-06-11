package com.aihoo.domain.payment.model.entity;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * <p>
 * 互联网医院业务收入统计表
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
@Data
@TableName("TB_TJ_HLWYY_YWSR")
public class TjHlwyyYwsr implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医疗机构代码 复合主键，医保的医院11位代码
     */
    @ExcelColumn(value = "医疗机构代码",col = 1)
    private String yljgdm;

    /**
     * 科室编码 复合主键，该科室代码对应“医疗卫生机构业务科室分类与代码”（卫统）标准的代码
     */
    @ExcelColumn(value = "科室编码",col = 2)
    private String ksbm;

    /**
     * 业务时间  复合主键，业务时间为实际发生结算的时间。格式为YYYYMMDD
     */
    @ExcelColumn(value = "业务时间",col = 3)
    private String ywsj;

    /**
     * 卫生机构（组织）代码
     */
    @ExcelColumn(value = "卫生机构（组织）代码",col = 4)
    private String wsjgdm;

    /**
     * 互联网诊疗总收入   参考《上海市卫生资源与医疗服务调查制度》中的门诊收入填报
     */
    @ExcelColumn(value = "互联网诊疗总收入",col = 5)
    private BigDecimal hlwzlzsr;

    /**
     * 互联网诊疗药品收入     西药费+中成药费+中草药费
     */
    @ExcelColumn(value = "互联网诊疗药品收入",col = 6)
    private BigDecimal hlwzlypsr;

    /**
     * 互联网诊疗医保收入     在互联网医院服务的医疗总费用中医保范围内收入部分
     */
    @ExcelColumn(value = "互联网诊疗医保收入",col = 7)
    private BigDecimal hlwzlybsr;

    /**
     * 互联网诊疗自费收入   在互联网医院服务的医疗总费用中医保范围外自费收入部分
     */
    @ExcelColumn(value = "互联网诊疗自费收入",col = 8)
    private BigDecimal hlwzlzfsr;

    /**
     * 修改标志  1、正常；2.撤销。
     */
    @ExcelColumn(value = "修改标志",col = 9)
    private String xgbz;
}