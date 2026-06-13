package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_mdt_order_dicom_study")
public class MdtOrderDicomStudy implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String orderId;
    private String studyInstanceUid;
    private String studyModality;
    private String accessionNo;
    private String birthday;
    private String bodypart;
    private String examDate;
    private String examName;
    private String examTime;
    private String hospId;
    private String imageCount;
    private String patId;
    private String patName;
    private String patSex;
    private String pathDetail;
    private String pathId;
    private String zipUrl;
    private String isUnzip;
    private String ossDirectory;
    private String isUploadMeiqing;
    private String isCancel;
    private String cancelReason;
    private String checkTime;
    private String uploadMeiqingTime;
    private String meiqingUrl;
    private String reportUrl;
    private String remark;
    private String cancelTime;

    private static final long serialVersionUID = 1L;
}
