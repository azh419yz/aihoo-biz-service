package com.aihoo.domain.visit.model.mapper;

import com.aihoo.domain.visit.model.entity.HosVisitImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HosVisitImgMapper extends BaseMapper<HosVisitImg> {
    List<HosVisitImg> selectByHosVisitId(@Param("id") String id);
}
