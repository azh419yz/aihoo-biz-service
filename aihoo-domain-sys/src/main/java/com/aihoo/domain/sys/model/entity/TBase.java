package com.aihoo.domain.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("t_base")
public class TBase implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String key;
    private String title;
    private String content;
    private String index;
    private String createUserId;
    private String createTime;
    private String updateTime;
    private String isDelete;
    private String status;
    @TableField(exist = false)
    private String type;
}
