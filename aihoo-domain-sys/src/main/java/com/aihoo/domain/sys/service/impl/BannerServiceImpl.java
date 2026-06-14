package com.aihoo.domain.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.aihoo.enums.BrandTypeEnum;
import com.aihoo.util.SecurityUtils;
import com.aihoo.domain.doctor.model.vo.SearchTeamDoctorVo;
import com.aihoo.domain.sys.model.mapper.BannerMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.consultation.model.mapper.MdtTeamMapper;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.consultation.model.entity.Mdt;
import com.aihoo.domain.consultation.model.entity.MdtTeam;
import com.aihoo.domain.sys.model.entity.*;
import com.aihoo.domain.sys.service.BannerService;
import com.aihoo.domain.sys.service.DiceService;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.consultation.service.MdtService;
import com.aihoo.common.PageResult;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.util.*;

/**
 * <p>
 * banner表 服务实现类
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
@Service
@Slf4j
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
    @Resource
    private MdtTeamMapper mdtTeamMapper;

    @Resource
    private DoctorUserMapper doctorUserMapper;

    @Resource
    private BannerMapper bannerMapper;

    @Resource
    private MdtService mdtService;

    @Resource
    private DoctorUserService doctorUserService;

    @Resource
    private DiceService diceService;

    @Override
    public PageResult bannerList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }

        QueryWrapper<Banner> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);

        if (null != map.get("type") && !"".equals(map.get("type"))) {
            wrapper.eq("type", map.get("type"));
        }

        wrapper.orderByAsc("`index`").orderByDesc("create_time");
        IPage<Banner> iPage = bannerMapper.selectPage(new Page<>(page, limit), wrapper);
        List<Banner> bannerList = iPage.getRecords();
        List newBannerList = new ArrayList<>();
        for (Banner banner : bannerList) {
            Map<String, Object> ban = new HashMap<>();
            ban.put("img", banner.getImg() == null ? "" : banner.getImg());
            ban.put("index", banner.getIndex() == null ? "" : banner.getIndex());
            ban.put("title", banner.getTitle() == null ? "" : banner.getTitle());
            ban.put("type", "");
            ban.put("typeName", "");
            ban.put("bannerType", "");
            ban.put("bannerTypeName", "");
            ban.put("videoUrl", "");
            String bannerType = banner.getBannerType();
            if (bannerType != null) {
                ban.put("bannerType", bannerType);
                if (bannerType.equals("IMAGE")) {
                    ban.put("bannerTypeName", "图片");
                } else if (bannerType.equals("VIDEO")) {
                    ban.put("bannerTypeName", "视频");
                    ban.put("videoUrl", banner.getVideoUrl());
                }
            }
            String type = banner.getType();
            if (!StringUtils.isEmpty(type)) {
                ban.put("type", type);//类型 NONE-无跳转 DOCKER-医生详情页 DISEASE-疾病 TEXTAREA-富文本
                Dict dict = diceService.getDoctorNameByCodeAndType(type);
                if (!StringUtils.isEmpty(dict)) {
                    ban.put("typeName", dict.getName());
                }
            }
            ban.put("content", banner.getContent() == null ? "" : banner.getContent());
            ban.put("id", banner.getId());
            newBannerList.add(ban);
        }
        PageResult bannerPageResult = new PageResult<>(newBannerList, iPage.getTotal());
        return bannerPageResult;
    }


    @Override
    public Boolean deleteBanner(String id) {
        Banner banner = new Banner();
        banner.setId(id);
        banner.setIsDelete("1");
        int i = bannerMapper.updateById(banner);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取所有的banner类型
     *
     * @return {}
     */
    @Override
    public JSONArray getDoctorType(String type) {
        return diceService.getDoctorType(type);
    }

    @Override
    public JSONObject getBannerDetails(String id) {
        Banner banner = this.bannerMapper.selectById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bannerType", "");
        jsonObject.put("bannerTypeName", "");
        jsonObject.put("videoUrl", "");
        String bannerType = banner.getBannerType();
        if (bannerType != null) {
            jsonObject.put("bannerType", bannerType);
            if (bannerType.equals("IMAGE")) {
                jsonObject.put("bannerTypeName", "图片");
            } else if (bannerType.equals("VIDEO")) {
                jsonObject.put("bannerTypeName", "视频");
                jsonObject.put("videoUrl", banner.getVideoUrl());
            }
        }
        jsonObject.put("id", banner.getId());
        jsonObject.put("type", banner.getType());
        jsonObject.put("otherId", banner.getOtherId());
        jsonObject.put("title", banner.getTitle());
        jsonObject.put("img", banner.getImg());
        jsonObject.put("index", banner.getIndex());
        jsonObject.put("name", "");
        jsonObject.put("content", banner.getContent() == null ? "" : banner.getContent());
        if (!StringUtils.isEmpty(banner.getOtherId())) {
            // 不为空说明有对应的关联
            if (banner.getType().equals(BrandTypeEnum.DOCKER.getCode())) {
                DoctorUser doctorUser = doctorUserService.findDoctorUserById(banner.getOtherId());
                if (StringUtils.isEmpty(doctorUser)) {
                    jsonObject.put("name", "");
                } else {
                    jsonObject.put("name", doctorUser.getName());
                }
            }
            if (banner.getType().equals(BrandTypeEnum.DISEASE.getCode())) {
                Mdt mdtById = mdtService.findMdtById(Integer.valueOf(banner.getOtherId()));
                if (!StringUtils.isEmpty(mdtById)) {
                    jsonObject.put("name", mdtById.getName());
                }
            }
        }
        Dict dict = diceService.getDoctorNameByCodeAndType(banner.getType());
        if (StringUtils.isEmpty(dict)) {
            jsonObject.put("typeName", "");
        } else {
            jsonObject.put("typeName", dict.getName());
        }
        return jsonObject;
    }

    @Override
    public JSONArray findDoctorAll() {
        JSONArray resp = new JSONArray();
        List<DoctorUser> doctorUsers = doctorUserService.findDoctorUserAll();
        if (CollectionUtils.isEmpty(doctorUsers)) {
            return resp;
        }
        doctorUsers.forEach(doctorUser -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", doctorUser.getId());
            jsonObject.put("name", doctorUser.getName() + " " + doctorUser.getHospitalName() + " " + doctorUser.getDepartName());
            resp.add(jsonObject);
        });
        return resp;
    }

    @Override
    public JSONArray findDiseaseAll() {
        JSONArray resp = new JSONArray();
        List<Mdt> mdtAll = mdtService.findMdtAll();
        if (CollectionUtils.isEmpty(mdtAll)) {
            return resp;
        }
        mdtAll.forEach(mdt -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", mdt.getId());
            jsonObject.put("name", mdt.getName());
            resp.add(jsonObject);
        });
        return resp;
    }

    @Override
    public boolean addBanner(Map<String, Object> map) {
        Banner banner = new Banner();
        banner.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        String type = map.get("type").toString();
        banner.setType(type);
        banner.setImg(map.get("img").toString());
        banner.setIndex(map.get("index").toString().trim());
        banner.setTitle(map.get("title") == null ? "" : map.get("title").toString());
        if (type.equals(BrandTypeEnum.DOCKER.getCode())) {
            banner.setOtherId(map.get("otherId").toString());
        } else if (type.equals(BrandTypeEnum.DISEASE.getCode())) {
            banner.setOtherId(map.get("otherId").toString());
        } else if (type.equals(BrandTypeEnum.MDTTEAM.getCode())) {
            banner.setOtherId(map.get("otherId").toString());
        } else if (type.equals(BrandTypeEnum.MDTDOCTOR.getCode())) {
            banner.setOtherId(map.get("otherId").toString());
        } else if (type.equals(BrandTypeEnum.TEXTAREA.getCode())) {
            banner.setContent(map.get("content").toString());
        }
        String bannerType = map.get("bannerType").toString();
        banner.setBannerType(bannerType);
        if ("VIDEO".equals(bannerType)) {
            banner.setVideoUrl(map.get("videoUrl").toString());
        }
        String id = (map.get("id") == null ? null : map.get("id").toString());
        int a;
        if (id != null) {
            banner.setId(id);
            a = bannerMapper.updateById(banner);
            if (a <= 0) {
                a = bannerMapper.insert(banner);
            }
        } else {
            a = bannerMapper.insert(banner);
        }
        return a > 0;
    }

    /**
     * mdt团队
     *
     * @param param
     * @return
     */
    @Override
    public List teams(Map<String, Object> param) {
        int page = 0;
        int limit = 10;
        try {
            page = ObjectUtil.isEmpty(param.get("page")) ? 0 : Integer.parseInt(param.get("page").toString());
            limit = ObjectUtil.isEmpty(param.get("limit")) ? 10 : Integer.parseInt(param.get("limit").toString());
        } catch (Exception e) {
            log.error("分页条件错误--->page:" + param.get("page") + " limit:" + param.get("limit"));
        }
        IPage<Map> teamPage = new Page<>(page, limit, false);
        List<MdtTeam> teamList;
        if (null == param.get("keyword")) {
            param.put("keyword", "");
        }
        String keyword = "%" + param.get("keyword").toString() + "%";
        teamList = mdtTeamMapper.teamListByKeyword(keyword, teamPage);
        List<SearchTeamDoctorVo> mdtTeamDoctors = new ArrayList<>();
        for (MdtTeam mdtTeam : teamList) {
            boolean isNormal = true;
            String teamId = mdtTeam.getId();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(teamId)) {
                List<SearchTeamDoctorVo> doctorUsers = doctorUserMapper.getByTeamId(teamId);
                if (null == doctorUsers || doctorUsers.size() == 0) {
                    continue;
                }
                SearchTeamDoctorVo oneDoctor = null;
                if (doctorUsers.size() == 1) {
                    oneDoctor = doctorUsers.get(0);
                    oneDoctor.setIsTeam("0");//是否是团队 0不是 1是
                } else {
                    for (SearchTeamDoctorVo doctorUser : doctorUsers) {
                        String isMain = doctorUser.getIsMain();
                        if (isMain.equals("1")) {
                            oneDoctor = doctorUser;
                            oneDoctor.setIsTeam("1");//是否是团队 0不是 1是
                        }
                    }
                    oneDoctor.setIntroduction(null);
                }
                oneDoctor.setTeamName(oneDoctor.getTeamName() + " " + oneDoctor.getHospitalName() + " " + oneDoctor.getDepartName());
                if (isNormal && null != oneDoctor) {
                    oneDoctor.setIsAuth(null);
                    oneDoctor.setStatus(null);
                    oneDoctor.setIsCancel(null);
                    oneDoctor.setIsMdt(null);
                    oneDoctor.setMdtName(mdtTeam.getMdtName());
                    mdtTeamDoctors.add(oneDoctor);
                }
            }
        }
        return mdtTeamDoctors;


    }
}
