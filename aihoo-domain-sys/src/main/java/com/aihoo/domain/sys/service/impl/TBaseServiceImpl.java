package com.aihoo.domain.sys.service.impl;

import com.aihoo.util.SecurityUtils;
import com.aihoo.util.StringHandler;
import com.aihoo.domain.sys.model.vo.TBaseVo;
import com.aihoo.domain.sys.model.mapper.TBaseMapper;
import com.aihoo.domain.sys.model.entity.TBase;
import com.aihoo.domain.sys.service.TBaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Classname TBaseServiceImpl
 * @Description hf
 * @Date 2020/9/29 17:07
 * @Created by ad
 */
@Service
public class TBaseServiceImpl extends ServiceImpl<TBaseMapper, TBase> implements TBaseService {

    private static final String COMMON  = "USEFUL_EXPRESSIONS";

    @Autowired
    private TBaseMapper tBaseMapper;

    @Override
    public List<TBaseVo> pageList(){
        QueryWrapper<TBase> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("`key`", COMMON);
        List<TBase> list = tBaseMapper.selectList(QueryWrapper);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        List<TBaseVo> vos = list.stream().map(v -> {
            TBaseVo vo = new TBaseVo();
            BeanUtils.copyProperties(v, vo);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public boolean addCommonWords(Map<String, Object> map){
        TBase tBase = new TBase();
        tBase.setKey(COMMON);
        tBase.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        tBase.setTitle("常用语");
        tBase.setContent(String.valueOf(map.get("content")));
        tBase.setIndex(String.valueOf(map.get("index")));
        int insert = tBaseMapper.insert(tBase);
        return insert > 0;
    }

    @Override
    public boolean updateCommonWords(Map<String, Object> map){
        TBase tBase = tBaseMapper.selectById(String.valueOf(map.get("id")));
        if (StringHandler.isNotBlank(String.valueOf(map.get("content")))) {
            tBase.setContent(String.valueOf(map.get("content")));
        }
        if (StringHandler.isNotBlank(String.valueOf(map.get("index")))) {
            tBase.setIndex(String.valueOf(map.get("index")));
        }
        return tBaseMapper.updateById(tBase) > 0;
    }


    @Transactional(rollbackFor = Exception.class)
	@Override
	public boolean saveOrUpdateDto(Map<String, Object> map) {
    	  QueryWrapper<TBase> tBaseQueryWrapper = new QueryWrapper<>();
          tBaseQueryWrapper.eq("`key`", "MTD_BANNER");
          TBase base = this.getOne(tBaseQueryWrapper);
          if (null == base) {
              base = new TBase();
              base.setTitle("问诊医生列表头部图片");
              base.setKey("MTD_BANNER");
          }
          base.setContent(map.get("content").toString());
          return this.saveOrUpdate(base);
	}
    
    @Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateDtoById(Map<String, Object> map) {
    	  TBase tBase = new TBase();
          tBase.setId(map.get("id").toString());
          tBase.setContent(map.get("content").toString());
          return this.updateById(tBase);
	}

	@Override
	public boolean updateDto(TBase tBase,Map<String, Object> map) {
		
		  QueryWrapper<TBase> tBaseQueryWrapper = new QueryWrapper<>();
          tBaseQueryWrapper.eq("`key`",  map.get("key"));
          boolean update = this.update(tBase, new UpdateWrapper<TBase>().eq("`key`", map.get("key")));
          return update;
	}
}
