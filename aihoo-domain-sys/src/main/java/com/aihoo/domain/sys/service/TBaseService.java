package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.vo.TBaseVo;
import com.aihoo.domain.sys.model.entity.TBase;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Classname TBaseService
 * @Description hf
 * @Date 2020/9/29 17:07
 * @Created by ad
 */
public interface TBaseService extends IService<TBase> {

    List<TBaseVo> pageList();

    boolean addCommonWords(Map<String, Object> map);

    boolean updateCommonWords(Map<String, Object> map);
    
    boolean saveOrUpdateDto(Map<String, Object> map);
    
    public boolean updateDtoById(Map<String, Object> map);
    
    public boolean updateDto(TBase tBase,Map<String, Object> map);
}
