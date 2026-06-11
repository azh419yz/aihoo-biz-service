package com.aihoo.domain.prescription.stub;

import lombok.Data;

import java.io.Serializable;

/**
 * 占位实体：原 t_hos_visit 表。
 * 来自 admin 的 HosVisit 实体。该域尚未迁移，本地占位以让 HosPreDrugServiceImpl 编译通过。
 * 待 visit 域迁移完成后删除此文件并改用正式类型。
 */
@Data
public class HosVisit implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String orderNum;
}
