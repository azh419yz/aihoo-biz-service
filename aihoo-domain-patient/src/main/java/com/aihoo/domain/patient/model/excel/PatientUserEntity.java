package com.aihoo.domain.patient.model.excel;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;

/**
 * 患者用户 Excel 导出实体。
 *
 * <p>原 admin 模块下的 com.aihoo.admin.common.excel.entity.PatientUserEntity，
 * 1:1 迁入 patient 域；注解 ExcelColumn 来自 shared-kernel（com.aihoo.excel）。</p>
 */
@Data
public class PatientUserEntity {

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
     * 手机号
     */
    @ExcelColumn(value = "手机号",col = 3)
    private String mobile;

    /**
     * 昵称
     */
    @ExcelColumn(value = "昵称",col = 4)
    private String nickName;

    /**
     * 头像
     */
    @ExcelColumn(value = "头像",col = 5)
    private String headImg;

    /**
     * 姓名
     */
    @ExcelColumn(value = "姓名",col = 6)
    private String name;

    /**
     * 年龄
     */
    @ExcelColumn(value = "年龄",col = 7)
    private String age;

    /**
     * 身份证
     */
    @ExcelColumn(value = "身份证",col = 8)
    private String idCard;

    /**
     * 性别 0-女 1-男
     */
    @ExcelColumn(value = "性别",col = 9)
    private String sex;

    /**
     * 生日
     */
    @ExcelColumn(value = "生日",col = 10)
    private String birthDay;

    /**
     * 状态(是否启用 1:启用 0:停用)
     */
    @ExcelColumn(value = "状态",col = 11)
    private String status;

    /**
     * 是否认证 NONE-未认证 WAIT-认证中 PASS-已认证 REJECT-认证失败
     */
    @ExcelColumn(value = "是否认证",col = 12)
    private String isAuth;

    /**
     * 是否绑定微信 0否 1是
     */
    @ExcelColumn(value = "是否绑定微信",col = 13)
    private String unionId;

    /**
     * 是否绑定支付宝 0否 1是
     */
    @ExcelColumn(value = "是否绑定支付宝",col = 14)
    private String alipayOpenId;

    /**
     * 是否绑定Apple 0否 1是
     */
    @ExcelColumn(value = "是否绑定Apple",col = 15)
    private String appleId;

    /**
     * 用户首次登录IP
     */
    @ExcelColumn(value = "用户首次登录IP",col = 16)
    private String ipAddress;

    /**
     * 用户首次登录城市
     */
    @ExcelColumn(value = "用户首次登录城市",col = 17)
    private String city;

    /**
     * 是否注销  0 否 1 是
     */
    @ExcelColumn(value = "是否注销 ",col = 18)
    private String isCancel;

}
