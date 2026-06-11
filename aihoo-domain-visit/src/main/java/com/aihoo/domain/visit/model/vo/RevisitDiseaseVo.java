package com.aihoo.domain.visit.model.vo;

import lombok.Data;

/**
 * @Classname RevisitDisease
 * @Description hf
 * @Date 2020/10/27 11:22
 * @Created by ad
 */
@Data
public class RevisitDiseaseVo {
    // 复诊对应的疾病
    private String revisitDiseaseNames;
    // 复诊编码
    private String visitMdtNum;
}
