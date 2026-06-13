package com.aihoo.domain.visit.model.mapper;

import com.aihoo.domain.visit.model.entity.HosRevisitImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HosRevisitImgMapper extends BaseMapper<HosRevisitImg> {
    List<HosRevisitImg> selectByRevisitId(@Param("id") String id);
}
