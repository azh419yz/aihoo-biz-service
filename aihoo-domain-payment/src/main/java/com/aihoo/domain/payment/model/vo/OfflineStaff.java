package com.aihoo.domain.payment.model.vo;

import lombok.Data;

/**
 * 医生 信息 数据
 **/
@Data
public class OfflineStaff {
    /**
     * 医生工号
     */
    private String staffCode;
    /**
     * 医生姓名
     */
    private String staffName;
    /**
     * 医生职称
     */
    private String staffTitle;
    /**
     * 医生简介
     */
    private String staffNote;
    /**
     * 科室名称
     */
    private String codeName;
    /**
     * 科室编号
     */
    private String codeId;
}