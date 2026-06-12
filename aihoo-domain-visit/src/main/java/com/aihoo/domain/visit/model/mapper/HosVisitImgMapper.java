package com.aihoo.domain.visit.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.entity.HosVisitImg;

import java.util.List;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/24 19:11
 * @description：问诊订单图片
 */
public interface HosVisitImgMapper extends BaseMapper<HosVisitImg> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(HosVisitImg record);

    HosVisitImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HosVisitImg record);

    int updateByPrimaryKey(HosVisitImg record);

    List<HosVisitImg> selectByHosVisitId(String id);
}