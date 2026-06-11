package com.aihoo.domain.visit.model.vo;

import lombok.Data;

/**
 * @Classname RevisitListVo
 * @Description hf
 * @Date 2020/10/23 18:46
 * @Created by ad
 */
@Data
public class RevisitListVo {
    //复诊订单编码
    private String visitMdtNum;
    // 处方主键id
    private String prescriptionKeyId;
    //处方编码
    private String prescriptionNum;
    //药品主键id
    private String drugNumKeyId;
    //药品订单id
    private String drugNum;
}
