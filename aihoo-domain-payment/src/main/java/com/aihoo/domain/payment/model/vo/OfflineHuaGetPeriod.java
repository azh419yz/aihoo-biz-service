package com.aihoo.domain.payment.model.vo;

import lombok.Data;

/**
 *  华东医院专家 获取具体的排班日期
 **/
@Data
public class OfflineHuaGetPeriod {

    /**
     * 时段id
     */
    private String periodId;
    /**
     * 时段开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 号源空余标识
     */
    private String freeFlag;
    /**
     * 号源数量
     */
    private String freeAmount;
}