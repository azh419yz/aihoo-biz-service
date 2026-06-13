package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("t_mdt")
public class Mdt implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;
    private String isDelete;
    private String status;
    private String index;
    private String type;
    private String img;
    private String introduction;
    private String teamIds;
    private BigDecimal price;
    private String detail;
    private String characteristic;
    private String isMdt;
}
