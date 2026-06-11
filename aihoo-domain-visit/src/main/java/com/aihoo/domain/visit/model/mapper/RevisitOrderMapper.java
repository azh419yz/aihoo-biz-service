package com.aihoo.domain.visit.model.mapper;

import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Classname RevisitOrderMapper
 * @Description hf
 * @Date 2020/9/22 20:55
 * @Created by ad
 */
public interface RevisitOrderMapper extends BaseMapper<HosRevisit> {
    HosRevisit getVisitDetails(String id);
}
