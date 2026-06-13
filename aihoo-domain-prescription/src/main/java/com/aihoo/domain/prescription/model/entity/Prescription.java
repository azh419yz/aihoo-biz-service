package com.aihoo.domain.prescription.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("t_hos_prescription")
public class Prescription implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    // 公共字段（合并自 HosPrescription）
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
    private String doctorSignet;
    private String checkStatus;
    private String checkTime;
    private String checkPharmaceutist;
    private String checkContent;
    private String checkReturn;
    private String totalPrice;
    private String payType;
    private String payTime;
    private String status;
    private String isPay;
    private String img;
    private String endTime;
    private String isCancel;
    private String disease;
    private String syndrome;
    private String drugstoreId;
    private String medicineStatusCode;

    // 药品配送订单字段（合并自 HosPreDrugOrder）
    private String shippendStartTime;
    private String shippendEndTime;
    private String isPre;
    private String hosPrescriptionId;
    private String expressCode;
    private String expressName;
    private String shippingNo;
    private String provinceCode;
    private String cityCode;
    private String districtCode;
    private String province;
    private String city;
    private String district;
    private String address;

    // 处方药品字段（合并自 HosPrescriptionDrug）
    private String drugId;
    private String size;
    private String drugDosCode;
    private String drugDosName;
    private String unitMeasure;
    private String packUnitCode;
    private String packUnitName;
    private String price;
    private String freqMedCode;
    private String freqMedName;
    private String routeAdmiCode;
    private String routeAdmiName;
    private String isAntibiotics;
    private String isInjection;
    private String isAnesthesia;
    private String isMonitor;
    private String content;
    private String number;
    private String useDay;
    private String dosage;

    // 处方用法字段（合并自 HosPrescriptionInstruction）
    private String usage;
    private String doseNumber;
    private String dose;
    private String times;
    private String decoctionMethod;
    private String decoctionSize;
    private String advice;
    private String remark;

    @TableField(exist = false)
    private List<Object> hosPrescriptionDiseases;

    @TableField(exist = false)
    private List<Object> hosPrescriptionDrugs;

    @TableField(exist = false)
    private List<Object> hosPreDrugOrders;
}
