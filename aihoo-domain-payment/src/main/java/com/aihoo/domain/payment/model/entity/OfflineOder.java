package com.aihoo.domain.payment.model.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
    private String preparationTime;
    /**
     * 制单人
     */
    private String preparationName;
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 客户id
     */
    private String customerId;
    /**
     * 客户经理
     */
    private String manager;
    /**
     * 客户经理手机号
     */
    private String managerPhone;
    /**
     * 客户姓名
     */
    private String managerName;
    /**
     * 就诊人
     */
    private String name;
    /**
     * 就诊人手机号
     */
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
    private String visitTime;
    /**
     * 就诊id
     */
    private String treatmentId;
    /**
     * 就诊项目名单
     */
    private String treatmentName;
    /**
     * 症状描述
     */
    private String symptom;
    /**
     * 就诊医院
     */
    private String hospitalName;
    /**
     * 就诊专科
     */
    private String doctorSpecialty;
    /**
     * 就诊医生
     */
    private String doctorName;
    /**
     * 费用
     */
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
    private String mode;
    /**
     * 手术时间
     */
    private String operationTime;
    /**
     * 状态
     */
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
