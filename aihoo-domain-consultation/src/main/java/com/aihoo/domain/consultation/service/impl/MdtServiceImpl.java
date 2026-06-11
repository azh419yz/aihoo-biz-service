package com.aihoo.domain.consultation.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.aihoo.common.PageResult;
import com.aihoo.constant.MdtTypeEnum;
import com.aihoo.constant.UserRoleEnum;
import com.aihoo.domain.consultation.model.entity.DMdtTag;
import com.aihoo.domain.consultation.model.entity.Mdt;
import com.aihoo.domain.consultation.model.entity.MdtTag;
import com.aihoo.domain.consultation.model.entity.MdtTeam;
import com.aihoo.domain.consultation.model.entity.MdtTeamDoctor;
import com.aihoo.domain.consultation.model.entity.MdtTeamTag;
import com.aihoo.domain.consultation.model.excel.MdtEntity;
import com.aihoo.domain.consultation.model.mapper.DMdtTagMapper;
import com.aihoo.domain.consultation.model.mapper.MdtMapper;
import com.aihoo.domain.consultation.model.mapper.MdtOrderDoctorMapper;
import com.aihoo.domain.consultation.model.mapper.MdtOrderMapper;
import com.aihoo.domain.consultation.model.mapper.MdtTagMapper;
import com.aihoo.domain.consultation.model.mapper.MdtTeamDoctorMapper;
import com.aihoo.domain.consultation.model.mapper.MdtTeamMapper;
import com.aihoo.domain.consultation.model.mapper.MdtTeamTagMapper;
import com.aihoo.domain.consultation.model.entity.MdtOrder;
import com.aihoo.domain.consultation.model.entity.MdtOrderDoctor;
import com.aihoo.domain.consultation.model.vo.MdtTeamDetailsVo;
import com.aihoo.domain.consultation.model.vo.MdtTeamVo;
import com.aihoo.domain.consultation.service.MdtService;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.util.SecurityUtils;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.util.StringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname DiseaseServiceImpl
 * @Description hf
 * @Date 2020/9/16 10:44
 * @Created by ad
 */
@Service
public class MdtServiceImpl extends ServiceImpl<MdtMapper, Mdt> implements MdtService {
    @Resource
    private MdtMapper mdtMapper;

    @Resource
    private MdtOrderDoctorMapper mdtOrderDoctorMapper;

    // TODO 跨域依赖：SysUserRoleService 来自 system/sys 域，暂未迁移，先用 ApplicationContext 获取
    private Object sysUserRoleService;

    // TODO 跨域依赖：DiceService 来自 system/sys 域，暂未迁移
    private Object diceService;

    // TODO 跨域依赖：DoctorUser / DoctorUserMapper 来自 doctor 域，暂未迁移
    private Object doctorUserMapper;

    @Autowired
    private ApplicationContext applicationContext;

    private Object getBeanIfPresent(String name) {
        try {
            return applicationContext.containsBean(name) ? applicationContext.getBean(name) : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Resource
    private DMdtTagMapper dMdtTagMapper;

    @Resource
    private MdtTagMapper mdtTagMapper;

    @Resource
    private MdtTeamMapper mdtTeamMapper;

    @Resource
    private MdtTeamDoctorMapper mdtTeamDoctorMapper;

    @Resource
    private MdtTeamTagMapper mdtTeamTagMapper;

    @Resource
    private MdtOrderMapper mdtOrderMapper;

    @Override
    public List<Mdt> findMdtAll() {
        QueryWrapper<Mdt> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        return mdtMapper.selectList(wrapper);
    }

    @Override
    public Mdt findMdtById(Integer otherId) {
        return mdtMapper.selectById(otherId);
    }

    @Override
    public PageResult<Mdt> list(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }

        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<Mdt> wrapper = new QueryWrapper<>();
        if (null != map.get("name") && !"".equals(map.get("name").toString())) {
            wrapper.like("name", map.get("name").toString());
        }
        wrapper.eq("is_delete", 0);
        wrapper.orderByAsc("`index`");
        IPage<Mdt> mdtIPage = this.mdtMapper.selectPage(new Page<>(page, limit), wrapper);
        List<Mdt> records = mdtIPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new PageResult<>();
        } else {
            records.forEach(record -> {
                List<MdtTag> mdtIds = this.mdtTagMapper.selectList(new QueryWrapper<MdtTag>().eq("mdt_id", record.getId()));
                if (!CollectionUtils.isEmpty(mdtIds)) {
                    List<String> tagIds = mdtIds.stream().map(MdtTag::getTagId).collect(Collectors.toList());
                    List<DMdtTag> dMdtTags = this.dMdtTagMapper.selectBatchIds(tagIds);
                    record.setDMdtTags(dMdtTags);
                } else {
                    record.setDMdtTags(Lists.newArrayList());
                }
            });
            return new PageResult<>(records, mdtIPage.getTotal());
        }
    }

    @Override
    public Boolean mdtEnableDisable(Map<String, Object> map) {
        Mdt mdt = new Mdt();
        mdt.setId(map.get("id").toString());
        mdt.setStatus(map.get("status").toString());
        int i = this.mdtMapper.updateById(mdt);
        return i > 0;
    }

    @Override
    public Mdt mdtDetails(String id) {
        return this.mdtMapper.mdtDetails(id);
    }


    @Override
    public void mdtBulkExport(String name, String code, String hospital, String moderator, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<Mdt> wrapper = new QueryWrapper<>();
        if (null != name && !"".equals(name)) {
            wrapper.like("name", name);
        }
        if (null != code && !"".equals(code)) {
            wrapper.eq("code", code);
        }
        if (null != hospital && !"".equals(hospital)) {
            wrapper.like("hospital", hospital);
        }
        if (null != moderator && !"".equals(moderator)) {
            wrapper.like("moderator", moderator);
        }
        wrapper.orderByDesc("`index`");
        wrapper.eq("is_delete", 0);
        List<Mdt> mdts = this.mdtMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(mdts)) {
            throw new BizException("当前条件下没有mdt数据");
        }
        List<MdtEntity> mdtEntity = new ArrayList<>();
        mdts.forEach(mdt -> {
            MdtEntity entity = new MdtEntity();
            if ("0".equals(mdt.getStatus())) {
                mdt.setStatus("禁用");
            } else {
                mdt.setStatus("启用");
            }
            BeanUtils.copyProperties(mdt, entity);
            mdtEntity.add(entity);
        });
        ExcelUtils.writeExcel(request, response, mdtEntity, MdtEntity.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + "mdt数据表格.xlsx");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveMdt(Map<String, Object> map, List<String> mdtTags) {
        Mdt mdt = new Mdt();
        mdt.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        mdt.setName(map.get("name").toString().trim());
        if (null != map.get("content") && !"".equals(map.get("content").toString())) {
            mdt.setContent(map.get("content").toString().trim());
        }
        /*mdt.setIconImg(map.get("iconImg").toString().trim());
        mdt.setPrice(map.get("price").toString().trim());
        mdt.setContent(map.get("content").toString().trim());
        mdt.setHospital(map.get("hospital").toString().trim());
        mdt.setModerator(map.get("moderator").toString().trim());
        mdt.setContactWay(map.get("contactWay").toString());
        mdt.setMdtSynopsis(map.get("mdtSynopsis").toString());*/
        List<Mdt> mdts = null;
        if (null != map.get("index") && !"".equals(map.get("index"))) {
            mdt.setIndex(map.get("index").toString().trim());
            // 当新插入的index 不为空 ,需要查询到比他大的所有mdt ，index都需加1
            mdts = this.mdtMapper.selectList(new QueryWrapper<Mdt>().ge("`index`", map.get("index")));
        }
        boolean res = this.save(mdt);
        if (!CollectionUtils.isEmpty(mdts)) {
            // index 依次都需要 加一
            List<String> ids = mdts.stream().map(Mdt::getId).collect(Collectors.toList());
            this.mdtMapper.updateMdtIdByIndex(ids);
        }

       /* MdtBanner mdtBanner = new MdtBanner();
        mdtBanner.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        mdtBanner.setMdtId(mdt.getId());
        // 插入mdt详情图片
        imgs.forEach(img -> {
            mdtBanner.setImg(img);
            mdtBannerMdtService.save(mdtBanner);
        });*/
        MdtTag mdtTag = new MdtTag();
        mdtTag.setMdtId(mdt.getId());
        // mdt 与 tag的关系
        mdtTags.forEach(s -> {
            mdtTag.setTagId(s);
            this.mdtTagMapper.insert(mdtTag);
        });


        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMdt(Mdt mdt, Map<String, Object> map) {
        //更新之前的index
        String index = this.mdtMapper.selectById(mdt.getId()).getIndex();
        // 更新mdt
        this.updateById(mdt);
        // 当index 不为空时 后面的index 加一 自身需要排除
        if (null != map.get("index") && !"".equals(map.get("index"))) {
            if (!(null != index && index.equals(map.get("index").toString()))) {
                //查询所有大于等于本次index的mdt
                List<String> ids = this.mdtMapper.selectMdtIdByIndex(map.get("index").toString());
                //排除本次更新的index
                ids.remove(mdt.getId());
                if (!CollectionUtils.isEmpty(ids)) {
                    this.mdtMapper.updateMdtIdByIndex(ids);
                }
            }
        }
        if (null != map.get("mdtTags") && !"".equals(map.get("mdtTags").toString())) {
            List<String> mdtTags = JSONArray.parseArray(JSON.toJSON(map.get("mdtTags")).toString(), String.class);
            if (!CollectionUtils.isEmpty(mdtTags)) {
                // 删除所有关联
                this.mdtTagMapper.delete(new UpdateWrapper<MdtTag>().eq("mdt_id", mdt.getId()));
                // 重新关联
                MdtTag mdtTag = new MdtTag();
                mdtTag.setMdtId(mdt.getId());
                // mdt 与 tag的关系
                mdtTags.forEach(s -> {
                    mdtTag.setTagId(s);
                    this.mdtTagMapper.insert(mdtTag);
                });
            }
        }
        /*this.mdtBannerMdtService.saveMdtBanner(map);*/
    }

    @Override
    public List<DMdtTag> mdtTagList() {
        QueryWrapper<DMdtTag> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        wrapper.orderByAsc("`index`");
        return dMdtTagMapper.selectList(wrapper);
    }

    @Override
    public void mdtTagDelete(Map<String, Object> map) {
        DMdtTag dMdtTag = new DMdtTag();
        dMdtTag.setId(map.get("id").toString());
        dMdtTag.setIsDelete(map.get("isDelete").toString());
        this.dMdtTagMapper.updateById(dMdtTag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mdtTagUpdate(Map<String, Object> map) {
        DMdtTag dMdtTag = new DMdtTag();
        dMdtTag.setId(map.get("id").toString());
        //更新之前的index
        DMdtTag exist = this.dMdtTagMapper.selectById(dMdtTag.getId());

        if (null != map.get("name") && !"".equals(map.get("name"))) {
            dMdtTag.setName(map.get("name").toString());
        }
        if (null != map.get("index") && !"".equals(map.get("index"))) {
            dMdtTag.setIndex(map.get("index").toString());
        }
        this.dMdtTagMapper.updateById(dMdtTag);
        // 当index存在 且已经修改 则后面都要修改
        if (null != map.get("index") && !"".equals(map.get("index"))) {
            if (null != exist.getIndex()) {
                if (!map.get("index").toString().equals(exist.getIndex())) {
                    List<String> ids = this.dMdtTagMapper.selectByIndex(map.get("index").toString());
                    //排除本次更新的index
                    ids.remove(dMdtTag.getId());
                    if (!CollectionUtils.isEmpty(ids)) {
                        this.dMdtTagMapper.updateByIndex(ids);
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mdtTagAdd(Map<String, Object> map) {
        DMdtTag dMdtTag = new DMdtTag();
        dMdtTag.setName(map.get("name").toString());
        dMdtTag.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        List<DMdtTag> dMdtTags = null;
        if (!StringUtils.isEmpty(map.get("index"))) {
            dMdtTag.setIndex(map.get("index").toString());
            // 当新插入的index 不为空 ,需要查询到比他大的所有标签 ，index都需加1
            dMdtTags = this.dMdtTagMapper.selectList(new QueryWrapper<DMdtTag>().ge("`index`", map.get("index")));
        }
        this.dMdtTagMapper.insert(dMdtTag);
        if (!CollectionUtils.isEmpty(dMdtTags)) {
            // index 依次都需要 加一
            List<String> ids = dMdtTags.stream().map(DMdtTag::getId).collect(Collectors.toList());
            this.dMdtTagMapper.updateByIndex(ids);
        }
    }

    @Override
    public JSONArray mdtTagNameAndId() {
        JSONArray jsonArray = new JSONArray();
        List<DMdtTag> dMdtTags = this.dMdtTagMapper.selectList(new QueryWrapper<DMdtTag>().eq("is_delete", 0));
        if (CollectionUtils.isEmpty(dMdtTags)) {
            return jsonArray;
        } else {
            dMdtTags.forEach(dMdtTag -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", dMdtTag.getId());
                jsonObject.put("name", dMdtTag.getName());
                jsonArray.add(jsonObject);
            });
            return jsonArray;
        }
    }

    @Override
    public JSONObject mdtAssistantAndConsultant() {
        JSONObject jsonObject = new JSONObject();
        JSONArray ConsultantArray = new JSONArray();
        JSONArray AssistantArray = new JSONArray();
        JSONArray CombinationConsultantArray = new JSONArray();
        JSONArray CombinationAssistantArray = new JSONArray();
        // 本系统已存在医生，已认证，已开启会诊，未禁用，未注销
        List<Map<String, String>> lists = this.mdtMapper.mdtAssistantAndConsultant();
        if (CollectionUtils.isEmpty(lists)) {
            jsonObject.put("ConsultantArray", ConsultantArray);
            jsonObject.put("AssistantArray", AssistantArray);
            jsonObject.put("CombinationConsultantArray", CombinationConsultantArray);
            jsonObject.put("CombinationAssistantArray", CombinationAssistantArray);
            return jsonObject;
        } else {
            List<Map<String, String>> doctorConsultant = lists.stream().filter(s -> s.get("doctor_type").contains(UserRoleEnum.CONSULTANT.getCode())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(doctorConsultant)) {
                doctorConsultant.forEach(s -> {
                    JSONObject object = new JSONObject();
                    object.put("id", s.get("id"));
                    object.put("name", s.get("name"));
                    ConsultantArray.add(object);
                });
            }
            jsonObject.put("ConsultantArray", ConsultantArray);
            List<Map<String, String>> doctorAssistant = lists.stream().filter(s -> s.get("doctor_type").contains(UserRoleEnum.ASSISTANT.getCode())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(doctorAssistant)) {
                doctorAssistant.forEach(s -> {
                    JSONObject object = new JSONObject();
                    object.put("id", s.get("id"));
                    object.put("name", s.get("name"));
                    AssistantArray.add(object);
                });
            }
            jsonObject.put("AssistantArray", AssistantArray);
            List<Map<String, String>> doctorCombinationConsultant = lists.stream().filter(s -> (s.get("doctor_type").contains(UserRoleEnum.CONSULTANT.getCode()) && "1".equals(String.valueOf(s.get("is_combination"))))).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(doctorCombinationConsultant)) {
                doctorCombinationConsultant.forEach(s -> {
                    JSONObject object = new JSONObject();
                    object.put("id", s.get("id"));
                    object.put("name", s.get("name"));
                    CombinationConsultantArray.add(object);
                });
            }
            jsonObject.put("CombinationConsultantArray", CombinationConsultantArray);
            List<Map<String, String>> doctorCombinationAssistant = lists.stream().filter(s -> (s.get("doctor_type").contains(UserRoleEnum.ASSISTANT.getCode()) && "1".equals(String.valueOf(s.get("is_combination"))))).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(doctorCombinationAssistant)) {
                doctorCombinationAssistant.forEach(s -> {
                    JSONObject object = new JSONObject();
                    object.put("id", s.get("id"));
                    object.put("name", s.get("name"));
                    CombinationAssistantArray.add(object);
                });
            }
            jsonObject.put("CombinationAssistantArray", CombinationAssistantArray);
            return jsonObject;
        }
    }

    @Override
    public PageResult<MdtTeamVo> mdtTeamList(String id, long page, long limit) {
        List<MdtTeamVo> list = new ArrayList<>();
        QueryWrapper<MdtTeam> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        wrapper.eq("mdt_id", id);
        IPage<MdtTeam> mdtTeams = this.mdtTeamMapper.selectPage(new Page<>(page, limit), wrapper);
        List<MdtTeam> records = mdtTeams.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new PageResult<>();
        } else {
            records.forEach(record -> {
                MdtTeamVo mdtTeamVo = new MdtTeamVo();
                // 团队主键id
                mdtTeamVo.setMdtTeamId(record.getId());
                // 团队名称
                mdtTeamVo.setMdtTeamName(record.getName());

                List<MdtTeamDoctor> mdtTeamDoctors = this.mdtTeamDoctorMapper.selectList(new QueryWrapper<MdtTeamDoctor>().eq("mdt_team_id", record.getId()));
                if (!CollectionUtils.isEmpty(mdtTeamDoctors)) {
                    // 会诊医生`
                    List<String> teamDoctorIds = mdtTeamDoctors.stream().filter(s -> "CONSULTANT".equals(s.getDoctorType())).
                            map(MdtTeamDoctor::getDoctorUserId).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(teamDoctorIds)) {
                        // TODO 跨域依赖：DoctorUserMapper.selectList 来自 doctor 域，暂未迁移
                        // List<DoctorUser> teamDoctors = this.doctorUserMapper.selectList(new QueryWrapper<DoctorUser>().in("id", teamDoctorIds));
                        // String mdtDoctorName = teamDoctors.stream().map(DoctorUser::getName).collect(Collectors.joining(","));
                        String mdtDoctorName = "";
                        mdtTeamVo.setMdtDoctorName(mdtDoctorName);
                    }
                    // 领衔医生
                    List<String> headDoctorNameIds = mdtTeamDoctors.stream().filter(s -> "1".equals(s.getIsMain())).
                            map(MdtTeamDoctor::getDoctorUserId).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(headDoctorNameIds)) {
                        // TODO 跨域依赖：DoctorUserMapper.selectList 来自 doctor 域，暂未迁移
                        // List<DoctorUser> headDoctorName = this.doctorUserMapper.selectList(new QueryWrapper<DoctorUser>().in("id", headDoctorNameIds));
                        // String headDoctorNames = headDoctorName.stream().map(DoctorUser::getName).collect(Collectors.joining(","));
                        String headDoctorNames = "";
                        mdtTeamVo.setHeadDoctorName(headDoctorNames);
                    }
                    // 助理医生
                    List<String> assistantDoctorNameIds = mdtTeamDoctors.stream().filter(s -> "ASSISTANT".equals(s.getDoctorType())).
                            map(MdtTeamDoctor::getDoctorUserId).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(assistantDoctorNameIds)) {
                        // TODO 跨域依赖：DoctorUserMapper.selectList 来自 doctor 域，暂未迁移
                        // List<DoctorUser> assistantDoctorName = this.doctorUserMapper.selectList(new QueryWrapper<DoctorUser>().in("id", assistantDoctorNameIds));
                        // String assistantDoctorNames = assistantDoctorName.stream().map(DoctorUser::getName).collect(Collectors.joining(","));
                        String assistantDoctorNames = "";
                        mdtTeamVo.setAssistantDoctorName(assistantDoctorNames);
                    }
                }
                list.add(mdtTeamVo);
            });
            return new PageResult<>(list, mdtTeams.getTotal());
        }
    }

    @Override
    public MdtTeamDetailsVo mdtTeamDetails(String mdtTeamId) {
        MdtTeam mdtTeam = this.mdtTeamMapper.selectMdtTeamList(mdtTeamId);
        MdtTeamDetailsVo res = new MdtTeamDetailsVo();
        // 团队id
        res.setId(mdtTeam.getId());
        // 团队名称
        res.setName(mdtTeam.getName());
        // 首页图片
        res.setHomeImg(mdtTeam.getHomeImg());
        // 列表图片
        res.setListImg(mdtTeam.getListImg());
        // 团队详情图片
        res.setDetailsImg(mdtTeam.getDetailsImg());
        // 团队简介
        res.setIntroduction(mdtTeam.getIntroduction());
        // 排序级别
        res.setIndex(mdtTeam.getIndex());
        // 总价格
        res.setPrice(mdtTeam.getPrice());
        // 标签详情
        List<DMdtTag> dMdtTagRes = new ArrayList<>();
        List<DMdtTag> dMdtTags = mdtTeam.getDMdtTags();
        if (!CollectionUtils.isEmpty(dMdtTags)) {
            dMdtTagRes = dMdtTags;
        }
        res.setDMdtTags(dMdtTagRes);
        //会诊模式
        String mdtType = mdtTeam.getMdtType();
        res.setMdtType(mdtType);
        // 医生详情
        boolean isTeam = mdtType.equals(MdtTypeEnum.TEAM.getCode());
        boolean isPersonal = mdtType.equals(MdtTypeEnum.PERSONAL.getCode());
        boolean isCombination = mdtType.equals(MdtTypeEnum.COMBINATION.getCode());
        if (isPersonal) {
            List<MdtTeamDetailsVo.mdtDoctor> consultantDoctor = new ArrayList<>();
            List<MdtTeamDetailsVo.mdtDoctor> assistantDoctor = new ArrayList<>();
            List<MdtTeamDoctor> doctors = mdtTeam.getMdtTeamDoctors();
            if (!CollectionUtils.isEmpty(doctors)) {
                List<MdtTeamDoctor> consultant = doctors.stream().filter(s -> UserRoleEnum.CONSULTANT.getCode().equals(s.getDoctorType())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(consultant)) {
                    consultantDoctor = setDoctors(consultant);
                }
                List<MdtTeamDoctor> assistant = doctors.stream().filter(s -> UserRoleEnum.ASSISTANT.getCode().equals(s.getDoctorType())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(assistant)) {
                    assistantDoctor = setDoctors(assistant);
                }
            }
            if (consultantDoctor.size() > 1) {
                throw new BizException("个人模式会诊医生只能有一个");
            }
            if (assistantDoctor.size() > 1) {
                throw new BizException("团队模式助理医生只能有一个");
            }
            if (assistantDoctor.size() > 0) {
                res.setAssistantDoctor(assistantDoctor.get(0));
            }
            res.setConsultantDoctor(consultantDoctor.get(0));
        } else if (isTeam) {
            List<MdtTeamDetailsVo.mdtDoctor> consultantDoctors = new ArrayList<>();
            List<MdtTeamDetailsVo.mdtDoctor> assistantDoctor = new ArrayList<>();
            List<MdtTeamDoctor> doctors = mdtTeam.getMdtTeamDoctors();
            if (!CollectionUtils.isEmpty(doctors)) {
                List<MdtTeamDoctor> consultant = doctors.stream().filter(s -> UserRoleEnum.CONSULTANT.getCode().equals(s.getDoctorType())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(consultant)) {
                    consultantDoctors = setDoctors(consultant);
                }
                List<MdtTeamDoctor> assistant = doctors.stream().filter(s -> UserRoleEnum.ASSISTANT.getCode().equals(s.getDoctorType())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(assistant)) {
                    assistantDoctor = setDoctors(assistant);
                }
            }
            if (assistantDoctor.size() > 1) {
                throw new BizException("团队模式助理医生只能有一个");
            }
            if (assistantDoctor.size() > 0) {
                res.setAssistantDoctor(assistantDoctor.get(0));
            }
            res.setConsultantDoctors(consultantDoctors);
        } else if (isCombination) {
            List<Map<String, MdtTeamDetailsVo.mdtDoctor>> combinationDoctors = new ArrayList<>();
            Map<String, MdtTeamDoctor> assistantDoctors = new HashMap();
            List<MdtTeamDoctor> doctors = mdtTeam.getMdtTeamDoctors();
            if (!CollectionUtils.isEmpty(doctors)) {
                List<MdtTeamDoctor> consultant = doctors.stream().filter(s -> UserRoleEnum.CONSULTANT.getCode().equals(s.getDoctorType())).collect(Collectors.toList());
                List<MdtTeamDoctor> assistant = doctors.stream().filter(s -> UserRoleEnum.ASSISTANT.getCode().equals(s.getDoctorType())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(assistant)) {
                    for (MdtTeamDoctor mdtTeamDoctor : assistant) {
                        assistantDoctors.put(mdtTeamDoctor.getConsultantId(), mdtTeamDoctor);
                    }
                }
                for (MdtTeamDoctor mdtTeamDoctor : consultant) {
                    Map<String, MdtTeamDetailsVo.mdtDoctor> hashMap = new HashMap<>();
                    MdtTeamDetailsVo.mdtDoctor mdtDoctor = setDoctor(mdtTeamDoctor);
                    hashMap.put("consultantDoctor", mdtDoctor);
                    MdtTeamDoctor teamDoctor = assistantDoctors.get(mdtTeamDoctor.getId());
                    if (null != teamDoctor) {
                        hashMap.put("assistantDoctor", setDoctor(teamDoctor));
                    } else {
                        hashMap.put("assistantDoctor", null);
                    }
                    combinationDoctors.add(hashMap);
                }
            }

            res.setCombinationDoctors(combinationDoctors);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mdtPersonalAdd(Map<String, Object> map) {
        MdtTeamDoctor consultantDoctor = JSON.parseObject(JSON.toJSON(map.get("consultantDoctor")).toString(), MdtTeamDoctor.class);
        BigDecimal assistantPrice = new BigDecimal(0);
        MdtTeamDoctor assistantDoctor = null;
        if (map.containsKey("assistantDoctor") && ObjectUtil.isNotEmpty(map.get("assistantDoctor"))) {
            assistantDoctor = JSON.parseObject(JSON.toJSON(map.get("assistantDoctor")).toString(), MdtTeamDoctor.class);
            assistantPrice = new BigDecimal(assistantDoctor.getPrice());
        }
        BigDecimal bigDecimal = new BigDecimal(consultantDoctor.getPrice());
        if (bigDecimal.compareTo(new BigDecimal(0)) == 0) {
            throw new BizException("团队价格不能为0");
        }
        BigDecimal price = bigDecimal.add(assistantPrice);
        // 新增mdt 团队
        MdtTeam mdtTeam = new MdtTeam();
        mdtTeam.setMdtId(map.get("mdtId").toString());
        mdtTeam.setName(map.get("name").toString());
        mdtTeam.setHomeImg(map.get("homeImg").toString());
        mdtTeam.setDetailsImg(map.get("detailsImg").toString());
        mdtTeam.setIntroduction(map.get("introduction").toString());
        mdtTeam.setPrice(price.toString());
        mdtTeam.setMdtType(MdtTypeEnum.PERSONAL.getCode());
        if (null != map.get("index") && !"".equals(map.get("index").toString())) {
            mdtTeam.setIndex(map.get("index").toString());
        }
        this.mdtTeamMapper.insert(mdtTeam);
        // 新增组合详情
        // 会诊医生
        addPersonalDoctor(mdtTeam.getId(), consultantDoctor, assistantDoctor);
        // 添加标签关联
        List<String> dMdtTags = JSONArray.parseArray(map.get("dMdtTags").toString(), String.class);
        addTeamTag(mdtTeam, dMdtTags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mdtCombinationAdd(Map<String, Object> map) {
        List<Map<String, MdtTeamDoctor>> doctors = JSON.parseObject(JSON.toJSON(map.get("combinationDoctors")).toString(), new TypeReference<List<Map<String, MdtTeamDoctor>>>() {
        });
        List<MdtTeamDoctor> consultantDoctors = new ArrayList<>();
        List<MdtTeamDoctor> assistantDoctors = new ArrayList<>();
        doctors.forEach(stringMdtTeamDoctorMap -> {
            if (ObjectUtil.isEmpty(stringMdtTeamDoctorMap.get("consultantDoctor"))) {
                throw new BizException("组合医生，会诊医生为空");
            }
            consultantDoctors.add(stringMdtTeamDoctorMap.get("consultantDoctor"));
            if (ObjectUtil.isNotEmpty(stringMdtTeamDoctorMap.get("assistantDoctor")) && stringMdtTeamDoctorMap.get("assistantDoctor").getDoctorUserId() != null) {
                assistantDoctors.add(stringMdtTeamDoctorMap.get("assistantDoctor"));
            }
        });
        BigDecimal consultantDecimal = consultantDoctors.stream().map(MdtTeamDoctor::getPrice).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal assistantDecimal = new BigDecimal("0");
        if (assistantDoctors.size() != 0) {
            assistantDecimal = assistantDoctors.stream().map(MdtTeamDoctor::getPrice).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        String price = consultantDecimal.add(assistantDecimal).toString();
        if (consultantDecimal.compareTo(new BigDecimal("0")) == 0) {
            throw new BizException("组合团队价格不能为0");
        }
        // 新增mdt 团队
        MdtTeam mdtTeam = new MdtTeam();
        mdtTeam.setMdtId(map.get("mdtId").toString());
        mdtTeam.setName(map.get("name").toString());
        mdtTeam.setHomeImg(map.get("homeImg").toString());
        mdtTeam.setDetailsImg(map.get("detailsImg").toString());
        mdtTeam.setIntroduction(map.get("introduction").toString());
        mdtTeam.setPrice(price);
        mdtTeam.setMdtType(MdtTypeEnum.COMBINATION.getCode());
        if (null != map.get("index") && !"".equals(map.get("index").toString())) {
            mdtTeam.setIndex(map.get("index").toString());
        }
        this.mdtTeamMapper.insert(mdtTeam);
        // 新增组合医生
        addCombinationDoctor(mdtTeam.getId(), doctors);
        // 添加标签关联
        List<String> dMdtTags = JSONArray.parseArray(map.get("dMdtTags").toString(), String.class);
        addTeamTag(mdtTeam, dMdtTags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mdtTeamAdd(Map<String, Object> map) {
        List<MdtTeamDoctor> consultantDoctors = JSONArray.parseArray(JSON.toJSON(map.get("consultantDoctors")).toString(), MdtTeamDoctor.class);
        MdtTeamDoctor assistantDoctor = null;
        BigDecimal assistantPrice = new BigDecimal(0);
        if (map.containsKey("assistantDoctor") && ObjectUtil.isNotEmpty(map.get("assistantDoctor"))) {
            assistantDoctor = JSON.parseObject(JSON.toJSON(map.get("assistantDoctor")).toString(), MdtTeamDoctor.class);
            assistantPrice = new BigDecimal(assistantDoctor.getPrice());
        }
        BigDecimal consultantDecimal = consultantDoctors.stream().map(MdtTeamDoctor::getPrice).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        String price = consultantDecimal.add(assistantPrice).toString();
        if (consultantDecimal.compareTo(new BigDecimal("0")) == 0) {
            throw new BizException("团队价格不能为0");
        }
        // 新增mdt 团队
        MdtTeam mdtTeam = new MdtTeam();
        mdtTeam.setMdtId(map.get("mdtId").toString());
        mdtTeam.setName(map.get("name").toString());
        mdtTeam.setHomeImg(map.get("homeImg").toString());
        mdtTeam.setDetailsImg(map.get("detailsImg").toString());
        mdtTeam.setIntroduction(map.get("introduction").toString());
        mdtTeam.setPrice(price);
        if (null != map.get("index") && !"".equals(map.get("index").toString())) {
            mdtTeam.setIndex(map.get("index").toString());
        }
        mdtTeam.setMdtType(MdtTypeEnum.TEAM.getCode());
        this.mdtTeamMapper.insert(mdtTeam);
        // 新增团队的医生
        addTeamDoctor(mdtTeam.getId(), consultantDoctors, assistantDoctor);
        // 添加标签关联
        List<String> dMdtTags = JSONArray.parseArray(map.get("dMdtTags").toString(), String.class);
        addTeamTag(mdtTeam, dMdtTags);
    }

    private void addTeamTag(MdtTeam mdtTeam, List<String> dMdtTags) {
        MdtTeamTag mdtTeamTag = new MdtTeamTag();
        mdtTeamTag.setMdtTeamId(mdtTeam.getId());
        dMdtTags.forEach(dMdtTag -> {
            mdtTeamTag.setTagId(dMdtTag);
            this.mdtTeamTagMapper.insert(mdtTeamTag);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mdtPersonalUpdate(Map<String, Object> map) {
        MdtTeamDoctor consultantDoctor = JSON.parseObject(JSON.toJSON(map.get("consultantDoctor")).toString(), MdtTeamDoctor.class);
        BigDecimal assistantPrice = new BigDecimal(0);
        MdtTeamDoctor assistantDoctor = null;
        if (map.containsKey("assistantDoctor") && ObjectUtil.isNotEmpty(map.get("assistantDoctor"))) {
            assistantDoctor = JSON.parseObject(JSON.toJSON(map.get("assistantDoctor")).toString(), MdtTeamDoctor.class);
            assistantPrice = new BigDecimal(assistantDoctor.getPrice());
        }
        BigDecimal bigDecimal = new BigDecimal(consultantDoctor.getPrice());
        if (bigDecimal.compareTo(new BigDecimal("0")) == 0) {
            throw new BizException("团队价格不能为0");
        }
        BigDecimal price = bigDecimal.add(assistantPrice);

        MdtTeam mdtTeam = new MdtTeam();
        mdtTeam.setId(map.get("mdtTeamId").toString());
        mdtTeam.setPrice(price.toString());
        mdtTeam.setMdtType(MdtTypeEnum.PERSONAL.getCode());

        if (null != map.get("name") && !"".equals(map.get("name").toString())) {
            mdtTeam.setName(map.get("name").toString());
        }
        if (null != map.get("homeImg") && !"".equals(map.get("homeImg").toString())) {
            mdtTeam.setHomeImg(map.get("homeImg").toString());
        }
        if (null != map.get("detailsImg") && !"".equals(map.get("detailsImg").toString())) {
            mdtTeam.setDetailsImg(map.get("detailsImg").toString());
        }
        if (null != map.get("index") && !"".equals(map.get("index").toString())) {
            mdtTeam.setIndex(map.get("index").toString());
        }
        if (null != map.get("introduction") && !"".equals(map.get("introduction").toString())) {
            mdtTeam.setIntroduction(map.get("introduction").toString());
        }
        // 更新团队信息
        this.mdtTeamMapper.updateById(mdtTeam);

        // 更新个人医生团队
        updatePersonalDoctor(mdtTeam.getId(), consultantDoctor, assistantDoctor);

        // 更新标签信息
        if (null != map.get("dMdtTags") && !"".equals(map.get("dMdtTags").toString())) {
            List<String> dMdtTags = JSONArray.parseArray(map.get("dMdtTags").toString(), String.class);
            if (!CollectionUtils.isEmpty(dMdtTags)) {
                // 删除之前的关联标签
                this.mdtTeamTagMapper.delete(new UpdateWrapper<MdtTeamTag>().eq("mdt_team_id", mdtTeam.getId()));
                // 重新写入
                addTeamTag(mdtTeam, dMdtTags);
            }
        }
    }

    @Override
    public JSONObject status() {
        // TODO 跨域依赖：SysUser / SysUserRoleService 来自 system/sys 域，暂未迁移
        // 旧代码会调用 sysUserRoleService.userRole() 与 SecurityUtils.getLoginUser()
        // 这里返回空对象以保证编译通过，业务逻辑需在依赖就位后恢复
        JSONObject status = new JSONObject();
        try {
            Object sysUserRoleServiceBean = getBeanIfPresent("sysUserRoleService");
            if (sysUserRoleServiceBean == null) {
                return status;
            }
            // 占位：跨域未迁移时返回空 status
            return status;
        } catch (Exception e) {
            return status;
        }
        /*
        SysUser loginUser = SecurityUtils.getLoginUser();
        List<String> roles = sysUserRoleService.userRole();
        //会诊助理医生角色
        boolean hzzlys = roles.contains(UserRoleEnum.HZZLYS.getCode());
        //医疗管家角色
        boolean ylgj = roles.contains(UserRoleEnum.YLGJ.getCode());
        assert loginUser != null;
        String adminId = loginUser.getId();
        String doctorUserId = null;
        if (hzzlys) {
            LambdaQueryWrapper<DoctorUser> doctorUserLambdaQueryWrapper = new QueryWrapper<DoctorUser>().lambda();
            doctorUserLambdaQueryWrapper.eq(DoctorUser::getPapersNumbers, loginUser.getIdCard());
            doctorUserLambdaQueryWrapper.eq(DoctorUser::getStatus, "1");
            doctorUserLambdaQueryWrapper.eq(DoctorUser::getIsCancel, "0");
            DoctorUser doctorUser = doctorUserMapper.selectOne(doctorUserLambdaQueryWrapper);
            assert doctorUser != null;
            doctorUserId = doctorUser.getId();
        }

        // ... 后续统计逻辑参考旧代码
        return status;
        */
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mdtCombinationUpdate(Map<String, Object> map) {
        List<Map<String, MdtTeamDoctor>> doctors = JSON.parseObject(JSON.toJSON(map.get("combinationDoctors")).toString(), new TypeReference<List<Map<String, MdtTeamDoctor>>>() {
        });
        List<MdtTeamDoctor> consultantDoctors = new ArrayList<>();
        List<MdtTeamDoctor> assistantDoctors = new ArrayList<>();
        doctors.forEach(stringMdtTeamDoctorMap -> {
            if (null == stringMdtTeamDoctorMap.get("consultantDoctor")) {
                throw new BizException("组合医生，会诊医生为空");
            }
            consultantDoctors.add(stringMdtTeamDoctorMap.get("consultantDoctor"));
            if (ObjectUtil.isNotEmpty(stringMdtTeamDoctorMap.get("assistantDoctor")) && stringMdtTeamDoctorMap.get("assistantDoctor").getDoctorUserId() != null) {
                assistantDoctors.add(stringMdtTeamDoctorMap.get("assistantDoctor"));
            }
        });
        BigDecimal consultantDecimal = new BigDecimal("0");
        BigDecimal assistantDecimal = new BigDecimal("0");
        if (!CollectionUtils.isEmpty(consultantDoctors)) {
            consultantDecimal = consultantDoctors.stream().map(MdtTeamDoctor::getPrice).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (consultantDecimal.compareTo(new BigDecimal("0")) == 0) {
            throw new BizException("团队价格不能为0");
        }
        if (!CollectionUtils.isEmpty(assistantDoctors)) {
            assistantDecimal = assistantDoctors.stream().map(MdtTeamDoctor::getPrice).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        String price = consultantDecimal.add(assistantDecimal).toString();

        MdtTeam mdtTeam = new MdtTeam();
        mdtTeam.setId(map.get("mdtTeamId").toString());
        mdtTeam.setPrice(price);
        mdtTeam.setMdtType(MdtTypeEnum.COMBINATION.getCode());

        if (null != map.get("name") && !"".equals(map.get("name").toString())) {
            mdtTeam.setName(map.get("name").toString());
        }
        if (null != map.get("homeImg") && !"".equals(map.get("homeImg").toString())) {
            mdtTeam.setHomeImg(map.get("homeImg").toString());
        }
        if (null != map.get("detailsImg") && !"".equals(map.get("detailsImg").toString())) {
            mdtTeam.setDetailsImg(map.get("detailsImg").toString());
        }
        if (null != map.get("index") && !"".equals(map.get("index").toString())) {
            mdtTeam.setIndex(map.get("index").toString());
        }
        if (null != map.get("introduction") && !"".equals(map.get("introduction").toString())) {
            mdtTeam.setIntroduction(map.get("introduction").toString());
        }
        // 更新团队信息
        this.mdtTeamMapper.updateById(mdtTeam);

        // 更新组合医生
        updateCombinationDoctor(mdtTeam.getId(), doctors);

        // 更新标签信息
        if (null != map.get("dMdtTags") && !"".equals(map.get("dMdtTags").toString())) {
            List<String> dMdtTags = JSONArray.parseArray(map.get("dMdtTags").toString(), String.class);
            if (!CollectionUtils.isEmpty(dMdtTags)) {
                // 删除之前的关联标签
                this.mdtTeamTagMapper.delete(new UpdateWrapper<MdtTeamTag>().eq("mdt_team_id", mdtTeam.getId()));
                // 重新写入
                addTeamTag(mdtTeam, dMdtTags);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mdtTeamUpdate(Map<String, Object> map) {
        List<MdtTeamDoctor> consultantDoctors = null;
        if (null != map.get("consultantDoctors") && !"".equals(map.get("consultantDoctors").toString())) {
            List<MdtTeamDoctor> doctors = JSONArray.parseArray(JSON.toJSON(map.get("consultantDoctors")).toString(), MdtTeamDoctor.class);
            if (!CollectionUtils.isEmpty(doctors)) {
                consultantDoctors = doctors;
            }
        }
        MdtTeamDoctor assistantDoctor = null;
        BigDecimal assistantPrice = new BigDecimal(0);
        if (map.containsKey("assistantDoctor") && ObjectUtil.isNotEmpty(map.get("assistantDoctor"))) {
            assistantDoctor = JSON.parseObject(JSON.toJSON(map.get("assistantDoctor")).toString(), MdtTeamDoctor.class);
            assistantDoctor.setPrice(StringUtil.isBlank(assistantDoctor.getPrice())?"0":assistantDoctor.getPrice());
            assistantPrice = new BigDecimal(assistantDoctor.getPrice());
        }
        BigDecimal consultantDecimal = new BigDecimal("0");
        if (!CollectionUtils.isEmpty(consultantDoctors)) {
            consultantDecimal = consultantDoctors.stream().map(MdtTeamDoctor::getPrice).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (consultantDecimal.compareTo(new BigDecimal("0")) == 0) {
            throw new BizException("团队价格不能为0");
        }
        String price = consultantDecimal.add(assistantPrice).toString();

        MdtTeam mdtTeam = new MdtTeam();
        mdtTeam.setId(map.get("mdtTeamId").toString());
        mdtTeam.setPrice(price);
        mdtTeam.setMdtType(MdtTypeEnum.TEAM.getCode());
        if (null != map.get("name") && !"".equals(map.get("name").toString())) {
            mdtTeam.setName(map.get("name").toString());
        }
        if (null != map.get("homeImg") && !"".equals(map.get("homeImg").toString())) {
            mdtTeam.setHomeImg(map.get("homeImg").toString());
        }
        if (null != map.get("detailsImg") && !"".equals(map.get("detailsImg").toString())) {
            mdtTeam.setDetailsImg(map.get("detailsImg").toString());
        }
//        if (null != map.get("listImg") && !"".equals(map.get("listImg").toString())) {
//            mdtTeam.setListImg(map.get("listImg").toString());
//        }
        if (null != map.get("index") && !"".equals(map.get("index").toString())) {
            mdtTeam.setIndex(map.get("index").toString());
        }
        if (null != map.get("introduction") && !"".equals(map.get("introduction").toString())) {
            mdtTeam.setIntroduction(map.get("introduction").toString());
        }
        // 更新团队信息
        this.mdtTeamMapper.updateById(mdtTeam);

        // 更新团队医生
        updateTeamDoctor(mdtTeam.getId(), consultantDoctors, assistantDoctor);
        // 更新标签信息
        if (null != map.get("dMdtTags") && !"".equals(map.get("dMdtTags").toString())) {
            List<String> dMdtTags = JSONArray.parseArray(map.get("dMdtTags").toString(), String.class);
            if (!CollectionUtils.isEmpty(dMdtTags)) {
                // 删除之前的关联标签
                this.mdtTeamTagMapper.delete(new UpdateWrapper<MdtTeamTag>().eq("mdt_team_id", mdtTeam.getId()));
                // 重新写入
                addTeamTag(mdtTeam, dMdtTags);
            }
        }
    }


    @Override
    public void mdtTeamDelete(Map<String, Object> map) {
        MdtTeam mdtTeam = new MdtTeam();
        mdtTeam.setId(map.get("mdtTeamId").toString());
        mdtTeam.setIsDelete("1");
        this.mdtTeamMapper.updateById(mdtTeam);
    }

    @Override
    public List<Map<String, String>> typeList(Map<String, Object> map) {
        // TODO 跨域依赖：DiceService.getMdtTypeList() 来自 system/sys 域，暂未迁移
        // 原代码：return diceService.getMdtTypeList();
        return new ArrayList<>();
    }

    /**
     * 修改个人医生
     *
     * @param mdtTeamId
     * @param consultantDoctor
     * @param assistantDoctor
     */
    private void updatePersonalDoctor(String mdtTeamId, MdtTeamDoctor consultantDoctor, MdtTeamDoctor assistantDoctor) {
        // 删除之前的关联医生
        this.mdtTeamDoctorMapper.delete(new QueryWrapper<MdtTeamDoctor>().eq("mdt_team_id", mdtTeamId));
        // 重新写入
        addPersonalDoctor(mdtTeamId, consultantDoctor, assistantDoctor);
    }

    /**
     * 修改组合医生
     *
     * @param mdtTeamId
     * @param combinationDoctors
     */
    private void updateCombinationDoctor(String mdtTeamId, List<Map<String, MdtTeamDoctor>> combinationDoctors) {
        // 删除之前的关联医生
        this.mdtTeamDoctorMapper.delete(new QueryWrapper<MdtTeamDoctor>().eq("mdt_team_id", mdtTeamId));
        // 重新写入
        addCombinationDoctor(mdtTeamId, combinationDoctors);
    }

    /**
     * 修改团队医生
     *
     * @param mdtTeamId
     * @param consultantDoctors
     * @param assistantDoctor
     */
    private void updateTeamDoctor(String mdtTeamId, List<MdtTeamDoctor> consultantDoctors, MdtTeamDoctor assistantDoctor) {
        // 删除之前的关联医生
        this.mdtTeamDoctorMapper.delete(new QueryWrapper<MdtTeamDoctor>().eq("mdt_team_id", mdtTeamId));
        // 重新写入
        addTeamDoctor(mdtTeamId, consultantDoctors, assistantDoctor);
    }

    /**
     * 增加组合医生
     *
     * @param mdtTeamId
     * @param combinationDoctors
     */
    private void addCombinationDoctor(String mdtTeamId, List<Map<String, MdtTeamDoctor>> combinationDoctors) {
        combinationDoctors.forEach(stringMdtTeamDoctorMap -> {
            MdtTeamDoctor consultantDoctor = stringMdtTeamDoctorMap.get("consultantDoctor");
            consultantDoctor.setMdtTeamId(mdtTeamId);
            consultantDoctor.setDoctorType(UserRoleEnum.CONSULTANT.getCode());
            int insert = this.mdtTeamDoctorMapper.insert(consultantDoctor);
            if (insert == 0) {
                throw new BizException("会诊医生添加失败");
            }
            MdtTeamDoctor assistantDoctor = stringMdtTeamDoctorMap.get("assistantDoctor");
            if (ObjectUtil.isNotEmpty(assistantDoctor) && null != assistantDoctor.getDoctorUserId()) {
                assistantDoctor.setIsMain("0");
                assistantDoctor.setMdtTeamId(mdtTeamId);
                assistantDoctor.setDoctorType(UserRoleEnum.ASSISTANT.getCode());
                assistantDoctor.setConsultantId(consultantDoctor.getId());
                int insert2 = this.mdtTeamDoctorMapper.insert(assistantDoctor);
                if (insert2 == 0) {
                    throw new BizException("助理医生添加失败");
                }
            }
        });
    }

    /**
     * 增加个人医生团队
     *
     * @param mdtTeamId
     * @param consultantDoctor
     * @param assistantDoctor
     */
    private void addPersonalDoctor(String mdtTeamId, MdtTeamDoctor consultantDoctor, MdtTeamDoctor assistantDoctor) {
        consultantDoctor.setMdtTeamId(mdtTeamId);
        consultantDoctor.setDoctorType(UserRoleEnum.CONSULTANT.getCode());
        int insert = this.mdtTeamDoctorMapper.insert(consultantDoctor);
        if (insert == 0) {
            throw new BizException("会诊医生添加失败");
        }
        if (null != assistantDoctor) {
            assistantDoctor.setIsMain("0");
            assistantDoctor.setMdtTeamId(mdtTeamId);
            assistantDoctor.setDoctorType(UserRoleEnum.ASSISTANT.getCode());
            assistantDoctor.setConsultantId(consultantDoctor.getId());
            int insert2 = this.mdtTeamDoctorMapper.insert(assistantDoctor);
            if (insert2 == 0) {
                throw new BizException("助理医生添加失败");
            }
        }
    }

    /**
     * 增加团队医生
     *
     * @param mdtTeamId
     * @param consultantDoctors
     * @param assistantDoctor
     */
    private void addTeamDoctor(String mdtTeamId, List<MdtTeamDoctor> consultantDoctors, MdtTeamDoctor assistantDoctor) {
        MdtTeamDoctor mdtTeamDoctor = new MdtTeamDoctor();
        mdtTeamDoctor.setMdtTeamId(mdtTeamId);
        consultantDoctors.forEach(doctor -> {
            mdtTeamDoctor.setDoctorUserId(doctor.getDoctorUserId());
            mdtTeamDoctor.setDoctorType(UserRoleEnum.CONSULTANT.getCode());
            mdtTeamDoctor.setIsMain(doctor.getIsMain());
            mdtTeamDoctor.setPrice(doctor.getPrice());
            this.mdtTeamDoctorMapper.insert(mdtTeamDoctor);
        });
        if (null != assistantDoctor) {
            assistantDoctor.setIsMain("0");
            assistantDoctor.setMdtTeamId(mdtTeamId);
            assistantDoctor.setDoctorType(UserRoleEnum.ASSISTANT.getCode());
            int insert2 = this.mdtTeamDoctorMapper.insert(assistantDoctor);
            if (insert2 == 0) {
                throw new BizException("助理医生添加失败");
            }
        }
    }

    private List<MdtTeamDetailsVo.mdtDoctor> setDoctors(List<MdtTeamDoctor> consultant) {
        List<MdtTeamDetailsVo.mdtDoctor> list = new ArrayList<>();
        consultant.forEach(s -> {
            MdtTeamDetailsVo.mdtDoctor doctorVo = new MdtTeamDetailsVo.mdtDoctor();
            doctorVo.setDoctorType(s.getDoctorType());
            doctorVo.setIsMain(s.getIsMain());
            doctorVo.setName(s.getDoctorUserName());
            doctorVo.setDoctorUserId(s.getDoctorUserId());
            doctorVo.setPrice(s.getPrice());
            list.add(doctorVo);
        });
        return list;
    }

    private MdtTeamDetailsVo.mdtDoctor setDoctor(MdtTeamDoctor consultant) {
        MdtTeamDetailsVo.mdtDoctor doctorVo = new MdtTeamDetailsVo.mdtDoctor();
        doctorVo.setDoctorType(consultant.getDoctorType());
        doctorVo.setIsMain(consultant.getIsMain());
        doctorVo.setName(consultant.getDoctorUserName());
        doctorVo.setDoctorUserId(consultant.getDoctorUserId());
        doctorVo.setPrice(consultant.getPrice());
        return doctorVo;
    }
}