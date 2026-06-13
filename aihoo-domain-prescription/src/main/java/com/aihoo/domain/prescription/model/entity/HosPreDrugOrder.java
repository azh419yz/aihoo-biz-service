package com.aihoo.domain.prescription.model.entity;

import com.aihoo.domain.consultation.model.entity.HosPrescription;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname HosPreDrugOrder
 * @Description hf
 * @Date 2020/9/24 14:55
 * @Created by ad
 */
@Data
@TableName("t_hos_pre_drug_order")
public class HosPreDrugOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
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
     * '订单编号-首字母P-付款单号
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
     * 状态 DONE订单关闭 CANCEL取消订单 WAIT待付款 PAY已付款待发货 SHIPPED已发货 END订单完成
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


    @TableField(exist = false)
    private HosPrescription hosPrescription;

    @TableField(exist = false)
    private List<HosPrescriptionDrug> hosPrescriptionDrugs;

}
