package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 医生用户表
 * </p>
 *
 * @author mcp
 * @since 2020-09-18
 */
@Data
@TableName("t_doctor_user")
public class DoctorUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 创建人id
     */
    private String createUserId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 姓名
     */
    private String name;

    /**
     * 标签 以|分隔
     */
    private String tag;

    /**
     * 工号
     */
    private String memberNum;

    /**
     * 医院id
     */
    private String hospitalId;

    /**
     * 就职医院
     */
    private String hospitalName;

    /**
     * 科室id
     */
    private String departId;

    /**
     * 科室编码
     */
    private String departCode;

    /**
     * 所在科室
     */
    private String departName;

    /**
     * 职称编码  t_dict type=DOCT_TITLE
     */
    private String officeHolderCode;

    /**
     * 职称
     */
    private String officeHolderName;

    /**
     * 擅长
     */
    private String beGoodAtText;

    /**
     * 简介
     */
    private String introductionText;


    /**
     * 状态(是否启用 1:启用 0:停用)
     */
    private String status;

    /**
     * 是否CA认证 NONE-未认证 WAIT-认证中 PASS-已认证 REJECT-认证失败
     */
    private String isAuth;

    /**
     * 登录token
     */
    private String token;

    /**
     * CA序列号
     */
    private String caNumber;

    /**
     * CA证书
     */
    @TableField(select = false)
    private String caCert;

    /**
     * 人员类别编码  t_dict type=PERSON_TYPE
     */
    private String personTypeCode;

    /**
     * 人员类别
     */
    private String personTypeName;

    /**
     * 职务编码  t_dict type=POSITION
     */
    private String positionCode;

    /**
     * 职务
     */
    private String positionName;

    /**
     * 证件类型编码  t_dict type=PAPERS
     */
    private String papersCode;

    /**
     * 证件
     */
    private String papersName;

    /**
     * 证件号码
     */
    private String papersNumbers;

    /**
     * 腾讯IM
     */
    private String userSig;

    /**
     * 是否注销 0 否 1是
     */
    private String isCancel;
}
