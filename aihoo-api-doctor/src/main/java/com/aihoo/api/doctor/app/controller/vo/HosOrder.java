package com.aihoo.api.doctor.app.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/24 19:49
 * @description：订单详情页
 */
@Data
@Schema(description = "问诊卡详情")
public class HosOrder implements Serializable {
    @Schema(description = "订单id")
    private String orderId;
    @Schema(description = "性别")
    private String sex;
    @Schema(description = "年龄")
    private String age;
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "状态")
    private String status;//状态 DONE订单关闭 CANCEL取消订单 DECLINE拒单 WAIT待付款 PAY已付款|待接单 HAVE已接单 END订单完成
    @Schema(description = "状态中文")
    private String statusName;//状态 DONE订单关闭 CANCEL取消订单 DECLINE拒单 WAIT待付款 PAY已付款|待接单 HAVE已接单 END订单完成
    @Schema(description = "头像")
    private String headImg;
    @Schema(description = "病情主诉")
    private String content;
    @Schema(description = "初诊结果")
    private String firstVisit;//初诊结果（拟诊）
    @Schema(description = "支付时间")
    private String payTime;
    @Schema(description = "问诊卡单号")
    private String orderNum;
    @Schema(description = "创建时间")
    private String createTime;
    @Schema(description = "结束时间")
    private String endTime;
    @Schema(description = "附件图片")
    private List imgList;
    @Schema(description = "倒计时")
    private String countDown;//倒计时
    @Schema(description = "是否到预约时间")
    private String isCanStart;//是否到预约时间 0否 1是
    @Schema(description = "患者id")
    private String patientId;//患者id
    @Schema(description = "问诊卡类型")
    private String type;
    @Schema(description = "问诊卡类型中文")
    private String typeName;
    @Schema(description = "处方审核状态")
    private String checkStatus;
    @Schema(description = "是否")
    private String isCanChat;
    @Schema(description = "五星好评 最低一星 最高五星")
    private String fiveStar;
    @Schema(description = "医嘱")
    private String doctorAdvice;
    @Schema(description = "医生id")
    private String doctorId;
    @Schema(description = "订单里面的选择疾病")
    private String diseaseName;//订单里面的选择疾病
    @Schema(description = "处方里的临床诊断")
    private String medicalCertificate;//处方里的临床诊断
    @Schema(description = "复诊预约开始时间")
    private String revisitStartTime;//复诊预约开始时间
    @Schema(description = "复诊预约结束时间")
    private String revisitEndTime;//复诊预约结束时间
    @Schema(description = "复诊预约时间展示")
    private String revisitTimeStr;//复诊预约时间展示
    @Schema(description = "患者id")
    private String hosSickId;
    @Schema(description = "地区编号")
    private String areaCode;
    @Schema(description = "地区名称")
    private String areaName;
    private Map btnJson = new HashMap();

    public void makeVisitbtnJson() {
//        this.btnJson.put("cancel", "0");
        this.btnJson.put("into", "0");
        this.btnJson.put("refuse", "0");
        this.btnJson.put("agree", "0");
        this.btnJson.put("details", "0");
        this.typeName = "在线复诊";
//        this.typeName = "专家咨询";
//        if (this.type.equals("IMAGE")) {
//            this.typeName = "图文问诊";
//        } else if (this.type.equals("VOICE")) {
//            this.typeName = "电话问诊";
//        } else if (this.type.equals("VIDEO")) {
//            this.typeName = "视频问诊";
//        }
        if (this.status.equals("HAVE")) {
            this.statusName = "进行中";
//            this.btnJson.put("cancel", "1");
            this.btnJson.put("into", "1");
        } else if (this.status.equals("PAY")) {
            this.statusName = "待接诊";
            this.btnJson.put("refuse", "0");
            this.btnJson.put("agree", "1");
        } else if (this.status.equals("END")) {
            this.statusName = "已完成";
            this.btnJson.put("details", "1");
        }
    }

    public void makeRevisitbtnJson() {
        this.btnJson.put("transfer", "0");
        this.btnJson.put("into", "0");
        this.btnJson.put("agree", "0");
        this.btnJson.put("details", "0");
        this.btnJson.put("prescription", "0");
        //状态 DECLINE 拒单 PAY 已付款|待接单 HAVE 已接单  START 复诊进行中 END 订单完成
        if (this.status.equals("START")) {
            this.statusName = "进行中";
//            Date start = DateUtil.parse(this.date.substring(0, 16), "yyyy-MM-dd HH:mm");
//            this.date.substring(0, 16);
//            if (new Date().before(start)) {
//                this.btnJson.put("transfer", "1");
//            }
            this.btnJson.put("into", "1");
        } else if (this.status.equals("HAVE")) {
            this.statusName = "已接单";
//            Date start = DateUtil.parse(this.date.substring(0, 16), "yyyy-MM-dd HH:mm");
//            this.date.substring(0, 16);
//            if (new Date().before(start)) {
//                this.btnJson.put("transfer", "1");
//            }
            this.btnJson.put("into", "1");
        } else if (this.status.equals("PAY")) {
            this.statusName = "待接诊";
//            this.btnJson.put("transfer", "1");
            this.btnJson.put("agree", "1");
        } else if (this.status.equals("END")) {
            this.statusName = "已完成";
            this.btnJson.put("details", "1");
            this.btnJson.put("prescription", "1");
        } else if (this.status.equals("DECLINE")) {
            this.statusName = "已拒单";
        }
    }
}
