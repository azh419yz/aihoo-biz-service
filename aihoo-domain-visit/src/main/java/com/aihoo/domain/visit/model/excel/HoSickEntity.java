package com.aihoo.domain.visit.model.excel;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;

/**
 * @Classname PatientUserEntity
 * @Description hf
 * @Date 2020/10/15 10:49
 * @Created by ad
 */
@Data
public class HoSickEntity {

    /**
     * 就诊人编号
     */
    @ExcelColumn(value = "就诊人编号",col = 1)
    private String id;

    /**
     * 创建时间
     */
    @ExcelColumn(value = "创建时间",col = 2)
    private String createTime;

    /**
     * 更新时间
     */
    @ExcelColumn(value = "更新时间",col = 3)
    private String updateTime;

    /**
     * 就诊人手机号
     */
    @ExcelColumn(value = "就诊人手机号",col = 4)
    private String mobile;

    /**
     * 就诊人姓名
     */
    @ExcelColumn(value = "就诊人姓名",col = 5)
    private String name;

    /**
     * 身份证
     */
    @ExcelColumn(value = "就诊人身份证",col = 6)
    private String idCard;

    /**
     * 性别 0-女 1-男
     */
    @ExcelColumn(value = "性别",col = 7)
    private String sex;

    /**
     * 就诊人年龄
     */
    @ExcelColumn(value = "就诊人年龄",col = 8)
    private String age;

    /**
     * 是否删除 0否 1是
     */
    @ExcelColumn(value = "是否删除",col = 9)
    private String isDelete;

    /**
     * 用户账号
     */
    @ExcelColumn(value = "用户账号",col = 10)
    private String patientUser;

    /**
     * 订单总数
     */
    @ExcelColumn(value = "订单总数",col = 11)
    private String sumNumber;

}
