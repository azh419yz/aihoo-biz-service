package com.aihoo.domain.prescription.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_hos_prescription_error")
public class HosPrescriptionError implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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

    @TableField(select = false)
    private String seal;

    private String doctorSignet;
    private String img;
    private String checkStatus;
    private String checkTime;
    private String checkPharmaceutist;
    private String checkPharmaceutistId;
    private String checkContent;

    @TableField(select = false)
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
    private String check_timeout;
    private String manualCheckContent;
    private String manualCheckTime;
    private String manualCheckPharmaceutist;
    private String manualCheckPharmaceutistId;

    @TableField(select = false)
    private String manualCheckReturn;
}
