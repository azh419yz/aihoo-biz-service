package com.aihoo.domain.consultation.model.mapper;

import com.aihoo.domain.consultation.model.entity.MdtTeam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Mapper
public interface MdtTeamMapper extends BaseMapper<MdtTeam> {
    List<MdtTeam> teamListByKeyword(@Param("keyword") String keyword, IPage<Map> teamPage);
    List<MdtTeam> teamList(@Param("mdtId") String mdtId, @Param("tagId") String tagId, IPage<Map> teamPage);
}
