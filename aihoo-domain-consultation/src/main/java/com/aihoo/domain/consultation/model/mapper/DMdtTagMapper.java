package com.aihoo.domain.consultation.model.mapper;

import com.aihoo.domain.consultation.model.entity.DMdtTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname DMdtTagMapper
 * @Description hf
 * @Date 2020/12/22 9:54
 * @Created by ad
 */
public interface DMdtTagMapper extends BaseMapper<DMdtTag> {
    List<String> selectByIndex(String index);

    void updateByIndex(@Param("ids") List<String> ids);

    List<DMdtTag> selectListByMdt(String id);
}