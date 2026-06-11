package com.aihoo.api.doctor.app.model;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 在线问诊信息表
 */
@Data
@TableName("t_hos_visit")
public class HosVisit implements Serializable {
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
     * 患者用户id
     */
    private String patientUserId;

    /**
     * 就诊人id
     */
    private String hosSickId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 性别 0-女 1-男
     */
    private String sex;

    /**
     * 年龄
     */
    private String age;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 病情描述
     */
    private String content;

    /**
     * 问诊类型 图文IMAGE、语音VOICE、视频VIDEO
     */
    private String type;

    /**
     * 支付金额
     */
    private String totalPrice;

    /**
     * 支付类型 支付宝ALIPAY 微信WECHAT
     */
    private String payType;

    /**
     * 付款时间
     */
    private String payTime;

    /**
     * 状态 DONE订单关闭 CANCEL取消订单 DECLINE拒单 WAIT待付款 PAY已付款|待接单 HAVE已接单 END订单完成
     */
    private String status;

    /**
     * 取消订单与拒单信息
     */
    private String msg;

    /**
     * 订单编号-首字母为V
     */
    private String orderNum;

    /**
     * 五星好评 最低一星 最高五星
     */
    private String fiveStar;

    /**
     * 医生id
     */
    private String doctorUserId;

    /**
     * 医嘱
     */
    private String doctorAdvice;

    /**
     * 初诊结果
     */
    private String firstVisit;

    private String infoSubmitTime;//问诊资料提交时间

    /**
     * 接单时间
     */
    private String haveTime;

    /**
     * 问诊开始时间
     */
    private String startTime;

    /**
     * 问诊结束时间
     */
    private String endTime;

    /**
     * 对应图片
     */
    @TableField(exist = false)
    private List<String> imgList;

    /**
     * 消息类型 IM
     */
    @TableField(exist = false)
    private String msgType;

    @TableField(exist = false)
    private String businessID;

    /**
     * 问诊资料-健康状况
     */
    private String healthInfo;

    /**
     * 问诊资料-基本状况
     */
    private String baseInfo;

}