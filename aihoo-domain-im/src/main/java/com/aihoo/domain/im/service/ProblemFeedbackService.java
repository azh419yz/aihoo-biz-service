package com.aihoo.domain.im.service;

import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.im.model.entity.Cancel;
import com.aihoo.domain.im.model.entity.LoginQuestion;
import com.aihoo.domain.im.model.entity.Proposal;

import java.util.Map;

/**
 * 问题反馈 service（IM 域）。包含：
 * - 登录问题（LoginQuestion）
 * - 反馈意见（Proposal）
 * - 申请注销（Cancel）
 *
 * 注意：feedback() 方法跨域调用 doctor/patient mapper 填充 mobile 字段，违反 IM 域基础域原则。
 * 整改任务：#89 阶段 1.5.1 - 移除 doctor/patient 跨域依赖。
 * 整改方案：mobile 过滤与填充上移到 api-admin/ProblemFeedbackController（应用层），im 域仅按 im 自身字段过滤。
 */
public interface ProblemFeedbackService {
    PageResult<LoginQuestion> loginQuestions(Map<String, Object> map);

    /**
     * 查询反馈意见。
     * 支持 im 域自身字段过滤（type、isDispose）；不再支持 mobile 过滤（移到 controller）。
     */
    PageResult<Proposal> feedback(Map<String, Object> map);

    PageResult<Cancel> applyForList(Map<String, Object> map);

    JsonResult audit(Map<String, Object> map);

    JsonResult disposeLoginQuestions(Map<String, Object> map);

    JsonResult disposeFeedback(Map<String, Object> map);
}
