package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.controller.vo.ProposalVo;
import com.aihoo.common.BaseController;
import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.im.model.entity.Cancel;
import com.aihoo.domain.im.model.entity.LoginQuestion;
import com.aihoo.domain.im.model.entity.Proposal;
import com.aihoo.domain.im.service.ProblemFeedbackService;
import com.aihoo.domain.patient.model.entity.PatientUser;
import com.aihoo.domain.patient.service.PatientUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 问题反馈 controller（admin）。
 *
 * 整改说明（#89）：mobile 过滤与 mobile 字段填充原本由 im 域 service 跨域调用 doctor/patient mapper，
 * 违反 IM 域基础域原则。整改后由本 controller 在应用层做跨域编排：
 * 1. 提前按 mobile 查询 doctor/patient id 集合，通过 map 透传给 im
 * 2. 拿到 Proposal 列表后批量回查 doctor/patient，组装 ProposalVo 填充 mobile
 */
@RestController
@SuppressWarnings({"unchecked", "rawtypes"})
@RequestMapping("/api/v1/")
public class ProblemFeedbackController extends BaseController {

    @Resource
    private ProblemFeedbackService problemFeedbackService;

    @Resource
    private DoctorUserService doctorUserService;

    @Resource
    private PatientUserService patientUserService;

    private final static List<String> COLLECT = Stream.of("0", "1").collect(Collectors.toList());

    @PostMapping("/loginQuestion")
    private PageResult<LoginQuestion> loginQuestions(@RequestBody Map<String, Object> map) {
        try {
            return this.problemFeedbackService.loginQuestions(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("查询登陆问题失败");
        }
    }

    @PostMapping("/disposeLoginQuestions")
    private JsonResult disposeLoginQuestions(@RequestBody Map<String, Object> map) {
        try {
            if (StringUtils.isEmpty(map.get("isDispose"))) {
                return error("处理转态不能为空");
            } else {
                if (!COLLECT.contains(map.get("isDispose").toString())) {
                    return error("非系统代码！");
                }
            }
            if (StringUtils.isEmpty(map.get("id"))) {
                return error("主键id不能为空");
            }
            return this.problemFeedbackService.disposeLoginQuestions(map);
        } catch (Exception e) {
            e.printStackTrace();
            return error("修改问题反馈表的处理状态 出错");
        }
    }

    @PostMapping("/feedback")
    private PageResult<ProposalVo> feedback(@RequestBody Map<String, Object> map) {
        try {
            Map<String, Object> imQuery = new HashMap<>(map);
            if (null != map.get("mobile") && !"".equals(map.get("mobile"))) {
                String mobile = map.get("mobile").toString();
                List<DoctorUser> doctorUsers = this.doctorUserService.list(
                        new QueryWrapper<DoctorUser>().eq("mobile", mobile));
                List<PatientUser> patientUsers = this.patientUserService.list(
                        new QueryWrapper<PatientUser>().eq("mobile", mobile));
                imQuery.put("doctorUserIds",
                        doctorUsers.stream().map(DoctorUser::getId).collect(Collectors.toList()));
                imQuery.put("patientUserIds",
                        patientUsers.stream().map(PatientUser::getId).collect(Collectors.toList()));
            }
            PageResult<Proposal> pageResult = this.problemFeedbackService.feedback(imQuery);
            return fillMobile(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("查询反馈意见失败");
        }
    }

    private PageResult<ProposalVo> fillMobile(PageResult<Proposal> pageResult) {
        if (pageResult == null || CollectionUtils.isEmpty(pageResult.getData())) {
            long cnt = pageResult == null ? 0L : pageResult.getCount();
            return new PageResult<ProposalVo>(new java.util.ArrayList<>(), cnt);
        }
        List<String> duIds = pageResult.getData().stream()
                .map(Proposal::getDoctorUserId).filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<String> puIds = pageResult.getData().stream()
                .map(Proposal::getPatientUserId).filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<String, String> duMobile = CollectionUtils.isEmpty(duIds)
                ? new HashMap<>()
                : this.doctorUserService.listByIds(duIds).stream()
                        .collect(Collectors.toMap(DoctorUser::getId, DoctorUser::getMobile, (a, b) -> a));
        Map<String, String> puMobile = CollectionUtils.isEmpty(puIds)
                ? new HashMap<>()
                : this.patientUserService.listByIds(puIds).stream()
                        .collect(Collectors.toMap(PatientUser::getId, PatientUser::getMobile, (a, b) -> a));

        List<ProposalVo> voList = pageResult.getData().stream().map(p -> {
            ProposalVo vo = new ProposalVo();
            vo.setId(p.getId());
            vo.setType(p.getType());
            vo.setContent(p.getContent());
            vo.setCreateTime(p.getCreateTime());
            vo.setIsDispose(p.getIsDispose());
            vo.setDoctorUserId(p.getDoctorUserId());
            vo.setPatientUserId(p.getPatientUserId());
            if (p.getPatientUserId() != null) {
                vo.setMobile(puMobile.get(p.getPatientUserId()));
            } else if (p.getDoctorUserId() != null) {
                vo.setMobile(duMobile.get(p.getDoctorUserId()));
            }
            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(voList, pageResult.getCount());
    }

    @PostMapping("/disposeFeedback")
    private JsonResult disposeFeedback(@RequestBody Map<String, Object> map) {
        try {
            if (StringUtils.isEmpty(map.get("isDispose"))) {
                return error("处理转态不能为空");
            } else {
                if (!COLLECT.contains(map.get("isDispose").toString())) {
                    return error("非系统代码！");
                }
            }
            if (StringUtils.isEmpty(map.get("id"))) {
                return error("主键id不能为空");
            }
            return this.problemFeedbackService.disposeFeedback(map);
        } catch (Exception e) {
            e.printStackTrace();
            return error("修改反馈意见表的处理状态 出错");
        }
    }

    @PostMapping("/applyForList")
    private PageResult<Cancel> applyForList(@RequestBody Map<String, Object> map) {
        try {
            return this.problemFeedbackService.applyForList(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("查询反馈意见失败");
        }
    }

    @PostMapping("/audit")
    private JsonResult audit(@RequestBody Map<String, Object> map) {
        try {
            if (StringUtils.isEmpty(map.get("status"))) {
                return error("审核状态必填！");
            } else {
                if ("REJECT".equals(map.get("status").toString())) {
                    return error("审核驳回时，驳回原因必填");
                }
            }
            if (StringUtils.isEmpty(map.get("id"))) {
                return error("主键id必传");
            }
            return this.problemFeedbackService.audit(map);
        } catch (Exception e) {
            e.printStackTrace();
            return error("审核出错");
        }
    }
}
