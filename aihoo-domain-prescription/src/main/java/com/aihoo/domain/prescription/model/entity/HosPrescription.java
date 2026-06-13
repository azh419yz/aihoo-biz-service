package com.aihoo.domain.prescription.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 处方主表
 */
@Data
public class HosPrescription implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    private String id;
    private String createTime;
    private String updateTime;
    private String patientUserId;
    private String hosSickId;
    private String doctorUserId;
    private String type;
    private String otherId;
    private String visitMdtNum;
    private String orderNum;
    private String prescriptionNum;
    private String feeType;
    private String name;
    private String idCard;
    private String sex;
    private String age;
    private String mobile;
    private String departCode;
    private String departName;
    private String medicalCertificate;
    private String seal;
    private String doctorSignet;
    private String img;
    private String checkStatus;
    private String checkStatusName;
    private String taboos;
    private String disease;
    private String drugstoreId;
    private String syndrome;
    private String checkTime;
    private String checkPharmaceutist;
    private String checkContent;
    private String checkReturn;
    private String totalPrice;
    private String payType;
    private String payTime;
    private String status;
    private String isPay;
    private String endTime;
    private String isCancel;
    private String kidneyStatus;
    private String liverStatus;
    private String womanStatus;
    private String allegeName;
    private String isDisable;
    private String isCanForce;
    private String checkTimeout;
    private String drugName;
    private String manualCheckContent;
    private String manualCheckTime;
    private String manualCheckPharmaceutist;
    private String manualCheckPharmaceutistId;
    private String manualCheckReturn;
    private String checkPharmaceutistId;
}
