package com.aihoo.domain.consultation.model.mapper;

import com.aihoo.domain.consultation.model.entity.MdtBanner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Classname DepartmentMapper
 * @Description hf
 * @Date 2020/9/17 13:58
 * @Created by ad
 */
public interface MdtBannerMdtMapper extends BaseMapper<MdtBanner> {
    void deleteByMdtId(String id);
}