package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 患者用户表
 */
@TableName("t_patient_user")
@Data
public class PatientUser implements Serializable {
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
     * 登录token
     */
    private String token;

    private String unionId;

    private String appleId;
}