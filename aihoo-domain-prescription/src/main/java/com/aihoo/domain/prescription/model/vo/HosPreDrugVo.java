package com.aihoo.domain.prescription.model.vo;

import com.aihoo.domain.prescription.model.entity.HosPreDrugOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Classname HosPrescriptionVo
 * @Description hf
 * @Date 2020/9/19 13:38
 * @Created by ad
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HosPreDrugVo extends HosPreDrugOrder {

    private String totalPrice;

    private String type;

    private String visitMdtNum;
}
