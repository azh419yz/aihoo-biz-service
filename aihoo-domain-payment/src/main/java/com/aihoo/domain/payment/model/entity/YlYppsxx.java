package com.aihoo.domain.payment.model.entity;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 药品配送信息表
 * </p>
 *
 * @author lx
 * @since 2020-10-28
 */
@Data
@TableName("TB_YL_YPPSXX")
public class YlYppsxx implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医疗机构代码  复合主键，医保的医院11位代码
     */
    @ExcelColumn(value = "医疗机构代码",col = 1)
    private String yljgdm;

    /**
     * 处方号    复合主键
     */
    @ExcelColumn(value = "处方号",col = 2)
    private String cyh;

    /**
     * 复诊流水号    互联网医院复诊诊疗系统中产生的唯一复诊编码
     */
    @ExcelColumn(value = "复诊流水号",col = 3)
    private String fzlsh;

    /**
     * 卫生机构（组织）代码
     */
    @ExcelColumn(value = "卫生机构（组织）代码",col = 4)
    private String wsjgdm;

    /**
     * 配送方式  0、医院取药；1、物流配送；2、药店取药。
     */
    @ExcelColumn(value = "配送方式",col = 5)
    private String psfs;

    /**
     * 配送开始时间  格式：YYYYMMDD HH:MM:SS
     */
    @ExcelColumn(value = "配送开始时间",col = 6)
    private String pskssj;

    /**
     * 配送结束时间    格式：YYYYMMDD HH:MM:SS
     */
    @ExcelColumn(value = "配送结束时间",col = 7)
    private String psjssj;

    /**
     * 配送状态  1、完成；0、未完成。
     */
    @ExcelColumn(value = "配送状态",col = 8)
    private String pszt;

    /**
     * 修改标志  1、正常；2.撤销。
     */
    @ExcelColumn(value = "修改标志",col = 9)
    private String xgbz;
}