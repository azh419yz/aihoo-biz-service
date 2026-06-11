package com.aihoo.domain.consultation.model.vo;

import com.aihoo.domain.consultation.model.entity.Mdt;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Classname MdtVo
 * @Description hf
 * @Date 2020/9/21 18:49
 * @Created by ad
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MdtVo extends Mdt {
    private String diseaseId;
    private String img;
}