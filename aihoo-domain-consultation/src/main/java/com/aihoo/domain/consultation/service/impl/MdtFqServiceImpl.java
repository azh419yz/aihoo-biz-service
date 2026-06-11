package com.aihoo.domain.consultation.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.consultation.model.mapper.MdtFqMapper;
import com.aihoo.domain.consultation.model.vo.MdtFqVo;
import com.aihoo.domain.consultation.service.MdtFqService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Classname DiseaseServiceImpl
 * @Description hf
 * @Date 2020/9/16 10:44
 * @Created by ad
 */
@Service
public class MdtFqServiceImpl extends ServiceImpl<MdtFqMapper, MdtFqVo> implements MdtFqService {


    @Autowired
    private MdtFqMapper fqMdMapper;


	@Override
	public PageResult<MdtFqVo> fuzzQueryMdt(Map<String, Object> map) {

		    long page = 1;
	        long limit = 10;

	        if (null != map.get("page") && !"".equals(map.get("page"))) {
	            page = Long.parseLong(map.get("page").toString());
	        }

	        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
	            limit = Long.parseLong(map.get("limit").toString());
	        }

	        QueryWrapper<MdtFqVo> wrapper = new QueryWrapper<>();

	        if (null != map.get("name") && !"".equals(map.get("name"))) {
	            wrapper.like("name", map.get("name").toString());
	        }

	        wrapper.orderByDesc("create_time");
	        wrapper.eq("is_delete", 0);
	        wrapper.eq("status", 1);
	        IPage<MdtFqVo> iPage = this.fqMdMapper.selectPage(new Page<>(page, limit), wrapper);

	        return new PageResult<>(iPage.getRecords(), iPage.getTotal());

	}

}