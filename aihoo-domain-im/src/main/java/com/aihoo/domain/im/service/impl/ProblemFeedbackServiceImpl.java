package com.aihoo.domain.im.service.impl;

import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.im.model.entity.Cancel;
import com.aihoo.domain.im.model.entity.LoginQuestion;
import com.aihoo.domain.im.model.entity.Proposal;
import com.aihoo.domain.im.model.mapper.CancelMapper;
import com.aihoo.domain.im.model.mapper.LoginQuestionMapper;
import com.aihoo.domain.im.model.mapper.ProposalMapper;
import com.aihoo.domain.im.service.ProblemFeedbackService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 问题反馈 service 实现。
 *
 * 整改后：feedback() 不再跨域调用 doctor/patient mapper。
 * 移动端筛选与 mobile 字段填充由应用层（api-admin ProblemFeedbackController）完成。
 */
@Service
public class ProblemFeedbackServiceImpl implements ProblemFeedbackService {

    @Resource
    private LoginQuestionMapper loginQuestionMapper;

    @Resource
    private ProposalMapper proposalMapper;

    @Resource
    private CancelMapper cancelMapper;

    @Override
    public PageResult<LoginQuestion> loginQuestions(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        QueryWrapper<LoginQuestion> wrapper = new QueryWrapper<>();

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        if (null != map.get("type") && !"".equals(map.get("type"))) {
            wrapper.eq("type", map.get("type"));
        }
        if (null != map.get("mobile") && !"".equals(map.get("mobile"))) {
            wrapper.eq("mobile", map.get("mobile"));
        }
        if (null != map.get("isDispose") && !"".equals(map.get("isDispose"))) {
            wrapper.eq("is_dispose", map.get("isDispose"));
        }
        IPage<LoginQuestion> loginQuestionsIPage = this.loginQuestionMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(loginQuestionsIPage.getRecords(), loginQuestionsIPage.getTotal());
    }

    @Override
    public PageResult<Proposal> feedback(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        QueryWrapper<Proposal> wrapper = new QueryWrapper<>();

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        if (null != map.get("type") && !"".equals(map.get("type"))) {
            wrapper.eq("type", map.get("type"));
        }
        if (null != map.get("isDispose") && !"".equals(map.get("isDispose"))) {
            wrapper.eq("is_dispose", map.get("isDispose"));
        }
        // 移动端过滤：doctorUserIds / patientUserIds 由应用层（api-admin）通过 doctor/patient 域反查后传入
        Object duIds = map.get("doctorUserIds");
        Object puIds = map.get("patientUserIds");
        boolean hasDu = duIds instanceof java.util.Collection && !((java.util.Collection<?>) duIds).isEmpty();
        boolean hasPu = puIds instanceof java.util.Collection && !((java.util.Collection<?>) puIds).isEmpty();
        if (hasDu && hasPu) {
            wrapper.and(w -> w.in("doctor_user_id", (java.util.Collection<?>) duIds)
                    .or().in("patient_user_id", (java.util.Collection<?>) puIds));
        } else if (hasDu) {
            wrapper.in("doctor_user_id", (java.util.Collection<?>) duIds);
        } else if (hasPu) {
            wrapper.in("patient_user_id", (java.util.Collection<?>) puIds);
        }
        wrapper.orderByDesc("create_time");
        IPage<Proposal> proposalIPage = this.proposalMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(proposalIPage.getRecords(), proposalIPage.getTotal());
    }

    @Override
    public PageResult<Cancel> applyForList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        QueryWrapper<Cancel> wrapper = new QueryWrapper<>();

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        if (null != map.get("type") && !"".equals(map.get("type"))) {
            wrapper.eq("type", map.get("type"));
        }
        if (null != map.get("status") && !"".equals(map.get("status"))) {
            wrapper.eq("status", map.get("status"));
        }
        IPage<Cancel> cancelIPage = this.cancelMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(cancelIPage.getRecords(), cancelIPage.getTotal());

    }

    @Override
    public JsonResult audit(Map<String, Object> map) {
        return null;
    }

    @Override
    public JsonResult disposeLoginQuestions(Map<String, Object> map) {
        LoginQuestion questions = new LoginQuestion();
        questions.setIsDispose(map.get("isDispose").toString());
        questions.setId(map.get("id").toString());
        this.loginQuestionMapper.updateById(questions);
        return JsonResult.ok("修改成功！");
    }

    @Override
    public JsonResult disposeFeedback(Map<String, Object> map) {
        Proposal proposal = new Proposal();
        proposal.setIsDispose(map.get("isDispose").toString());
        proposal.setId(map.get("id").toString());
        this.proposalMapper.updateById(proposal);
        return JsonResult.ok("修改成功！");
    }
}
