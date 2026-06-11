package com.aihoo.api.doctor.app.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 用于待处理订单数据
 * @Return:
 **/
@Data
public class OrderResult implements Serializable {
    //订单id
    private String orderId;
    //患者姓名
    private String name;
    //预约时间2018-12-12 11:22
    private String date;
    //问诊类型 图文IMAGE、语音VOICE、视频VIDEO
    private String type;
    private String typeName;
    //病情主诉
    private String content;
    //订单状态
    private String status;
    private String statusName;
    //订单价格
    private String totalPrice;
    //患者id
    private String patientId;
    //处方审核结果
    private String checkStatus;
    //处方审核结果
    private String checkStatusName;

    private String isCanChat;

    private String createTime;

    private String endTime;

    //问诊订单按钮 cancel取消订单  into进入诊室  refuse拒单  agree接单  details查看记录
    //复诊订单按钮 transfer转单  into进入诊室  agree接单  details查看记录  prescription处方
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
