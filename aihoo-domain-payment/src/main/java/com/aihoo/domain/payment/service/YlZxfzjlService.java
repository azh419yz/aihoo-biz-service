package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.YlZxfzjl;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 在线复诊记录表 服务类
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
public interface YlZxfzjlService extends IService<YlZxfzjl> {

    PageResult<YlZxfzjl> page(Map<String,Object> map);

    void ylZxfzjlOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response);
}