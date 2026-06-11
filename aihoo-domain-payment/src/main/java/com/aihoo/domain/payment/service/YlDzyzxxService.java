package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.YlDzyzxx;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 电子医嘱信息表 服务类
 * </p>
 *
 * @author lx
 * @since 2020-10-28
 */
public interface YlDzyzxxService extends IService<YlDzyzxx> {

    PageResult<YlDzyzxx> page(Map<String,Object> map);

    void ylDzyzxxOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response);
}