package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.YlHlwyyFwsfmx;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 互联网服务收费明细表 服务类
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
public interface YlHlwyyFwsfmxService extends IService<YlHlwyyFwsfmx> {

    PageResult<YlHlwyyFwsfmx> page(Map<String,Object> map);

    void ylHlwyyFwsfmxOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response);
}