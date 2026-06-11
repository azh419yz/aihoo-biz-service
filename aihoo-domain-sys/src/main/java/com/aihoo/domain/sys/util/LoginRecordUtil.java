package com.aihoo.domain.sys.util;

import com.aihoo.domain.sys.model.entity.LoginRecord;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.domain.sys.model.mapper.LoginRecordMapper;
import com.aihoo.util.SecurityUtils;
import com.aihoo.util.UserAgentGetter;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 登录/操作记录写入器。
 *
 * <p>从旧 admin 的 com.aihoo.admin.common.utils.LoginRecordUtil 迁入，
 * 放入 domain-sys（依赖 LoginRecord 实体 + LoginRecordMapper）。
 * SecurityUtils/UserAgentGetter 来自 shared-kernel。</p>
 */
@Component
public class LoginRecordUtil {

    @Autowired
    private LoginRecordMapper loginRecordMapper;

    @Async(value = "asyncExecutor")
    public void saveLoginRecord(HttpServletRequest request, String remark) {
        SysUser loginUserInfo = SecurityUtils.getLoginUserId() == null
                ? null
                : getSysUserByLogin();
        String username = "";
        String userId = "1";
        if (null == loginUserInfo) {
            username = "管理员";
        } else {
            username = StringUtils.isNotBlank(loginUserInfo.getUserName()) ? loginUserInfo.getUserName() : "";
            userId = loginUserInfo.getId();
        }
        UserAgentGetter userAgentGetter = new UserAgentGetter(request);
        String ipAddr = userAgentGetter.getIpAddr();
        String os = userAgentGetter.getOS();
        String browser = userAgentGetter.getBrowser();
        String device = userAgentGetter.getDevice();

        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setIpAddress(StringUtils.isNotBlank(ipAddr) ? ipAddr : "");
        loginRecord.setUserName(username);
        loginRecord.setUserId(userId);
        loginRecord.setRemark(remark);
        loginRecord.setCreatedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        loginRecord.setOsName(StringUtils.isNotBlank(os) ? os : "");
        loginRecord.setBrowserType(StringUtils.isNotBlank(browser) ? browser : "");
        loginRecord.setDevice(StringUtils.isNotBlank(device) ? device : "");
        loginRecordMapper.insert(loginRecord);
    }

    /**
     * 反射从 Spring Security principal 取出 SysUser（LoginUser 模式）。
     * 避免本类对 api-admin LoginUser 类的硬依赖。
     */
    private SysUser getSysUserByLogin() {
        try {
            org.springframework.security.core.Authentication auth =
                    org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getPrincipal() == null) {
                return null;
            }
            Object principal = auth.getPrincipal();
            return (SysUser) principal.getClass().getMethod("getSysUser").invoke(principal);
        } catch (Exception e) {
            return null;
        }
    }
}
