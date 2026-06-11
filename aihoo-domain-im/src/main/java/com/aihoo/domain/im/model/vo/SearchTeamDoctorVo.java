package com.aihoo.domain.im.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @program: aihoo-root
 * @description: mdt搜索结果实体
 * @author: Mr.Li
 * @create: 2020-12-23 17:24
 **/
@Data
public class SearchTeamDoctorVo {
    /**
     * JsonInclude注解的参数不参与序列化
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String isAuth;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String isCancel;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String isMdt;

    private String positionName;
    private String teamName;
    private String mdtName;



    /**
     * 医生名称
     */
    private String doctorName;
    /**
     * 是否是领衔医生
     */
    private String isMain;
    /**
     * 团队id
     */
    private String teamId;
    /**
     * 职称
     */
    private String officeHolderName;
    /**
     * 医院名称
     */
    private String hospitalName;
    /**
     * 是否是团队1是0不是
     */
    private String isTeam;
    /**
     * 科室名称
     */
    private String departName;
    /**
     * 医生个人头像
     */
    private String doctorHeadImg;
    /**
     * 价格
     */
    private String teamPrice;
    /**
     * 擅长
     */
    private String beGoodAtText;
    /**
     * 团队简介
     */
    private String introduction;
}
