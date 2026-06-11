package com.aihoo.api.doctor.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.controller.vo.AreaVo;
import com.aihoo.api.doctor.app.model.Area;

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
