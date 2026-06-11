package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.YlGhdxx;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 挂号单信息表 服务类
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
public interface YlGhdxxService extends IService<YlGhdxx> {

    PageResult<YlGhdxx> page(Map<String,Object> map);

    void ylGhdxxOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response);
}