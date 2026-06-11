package com.aihoo.domain.consultation.model.mapper;

import com.aihoo.domain.consultation.model.entity.MdtTeam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * @Classname MdtTeamMapper
 * @Description hf
 * @Date 2020/12/24 17:38
 * @Created by ad
 */
public interface MdtTeamMapper extends BaseMapper<MdtTeam> {

    MdtTeam selectMdtTeamList(String mdtTeamId);

    List<MdtTeam> teamListByKeyword(String keyword, IPage<Map> teamPage);

    List<MdtTeam> teamList(String mdtId, String tagId, IPage<Map> teamPage);
}