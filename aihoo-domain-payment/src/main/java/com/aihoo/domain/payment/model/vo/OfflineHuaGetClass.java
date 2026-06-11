package com.aihoo.domain.payment.model.vo;

import lombok.Data;

/**
 * 华东医院 获取专家的排版的日期
 **/
@Data
public class OfflineHuaGetClass {

    /**
     *附加标志
     */
    private String appendFlag;
    /**
     *停止标志 0正常 1停诊
     */
    private String stopFlag;
    /**
     *实体名称
     */
    private String entityName;
    /**
     *结束时间
     */
    private String endTime;
    /**
     *节点名
     */
    private String nodeName;
    /**
     *上下午标识
     */
    private String dayType;
    /**
     *开始时间
     */
    private String startTime;
    /**
     *排版id
     */
    private String classId;
    /**
     *   0限号 1不限号
     */
    private String noLimit;
    /**
     *总额
     */
    private String totalAmount;
    /**
     *节点代码
     */
    private String nodeCode;
    /**
     *诊室
     */
    private String postion;
    /**
     *排版日期
     */
    private String classDate;
    /**
     *专家标志
     */
    private String expertFlag;
    /**
     *价格
     */
    private String price;
    /**
     *实体码
     */
    private String entityCode;
    /**
     * 挂号列表（主任，辅主任）
     */
    private String serviceType;
    /**
     *附属物
     */
    private String appendLast;
    /**
     *号院数量
     */
    private String freeAmount;
}