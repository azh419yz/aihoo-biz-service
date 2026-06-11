package com.aihoo.api.doctor.app.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 药品配送信息表
 */
@Data
@TableName("t_hos_pre_drug_order")
public class HosPreDrugOrder implements Serializable {
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
     * 配送开始时间
     */
    private String shippendStartTime;
    
    /**
     * 配送结束时间
     */
    private String shippendEndTime;

    /**
     * 是否关联处方 0不关联 1关联
     */
    private String isPre;

    /**
     * 处方id
     */
    private String hosPrescriptionId;

    /**
     * 订单编号-首字母P-付款单号
     */
    private String orderNum;

    /**
     * 快递公司编码
     */
    private String expressCode;

    /**
     * 快递公司名称
     */
    private String expressName;

    /**
     * 快递单号
     */
    private String shippingNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 省
     */
    private String provinceCode;

    /**
     * 市
     */
    private String cityCode;

    /**
     * 区
     */
    private String districtCode;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 详情地址
     */
    private String address;

    /**
     * 是否默认地址 1:是 0:不是
     */
    private String isDefault;

    /**
     * 状态 DONE订单关闭 CANCEL取消订单 WAIT待付款 PAY已付款 END订单完成 REFUNDWAIT退款进行中 REFUNDSUCCESS退款成功 CHANGE退款异常  REFUNDCLOSE退款关闭
     */
    private String status;

    /**
     * 患者用户id
     */
    private String patientUserId;

    /**
     * 付款时间
     */
    private String payTime;

    /**
     * 支付状态 0已支付，1未支付
     */
    private String isPay;
    private String supplier;
    private String type;
    private static final long serialVersionUID = 1L;
}