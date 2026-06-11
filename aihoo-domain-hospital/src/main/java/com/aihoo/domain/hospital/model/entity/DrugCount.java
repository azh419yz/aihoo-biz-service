package com.aihoo.domain.hospital.model.entity;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 药品对账表
 * </p>
 *
 * @author lx
 * @since 2020-11-04
 */
@Data
public class DrugCount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    /*@ExcelColumn(value = "序号",col = 1)*/
    private Long id;

    /**
     * 合偶平方药品ID
     */
    @ExcelColumn(value = "合偶平方药品ID",col = 2)
    private String mshId;

    /**
     * 药品名称
     */
    @ExcelColumn(value = "药品名称",col = 3)
    private String name;

    /**
     * 供应商药品编号
     */
    @ExcelColumn(value = "供应商药品编号",col = 4)
    private String supplierCode;

    /**
     * 数量
     */
    @ExcelColumn(value = "数量",col = 5)
    private Integer num;

    /**
     * 单价
     */
    @ExcelColumn(value = "单价",col = 6)
    private BigDecimal price;

    /**
     * 总价
     */
    @ExcelColumn(value = "总价",col = 7)
    private BigDecimal totalPrice;

    /**
     * 供应商
     */
    @ExcelColumn(value = "供应商",col = 8)
    private String supplier;

    /**
     * 支付类型 支付宝ALIPAY 微信WECHAT
     */
    @ExcelColumn(value = "支付方式",col = 9)
    private String payType;

    /**
     * 交易状态 0：成功、1：失败
     */
    @ExcelColumn(value = "交易状态",col = 10)
    private String tradeStatus;

    /**
     * 创建时间
     */
    @ExcelColumn(value = "完成时间",col = 11)
    private LocalDateTime createTime;

    /**
     * 統計月份
     */
    @ExcelColumn(value = "统计月份",col = 11)
    private String month;

}
