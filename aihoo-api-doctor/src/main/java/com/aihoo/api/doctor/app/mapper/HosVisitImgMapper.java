package com.aihoo.api.doctor.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.api.doctor.app.model.HosVisit;
import com.aihoo.api.doctor.app.model.HosVisitImg;

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