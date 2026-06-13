package com.aihoo.domain.sys.model.mapper;

import com.aihoo.domain.sys.model.entity.Banner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * banner表 Mapper 接口
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
public interface BannerMapper extends BaseMapper<Banner> {
    void deleteBanner(Long id);
}
