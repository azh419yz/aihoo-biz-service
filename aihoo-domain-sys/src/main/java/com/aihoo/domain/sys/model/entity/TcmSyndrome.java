package com.aihoo.domain.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 中医辨证表
 */
@Data
@TableName("tcm_syndrome")
public class TcmSyndrome implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long diseaseId;
    private String syndromeName;
    private String syndromeType;
    private String mainSymptoms;
    private String secondarySymptoms;
    private String tonguePulse;
    private String pathogenesis;
    private String treatmentPrinciple;
    private String recommendedFormula;
    private String modifiedFormula;
    private String acupoints;
    private String dietaryAdvice;
    private String dailyRegimen;
    private String prognosis;
    private String differentialDiagnosis;
    private String remark;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
