package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.YlHlwyyFwsf;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 互联网服务收费表 服务类
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
public interface YlHlwyyFwsfService extends IService<YlHlwyyFwsf> {

    PageResult<YlHlwyyFwsf> page(Map<String,Object> map);

    void ylHlwyyFwsfOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response);
}