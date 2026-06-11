package com.aihoo.domain.payment.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: OfflineOderVo .class
 * @author: gjk
 * @create: 2021-04-02 14:04
 **/
@Data
public class OfflineOderVo implements Serializable {

    /**
     * 就诊人姓名
     */
    private String name;
    /**
     * 就诊人身份证号码
     */
    private String certificates;
    /**
     * 就诊人出生日期
     */
    private String birth;
    /**
     * 就诊人性别
     */
    private String sex;
    /**
     * 初/复诊
     */
    private String type;
}