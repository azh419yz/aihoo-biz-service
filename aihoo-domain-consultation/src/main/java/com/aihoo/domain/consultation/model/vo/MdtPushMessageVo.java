package com.aihoo.domain.consultation.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MdtPushMessageVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String intro;
    private String isRead;
    private String setTime;
    private String otherId;
    private String orderNum;
    private String content;
}