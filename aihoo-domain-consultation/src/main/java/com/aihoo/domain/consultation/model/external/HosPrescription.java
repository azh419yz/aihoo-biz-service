package com.aihoo.domain.consultation.model.external;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname HosPrescription
 * @Description hf
 * @Date 2020/9/24 9:42
 * @Created by ad
 */
// TODO 跨域stub：此为处方域实体占位，请迁移 prescription 域后删除此文件并改为引用 com.aihoo.domain.prescription.model.entity.HosPrescription
@Data
@TableName("t_hos_prescription")
public class HosPrescription implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id",type = IdType.AUTO)
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
     * 开方人
     */
    private String doctorUserId;

    /**
     * 类型 - REVISIT复诊 MDT
     */
    private String type;

    /**
     * REVISIT对应t_hos_revisit的id
     */
    private String otherId;

    /**
     * 订单编号-REVISIT或MDT的编码
     */
    private String visitMdtNum;

    /**
     * 订单编号-首字母P-付款单号
     */
    private String orderNum;

    /**
     * 处方编码
     */
    private String prescriptionNum;

    /**
     * 费别 自费SELF
     */
    private String feeType;

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
     * 科室编码
     */
    private String departCode;

    /**
     * 科室
     */
    private String departName;

    /**
     * 临床诊断
     */
    private String medicalCertificate;

    /**
     * 医生签章
     */
    private String doctorSignet;

    /**
     * 审核处方 WAIT-等待审核 PASS-审核通过 REJECT-审核驳回
     */
    private String checkStatus;

    /**
     * 审核时间
     */
    private String checkTime;

    /**
     * 审核药师
     */
    private String checkPharmaceutist;

    /**
     * 审核意见
     */
    private String checkContent;

    /**
     * 审核返回参数
     */
    private String checkReturn;

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
     * 状态 DONE订单关闭 CANCEL取消订单 WAIT待付款 PAY已付款 END订单完成
     */
    private String status;

    /**
     * 支付状态 0已支付，1未支付
     */
    private String isPay;

    /**
     * 处方笺
     */
    private String img;

    /**
     * 订单结束时间
     */
    private String endTime;

    /**
     * 是否废除 0为废除 1已废除
     */
    private String isCancel;

    @TableField(exist = false)
    private List<HosPrescriptionDrug> hosPrescriptionDrugs;

    /**
     * 辨病
     */
    private String disease;
    /**
     * 辩证
     */
    private String syndrome;
    /**
     * 药店ID
     */
    private String drugstoreId;
    /**
     * 药态 1:中药饮片-自煎 2:中药饮片-代煎 3:颗粒
     */
    private String medicineStatusCode;
}