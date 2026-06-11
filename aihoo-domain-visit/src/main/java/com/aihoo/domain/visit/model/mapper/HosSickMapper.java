package com.aihoo.domain.visit.model.mapper;


import com.aihoo.domain.visit.model.entity.HosSick;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 就诊人信息表 Mapper 接口
 * </p>
 *
 * @author mcp
 * @since 2020-10-08
 */
public interface HosSickMapper extends BaseMapper<HosSick> {

    List<HosSick> hosSickList(Map<String, Object> map);

    HosSick hosSickDetails(@Param("id")String id);

    int getCount(Map<String, Object> map);
}
