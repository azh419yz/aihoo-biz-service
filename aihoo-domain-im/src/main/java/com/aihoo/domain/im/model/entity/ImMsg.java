package com.aihoo.domain.im.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * IM消息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ImMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String createTime;
    private String updateTime;
    private String createTimeStr;
    private String fromAccount;
    private String toAccount;
    private String msgSeq;
    private String msgRandom;
    private String msgTime;
    private String msgKey;
    private String sendMsgResult;
    private String errorInfo;
    private String orderNum;
    private String orderType;
    private String msgType;
    private String msgTypeName;
    private String loadParam;
    private String doctorPeerReadStatus;
    private String peerReadStatus;
    private String msgStatus;
}
