package com.aihoo.domain.consultation.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SeriesVo implements Serializable {
    /**
     * SERIESNO	NUMBER	序列号
     */
    private int SeriesNo;
    /**
     * SERIESINSTANCEUID	序列UID
     */
    private String SeriesInstanceUid;
    /**
     * SERIESDATE 序列日期yyyyMMdd
     */
    private String SeriesDate;
    /**
     * SERIESTIME 序列时间HHmmss
     */
    private String SeriesTime;
    /**
     * ERIESDESCRIPTION 描述
     */
//    private String SeriesDescription;
    /**
     * 影像对象添加到这里
     */
    private List<ImageVo> ImageDTOs;

}