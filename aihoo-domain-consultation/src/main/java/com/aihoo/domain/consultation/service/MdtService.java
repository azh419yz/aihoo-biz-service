package com.aihoo.domain.consultation.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.consultation.model.entity.DMdtTag;
import com.aihoo.domain.consultation.model.entity.Mdt;
import com.aihoo.domain.consultation.model.vo.MdtTeamDetailsVo;
import com.aihoo.domain.consultation.model.vo.MdtTeamVo;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * MDT 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-09-21
 */
public interface MdtService extends IService<Mdt> {

    List<Mdt> findMdtAll();

    Mdt findMdtById(Integer otherId);

    PageResult<Mdt> list(Map<String, Object> map);

    Boolean mdtEnableDisable(Map<String, Object> map);

    Mdt mdtDetails(String id);

    void mdtBulkExport(String name,String code,String hospital,String moderator, HttpServletRequest request, HttpServletResponse response);

    boolean saveMdt(Map<String, Object> map, List<String> mdtTags );

    void updateMdt( Mdt mdt , Map<String, Object> map);

    List<DMdtTag> mdtTagList();

    void mdtTagDelete(Map<String, Object> map);

    void mdtTagUpdate(Map<String, Object> map);

    void mdtTagAdd(Map<String, Object> map);

    JSONArray mdtTagNameAndId();

    JSONObject mdtAssistantAndConsultant();

    PageResult<MdtTeamVo> mdtTeamList(String id,long page,long limit);

    MdtTeamDetailsVo mdtTeamDetails(String mdtTeamId);

    void mdtTeamAdd(Map<String, Object> map);

    void mdtTeamUpdate(Map<String, Object> map);

    void mdtTeamDelete(Map<String, Object> map);

    List<Map<String, String>> typeList(Map<String, Object> map);

    void mdtCombinationAdd(Map<String, Object> map);

    void mdtCombinationUpdate(Map<String, Object> map);

    void mdtPersonalAdd(Map<String, Object> map);

    void mdtPersonalUpdate(Map<String, Object> map);

    JSONObject status();

}