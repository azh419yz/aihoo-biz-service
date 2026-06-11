package com.aihoo.domain.consultation.service.impl;


import com.aihoo.domain.consultation.model.entity.MdtBanner;
import com.aihoo.domain.consultation.model.mapper.MdtBannerMdtMapper;
import com.aihoo.domain.consultation.service.MdtBannerMdtService;
import com.aihoo.util.SecurityUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.annotation.Resource;

/**
 * @Classname DepartmentServiceImpl
 * @Description hf
 * @Date 2020/9/18 17:15
 * @Created by ad
 */
@Service
public class MdtBannerMdtServiceImpl extends ServiceImpl<MdtBannerMdtMapper, MdtBanner> implements MdtBannerMdtService {

    @Resource
    private MdtBannerMdtMapper mdtBannerMdtMapper;

    @Override
    public void deleteByMdtId(String id) {
        this.mdtBannerMdtMapper.deleteByMdtId(id);
    }


	public void saveMdtBanner(Map<String, Object> map) {
        List<String> imgs = JSONArray.parseArray(JSON.toJSON(map.get("imgs")).toString(), String.class);
        if (!CollectionUtils.isEmpty(imgs)) {
            //对应图片不为空删除所有mdtId对应的图片重新set新传入的图片
            this.deleteByMdtId(map.get("id").toString());
            MdtBanner mdtBanner = new MdtBanner();
            mdtBanner.setMdtId(map.get("id").toString());
            mdtBanner.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
            imgs.forEach(img -> {
                mdtBanner.setImg(img);
                this.save(mdtBanner);
            });
        }
	}
}