package com.aihoo.domain.payment.model.excel;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;

/**
 * 会诊订单药品用量导出 Entity
 *
 * <p>从旧 admin 的 com.aihoo.admin.common.excel.entity.MdtOrderDrugExportEntity 迁入。</p>
 */
@Data
public class MdtOrderDrugExportEntity {

    @ExcelColumn(value = "药品名称", col = 1)
    private String name;

    @ExcelColumn(value = "价格", col = 2)
    private String price;

    @ExcelColumn(value = "数量", col = 3)
    private String number;

    @ExcelColumn(value = "单位", col = 4)
    private String unit;

    @ExcelColumn(value = "订单ID", col = 5)
    private String orderId;

    @ExcelColumn(value = "处方ID", col = 6)
    private String preId;
}