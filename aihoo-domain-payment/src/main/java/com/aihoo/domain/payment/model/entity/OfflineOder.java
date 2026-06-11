package com.aihoo.domain.payment.model.entity;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 线下订单
 **/
@Data
@TableName("t_offline_order")
public class OfflineOder implements Serializable, Comparable<OfflineOder> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 是否删除 0存在  1删除
     */
    private String isDelete;
    /**
     * 制单时间
     */
    @ExcelColumn(value = "制单时间", col = 1)
    private String preparationTime;
    /**
     * 制单人
     */
    @ExcelColumn(value = "制单人", col = 17)
    private String preparationName;
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 企业名称
     */
    @ExcelColumn(value = "所属企业", col = 2)
    private String companyName;
    /**
     * 客户id
     */
    @ExcelColumn(value = "客户ID", col = 3)
    private String customerId;
    /**
     * 客户经理
     */
    @ExcelColumn(value = "客户经理", col = 4)
    private String manager;
    /**
     * 客户经理手机号
     */
    @ExcelColumn(value = "客户经理手机号", col = 5)
    private String managerPhone;
    /**
     * 客户姓名
     */
    @ExcelColumn(value = "客户姓名", col = 6)
    private String managerName;
    /**
     * 就诊人
     */
    @ExcelColumn(value = "就诊人", col = 7)
    private String name;
    /**
     * 就诊人手机号
     */
    @ExcelColumn(value = "就诊人手机号", col = 9)
    private String phone;
    /**
     * 备用手机号
     */
    private String sparePhone;
    /**
     * 就诊人证件类型（身份证、户口本等。。。）
     */
    private String certificatesType;
    /**
     * 身份证号码
     */
    private String certificates;
    /**
     * 出生日期
     */
    private String birth;
    /**
     * 性别
     */
    private String sex;
    /**
     * 初诊/复诊  1/2
     */
    private String type;
    /**
     * 就诊日期
     */
    @ExcelColumn(value = "就诊日期", col = 10)
    private String visitTime;
    /**
     * 就诊id
     */
    private String treatmentId;
    /**
     * 就诊项目名单
     */
    @ExcelColumn(value = "就诊项目", col = 11)
    private String treatmentName;
    /**
     * 症状描述
     */
    private String symptom;
    /**
     * 就诊医院
     */
    @ExcelColumn(value = "就诊医院", col = 12)
    private String hospitalName;
    /**
     * 就诊专科
     */
    @ExcelColumn(value = "就诊科室", col = 13)
    private String doctorSpecialty;
    /**
     * 就诊医生
     */
    @ExcelColumn(value = "就诊医生", col = 14)
    private String doctorName;
    /**
     * 费用
     */
    @ExcelColumn(value = "费用结算", col = 15)
    private String price;
    /**
     * 手术项目
     */
    private String operation;
    /**
     * 医院科室
     */
    private String department;
    /**
     * 体检套餐
     */
    private String examination;
    /**
     * 备注
     */
    private String statement;
    /**
     * 结算方式
     */
    @ExcelColumn(value = "结算方式", col = 16)
    private String mode;
    /**
     * 手术时间
     */
    private String operationTime;
    /**
     * 状态
     */
    @ExcelColumn(value = "处理状态", col = 8)
    private String status;
    /**
     * 手术方案
     */
    private String preparationMode;

    /**
     * 计算各个医院的总个数
     */
    @TableField(exist = false)
    private String isCountHospital;

    /**
     * 计算各个医院的总费用
     */
    @TableField(exist = false)
    private String isSumPrice;

    /**
     * 计算总医院的数据
     */
    @TableField(exist = false)
    private String countHospital;

    /**
     * 计算总金额
     */
    @TableField(exist = false)
    private String sumPrice;

    /**
     * 1 未改变  2以改变
     */
    private String only;

    /**
     * 医保卡号
     */
    private String insuranceCard;

    @Override
    public int compareTo(OfflineOder o) {
        return this.hospitalName.compareTo(o.getHospitalName());
    }
}