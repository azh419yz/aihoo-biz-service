package com.aihoo.domain.hospital.model.mapper;

import com.aihoo.domain.hospital.model.entity.DrugCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 药品对账表 Mapper 接口
 * </p>
 *
 * @author lx
 * @since 2020-11-04
 */
public interface DrugCountMapper extends BaseMapper<DrugCount> {

    List<DrugCount> selectDrugs(@Param("map") Map<String, Object> map);

    Integer selectDrugsCount(@Param("map") Map<String, Object> map);

    List<DrugCount> selectDrugsExecl(@Param("map")Map<String, Object> map);
}
