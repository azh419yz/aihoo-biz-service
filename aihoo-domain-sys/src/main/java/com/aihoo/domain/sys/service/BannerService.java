package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.Banner;
import com.aihoo.common.PageResult;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * banner表 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
public interface BannerService extends IService<Banner> {

    PageResult bannerList(Map<String,Object> map);



    Boolean deleteBanner(String id);

    JSONArray getDoctorType(String type);

    JSONObject getBannerDetails(String id);

    boolean addBanner(Map<String, Object> map);

    JSONArray findDoctorAll();

    JSONArray findDiseaseAll();

    List teams(Map<String, Object> map);
}
