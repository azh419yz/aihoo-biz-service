package com.aihoo.domain.visit.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.visit.model.vo.RevisitOrderTradeInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 复诊信息表 服务类
 * </p>
 *
 * @author lx
 * @since 2020-11-02
 */
public interface HosRevisitService extends IService<HosRevisit> {

    /**
     * 分页
     * @param param
     * @return
     * @throws Exception
     */
    PageResult<RevisitOrderTradeInfoVo> page(Map<String, Object> param) throws Exception;

    void revisitOrderOutExecl(Map<String,Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
