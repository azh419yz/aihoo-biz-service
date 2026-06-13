package com.aihoo.domain.sys.service.impl;

import com.aihoo.util.SecurityUtils;
import com.aihoo.util.UserAgentGetter;
import com.aihoo.domain.sys.model.mapper.LoginRecordMapper;
import com.aihoo.domain.sys.model.entity.LoginRecord;
import com.aihoo.domain.sys.service.LoginRecordService;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangfan
 * @since 2019-02-11
 */
@Service
public class LoginRecordServiceImpl extends ServiceImpl<LoginRecordMapper, LoginRecord> implements LoginRecordService {
    @Resource
    private  LoginRecordMapper loginRecordMapper;
    @Override
    public PageResult<LoginRecord> getList(Map<String, Object> map, HttpServletRequest request) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<LoginRecord> wrapper = new QueryWrapper<LoginRecord>();
        if (null != map.get("startDate") && !"".equals(map.get("startDate"))&&null != map.get("endDate") && !"".equals(map.get("endDate"))) {
            wrapper.between("created_date",map.get("startDate").toString(),map.get("endDate").toString());
        }
        wrapper.orderByDesc("created_date");
        IPage<LoginRecord> iPage = this.loginRecordMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public void addLoginRecord(String userId, HttpServletRequest request){
        UserAgentGetter agentGetter = new UserAgentGetter(request);
        LoginRecord loginRecord = new LoginRecord();
        Object loginUser = SecurityUtils.getLoginUser();
        String userName = loginUser != null ? ((com.aihoo.domain.sys.model.entity.SysUser) loginUser).getUserName() : "";
        loginRecord.setUserName(userName);
        loginRecord.setUserId(userId);
        loginRecord.setOsName(agentGetter.getOS());
        loginRecord.setDevice(agentGetter.getDevice());
        loginRecord.setBrowserType(agentGetter.getBrowser());
        loginRecord.setIpAddress(agentGetter.getIpAddr());
        loginRecord.setRemark("登陆操作：登陆人"+ userId+ "==>" + userName);
        loginRecordMapper.insert(loginRecord);
    }
}
