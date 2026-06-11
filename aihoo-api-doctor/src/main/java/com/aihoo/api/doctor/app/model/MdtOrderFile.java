package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname MdtOrderFile
 * @Description hf
 * @Date 2020/9/30 14:15
 * @Created by ad
 */
@Data
@TableName("t_mdt_order_file")
public class MdtOrderFile implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * mdt订单id
     */
    private String mdtOrderId;

    /**
     * 类型 门诊记录OUT_FILE 检查报告CHECK_FILE 影像资料VIDEO_FILE
     */
    private String type;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 备注
     */
    private String remark;


    private static final long serialVersionUID = 1L;
}
