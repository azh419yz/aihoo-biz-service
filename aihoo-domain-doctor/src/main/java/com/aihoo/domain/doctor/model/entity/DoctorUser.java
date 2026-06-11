package com.aihoo.domain.doctor.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname DoctorUser
 * @Description hf
 * @Date 2020/9/16 10:28
 * @Created by ad
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
     * 年龄
     */
    private String age;

    /**
     * 性别 0 代表男 1代表女
     */
    private String sex;

    /**
     * 生日
     */
    private String birthday;

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
     * 成就
     */
    private String achievement;

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
     * 邮箱
     */
    private String email;

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
     * 用户登录即时通信 IM 的密码
     */
    private String userSig;

    /**
     * 是否注销 0 否 1是
     */
    private String isCancel;

    /**
     * 排序
     */
    @TableField("`index`")
    private String index;

    /**
     * '会诊医生类型   助理医生 ASSISTANT  会诊医生 CONSULTANT'
     */
    private String doctorType;

    @TableField(exist = false)
    private DoctorSet doctorSet;

    @TableField(exist = false)
    private List<DoctorSetTimes> doctorSetTimes;

    /**
     * 展示好评率
     */
    @TableField(exist = false)
    private String highOpinion;

    /**
     * 真实好评率
     */
    @TableField(exist = false)
    private String realHighOpinion;

    /**
     * 接单量
     */
    @TableField(exist = false)
    private String orderNumber;

    /**
     * 总账单金额
     */
    @TableField(exist = false)
    private String totalBill;

    /**
     * 问诊订单金额
     */
    @TableField(exist = false)
    private String visitBill;

    /**
     * 复诊订单金额
     */
    @TableField(exist = false)
    private String revisitBill;

    /**
     * 是否开启专家咨询
     */
    @TableField(exist = false)
    private String isImg;

    /**
     * 是否开启语音问诊
     */
    @TableField(exist = false)
    private String isVoice;

    /**
     * 是否开启视频问诊
     */
    @TableField(exist = false)
    private String isVideo;

    /**
     * 是否开启复诊
     */
    @TableField(exist = false)
    private String isRevisit;

    /**
     * 是否开启会诊
     */
    @TableField(exist = false)
    private String isMdt;

    private String medicalLicensePageOne;
    private String medicalLicensePageTwo;
    private String medicalLicenseNo;
    private String medicalLicenseIssueDate;
    private String practiceCertificatePageOne;
    private String practiceCertificatePageTwo;
    private String practiceCertificateNo;
    private String practiceCertificateIssueDate;
    private String area;
}
