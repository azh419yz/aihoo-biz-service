package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.payment.model.mapper.ProposalMapper;
import com.aihoo.domain.payment.model.entity.Proposal;
import com.aihoo.api.doctor.app.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 反馈意见表 服务实现类
 * </p>
 *
 * @author zys
 * @since 2020-09-30
 */
@Service
public class ProposalServiceImpl extends ServiceImpl<ProposalMapper, Proposal> implements ProposalService {

    @Autowired
    private ProposalMapper proposalMapper;

    /**
     * 用户提交意见反馈
     *
     * @param doctorUserId 医生ID
     * @param content      内容
     * @param type         类型
     */
    @Override
    public boolean createProposal(String doctorUserId, String content, String type) {
        Proposal proposal = new Proposal();
        proposal.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        proposal.setDoctorUserId(doctorUserId);
        proposal.setContent(content);
        proposal.setType(type);
        int insert = proposalMapper.insert(proposal);
        if (insert <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public Long countByDoctorUserId(String doctorUserId) {
        return proposalMapper.selectCount(new LambdaQueryWrapper<Proposal>()
                .eq(Proposal::getDoctorUserId, doctorUserId));
    }
}
