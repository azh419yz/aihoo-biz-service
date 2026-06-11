package com.aihoo.domain.consultation.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 检查
 */
@Data
public class StudyVo implements Serializable {
    /**
     * STUDYINSTANCEUID	检查UIDDExam
     */
    private String StudyInstanceUid;
    /**
     * SERIALNO	AccessNo 检查号
     */
    private String AccessionNo;
    /**
     * ATID	病人ID
     */
    private String PatId;
    /**
     * ATNAME	病人名称
     */
    private String PatName;
    /**
     * 病人性别
     */
    private String PatSex;
    /**
     *MSH0001
     */
    private String HospID;
    /**
     * BODYPART	检查部位
     */
    private String Bodypart;

    /**
     * ODALITY	模态
     */
    private String StudyModality;
    /**
     * ATHDETAIL	详细路径编号
     */
    private int PathId;
    /**
     * ATHDETAIL	详细路径
     */
    private String PathDetail;
    /**
     * EXAMDATE	检查日期
     */
    private String ExamDate;
    /**
     * EXAMTIME	检查时间
     */
    private String ExamTime;
    /**
     * IMAGECOUNT	整个检查的图像总数
     */
    private int ImageCount;
    /**
     * TUDYID	检查ID
     */
//    private String ExamId;
    /**
     * TUDYDESCRIPTION	检查描述
     */
//    private String StudyDescription;
    /**
     * ERIESCOUNT	整个检查的序列总数
     */
//    private int SeriesCount;
    /**
     * BIRTHDAY	出生年月
     */
    private String Birthday;
    /**
     * EXAMNAME	检查名称
     */
    private String ExamName;
    /**
     * 序列对象添加到这里
     */
    private List<SeriesVo> SeriesDTOs;
}