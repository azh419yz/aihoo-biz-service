package com.aihoo.domain.payment.model.vo;

import java.io.Serializable;

public class ExcelOfflineBlacklist implements Serializable {
    /**
     * 疾病编码
     */
    private String name;

    /**
     * 疾病编码
     */
    private String mobile;

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
     * 医院id
     */
    private String hospitalId;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 拉入黑名单时间
     */
    private String realDate;


    private static final long serialVersionUID = 1L;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPapersCode() {
        return papersCode;
    }

    public void setPapersCode(String papersCode) {
        this.papersCode = papersCode;
    }

    public String getPapersName() {
        return papersName;
    }

    public void setPapersName(String papersName) {
        this.papersName = papersName;
    }

    public String getPapersNumbers() {
        return papersNumbers;
    }

    public void setPapersNumbers(String papersNumbers) {
        this.papersNumbers = papersNumbers;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getRealDate() {
        return realDate;
    }

    public void setRealDate(String realDate) {
        this.realDate = realDate;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}