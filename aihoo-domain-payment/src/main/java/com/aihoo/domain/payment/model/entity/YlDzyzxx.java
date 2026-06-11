package com.aihoo.domain.payment.model.entity;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 电子医嘱信息表
 * </p>
 *
 * @author lx
 * @since 2020-10-28
 */
@Data
@TableName("TB_YL_DZYZXX")
public class YlDzyzxx implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医疗机构代码  复合主键，医保的医院11位代码
     */
    @ExcelColumn(value = "医疗机构代码",col = 1)
    private String yljgdm;

    /**
     * 医嘱项目类型     复合主键，参考CV06.00.229医嘱项目类型代码表
     */
    @ExcelColumn(value = "医嘱项目类型",col = 2)
    private String yzxmlx;

    /**
     * 医嘱编码     复合主键，填写医嘱项目类型对应的医嘱编号，例如：医嘱项目类型为“01”时，填写处方号，医嘱项目类型为“02”或“03”时，填写检查或检验申请单号
     */
    @ExcelColumn(value = "医嘱编码",col = 3)
    private String yzbm;

    /**
     * 卫生机构（组织）代码
     */
    @ExcelColumn(value = "卫生机构（组织）代码",col = 4)
    private String wsjgdm;

    /**
     * 复诊流水号    互联网医院复诊诊疗系统中产生的唯一复诊编码
     */
    @ExcelColumn(value = "复诊流水号",col = 5)
    private String fzlsh;

    /**
     * 卡号
     */
    @ExcelColumn(value = "卡号",col = 6)
    private String kh;

    /**
     * 卡类型
     */
    @ExcelColumn(value = "卡类型",col = 7)
    private String klx;

    /**
     * 身份证件号码
     */
    @ExcelColumn(value = "身份证件号码",col = 8)
    private String sfzjhm;

    /**
     * 身份证件类别代码
     */
    @ExcelColumn(value = "身份证件类别代码",col = 9)
    private String sfzjlx;

    /**
     * 就诊科室代码    该科室代码对应“医疗卫生机构业务科室分类与代码”（卫统）标准的代码
     */
    @ExcelColumn(value = "就诊科室代码",col = 10)
    private String jzksdm;

    /**
     * 医嘱开具时间     医嘱项目类型为“01”时填报开方时间；医嘱项目类型为“02”或“03”时填写申请时间
     */
    @ExcelColumn(value = "医嘱开具时间",col = 11)
    private String yzkjsj;

    /**
     * 医生工号
     */
    @ExcelColumn(value = "医生工号",col = 12)
    private String ysgh;

    /**
     * 医生姓名
     */
    @ExcelColumn(value = "医生姓名",col = 13)
    private String ysxm;

    /**
     * 医生身份证号
     */
    @ExcelColumn(value = "医生身份证号",col = 14)
    private String yssfz;

    /**
     * 收费标志   是否已收费1、是；0、否。
     */
    @ExcelColumn(value = "收费标志",col = 15)
    private String sfbz;

    /**
     * 医嘱开具签名值    Base64编码
     */
    @ExcelColumn(value = "医嘱开具签名值",col = 16)
    private String yzkjqmz;

    /**
     * 医护人员证书序列号    开方医生的CA证书序列号。上海CA签发证书序列号
     */
    @ExcelColumn(value = "医护人员证书序列号",col = 17)
    private String yhryzsxlh;

    /**
     * 可信时间戳    Base64编码
     */
    @ExcelColumn(value = "可信时间戳",col = 18)
    private String kxsjc;

    /**
     * 审方药师姓名   医嘱项目类型为“01”时必填。其他医嘱项目类型填报“-”
     */
    @ExcelColumn(value = "审方药师姓名",col = 19)
    private String sfysxm;

    /**
     * 审方药师身份证号  医嘱项目类型为“01”时必填。其他医嘱项目类型填报“-”
     */
    @ExcelColumn(value = "审方药师身份证号",col = 20)
    private String sfyssfzh;

    /**
     * 发药药师姓名    医嘱项目类型为“01”时必填。其他医嘱项目类型填报“-”
     */
    @ExcelColumn(value = "发药药师姓名",col = 21)
    private String fyysxm;

    /**
     * 发药药师身份证号    医嘱项目类型为“01”时必填。其他医嘱项目类型填报“-”
     */
    @ExcelColumn(value = "发药药师身份证号",col = 22)
    private String fyyssfzh;

    /**
     * 修改标志  1、正常；2.撤销。
     */
    @ExcelColumn(value = "修改标志",col = 23)
    private String xgbz;
}