package com.aihoo.domain.payment.model.vo;

import lombok.Data;

/**
 *  华东医院 科室下的医生
 **/
@Data
public class OfflineNodeDoctor {

    /**
     * 医生姓名
     */
    private String entityName;
    /**
     * 性别
     */
    private String sex;
    /**
     * title
     */
    private String title;
    /**
     * 科室对应的医生id
     */
    private String mapId;
    /**
     * 学术头衔
     */
    private String techTitle;
    /**
     * 科室id
     */
    private String nodeId;
    /**
     * 科室名称
     */
    private String nodeName;
    /**
     * 2专家  13特需
     */
    private String poolId;
}