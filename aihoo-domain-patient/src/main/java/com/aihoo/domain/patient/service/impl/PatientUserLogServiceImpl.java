package com.aihoo.domain.patient.service.impl;

import com.aihoo.domain.patient.model.entity.PatientUser;
import com.aihoo.domain.patient.model.entity.PatientUserLog;
import com.aihoo.domain.patient.model.mapper.PatientUserLogMapper;
import com.aihoo.domain.patient.service.PatientUserLogService;
import com.aihoo.util.SecurityUtils;
import com.aihoo.util.UserAgentGetter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 患者用户日志服务实现。
 *
 * <p>跨域依赖处理：
 * <ul>
 *   <li>LoginRecordUtil 已迁入 domain-sys（com.aihoo.domain.sys.util.LoginRecordUtil）</li>
 *   <li>SecurityUtils / UserAgentGetter 来自 shared-kernel（com.aihoo.util.*）</li>
 * </ul>
 * </p>
 */
@Service
public class PatientUserLogServiceImpl extends ServiceImpl<PatientUserLogMapper, PatientUserLog> implements PatientUserLogService {

    @Autowired
    private com.aihoo.domain.sys.util.LoginRecordUtil loginRecordUtil;

    @Override
    public boolean saveUserLog( List<PatientUser> patientUsers, HttpServletRequest request, String mobile) {
        UserAgentGetter userAgentGetter = new UserAgentGetter(request);
        PatientUserLog log = new PatientUserLog();
        log.setActionType("CANCEL");
        log.setPatientUserId(patientUsers.get(0).getId());
        log.setOsName(userAgentGetter.getOS());
        log.setIpAddress(userAgentGetter.getIpAddr());
        log.setRemark("操作人id：" + Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString() + "注销患者用户成功，手机号为：" + mobile);
        loginRecordUtil.saveLoginRecord(request, "用户注销");
        this.save(log);
        return false;
    }
}
