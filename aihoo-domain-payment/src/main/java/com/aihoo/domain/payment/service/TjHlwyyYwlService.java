package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.TjHlwyyYwl;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 互联网医院业务量统计表 服务类
 * </p>
 *
 * @author lx
 * @since 2020-10-26
 */
public interface TjHlwyyYwlService extends IService<TjHlwyyYwl> {

    PageResult<TjHlwyyYwl> page(Map<String,Object> map);

    void tjHlwyyYwlOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response);
}