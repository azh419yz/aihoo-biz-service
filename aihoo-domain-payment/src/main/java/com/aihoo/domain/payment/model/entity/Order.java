package com.aihoo.domain.payment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("t_order")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String createTime;
    private String updateTime;
    private String orderNum;
    private String orderType;
    private String otherId;
    private String patientUserId;
    private String payStatus;
    private String totalPrice;
    private String payType;
    private String payTime;
    private String status;
    private String isPay;
    private String isRefund;
}
