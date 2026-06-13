package com.aihoo.domain.sys.util;

import com.aihoo.util.SecurityUtils;
import com.aihoo.util.UserAgentGetter;
import com.aihoo.domain.sys.model.mapper.LoginRecordMapper;
import com.aihoo.domain.sys.model.entity.LoginRecord;
import com.aihoo.domain.sys.model.entity.SysUser;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LoginRecordUtil {

    @Autowired
    private LoginRecordMapper loginRecordMapper;

    @Async(value = "asyncExecutor")
    public void saveLoginRecord(HttpServletRequest request, String remark) {
        SysUser loginUserInfo = (SysUser) SecurityUtils.getLoginUser();
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
        loginRecord.setRemark(remark);//操作描述
        loginRecord.setCreatedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        loginRecord.setOsName(StringUtils.isNotBlank(os) ? os : "");
        loginRecord.setBrowserType(StringUtils.isNotBlank(browser) ? browser : "");
        loginRecord.setDevice(StringUtils.isNotBlank(device) ? device : "");
        loginRecordMapper.insert(loginRecord);
    }
}
