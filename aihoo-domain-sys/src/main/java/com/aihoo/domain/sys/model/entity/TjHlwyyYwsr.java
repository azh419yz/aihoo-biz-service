package com.aihoo.domain.sys.model.entity;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
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
    private String yljgdm;

    /**
     * 科室编码 复合主键，该科室代码对应“医疗卫生机构业务科室分类与代码”（卫统）标准的代码
     */
    private String ksbm;

    /**
     * 业务时间  复合主键，业务时间为实际发生结算的时间。格式为YYYYMMDD
     */
    private String ywsj;

    /**
     * 卫生机构（组织）代码
     */
    private String wsjgdm;

    /**
     * 互联网诊疗总收入   参考《上海市卫生资源与医疗服务调查制度》中的门诊收入填报
     */
    private BigDecimal hlwzlzsr;

    /**
     * 互联网诊疗药品收入     西药费+中成药费+中草药费
     */
    private BigDecimal hlwzlypsr;

    /**
     * 互联网诊疗医保收入     在互联网医院服务的医疗总费用中医保范围内收入部分
     */
    private BigDecimal hlwzlybsr;

    /**
     * 互联网诊疗自费收入   在互联网医院服务的医疗总费用中医保范围外自费收入部分
     */
    private BigDecimal hlwzlzfsr;

    /**
     * 修改标志  1、正常；2.撤销。
     */
    private String xgbz;
}
