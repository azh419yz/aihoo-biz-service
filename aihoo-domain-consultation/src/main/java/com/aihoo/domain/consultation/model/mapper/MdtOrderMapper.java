package com.aihoo.domain.consultation.model.mapper;

import com.aihoo.domain.consultation.model.entity.MdtOrder;
import com.aihoo.domain.consultation.model.vo.MdtOrderTradeInfoVo;
import com.aihoo.domain.consultation.model.vo.MdtOrderVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * @Classname MdtOrderMapper
 * @Description hf
 * @Date 2020/9/30 13:21
 * @Created by ad
 */
public interface MdtOrderMapper extends BaseMapper<MdtOrder> {

    /**
     * 分页
     * @param map
     * @return
     * @throws Exception
     */
    List<MdtOrderTradeInfoVo> getInfos(Map<String, Object> map) throws Exception;

    /**
     * 分页总计
     * @param map
     * @return
     * @throws Exception
     */
    int getTotal(Map<String, Object> map) throws Exception;

    MdtOrder findMdtOrderById(String id);

    IPage<MdtOrderVo> findMapsPage(Page<Object> objectPage, Map<String, Object> map);

}