package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * t_mdt_order_dicom_study
 *
 * @author
 */
@Data
@TableName("t_mdt_order_dicom_study")
public class MdtOrderDicomStudy implements Serializable {
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
     * 订单id
     */
    private String orderId;

    /**
     * uid
     */
    private String studyInstanceUid;

    /**
     * 检查模态，如：CT、MR、CR、DR、X等
     */
    private String studyModality;

    /**
     * 检查号
     */
    private String accessionNo;

    /**
     * 患者生日，这里表示1980年09月08日1980-09-08
     */
    private String birthday;

    /**
     * 检查部位
     */
    private String bodypart;

    /**
     * 检查日期，8位字符串，这里表示2020年11月06日
     */
    private String examDate;

    /**
     * 检查名称
     */
    private String examName;

    /**
     * 检查时间，6位字符串
     */
    private String examTime;

    /**
     * 给医院分配的唯一编号，这里固定传MSH0001
     */
    private String hospId;

    /**
     * 本次检查所有图像的数量
     */
    private String imageCount;

    /**
     * 患者ID
     */
    private String patId;

    /**
     * 患者姓名
     */
    private String patName;

    /**
     * 患者性别
     */
    private String patSex;

    /**
     * 留空
     */
    private String pathDetail;

    /**
     * 一个存储位置对应一个PathId，这里固定传52
     */
    private String pathId;

    /**
     * 压缩包地址
     */
    private String zipUrl;

    /**
     * 是否解压 0-否 1-是 2-解压失败
     */
    private String isUnzip;

    /**
     * oss文件夹路径/dicom/202104231612jkls
     */
    private String ossDirectory;

    /**
     * 是否上传梅清
     */
    private String isUploadMeiqing;

    /**
     * 是否作废
     */
    private String isCancel;

    /**
     * 作废原因
     */
    private String cancelReason;

    /**
     * 检查时间
     */
    private String checkTime;

    /**
     * 上传梅清平台时间
     */
    private String uploadMeiqingTime;

    /**
     * 梅清地址
     */
    private String meiqingUrl;

    /**
     * 报告文件
     */
    private String reportUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 取消时间
     */
    private String cancelTime;


    private static final long serialVersionUID = 1L;
}