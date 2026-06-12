package com.aihoo.domain.visit.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.domain.visit.model.entity.HosRevisitImg;

import java.util.List;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/24 20:58
 * @description：复诊附件图片
 */
public interface HosRevisitImgMapper extends BaseMapper<HosRevisitImg> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(HosRevisitImg record);

    HosRevisitImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HosRevisitImg record);

    int updateByPrimaryKey(HosRevisitImg record);

    List<HosRevisitImg> selectByRevisitId(String id);
}