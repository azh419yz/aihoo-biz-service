package com.aihoo.domain.payment.model.vo;

import lombok.Data;

/**
 * 华山医院 科室信息
 **/
@Data
public class OfflineDepartment {

    /**
     * 主键id
     */
    private String id;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 科室编号
     */
    private String codeId;
    /**
     * 科室名称
     */
    private String codeName;
    /**
     * 院区id
     */
    private String districtId;
    /**
     * 挂哈类型id
     */
    private String bookingId;
}