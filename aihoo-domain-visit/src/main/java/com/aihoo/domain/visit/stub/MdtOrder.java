package com.aihoo.domain.visit.stub;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 占位实体：原 t_mdt_order 表。
 * 来自 admin 的 MdtOrder 实体，仅 HosSickServiceImpl.hosSickDetails 用于计数。
 * 待 consultation 域统一后改用 com.aihoo.domain.consultation.model.entity.MdtOrder。
 */
@Data
@TableName("t_mdt_order")
public class MdtOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String hosSickId;
}
