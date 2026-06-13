package com.aihoo.api.admin.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 反馈意见 VO（admin 端）。
 *
 * 由 controller 在应用层做跨域编排：从 doctor/patient 域按 id 批量回查，填充 mobile。
 * mobile 是 #89 整改中从 im 域 service 上移到应用层的字段。
 */
@Data
public class ProposalVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String createTime;
    private String updateTime;
    private String type;
    private String patientUserId;
    private String doctorUserId;
    private String content;
    private String isDispose;
    private String mobile;
}
