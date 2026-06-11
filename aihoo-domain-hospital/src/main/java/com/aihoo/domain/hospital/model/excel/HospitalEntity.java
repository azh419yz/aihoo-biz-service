package com.aihoo.domain.hospital.model.excel;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;


/**
 * @Classname HospitalEntity
 * @Description hf
 * @Date 2020/10/12 10:15
 * @Created by ad
 */
@Data
public class HospitalEntity {

    /**
     * 医院名称
     */
    @ExcelColumn(value = "医院名称",col = 1)
    private String hosName;

    /**
     * 医院等级编码 d_dict type=HOS_GRADE
     */
    @ExcelColumn(value = "医院等级编码",col =2)
    private String hosGradeCode;

    /**
     * 医院等级名称
     */
    @ExcelColumn(value = "医院等级",col =3)
    private String hosGradeName;

    /**
     * 医院级别编码 d_dict type=HOS_LEVEL
     */
    @ExcelColumn(value = "医院级别编码",col =4)
    private String hosLevelCode;

    /**
     * 医院级别名称
     */
    @ExcelColumn(value = "医院级别",col = 5)
    private String hosLevelName;

    /**
     * 医院类型编码 d_dict type=HOS_CATE
     */
    @ExcelColumn(value = "医院类型编码",col = 6)
    private String hosCateCode;

    /**
     * 医院类型名称
     */
    @ExcelColumn(value = "医院类型",col = 7)
    private String hosCateName;

    /**
     * 医院性质编码 d_dict type=HOS_ATT
     */
    @ExcelColumn(value = "医院性质编码",col = 8)
    private String hosAttCode;

    /**
     * 医院性质名称
     */
    @ExcelColumn(value = "医院性质",col = 9)
    private String hosAttName;

    /**
     * 省
     */
    @ExcelColumn(value = "省code",col = 10)
    private String provinceCode;

    /**
     * 省
     */
    @ExcelColumn(value = "医院所属省",col = 11)
    private String province;

    /**
     * 市
     */
    @ExcelColumn(value = "市code",col = 12)
    private String cityCode;

    /**
     * 市
     */
    @ExcelColumn(value = "医院所属市",col = 13)
    private String city;

    /**
     * 区
     */
    @ExcelColumn(value = "区code",col = 14)
    private String districtCode;

    /**
     * 区
     */
    @ExcelColumn(value = "医院所属区",col = 15)
    private String district;

    /**
     * 医院详情地址
     */
    @ExcelColumn(value = "医院详情地址",col = 16)
    private String address;

    /**
     * 医院电话
     */
    @ExcelColumn(value = "医院电话",col = 17)
    private String hosMobile;

    /**
     * 医院简介
     */
    @ExcelColumn(value = "医院简介",col = 18)
    private String content;

    /**
     * 医院所属科室code
     */
    @ExcelColumn(value = "医院所属科室code",col = 19)
    private String departCode;

    /**
     * 医院所属科室
     */
    @ExcelColumn(value = "医院所属科室",col = 20)
    private String departName;
}
