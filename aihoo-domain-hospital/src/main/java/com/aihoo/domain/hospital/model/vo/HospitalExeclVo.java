package com.aihoo.domain.hospital.model.vo;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;

@Data
public class HospitalExeclVo {

    /**
     * 医院名称
     */
    @ExcelColumn(value = "医院名称",col = 1)
    private String hosName;

    /**
     * 医院等级名称
     */
    @ExcelColumn(value = "医院等级名称",col =2)
    private String hosGradeName;

    /**
     * 医院级别名称
     */
    @ExcelColumn(value = "医院级别名称",col = 3)
    private String hosLevelName;

    /**
     * 医院类型名称
     */
    @ExcelColumn(value = "医院类型名称",col = 4)
    private String hosCateName;

    /**
     * 医院性质名称
     */
    @ExcelColumn(value = "医院性质名称",col = 5)
    private String hosAttName;

    /**
     * 省
     */
    @ExcelColumn(value = "省",col = 6)
    private String province;

    /**
     * 市
     */
    @ExcelColumn(value = "市",col = 7)
    private String city;

    /**
     * 区
     */
    @ExcelColumn(value = "区",col = 8)
    private String district;

    /**
     * 医院详情地址
     */
    @ExcelColumn(value = "医院详情地址",col = 9)
    private String address;

    /**
     * 医院电话
     */
    @ExcelColumn(value = "医院电话",col = 10)
    private String hosMobile;

    /**
     * 医院简介
     */
    @ExcelColumn(value = "医院简介",col = 11)
    private String content;

    /**
     * 医院所属科室
     */
    @ExcelColumn(value = "医院所属科室",col = 12)
    private String departName;
}
