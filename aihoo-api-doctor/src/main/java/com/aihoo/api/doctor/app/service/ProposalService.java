package com.aihoo.api.doctor.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.model.Proposal;


/**
 * <p>
 * 反馈意见表 服务类
 * </p>
 *
 * @author zys
 * @since 2020-09-30
 */
public interface ProposalService extends IService<Proposal> {

    boolean createProposal(String doctorUserId, String content, String type);

    Long countByDoctorUserId(String doctorUserId);
}
