package com.aihoo.api.admin.controller;

import com.aihoo.domain.sys.model.entity.LoginRecord;
import com.aihoo.domain.sys.service.LoginRecordService;
import com.aihoo.common.PageResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 登录日志管理
 **/
@Controller
@RequestMapping("/api/v1/sys/loginRecord")
public class LoginRecordController {

    @Resource
    private LoginRecordService loginRecordService;

    /**
     * 查询所有登录日志
     **/
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<LoginRecord> list(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            return loginRecordService.getList(map,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("用户分页查询出错");
        }
    }

}
