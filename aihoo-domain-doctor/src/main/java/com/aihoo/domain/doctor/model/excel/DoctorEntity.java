package com.aihoo.domain.doctor.model.excel;

import com.aihoo.excel.ExcelColumn;
import lombok.Data;


/**
 * @Classname DoctorEntity
 * @Description hf
 * @Date 2020/10/13 13:42
 * @Created by ad
 */
@Data
public class DoctorEntity {

    /**
     * 创建时间
     */
 /*   @ExcelColumn(value = "创建时间",col = 2)
    private String createTime;*/

    /**
     * 更新时间
     */
   /* @ExcelColumn(value = "更新时间",col = 3)
    private String updateTime;*/

    /**
     * 创建人账号
     */
/*    @ExcelColumn(value = "创建人账号",col = 4)
    private String createUserName;*/

    /**
     * 手机号
     */
    @ExcelColumn(value = "手机号", col = 5)
    private String mobile;

    /**
     * 头像
     */
    @ExcelColumn(value = "头像", col = 6)
    private String headImg;

    /**
     * 姓名
     */
    @ExcelColumn(value = "姓名", col = 7)
    private String name;

    /**
     * 年龄
     */
    @ExcelColumn(value = "年龄", col = 8)
    private String age;

    /**
     * 性别 0 代表男 1代表女
     */
    @ExcelColumn(value = "性别", col = 9)
    private String sex;

    /**
     * 生日
     */
    @ExcelColumn(value = "生日", col = 10)
    private String birthday;

    /**
     * 标签 以|分隔
     */
    @ExcelColumn(value = "标签", col = 11)
    private String tag;

    /**
     * 工号
     */
    @ExcelColumn(value = "工号", col = 12)
    private String memberNum;

    /**
     * 医院id
     */
    // @ExcelColumn(value = "医院id",col = 13)
    private String hospitalId;


    /**
     * 就职医院
     */
    @ExcelColumn(value = "就职医院", col = 14)
    private String hospitalName;

    /**
     * 科室id
     */
    //@ExcelColumn(value = "科室id",col = 15)
    private String departId;

    /**
     * 科室编码
     */
    //@ExcelColumn(value = "科室编码",col = 16)
    private String departCode;

    /**
     * 所在科室
     */
    @ExcelColumn(value = "所在科室", col = 17)
    private String departName;

    /**
     * 职称编码  t_dict type=DOCT_TITLE
     */
    // @ExcelColumn(value = "职称编码",col = 18)
    private String officeHolderCode;

    /**
     * 成就
     */
    @ExcelColumn(value = "成就", col = 18)
    private String achievement;
    /**
     * 职称
     */
    @ExcelColumn(value = "职称", col = 19)
    private String officeHolderName;

    /**
     * 擅长
     */
    @ExcelColumn(value = "擅长", col = 20)
    private String beGoodAtText;

    /**
     * 简介
     */
    @ExcelColumn(value = "简介", col = 21)
    private String introductionText;

    /**
     * 状态(是否启用 1:启用 0:停用)
     */
    @ExcelColumn(value = "状态", col = 22)
    private String status;

    /**
     * 是否CA认证 NONE-未认证 WAIT-认证中 PASS-已认证 REJECT-认证失败
     */
    @ExcelColumn(value = "是否CA认证", col = 23)
    private String isAuth;

    /**
     * CA序列号
     */
    @ExcelColumn(value = "CA序列号", col = 24)
    private String caNumber;

    /**
     * 人员类别编码  t_dict type=PERSON_TYPE
     */
    //@ExcelColumn(value = "人员类别编码",col = 25)
    private String personTypeCode;

    /**
     * 人员类别
     */
    @ExcelColumn(value = "人员类别", col = 26)
    private String personTypeName;

    /**
     * 职务编码  t_dict type=POSITION
     */
    //@ExcelColumn(value = "职务编码",col = 27)
    private String positionCode;

    /**
     * 职务
     */
    @ExcelColumn(value = "职务", col = 28)
    private String positionName;

    /**
     * 证件类型编码  t_dict type=PAPERS
     */
    // @ExcelColumn(value = "证件编码",col = 29)
    private String papersCode;

    /**
     * 证件
     */
    @ExcelColumn(value = "证件类型", col = 30)
    private String papersName;

    /**
     * 证件号码
     */
    @ExcelColumn(value = "证件号码", col = 31)
    private String papersNumbers;

    /**
     * 是否开启图文问诊 0-未开 1-开启
     */
    @ExcelColumn(value = "是否开启专家咨询", col = 32)
    private String isImg;
    /*
     *//**
     * 是否开启语音问诊 0-未开 1-开启
     *//*
    @ExcelColumn(value = "是否开启电话问诊",col = 33)
    private String isVoice;

    *//**
     * 是否开启视频问诊 0-未开 1-开启
     *//*
    @ExcelColumn(value = "是否开启视频问诊",col = 34)
    private String isVideo;*/

    /**
     * 是否开启复诊 0-未开 1-开启
     */
    @ExcelColumn(value = "是否开启复诊", col = 35)
    private String isRevisit;

    /**
     * 是否开启会诊 0-未开 1-开启
     */
    @ExcelColumn(value = "是否开启会诊", col = 36)
    private String isMdt;

    /**
     * 专家咨询价格
     */
    @ExcelColumn(value = "专家咨询价格", col = 37)
    private String imgPrice;

    /**
     * 会诊医生类型
     */
    @ExcelColumn(value = "会诊医生类型", col = 38)
    private String doctorType;

    /**
     * 语音问诊价格
     */
   /* @ExcelColumn(value = "电话问诊价格",col = 38)
    private String voicePrice;*/

    /**
     * 视频问诊价格
     */
  /*  @ExcelColumn(value = "视频问诊价格",col = 39)
    private String videoPrice;*/

}
