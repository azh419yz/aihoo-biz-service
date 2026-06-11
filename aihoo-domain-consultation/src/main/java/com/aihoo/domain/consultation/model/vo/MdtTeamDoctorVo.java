package com.aihoo.domain.consultation.model.vo;

import lombok.Data;

/**
 * @Classname MdtTeamDoctorVo
 * @Description hf
 * @Date 2020/12/25 16:36
 * @Created by ad
 */
@Data
public class MdtTeamDoctorVo {
    private String doctorUserId;
    private String doctorType;
    private String isMain;
    private String price;
}