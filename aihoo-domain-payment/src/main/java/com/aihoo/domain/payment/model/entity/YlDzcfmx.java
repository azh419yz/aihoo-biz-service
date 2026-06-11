package com.aihoo.domain.payment.model.entity;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 电子处方明细表
 * </p>
 *
 * @author lx
 * @since 2020-10-28
 */
@Data
@TableName("TB_YL_DZCFMX")
public class YlDzcfmx implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医疗机构代码  复合主键，医保的医院11位代码。
     */
    @ExcelColumn(value = "医疗机构代码",col = 1)
    private String yljgdm;

    /**
     * 处方号   复合主键
     */
    @ExcelColumn(value = "处方号",col = 2)
    private String cyh;

    /**
     * 处方项目明细号码    复合主键，处方内容项目明细编码
     */
    @ExcelColumn(value = "处方项目明细号码",col = 3)
    private String cfmxh;

    /**
     * 开方时间    格式：YYYYMMDD HH:MM:SS
     */
    @ExcelColumn(value = "开方时间",col = 4)
    private String kfsj;

    /**
     * 项目标准代码    医保统一要求的收费编码
     */
    @ExcelColumn(value = "项目标准代码",col = 5)
    private String xmbmyb;

    /**
     * 剂型代码    参考药物剂型代码CV08.50.002
     */
    @ExcelColumn(value = "剂型代码",col = 6)
    private String jxdm;

    /**
     * 用药途径代码     参考用药途径代码CV06.00.102
     */
    @ExcelColumn(value = "用药途径代码",col = 7)
    private String yf;

    /**
     * 用药天数
     */
    @ExcelColumn(value = "用药天数",col = 8)
    private Integer yyts;

    /**
     * 发药数量    发药包装数量
     */
    @ExcelColumn(value = "发药数量",col = 9)
    private Integer ypsl;

    /**
     * 发药数量单位     发药包装单位
     */
    @ExcelColumn(value = "发药数量单位",col = 10)
    private String ypdw;

    /**
     * 用药频次代码
     */
    @ExcelColumn(value = "用药频次代码",col = 11)
    private String sypcdm;

    /**
     * 每次使用剂量（数量）
     */
    @ExcelColumn(value = "每次使用剂量（数量）",col = 12)
    private Integer jl;

    /**
     * 每次使用剂量（数量）单位      例如克（g）、毫克（mg）、毫升（ml）；U.、I.U.、片、粒，瓶、小时、日、个、次等。对应不同药品使用不同单位
     */
    @ExcelColumn(value = "每次使用剂量（数量）单位",col = 13)
    private String dw;

    /**
     * 处方贴数     中药必填
     */
    @ExcelColumn(value = "处方贴数",col = 14)
    private Integer cfts;

    /**
     * 中药煎煮法代码     中药必填。1、包煎；2、冲服；3、后煎；4、后下；5、另包；6、先煎；7、烊化；9、其他。
     */
    @ExcelColumn(value = "中药煎煮法代码",col = 15)
    private String jydm;

    /**
     * 修改标志  1、正常；2.撤销。
     */
    @ExcelColumn(value = "修改标志",col = 16)
    private String xgbz;
}