package com.aihoo.domain.hospital.model.vo;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;

import java.util.List;

/***
 * 医院管理导出vo
 */
@Data
public class HospitalListVo {

    /**
     * 医院id
     */
    @ExcelColumn(value = "医院id",col = 1)
    private String id;

    /**
     * 医院名称
     */
    @ExcelColumn(value = "医院名称",col = 2)
    private String hosName;

    /**
     * 科室编码名称
     */
    @ExcelColumn(value = "科室编码名称",col =3)
    private List<String> departList;
}
