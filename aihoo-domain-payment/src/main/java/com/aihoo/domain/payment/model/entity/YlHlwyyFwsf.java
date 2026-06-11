package com.aihoo.domain.payment.model.entity;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * <p>
 * 互联网服务收费表
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
@Data
@TableName("TB_YL_HLWYY_FWSF")
public class YlHlwyyFwsf implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医疗机构代码   复合主键，医保的医院11位代码
     */
    @ExcelColumn(value = "医疗机构代码",col = 1)
    private String yljgdm;

    /**
     * 收/退费编号  复合主键，见说明（1）
     */
    @ExcelColumn(value = "收/退费编号",col = 2)
    private String stfbh;

    /**
     * 退费标志  复合主键。1、收费；2、退费。
     */
    @ExcelColumn(value = "退费标志",col = 3)
    private String stfbz;

    /**
     * 卫生机构（组织）代码
     */
    @ExcelColumn(value = "卫生机构（组织）代码",col = 4)
    private String wsjgdm;

    /**
     * 复诊流水号   互联网医院复诊诊疗系统中产生的唯一复诊编码。
     */
    @ExcelColumn(value = "复诊流水号",col = 5)
    private String fzlsh;

    /**
     * 卡号
     */
    @ExcelColumn(value = "卡号",col = 6)
    private String kh;

    /**
     * 卡类型  卡类型详见“挂号单信息表”说明（2）
     */
    @ExcelColumn(value = "卡类型",col = 7)
    private String klx;

    /**
     * 身份证件号码
     */
    @ExcelColumn(value = "身份证件号码",col = 8)
    private String sfzjhm;

    /**
     * 身份证件类别代码   参见卫标CV02.01.101身份证件类别代码
     */
    @ExcelColumn(value = "身份证件类别代码",col = 9)
    private String sfzjlx;

    /**
     * 保险类型    用来区分对象所属保险类型。见前述“挂号单信息表”的说明（3）
     */
    @ExcelColumn(value = "保险类型",col = 10)
    private String bxlx;

    /**
     * 收/退费时间    格式：YYYYMMDD HH:MM:SS
     */
    @ExcelColumn(value = "收/退费时间 ",col = 11)
    // private LocalDateTime stfsj;
    private String stfsj;

    /**
     * 收/退费总额   收退费均以正数表达。详见说明（5）
     */
    @ExcelColumn(value = "收/退费总额",col = 12)
    private BigDecimal stfze;

    /**
     * 医保范围外自费   医保范围外自费总额。见说明（2）
     */
    @ExcelColumn(value = "医保范围外自费",col = 13)
    private BigDecimal ybfwwzf;

    /**
     * 挂号费   当挂号费一并收取时指自费挂号费。
     */
    @ExcelColumn(value = "挂号费",col = 14)
    private BigDecimal ghf;

    /**
     * 诊疗费
     */
    @ExcelColumn(value = "诊疗费",col = 15)
    private BigDecimal zlf;

    /**
     * 治疗费 见说明（3）开展此项服务后必填
     */
    @ExcelColumn(value = "治疗费",col = 16)
    private BigDecimal zhf;

    /**
     * 护理费  见说明（3）开展此项服务后必填
     */
    @ExcelColumn(value = "护理费",col = 17)
    private BigDecimal hlf;

    /**
     * 检查费  见说明（3）开展此项服务后必填
     */
    @ExcelColumn(value = "检查费",col = 18)
    private BigDecimal jcf;

    /**
     * 化验费  见说明（3）开展此项服务后必填
     */
    @ExcelColumn(value = "化验费",col = 19)
    private BigDecimal hyf;

    /**
     * 透视费  见说明（3）开展此项服务后必填
     */
    @ExcelColumn(value = "透视费",col = 20)
    private BigDecimal tsf;

    /**
     * 摄片费  见说明（3）开展此项服务后必填
     */
    @ExcelColumn(value = "摄片费",col = 21)
    private BigDecimal spf;

    /**
     * 西药费  见说明（3）无费用时填写“0”
     */
    @ExcelColumn(value = "西药费",col = 22)
    private BigDecimal xyf;

    /**
     * 中成药费  见说明（3）无费用时填写“0”
     */
    @ExcelColumn(value = "中成药费",col = 23)
    private BigDecimal zcyf;

    /**
     * 中草药费  见说明（3）无费用时填写“0”
     */
    @ExcelColumn(value = "中草药费",col = 24)
    private BigDecimal zcaf;

    /**
     * 其他费用  见说明（3）无费用时填写“0”
     */
    @ExcelColumn(value = "其他费用",col = 25)
    private BigDecimal qtf;

    /**
     * 自费部分支付渠道   0、无自费部分；1、支付宝；2、微信；3、银联。
     */
    @ExcelColumn(value = "自费部分支付渠道",col = 26)
    private String zfzgqd;

    /**
     * 处方张数
     */
    @ExcelColumn(value = "处方张数",col = 27)
    private Integer cfzs;

    /**
     * 发票号  超过一张发票时，以“；”间隔
     */
    @ExcelColumn(value = "发票号",col = 28)
    private String fph;

    /**
     * 修改标志   1、正常；2.撤销。
     */
    @ExcelColumn(value = "修改标志",col = 29)
    private String xgbz;
}