package com.aihoo.domain.consultation.model.vo;

import lombok.Data;

/**
 * @Classname MdtTeamVo
 * @Description hf
 * @Date 2020/12/24 17:21
 * @Created by ad
 */
@Data
public class MdtTeamVo {
    // 团队主键id
    private String mdtTeamId;
    // 团队名称
    private String mdtTeamName;
    // 会诊医生
    private String mdtDoctorName;
    // 领衔医生
    private String headDoctorName;
    // 助理医生
    private String assistantDoctorName;

}