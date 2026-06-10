package com.aihoo.domain.sys.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class TBaseVo {

    private String id;

    /**
     * 内容
     */
    private String content;

    /**
     * 排序字段
     */
    @TableField("`index`")
    private String index;
}
