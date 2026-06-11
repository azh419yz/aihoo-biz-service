package com.aihoo.domain.consultation.service;

import com.aihoo.domain.consultation.model.entity.MdtBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * MDT 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-09-21
 */
public interface MdtBannerMdtService extends IService<MdtBanner> {

    void deleteByMdtId(String id);

    void saveMdtBanner(Map<String, Object> map);
}