package com.aihoo.domain.visit.model.mapper;

import com.aihoo.domain.visit.model.entity.HosVisit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Classname VisitOrderMapper
 * @Description hf
 * @Date 2020/9/22 16:43
 * @Created by ad
 */
public interface VisitOrderMapper extends BaseMapper<HosVisit> {
    HosVisit getInquiryDetails(@Param("id") String id);
}
