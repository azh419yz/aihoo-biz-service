package com.aihoo.domain.visit.model.mapper;

import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.visit.model.vo.RevisitOrderTradeInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 复诊信息表 Mapper 接口
 * </p>
 *
 * @author lx
 * @since 2020-11-02
 */
public interface HosRevisitMapper extends BaseMapper<HosRevisit> {

    /**
     * 分页
     * @param map
     * @return
     * @throws Exception
     */
    List<RevisitOrderTradeInfoVo> getInfos(Map<String, Object> map) throws Exception;

    /**
     * 分页总计
     * @param map
     * @return
     * @throws Exception
     */
    int getTotal(Map<String, Object> map) throws Exception;
}
