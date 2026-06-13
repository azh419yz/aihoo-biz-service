package com.aihoo.domain.sys.service.impl;

import com.aihoo.util.SecurityUtils;
import com.aihoo.domain.sys.model.mapper.SearchMapper;
import com.aihoo.domain.sys.model.entity.SearchKeywords;
import com.aihoo.domain.sys.service.SearchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname SearchServiceImpl
 * @Description hf
 * @Date 2020/9/29 17:53
 * @Created by ad
 */
@Service
public class SearchServiceImpl extends ServiceImpl<SearchMapper, SearchKeywords> implements SearchService {

    @Transactional(rollbackFor = Exception.class)
	@Override
	public boolean saveDto(Map<String, Object> map) {
		   SearchKeywords searchKeywords = new SearchKeywords();
           searchKeywords.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
           searchKeywords.setKeyword(map.get("keyword").toString());
           searchKeywords.setType(map.get("type").toString());
           searchKeywords.setIndex(map.get("index").toString());
           return this.save(searchKeywords);
	}
    
    @Transactional(rollbackFor = Exception.class)
   	@Override
    public boolean updateDtoById(SearchKeywords searchKeywords) {
    	
    	return this.updateById(searchKeywords);
    }
    
}
