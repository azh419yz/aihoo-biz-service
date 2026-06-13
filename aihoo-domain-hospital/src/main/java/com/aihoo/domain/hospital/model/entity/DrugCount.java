package com.aihoo.domain.hospital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_drugcount")
public class DrugCount implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String mshId;
    private String name;
    private String supplierCode;
    private Integer num;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private String supplier;
    private String payType;
    private String tradeStatus;
    private LocalDateTime createTime;
    private String month;
}
