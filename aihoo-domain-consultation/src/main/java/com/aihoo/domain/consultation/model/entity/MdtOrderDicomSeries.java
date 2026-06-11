package com.aihoo.domain.consultation.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_mdt_order_dicom_series
 * @author
 */
@Data
@TableName("t_mdt_order_dicom_series")
public class MdtOrderDicomSeries implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 日期yyyyMMdd
     */
    private String seriesDate;

    /**
     * uid
     */
    private String seriesInstanceUid;

    /**
     * 序号
     */
    private String seriesNo;

    /**
     * 时间HHmmss
     */
    private String seriesTime;

    /**
     * studyid
     */
    private String studyId;

    private static final long serialVersionUID = 1L;
}