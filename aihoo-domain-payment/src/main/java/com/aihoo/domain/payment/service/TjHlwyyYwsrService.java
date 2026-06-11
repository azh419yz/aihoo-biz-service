package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.TjHlwyyYwsr;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 互联网医院业务收入统计表 服务类
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
public interface TjHlwyyYwsrService extends IService<TjHlwyyYwsr> {

    PageResult<TjHlwyyYwsr> page(Map<String,Object> map);

    void tjHlwyyYwsrOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response);
}