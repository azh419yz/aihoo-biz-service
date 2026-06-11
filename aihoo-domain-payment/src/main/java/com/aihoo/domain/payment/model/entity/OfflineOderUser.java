package com.aihoo.domain.payment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 线下挂号 用户信息表
 **/
@Data
@TableName("t_offline_oder_user")
public class OfflineOderUser implements Serializable {

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
     * 是否删除 0未删除 1已删除
     */
    private String isDelete;
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
     * 客户手机号
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
     * 就诊人手机号备用
     */
    private String sparePhone;
    /**
     * 证件类型
     */
    private String certificatesType;
    /**
     * 身份证
     */
    private String certificates;
    /**
     * 出生日期
     */
    private String birth;
    /**
     * 性别 1女 2男
     */
    private String sex;
    /**
     * 医保卡
     */
    private String insuranceCard;
    /**
     * 备注
     */
    private String statement;

}