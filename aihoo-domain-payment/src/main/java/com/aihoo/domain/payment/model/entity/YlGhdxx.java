package com.aihoo.domain.payment.model.entity;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 挂号单信息表
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
@Data
@TableName("TB_YL_GHDXX")
public class YlGhdxx implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医疗机构代码  复合主键，医保的医院11位代码
     */
    @ExcelColumn(value = "医疗机构代码",col = 1)
    private String yljgdm;

    /**
     * 挂/退号日期  复合主键，格式为YYYYMMDD
     */
    @ExcelColumn(value = "挂/退号日期",col = 2)
    private String ghrq;

    /**
     * 复诊流水号    复合主键，互联网医院复诊诊疗系统中产生的唯一复诊编码
     */
    @ExcelColumn(value = "复诊流水号",col = 3)
    private String fzlsh;

    /**
     * 退号标志   复合主键。1、挂号；2：退号。见说明（1）
     */
    @ExcelColumn(value = "退号标志",col = 4)
    private String gthbz;

    /**
     * 卫生机构（组织）代码
     */
    @ExcelColumn(value = "卫生机构（组织）代码",col = 5)
    private String wsjgdm;

    /**
     * 卡号
     */
    @ExcelColumn(value = "卡号",col = 6)
    private String kh;

    /**
     * 卡类型   见说明（2）
     */
    @ExcelColumn(value = "卡类型",col = 7)
    private String klx;

    /**
     * 身份证件号码
     */
    @ExcelColumn(value = "身份证件号码",col = 8)
    private String sfzjhm;

    /**
     * 身份证件类别代码     参见卫标CV02.01.101身份证件类别代码
     */
    @ExcelColumn(value = "身份证件类别代码",col = 9)
    private String sfzjlx;

    /**
     * 健康码标识
     */
    @ExcelColumn(value = "健康码标识",col = 10)
    private String jkmbs;

    /**
     * 认证日期   患者实名认证日期。格式YYYYMMDD
     */
    @ExcelColumn(value = "认证日期",col = 11)
    private String dyrq;

    /**
     * 认证类型   1、微信号认证；2、支付宝认证；3、银联认证；4、医联认证；5、市卫健委；6、公安认证；9、其他认证；
     */
    @ExcelColumn(value = "认证类型",col = 12)
    private String rzlx;

    /**
     * 保险类型   用来区分该次收费属于医保类型、干保类型还是自费类型。见说明（3）
     */
    @ExcelColumn(value = "保险类型",col = 13)
    private String bxlx;

    /**
     * 是否预约挂号  1、是；0、否。
     */
    @ExcelColumn(value = "是否预约挂号",col = 14)
    private String sfyy;

    /**
     * 预约单ID    关联预约单信息表的预约单ID。如果预约挂号字段为“1”，则必填
     */
    @ExcelColumn(value = "预约单ID",col = 15)
    private String yydid;

    /**
     * 发票号
     */
    @ExcelColumn(value = "发票号",col = 16)
    private String fph;

    /**
     * 挂号类别   100、普通门诊。依据政策《互联网医院管理办法（试行）》要求，仅保留100 普通门诊
     */
    @ExcelColumn(value = "挂号类别",col = 17)
    private String ghlb;

    /**
     * 科室编码    该科室代码对应“医疗卫生机构业务科室分类与代码”（卫统）标准的代码
     */
    @ExcelColumn(value = "科室编码",col = 18)
    private String ksbm;

    /**
     * 外地标志   1、本市；2、外地；3、境外（港澳台）；4、外国；5、未知
     */
    @ExcelColumn(value = "外地标志",col = 19)
    private String wdbz;

    /**
     * 修改标志  1、正常；2.撤销。
     */
    @ExcelColumn(value = "修改标志",col = 20)
    private String xgbz;

    /**
     * 多源认证时间，示例：20220214 16:20:28
     */
    @ExcelColumn(value = "认证时间",col = 21)
    @TableField(exist = false)
    private String authTime;
}