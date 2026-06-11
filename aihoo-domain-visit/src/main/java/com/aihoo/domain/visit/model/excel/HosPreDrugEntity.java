package com.aihoo.domain.visit.model.excel;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;


/**
 * @Classname HosPreDrugEntity
 * @Description hf
 * @Date 2020/10/26 11:34
 * @Created by ad
 */
@Data
public class HosPreDrugEntity {

    /**
     * 创建时间
     */
    @ExcelColumn(value = "创建时间",col = 1)
    private String createTime;

    /**
     * 更新时间
     */
    @ExcelColumn(value = "更新时间",col = 2)
    private String updateTime;

    /**
     * 收件人姓名
     */
    @ExcelColumn(value = "收件人姓名",col = 3)
    private String name;

    /**
     * 手机号
     */
    @ExcelColumn(value = "收件人手机号",col = 4)
    private String mobile;

    /**
     * 订单类型
     */
    @ExcelColumn(value = "订单类型",col = 5)
    private String type;

    /**
     * 关联复诊/会诊订单编号
     */
    @ExcelColumn(value = "关联复诊/会诊订单编号",col = 6)
    private String visitMdtNum;

    /**
     * 药品订单编号
     */
    @ExcelColumn(value = "药品订单编号",col = 7)
    private String orderNum;

    /**
     * 地址
     */
    @ExcelColumn(value = "地址",col = 8)
    private String address;

    /**
     * 订单状态
     */
    @ExcelColumn(value = "订单状态",col = 9)
    private String status;

    /**
     * 订单金额
     */
    @ExcelColumn(value = "订单金额",col = 10)
    private String totalPrice;

    /**
     * 就诊人姓名
     */
    @ExcelColumn(value = "就诊人姓名",col = 11)
    private String hoSickName;

    /**
     * 就诊人电话
     */
    @ExcelColumn(value = "就诊人电话",col = 12)
    private String hoSickMobile;

    /**
     * 药品名称
     */
    @ExcelColumn(value = "药品名称",col = 13)
    private String drugName;

    /**
     * 药品规格
     */
    @ExcelColumn(value = "药品规格",col = 14)
    private String size;

    /**
     * 药品单价
     */
    @ExcelColumn(value = "药品单价",col = 15)
    private String price;

    /**
     * 药品数量
     */
    @ExcelColumn(value = "药品数量",col = 16)
    private String number;

    /**
     * 小计
     */
    @ExcelColumn(value = "小计",col = 17)
    private String sumPrice;

    /**
     * 付款时间
     */
    @ExcelColumn(value = "付款时间",col = 18)
    private String payTime;

    /**
     * 产品总数
     */
    @ExcelColumn(value = "产品总数",col = 19)
    private String drugSumNumber;

    /**
     * 运费
     */
    @ExcelColumn(value = "运费",col = 20)
    private String freight;

    /**
     * 总计
     */
    @ExcelColumn(value = "总计",col = 21)
    private String priceSum;

}
