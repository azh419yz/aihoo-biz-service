package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_mdt_order_file")
public class MdtOrderFile implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String mdtOrderId;
    private String type;
    private String fileUrl;
    private String remark;

    private static final long serialVersionUID = 1L;
}
