package com.aihoo.api.doctor.app.model;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * @Classname HosPrescription
 * @Description hf
 * @Date 2020/9/24 9:42
 * @Created by ad
 */
@Data
@TableName("t_hos_prescription")
@Schema(description = "处方表")
public class HosPrescription implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private String id;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private String createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private String updateTime;

    /**
     * 患者用户id
     */
    @Schema(description = "患者用户id")
    private String patientUserId;

    /**
     * 就诊人id
     */
    @Schema(description = "就诊人id")
    private String hosSickId;

    /**
     * 开方人
     */
    @Schema(description = "开方人")
    private String doctorUserId;

    /**
     * 类型 - REVISIT复诊 MDT
     */
    @Schema(description = "类型 - REVISIT复诊 MDT")
    private String type;

    /**
     * REVISIT对应t_hos_revisit的id
     */
    @Schema(description = "REVISIT对应t_hos_revisit的id")
    private String otherId;

    /**
     * 订单编号-REVISIT或MDT的编码
     */
    @Schema(description = "订单编号-REVISIT或MDT的编码")
    private String visitMdtNum;

    /**
     * 订单编号-首字母P-付款单号
     */
    @Schema(description = "订单编号-首字母P-付款单号")
    private String orderNum;

    /**
     * 处方编码
     */
    @Schema(description = "处方编码")
    private String prescriptionNum;

    /**
     * 费别 自费SELF
     */
    @Schema(description = "费别 自费SELF")
    private String feeType;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 身份证
     */
    @Schema(description = "身份证")
    private String idCard;

    /**
     * 性别 0-女 1-男
     */
    @Schema(description = "性别 0-女 1-男")
    private String sex;

    /**
     * 年龄
     */
    @Schema(description = "年龄")
    private String age;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String mobile;

    /**
     * 科室编码
     */
    @Schema(description = "科室编码")
    private String departCode;

    /**
     * 科室
     */
    @Schema(description = "科室")
    private String departName;

    /**
     * 临床诊断
     */
    @Schema(description = "临床诊断")
    private String medicalCertificate;

    /**
     * 医生签章
     */
    @TableField(select = false)
    @Schema(description = "医生签章")
    private String seal;

    /**
     * 医生签章URL
     */
    @Schema(description = "医生签章URL")
    private String doctorSignet;

    /**
     * 处方笺
     */
    @Schema(description = "处方笺")
    private String img;

    /**
     * 审核处方 WAIT-等待审核 PASS-审核通过 MANUALAUDIT-人工审核中 REJECT-审核驳回
     */
    @Schema(description = "审核处方 WAIT-等待审核 PASS-审核通过 MANUALAUDIT-人工审核中 REJECT-审核驳回")
    private String checkStatus;

    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    private String checkTime;

    /**
     * 审核药师
     */
    @Schema(description = "审核药师")
    private String checkPharmaceutist;

    /**
     * 审核药师id
     */
    @Schema(description = "审核药师id")
    private String checkPharmaceutistId;

    /**
     * 审核意见
     */
    @Schema(description = "审核意见")
    private String checkContent;

    /**
     * 审核返回参数
     */
    @TableField(select = false)
    @Schema(description = "审核返回参数")
    private String checkReturn;

    /**
     * 支付金额
     */
    @Schema(description = "支付金额")
    private String totalPrice;

    /**
     * 支付类型 支付宝ALIPAY 微信WECHAT
     */
    @Schema(description = "支付类型 支付宝ALIPAY 微信WECHAT")
    private String payType;

    /**
     * 付款时间
     */
    @Schema(description = "付款时间")
    private String payTime;

    /**
     * 状态 DONE订单关闭 CANCEL取消订单 WAIT待付款 PAY已付款 END订单完成
     * 状态NEW
     * WAIT待付款
     * PAY已付款
     * MIXING调配中
     * UNDISPATCH待发货
     * DISTRIBUTING配送中
     * END订单完成
     * REFUND_PROCESSING退款中
     * REFUNDED已退款
     * WITHDRAW 撤回
     */
    @Schema(description = "状态 WAIT待付款 PAY已付款 MIXING调配中 UNDISPATCH待发货 DISTRIBUTING配送中 END订单完成 REFUND_PROCESSING退款中 REFUNDED已退款 WITHDRAW 撤回")
    private String status;

    /**
     * 支付状态 0已支付，1未支付
     */
    @Schema(description = "支付状态 0已支付，1未支付")
    private String isPay;

    /**
     * 订单结束时间
     */
    @Schema(description = "订单结束时间")
    private String endTime;

    /**
     * 是否废除 0为废除 1已废除
     */
    @Schema(description = "是否废除 0为废除 1已废除")
    private String isCancel;

    /**
     * 肾功能状况0-肾功能不全;2-严重肾功能不全
     */
    @Schema(description = "肾功能状况0-肾功能不全;2-严重肾功能不全")
    private String kidneyStatus;

    /**
     * 肝功能状况0-肝功能不全;2-严重肝功能不全
     */
    @Schema(description = "肝功能状况0-肝功能不全;2-严重肝功能不全")
    private String liverStatus;

    /**
     * 妊娠/哺乳	0-哺乳期;1-妊娠期;
     */
    @Schema(description = "妊娠/哺乳	0-哺乳期;1-妊娠期;")
    private String womanStatus;

    /**
     * 过敏源名称，如果有多个，用“；”隔开串起来。
     */
    @Schema(description = "过敏源名称，如果有多个，用“；”隔开串起来。")
    private String allegeName;

    /**
     * 是否存在拦截等级药物 0否 1是 标记是否可以强行执行
     */
    @Schema(description = "是否存在拦截等级药物 0否 1是 标记是否可以强行执行")
    private String isDisable;

    /**
     * 医生审核超时结束时间
     */
    @Schema(description = "医生审核超时结束时间")
    private String check_timeout;

    /**
     * 审核意见
     */
    @Schema(description = "审核意见")
    private String manualCheckContent;

    /**
     * 人工审核时间
     */
    @Schema(description = "人工审核时间")
    private String manualCheckTime;

    /**
     * 人工审核药师
     */
    @Schema(description = "人工审核药师")
    private String manualCheckPharmaceutist;

    /**
     * 人工审核药师id
     */
    @Schema(description = "人工审核药师id")
    private String manualCheckPharmaceutistId;

    /**
     * 是否要强制执行 0-否 1-是
     */
    @Schema(description = "是否要强制执行 0-否 1-是")
    private String isCanForce;

    /**
     * 人工审核返回参数
     */
    @TableField(select = false)
    @Schema(description = "人工审核返回参数")
    private String manualCheckReturn;

    /**
     * 审核状态名
     */
    @TableField(exist = false)
    @Schema(description = "审核状态名")
    private String checkStatusName;

//    public String getCheckStatusName() {
//        return StatusEnumUtil.getPrescriptionCheckStatus(checkStatus);
//    }

    /**
     * 药品列表
     */
    @TableField(exist = false)
    @Schema(description = "药品列表")
    private List<HosPrescriptionDrug> hosPrescriptionDrugList;

    /**
     * 拼接后的药品名
     */
    @TableField(exist = false)
    @Schema(description = "拼接后的药品名")
    private String drugName;

    /**
     * 辨病
     */
    @Schema(description = "辨病")
    private String disease;

    /**
     * 辩证
     */
    @Schema(description = "辩证")
    private String syndrome;

    /**
     * 药店ID
     */
    @Schema(description = "药店ID")
    private String drugstoreId;

    /**
     * 药态 1:中药饮片-自煎 2:中药饮片-代煎 3:颗粒
     */
    @Schema(description = "药态 1:中药饮片-自煎 2:中药饮片-代煎 3:颗粒")
    private String medicineStatusCode;

    /**
     * 患者确认状态 0 没有 1确认
     */
    @Schema(description = "患者确认状态 0 没有 1确认")
    private Integer confirmedStatus;

    /**
     * 禁忌
     */
    public JSONArray getTaboos() {
        JSONArray params = new JSONArray();
        if (StrUtil.isNotBlank(kidneyStatus)) {
            JSONObject map1 = new JSONObject();
            JSONObject value1 = new JSONObject();
            map1.put("code", "kidneyStatus");
            map1.put("name", "肾功能状况");
            map1.put("type", "SELECT");
            value1.put("code", kidneyStatus);
            switch (kidneyStatus) {
                case "0":
                    value1.put("name", "肾功能不全");
                    break;
                case "2":
                    value1.put("name", "严重肾功能不全");
                    break;
            }
            map1.put("value", Collections.singletonList(value1));
            params.add(map1);
        }
        if (StrUtil.isNotBlank(liverStatus)) {
            JSONObject map2 = new JSONObject();
            JSONObject value2 = new JSONObject();
            map2.put("code", "liverStatus");
            map2.put("name", "肝功能状况");
            map2.put("type", "SELECT");
            value2.put("code", liverStatus);
            switch (liverStatus) {
                case "0":
                    value2.put("name", "肝功能不全");
                    break;
                case "2":
                    value2.put("name", "严重肝功能不全");
                    break;
            }
            map2.put("value", Collections.singletonList(value2));
            params.add(map2);
        }
        if (StrUtil.isNotBlank(womanStatus)) {
            JSONObject map3 = new JSONObject();
            JSONObject value3 = new JSONObject();
            map3.put("code", "womanStatus");
            map3.put("name", "妊娠/哺乳");
            map3.put("type", "SELECT");
            value3.put("code", womanStatus);
            switch (womanStatus) {
                case "0":
                    value3.put("name", "哺乳期");
                    break;
                case "1":
                    value3.put("name", "妊娠期");
                    break;
            }
            map3.put("value", Collections.singletonList(value3));
            params.add(map3);
        }
        if (StrUtil.isNotBlank(allegeName)) {
            JSONObject map4 = new JSONObject();
            map4.put("code", "allegeName");
            map4.put("name", "过敏源名称");
            map4.put("type", "TEXT");
            JSONObject value4 = new JSONObject();
            value4.put("code", "");
            value4.put("name", allegeName);
            map4.put("value", Collections.singletonList(value4));
            params.add(map4);
        }
        return params;
    }
}