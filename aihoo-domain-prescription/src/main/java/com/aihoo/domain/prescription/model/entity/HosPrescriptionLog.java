package com.aihoo.domain.prescription.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 处方日志表
 */
@Data
@TableName("t_hos_prescription_log")
public class HosPrescriptionLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String hosPrescriptionId;
    private String type;
    private String patientUserId;
    private String doctorUserId;
    private String status;
    private String remark;
    private String sickId;
}
