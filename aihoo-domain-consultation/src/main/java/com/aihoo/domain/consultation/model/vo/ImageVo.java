package com.aihoo.domain.consultation.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 影像对象
 */
@Data
public class ImageVo implements Serializable {
    /**
     * IMAGEFILE	文件名称
     */
    private String ImageFile;
    /**
     * IMAGENO	图像编号
     */
    private int ImageNo;
    /**
     * SOPINSTANCEUID	图像唯一号
     */
    private String SopInstanceUid;
    /**
     * IMAGEDATE	图像日期
     */
    private String ImageDate;
    /**
     * IMAGETIME	图像时间
     */
    private String ImageTime;
    /**
     * 窗宽
     */
//    private int WnWidth;
    /**
     * 窗位
     */
//    private int WinLevel;
    /**
     * REGDT
     * SOPCLASSUID
     */
//    private String SopclassUid;
}