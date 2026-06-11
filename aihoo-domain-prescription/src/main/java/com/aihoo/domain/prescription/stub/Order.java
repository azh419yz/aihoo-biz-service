package com.aihoo.domain.prescription.stub;

import lombok.Data;

import java.io.Serializable;

/**
 * 占位实体：原 t_order 表。
 * 来自 admin 的 Order 实体。该域尚未迁移，本地占位以让 HosPreDrugServiceImpl 编译通过。
 * 待 payment/order 域迁移完成后删除此文件并改用正式类型。
 */
@Data
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String orderType;
    private String orderNum;
    private String payType;
    private String payOrderNum;
    private String patientUserId;
    private String otherId;
    private String totalPrice;
    private String payTime;
    private String payStatus;
}
