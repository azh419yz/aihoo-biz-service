package com.aihoo.domain.visit.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.vo.VisitOrderTradeInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * <p>
 * 在线问诊信息表 服务类
 * </p>
 *
 * @author lx
 * @since 2020-11-02
 */
public interface HosVisitService extends IService<HosVisit> {

    /**
     * 分页
     * @param param
     * @return
     * @throws Exception
     */
    PageResult<VisitOrderTradeInfoVo> page(Map<String, Object> param) throws Exception;

    void visitOrderOutExecl(Map<String,Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
