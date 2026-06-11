package com.aihoo.domain.prescription.stub;

import lombok.Data;

import java.io.Serializable;

/**
 * 占位实体：原 t_mdt_order 表。
 * 来自 admin 的 MdtOrder 实体。该域尚未迁移，本地占位以让 HosPreDrugServiceImpl / HosPrescriptionServiceImpl 编译通过。
 * 待 consultation 域迁移完成后删除此文件并改用正式类型。
 */
@Data
public class MdtOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String createTime;
    private String updateTime;
    private String orderNum;

    // 处方ID
    private String preId;
    // 药态
    private String medicineStatusCode;
    // 药房ID
    private String drugstoreId;
    // 患者备注
    private String hosSickRemark;
    // 药师备注
    private String remark;
    // 药品照片
    private String pic;
    // 是否已打印PDF
    private String pdfFlag;
    // 是否已打印快递标签
    private String expressFlag;
    // 收件人姓名
    private String receiveName;
    // 收件人电话
    private String receivePhone;
    // 收件人区域
    private String receiveArea;
    // 收件人地址
    private String receiveAddress;
}
