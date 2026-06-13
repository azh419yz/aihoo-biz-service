package com.aihoo.domain.visit.model.vo;

import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.entity.HosVisitImg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class HosVisitSelectVo extends HosVisit {
    private String doctorName;
    private String doctorHeadImg;
    private String hospitalName;
    private String officeHolderName;
    private String departName;
    private String sickName;
    private String imUserId;
    private String imUserSig;
    private String fiveStarProportion;
    private String orderNumber;

    private List<HosVisitImg> imgs;

    @Schema(name = "hosPrescriptions", description = "处方")
    List<HosPrescription> hosPrescriptions;
}
