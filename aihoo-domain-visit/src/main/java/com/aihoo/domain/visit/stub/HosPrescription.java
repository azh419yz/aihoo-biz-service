package com.aihoo.domain.visit.stub;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 占位实体：原 t_hos_prescription 表。
 * 字段集合取自 RevisitOrderServiceImpl.prescriptionBulkExport / prescriptionDurgBulkExport 的使用面。
 * 待 prescription 域统一后改用 com.aihoo.domain.prescription.model.entity.HosPrescription。
 */
@Data
@TableName("t_hos_prescription")
public class HosPrescription implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String doctorUserId;
    private String name;
    private String idCard;
    private String sex;
    private String age;
    private String mobile;
    private String type;
    private String visitMdtNum;
    private String orderNum;
    private String departName;
    private String img;
    private String checkStatus;
    private String checkPharmaceutist;
    private String medicalCertificate;
    private String totalPrice;
    private String payType;
    private String payTime;
    private String status;
    private String msg;
    private String isPay;
}
