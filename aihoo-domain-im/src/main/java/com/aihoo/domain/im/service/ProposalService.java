package com.aihoo.domain.im.service;

import com.aihoo.domain.im.model.entity.Proposal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 反馈意见表 服务类
 * </p>
 */
public interface ProposalService extends IService<Proposal> {

    boolean createProposal(String doctorUserId, String content, String type);

    Long countByDoctorUserId(String doctorUserId);
}
