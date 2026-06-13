package com.aihoo.domain.sys.service;

import java.util.Map;

import com.aihoo.domain.sys.model.entity.SearchKeywords;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Classname SearchService
 * @Description hf
 * @Date 2020/9/29 17:53
 * @Created by ad
 */
public interface SearchService extends IService<SearchKeywords> {
	
	public boolean saveDto(Map<String, Object> map);
	
	  public boolean updateDtoById(SearchKeywords searchKeywords);
}
