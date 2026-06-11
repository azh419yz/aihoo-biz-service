package com.aihoo.domain.consultation.model.vo;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

import java.util.List;

/**
 * @Classname MdtDetailsVo
 * @Description hf
 * @Date 2020/11/12 16:29
 * @Created by ad
 */
@Data
public class MdtDetailsVo {
    //会诊编码
    private String orderNum;
    // mdt管理员
    private String moderator;
    //mdt 项目
    private String mdtName;
    //mdt医院
    private String mdtHospital;
    //mdt管理员手机号
    private String contactWay;
    //门诊记录备注
    private String outFileRemark;
    //影像资料备注
    private String checkFileRemark;
    //处方图片集合
    private List<String> prescriptionImgList;

    private BasicInformation basicInformation;
    private InformationConsultation informationConsultation;
    private ConsultationReport consultationReport;


    // 基本信息实体
    @Data
    public static class BasicInformation{
        // 患者姓名
        private String name;
        //性别
        private String sex;
        //年龄
        private String age;
        //身份证号码
        private String idCard;
        //联系方式
        private String mobile;
        //会诊状态
        private String status;
        //订单编号
        private String orderNum;
        //创建时间
        private String createTime;
        //付款时间
        private String payTime;
        //完成时间
        private String mdtEndTime;
        //会诊费
        private String totalPrice;
        //医疗管家姓名
        private String adminName;
    }

    //会诊信息
    @Data
    public static class InformationConsultation{
        //预约会诊时间
        private String mdtAppointmentTime;
        //会诊类型
        private String mdtType;
        //会诊备注
        private String mdtRemark;
        //主治医生
        private String attendingDoctorName;
        //会诊医生
        private String consultationDoctorName;
        //会诊开始时间
        private String mdtStartTime;
        //会诊结束时间
        private String mdtEndTime;
        //视频记录
        private JSONArray videoRecord;
        //会诊记录文件
        private JSONArray consultationRecords;
    }

  /*  // 病史资料
    @Data
    public static class TheHistoryData{

        private List<MdtOrderFile> theOutpatientRecords;
        private List<MdtOrderFile> inspectionReports;


    }

    // MDT文件病史文件
    @Data
    public static class MdtOrderFile{
        //创建时间
        private String createTime;
        //会诊记录文件
        private String outFileRemark;
        //文件地址
        private String fileUrl;
    }
*/
    //会诊报告
    @Data
    public static class ConsultationReport{
        // 患者姓名
        private String name;
        //性别
        private String sex;
        //年龄
        private String age;
        //梅清给的图片地址
        private String imgPath;
        //主治医生
        private String attendingDoctorName;
        //会诊医生
        private String consultationDoctorName;
        //会诊开始时间
        private String mdtStartTime;
        // 病史摘要
        private String medicalHistorySummary;
        // 会诊摘要
        private String consultationSummary;
        // 诊断结果
        private String diagnosisResults;
        // 治疗方案
        private String treatmentPlan;
        // 审核记录
        private List<AuditLogging> auditLoggings;

    }
    // 审核记录
    @Data
    public static class AuditLogging{
        //审核医生姓名
        private String auditDoctorName;
        //审核结果
        private String auditResult;
        //详细意见内容
        private String auditOpinion;
        //审核时间
        private String auditTime;
    }
}