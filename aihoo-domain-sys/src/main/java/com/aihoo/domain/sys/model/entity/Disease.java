package com.aihoo.domain.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("d_disease")
public class Disease implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String name;

    private String code;

    private String isDelete;
}
