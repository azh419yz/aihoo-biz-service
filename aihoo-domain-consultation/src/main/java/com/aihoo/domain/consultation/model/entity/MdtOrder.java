package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname MdtOrder
 * @Description hf
 * @Date 2020/9/30 13:17
 * @Created by ad
 */
@Data
@TableName("t_mdt_order")
public class MdtOrder implements Serializable {
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
     * 客服id
     */
    private String adminId;

    /**
     * MDT_team的id
     */
    private String mdtTeamId;

    /**
     * 疾病编码
     */
    private String mdtCode;

    /**
     * 疾病名称
     */
    private String mdtName;

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
     * 状态
     * 未调配 - UNALLOCATED
     * 调配中 - ALLOCATING
     * 待发货 - PENDING_SHIPMENT
     * 配送中 - IN_TRANSIT
     * 已完成 - COMPLETED
     * 退款中 - REFUNDING
     * 已退款 - REFUNDED
     */
    private String status;

    /**
     * 取消订单与拒单信息
     */
    private String msg;

    /**
     * 订单编号-首字母为M
     */
    private String orderNum;

    /**
     * 医生id
     */
    private String doctorUserId;

    /**
     * 医嘱
     */
    private String doctorAdvice;

    /**
     * 会诊预约时间
     */
    private String mdtAppointmentTime;

    /**
     * 会诊开始时间
     */
    private String mdtStartTime;

    /**
     * 会诊结束时间
     */
    private String mdtEndTime;

    /**
     * 会诊时间 日期
     */
    private String mdtDateStr;

    /**
     * 会诊时间 时间区间
     */
    private String mdtTimeStr;

    /**
     * 门诊记录备注
     */
    private String outFileRemark;

    /**
     * 检查报告备注
     */
    private String checkFileRemark;

    /**
     * 影像资料备注
     */
    private String videoFileRemark;

    /**
     * 挂号费价格
     */
    private String registerPrice;

    /**
     * 挂号费订单编号-首字母为E
     */
    private String regOrderNum;

    /**
     * 挂号费支付类型 支付宝ALIPAY 微信WECHAT
     */
    private String regPayType;

    /**
     * 挂号费付款时间
     */
    private String regPayTime;

    /**
     * 会诊主治医生
     */
    private String attendingDoctorName;

    /**
     * 会诊医生
     */
    private String consultationDoctorName;

    /**
     * 会诊单支付状态 0已支付，1未支付
     */
    private String isPay;

    /**
     * 会诊挂号单支付状态 0已支付，1未支付
     */
    private String isRegPay;

    /**
     * 是否清算 0未清算 1已清算
     */
    private String isCount;

    /**
     * MDT中IM聊天医生账号
     */
    private String imDoctorNumber;

    /**
     * mdt所属医院
     */
    private String mdtHospital;

    /**
     * 协调人（mdt管理员）
     */
    private String moderator;

    /**
     * 联系方式
     */
    private String contactWay;

    /**
     * 会诊类型 机构会诊 HOS_MDT    医生会诊 DOCTOR_MDT
     */
    private String mdtType;

    /**
     * 会诊的备注
     */
    private String mdtRemark;

    /**
     * 会议室连接
     */
    private String mdtRoomUrl;

    /**
     * 会议室ID
     */
    private String mdtRoomId;

    /**
     * 诊室开启状态 0关闭，1开启
     */
    private String isCanChat;

    /**
     * 是否发送申请会诊成功 0失败 1成功
     */
    private String isSendSuccess;

    /**
     * 编写报告人t_mdt_team_doctor的id
     */
    private String reportDoctorId;

    /**
     * 开处方人t_mdt_team_doctor的id
     */
    private String prescriptionDoctorId;

    private String isAgree;

    @TableField(exist = false)
    private MdtOrderReport mdtOrderReport;
    @TableField(exist = false)
    private List<MdtOrderReportAudit> mdtOrderReportAudit;
    @TableField(exist = false)
    private List<MdtReportRecord> mdtReportRecord;
    @TableField(exist = false)
    private String adminName;


    // 处方ID
    private String preId;
    // 药态
    private String medicineStatusCode;
    // 药房ID
    private String drugstoreId;
    // 患者备注
    private String hosSickRemark;
    // 药师备注
    private String remark;
    // 药品照片
    private String pic;
    // 是否已打印PDF
    private String pdfFlag;
    // 是否已打印快递标签
    private String expressFlag;
    // 收件人姓名
    private String receiveName;
    // 收件人电话
    private String receivePhone;
    // 收件人区域
    private String receiveArea;
    // 收件人地址
    private String receiveAddress;

}