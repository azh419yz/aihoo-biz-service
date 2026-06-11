package com.aihoo.domain.payment.model.entity;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
    @ExcelColumn(value = "订单编号", col = 1)

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
    @ExcelColumn(value = "就诊人", col = 2)
    private String name;
    /**
     * 证件类型id
     */
    private String certificatesType;
    /**
     * 身份证号
     */
    @ExcelColumn(value = "身份证号", col = 3)
    private String certificates;
    /**
     * 手机号
     */
    @ExcelColumn(value = "手机号", col = 4)
    private String phone;
    /**
     * 医院id
     */
    private String districtId;
    /**
     * 医院
     */
    @ExcelColumn(value = "就诊医院", col = 5)
    private String districtName;
    /**
     * 挂号类型id
     */
    private String groupId;
    /**
     * 挂号类型
     */
    @ExcelColumn(value = "挂号类型", col = 6)
    private String groupName;
    /**
     * 科室id
     */
    private String codeId;
    /**
     * 科室
     */
    @ExcelColumn(value = "科室", col = 7)
    private String codeName;
    /**
     * 医生工号
     */
    private String staffId;
    /**
     * 医生
     */
    @ExcelColumn(value = "医生", col = 8)
    private String staffName;
    /**
     * 状态
     */
    @ExcelColumn(value = "订单状态", col = 9)
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
    @ExcelColumn(value = "费用参考", col = 11)
    private String money;
    /**
     * 就诊类型 1自费 2医保卡
     */
    @ExcelColumn(value = "就诊类型", col = 12)
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
    @ExcelColumn(value = "下单时间", col = 13)
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

    @ExcelColumn(value = "就诊时间", col = 10)
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