package com.aihoo.domain.consultation.model.mapper;

import com.aihoo.domain.consultation.model.entity.Mdt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Classname DiseaseMapper
 * @Description hf
 * @Date 2020/9/16 10:47
 * @Created by ad
 */
public interface MdtMapper extends BaseMapper<Mdt> {
    Mdt mdtDetails(String id);

    List<String> selectMdtIdByIndex(String index);

    void updateMdtIdByIndex(@Param("ids") List<String> ids);

    List<Map<String,String>> mdtAssistantAndConsultant();

    IPage<Mdt> selectMdtList(Page<Mdt> paging, @Param("map") Map<String, Object> map);
}