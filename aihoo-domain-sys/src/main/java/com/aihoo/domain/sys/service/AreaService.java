package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.vo.AreaVo;
import com.aihoo.domain.sys.model.entity.Area;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地区表 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
public interface AreaService extends IService<Area> {

    String getAreaByCode(String code);

    List<AreaVo> l3List();

    List<AreaVo> l2List();

    List<Area> provincesList();

    List<Area> cityList(String parentAreaCode);

    List<Area> districtList(String parentAreaCode);
}
