package com.aihoo.domain.payment.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *   预约页面（返回前端的对象）
 **/
@Data
public class OfflineTreatmentTime implements Serializable {
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
     * 院区id
     */
    private String districtId;
    /**
     * 院区
     */
    private String district;
    /**
     *  科室id
     */
    private String codeId;
    /**
     *   科室名称
     */
    private String codeName;
    /**
     *  挂号id
     */
    private String groupId;
    /**
     *  挂号类型
     */
    private String groupName;
    /**
     * 擅长
     */
    private String staffNote;
    /**
     * 状态
     */
    private String bookingNum;

}