package com.aihoo.domain.patient.model.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 患者用户表
 * </p>
 *
 * @author mcp
 * @since 2020-10-08
 */
@Data
@TableName("t_patient_user")
public class PatientUser implements Serializable {

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
     * 手机号
     */
    private String mobile;

    /**
     * 微信openId
     */
    private String wechatOpenId;

    /**
     * 支付宝openId
     */
    private String alipayOpenId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 性别 0-女 1-男
     */
    private String sex;

    /**
     * 生日
     */
    private String birthDay;

    /**
     * 状态(是否启用 1:启用 0:停用)
     */
    private String status;

    /**
     * 是否认证 NONE-未认证 WAIT-认证中 PASS-已认证 REJECT-认证失败
     */
    private String isAuth;

    /**
     * 认证时间
     */
    private String authTime;

    /**
     * 登录token
     */
    private String token;

    /**
     * 微信用户统一标识
     */
    private String unionId;

    /**
     * Apple用户唯一标识
     */
    private String appleId;

    /**
     * 是否注销  0 否 1 是
     */
    private String isCancel;

    @TableField(exist = false)
    private List<Address> addresses;

    /**
     * 就诊人列表（跨域字段，对应旧 t_hos_sick 表）
     * TODO: 就诊人域迁移后，改用 Visit/HosSick 跨域类型。
     */
    @TableField(exist = false)
    private List<HosSickRef> hosSicks;
}
