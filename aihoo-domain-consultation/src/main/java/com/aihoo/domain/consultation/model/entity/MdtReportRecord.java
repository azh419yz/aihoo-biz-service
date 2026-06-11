package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname MdtReportRecord
 * @Description hf
 * @Date 2020/11/16 11:36
 * @Created by ad
 */
@Data
@TableName("t_mdt_report_record")
public class MdtReportRecord implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * mdt订单id
     */
    private String mdtOrderId;

    /**
     * 视频记录地址
     */
    private String videoRecord;

    /**
     * 会诊记录文件地址 多个文件以英文逗号分隔
     */
    private String consultationRecords;

    private static final long serialVersionUID = 1L;
}