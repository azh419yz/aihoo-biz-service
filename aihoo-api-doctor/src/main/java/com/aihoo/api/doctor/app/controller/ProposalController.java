package com.aihoo.api.doctor.app.controller;


import com.aihoo.common.JsonResult;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.im.service.ProposalService;
import com.aihoo.security.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * <p>
 * 意见反馈 前端控制器
 * </p>
 *
 * @author zys
 * @since 2020-09-30
 */


@Controller
@RequestMapping("/api/v1/proposal")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    /**
     * 医生提交意见
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("createProposal")
    public JsonResult createProposal(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("content") || "".equals(map.get("content"))) {
                return JsonResult.error("请先填写反馈内容!");
            }
            String doctorUserId = AuthUtil.getLoginUserId();
            if (null == doctorUserId) {
                return JsonResult.error("请您登陆后操作!");
            }
            proposalService.createProposal(doctorUserId, (String) map.get("content"), "DOCKER");
            return JsonResult.ok("感谢您的反馈!");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("提交失败!");
        }
    }
}
