package com.aihoo.domain.hospital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("TB_DIC_Hospital")
public class DicHospital implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "yljgdm", type = IdType.INPUT)
    private String yljgdm;

    private String wsjgdm;
    private String yymc;
    private String yljgjb;
    private String yljgdj;
    private String yljglb;
    private String zffs;
    private String yljgsx;
    private String ssqdm;
    private String fzpdyj;
    private String fzpdsjly;
    private Integer fzpdsx;
    private String xgbz;
}
