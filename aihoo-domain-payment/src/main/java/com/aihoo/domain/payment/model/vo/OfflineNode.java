package com.aihoo.domain.payment.model.vo;

import lombok.Data;

/**
 *  华东医院 科室信息
 **/
@Data
public class OfflineNode {

    /**
     * 科室id
     */
    private String nodeId;
    /**
     * 科室name
     */
    private String nodeName;
    /**
     *挂号类型
     */
    private String poolId;
}