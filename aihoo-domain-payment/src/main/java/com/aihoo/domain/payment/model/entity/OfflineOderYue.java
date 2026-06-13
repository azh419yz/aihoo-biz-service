package com.aihoo.domain.payment.model.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 预约后的订单表
 **/
@Data
@TableName("t_offline_order_yue")
public class OfflineOderYue {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**
     * 预约id
     */
    private String aboutId;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 就诊人
     */
    private String name;
    /**
     * 证件类型id
     */
    private String certificatesType;
    /**
     * 身份证号
     */
    private String certificates;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 医院id
     */
    private String districtId;
    /**
     * 医院
     */
    private String districtName;
    /**
     * 挂号类型id
     */
    private String groupId;
    /**
     * 挂号类型
     */
    private String groupName;
    /**
     * 科室id
     */
    private String codeId;
    /**
     * 科室
     */
    private String codeName;
    /**
     * 医生工号
     */
    private String staffId;
    /**
     * 医生
     */
    private String staffName;
    /**
     * 状态
     */
    private String isStatus;
    /**
     * 状态标识
     */
    private String isStatusCode;
    /**
     * 排版时间
     */
    private String schedulingType;
    /**
     * 1上午 2下午
     */
    private String schedulingDate;
    /**
     * 费用参考
     */
    private String money;
    /**
     * 就诊类型 1自费 2医保卡
     */
    private String treatmentType;
    /**
     *  就诊卡号
     */
    private String treatmentName;
    /**
     *  发送短信 0用户 1用户 2客服
     */
    private String message;
    /**
     * 下单时间
     */
    private String decisionTime;
    /**
     * 预约时间段
     */
    private String yueTime;
    /**
     * 就诊序号 3001(上午,3号房间,01号)
     */
    private String yueId;
    /**
     * 支付时间
     */
    private String time;
    /**
     * 楼层
     */
    private String floor;
    /**
     * 预约编号
     */
    private String bookingId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 是否删除 0未删除 1已删除
     */
    private String isDelete;
    @TableField(exist = false)
    private String date;

    /**
     * 取消时间
     */
    private String delTime;

    /**
     * 蓝色底纹标识 1改动 2未改动
     */
    private String only;
}