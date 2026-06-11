package com.aihoo.domain.payment.model.excel;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;

/**
 * 会诊订单导出 Entity
 *
 * <p>从旧 admin 的 com.aihoo.admin.common.excel.entity.MdtOrderExportEntity 迁入。</p>
 */
@Data
public class MdtOrderExportEntity {

    @ExcelColumn(value = "订单ID", col = 1)
    private String orderId;

    @ExcelColumn(value = "处方ID", col = 2)
    private String preId;

    @ExcelColumn(value = "患者姓名", col = 3)
    private String name;

    @ExcelColumn(value = "性别", col = 4)
    private String sex;

    @ExcelColumn(value = "年龄", col = 5)
    private String age;

    @ExcelColumn(value = "疾病名称", col = 6)
    private String mdtName;

    @ExcelColumn(value = "支付金额", col = 7)
    private String price;

    @ExcelColumn(value = "支付方式", col = 8)
    private String payType;

    @ExcelColumn(value = "支付时间", col = 9)
    private String payTime;

    @ExcelColumn(value = "订单状态", col = 10)
    private String status;

    @ExcelColumn(value = "收件人", col = 11)
    private String receiveName;

    @ExcelColumn(value = "收件人电话", col = 12)
    private String receivePhone;

    @ExcelColumn(value = "收件地址", col = 13)
    private String receiveAddress;

    @ExcelColumn(value = "全部剂数", col = 14)
    private String doseNumber;
}