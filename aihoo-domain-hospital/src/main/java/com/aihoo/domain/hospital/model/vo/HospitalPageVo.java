package com.aihoo.domain.hospital.model.vo;

import lombok.Data;

/**
 * 医院管理分页响应vo
 */
@Data
public class HospitalPageVo {

    private String id;

    private String hospitalNo;

    private String createUserId;

    private String hosName;

    private String hosGradeCode;

    private String hosGradeName;

    private String hosLevelCode;

    private String hosLevelName;

    private String hosCateCode;

    private String hosCateName;

    private String hosAttCode;

    private String hosAttName;

    private String status;

    private Long doctorCount;
}
