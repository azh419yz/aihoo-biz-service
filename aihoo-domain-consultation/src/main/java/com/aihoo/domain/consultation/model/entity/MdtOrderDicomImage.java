package com.aihoo.domain.consultation.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_mdt_order_dicom_image
 * @author
 */
@Data
@TableName("t_mdt_order_dicom_image")
public class MdtOrderDicomImage implements Serializable {
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
    private String imageDate;

    /**
     * oss地址，不包含前半部分
     */
    private String imageFile;

    /**
     * 序号
     */
    private String imageNo;

    /**
     * 时间HHmmss
     */
    private String imageTime;

    /**
     * uid
     */
    private String sopinstanceUid;

    /**
     * series_id
     */
    private String seriesId;

    private static final long serialVersionUID = 1L;
}