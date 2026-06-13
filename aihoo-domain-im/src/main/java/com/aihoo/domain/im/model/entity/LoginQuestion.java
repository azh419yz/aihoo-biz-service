package com.aihoo.domain.im.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_login_questions")
public class LoginQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String type;
    private String mobile;
    private String content;
    private String isDispose;
}
