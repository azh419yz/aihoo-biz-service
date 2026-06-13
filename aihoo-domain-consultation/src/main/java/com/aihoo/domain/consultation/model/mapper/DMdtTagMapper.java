package com.aihoo.domain.consultation.model.mapper;

import com.aihoo.domain.consultation.model.entity.DMdtTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DMdtTagMapper extends BaseMapper<DMdtTag> {
    List<DMdtTag> selectListByMdt(Integer mdtId);
}
