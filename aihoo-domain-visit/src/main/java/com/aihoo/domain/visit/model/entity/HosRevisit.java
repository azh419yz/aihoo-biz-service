package com.aihoo.domain.visit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname HosRevisit
 * @Description hf
 * @Date 2020/9/22 20:08
 * @Created by ad
 */
@Data
@TableName("t_hos_revisit")
public class HosRevisit implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 医生id
     */
    private String doctorUserId;

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
     * 就诊医院
     */
    private String hospitalName;

    /**
     * 就诊科室
     */
    private String departName;

    /**
     * 选择疾病
     */
    private String diseaseName;

    /**
     * 复诊预约开始时间
     */
    private String revisitStartTime;

    /**
     * 复诊预约结束时间
     */
    private String revisitEndTime;

    /**
     * 复诊时间 日期
     */
    private String revisitDateStr;

    /**
     * 复诊时间 时间区间
     */
    private String revisitTimeStr;

    /**
     * 病情主诉
     */
    private String content;

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
     * 状态 DONE订单关闭 CANCEL取消订单 DECLINE拒单 WAIT待付款 PAY已付款|待接单 HAVE已接单 START复诊进行中 END订单完成  REFUNDWAIT退款进行中 REFUNDSUCCESS退款成功 CHANGE退款异常  REFUNDCLOSE退款关闭
     */
    private String status;

    /**
     * 取消订单与拒单信息
     */
    private String msg;

    /**
     * 订单编号-首字母为R
     */
    private String orderNum;

    /**
     * 五星好评 最低一星 最高五星
     */
    private String fiveStar;

    /**
     * 医嘱
     */
    private String doctorAdvice;

    /**
     * 接单时间
     */
    private String haveTime;

    /**
     * 問診開始時間
     */
    private String startTime;

    /**
     * 问诊结束时间
     */
    private String endTime;

    /**
     * 是否已读IM聊天消息 0 是 1否
     */
    private String isReadIm;

    /**
     * 支付状态 0已支付，1未支付'
     */
    private String isPay;

    @TableField(exist = false)
    private List<HosRevisitImg> hosRevisitImg;

    /**
     * 处方单的id
     */
    @TableField(exist = false)
    private String prescriptionId;

    /**
     * 处方单主键id
     */
    @TableField(exist = false)
    private String prescriptionKeyId;

    /**
     * 药品订单id
     */
    @TableField(exist = false)
    private String drugOrderId;

    /**
     * 药品订单主键id
     */
    @TableField(exist = false)
    private String drugOrderKeyId;

    /**
     * 医生姓名
     */
    @TableField(exist = false)
    private String doctorName;

    /**
     * 患者账号（用户的手机号）
     */
    @TableField(exist = false)
    private String UserMobile;

    /**
     * 复诊的医院
     */
    @TableField(exist = false)
    private String revisitHospitalName;

    /**
     * 复诊的科室
     */
    @TableField(exist = false)
    private String revisitDepartName;

    /**
     * 复诊所属疾病
     */
    @TableField(exist = false)
    private String revisitDiseaseName;

}
