package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_mdt_order")
public class MdtOrder implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String patientUserId;
    private String hosSickId;
    private String adminId;
    private String mdtTeamId;
    private String mdtCode;
    private String mdtName;
    private String name;
    private String idCard;
    private String sex;
    private String age;
    private String mobile;
    private String totalPrice;
    private String payType;
    private String payTime;
    private String status;
    private String msg;
    private String orderNum;
    private String doctorUserId;
    private String doctorAdvice;
    private String mdtAppointmentTime;
    private String mdtStartTime;
    private String mdtEndTime;
    private String mdtDateStr;
    private String mdtTimeStr;
    private String outFileRemark;
    private String checkFileRemark;
    private String videoFileRemark;
    private String registerPrice;
    private String regOrderNum;
    private String regPayType;
    private String regPayTime;
    private String attendingDoctorName;
    private String consultationDoctorName;
    private String isPay;
    private String isRegPay;
    private String isCount;
    private String imDoctorNumber;
    private String mdtHospital;
    private String moderator;
    private String contactWay;
    private String mdtType;
    private String mdtRemark;
    private String mdtRoomUrl;
    private String mdtRoomId;
    private String isCanChat;
    private String isSendSuccess;
    private String isBalance;
    private String reportDoctorId;
    private String prescriptionDoctorId;
    private String isAgree;
}
