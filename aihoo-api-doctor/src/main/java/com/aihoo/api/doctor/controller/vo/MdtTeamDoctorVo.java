package com.aihoo.api.doctor.controller.vo;

import lombok.Data;

@Data
public class MdtTeamDoctorVo {
    /**
     * 医生id
     */
    private String id;

    /**
     * 证件号码
     */
    private String papersNumbers;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 姓名
     */
    private String name;

    /**
     * 就职医院
     */
    private String hospitalName;

    /**
     * 所在科室
     */
    private String departName;

    /**
     * 职称
     */
    private String officeHolderName;

    /**
     * 擅长
     */
    private String beGoodAtText;

    /**
     * 成就
     */
    private String achievement;

    /**
     * 简介
     */
    private String introductionText;

    /**
     * 会诊费
     */
    private String price;

    /**
     * 是否是主治医生 0:不是，1:是
     */
    private String isMain;

    /**
     * 会诊医生类型   助理医生 ASSISTANT  会诊医生 CONSULTANT
     */
    private String doctorType;
}
