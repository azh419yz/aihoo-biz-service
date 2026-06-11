package com.aihoo.domain.consultation.model.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Classname MdtVo
 * @Description hf
 * @Date 2020/9/21 18:49
 * @Created by ad
 */
@Data
@TableName("t_mdt")
public class MdtFqVo {
    private Integer id;
    private String name;
    private String price;
}