package com.aihoo.domain.im.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aihoo.common.PageResult;
import com.aihoo.domain.im.model.entity.ImCustomerMsg;
import com.aihoo.domain.im.model.entity.ImMsgCustomerContent;
import com.aihoo.domain.im.model.mapper.ImCustomerMsgMapper;
import com.aihoo.domain.im.model.vo.CustomerVo;
import com.aihoo.domain.im.model.vo.SearchTeamDoctorVo;
import com.aihoo.domain.im.service.CustomerService;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.domain.sys.model.mapper.SysUserMapper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname CustomerServiceImpl
 * @Description hf
 * @Date 2020/11/10 13:19
 * @Created by ad
 */
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private ImCustomerMsgMapper imCustomerMsgMapper;

    // TODO: 跨域依赖（aihoo-domain-patient 待引入），旧代码引用 com.aihoo.admin.system.mapper.PatientUserMapper
    // @Resource
    // private PatientUserMapper patientUserMapper;

    // TODO: 跨域依赖（aihoo-domain-consultation 待创建），旧代码引用 com.aihoo.admin.system.mapper.MdtTeamMapper
    // @Resource
    // private MdtTeamMapper mdtTeamMapper;

    // TODO: 跨域依赖（aihoo-domain-doctor 待引入），旧代码引用 com.aihoo.admin.system.mapper.DoctorUserMapper
    // @Resource
    // private DoctorUserMapper doctorUserMapper;

    // TODO: 跨域依赖（aihoo-domain-consultation 待创建），旧代码引用 com.aihoo.admin.system.mapper.MdtMapper
    // @Resource
    // private MdtMapper mdtMapper;

    // TODO: 跨域依赖（aihoo-domain-consultation 待创建），旧代码引用 com.aihoo.admin.system.mapper.DMdtTagMapper
    // @Resource
    // private DMdtTagMapper dMdtTagMapper;

    private static final String CUSTOMER_IMAGE = "https://internet-hospital-prod.oss-accelerate.aliyuncs.com/admin/2020111111134544dfa87fb49c4c1bbb7f7d0d410e5e0d.png";

    @Override
    public PageResult<SysUser> customerList(Map<String, Object> map) {
        Page<SysUser> setPage = new Page<>(Long.parseLong(map.get("page").toString()), Long.parseLong(map.get("limit").toString()));
        IPage<SysUser> sysUsers = this.sysUserMapper.customerList(setPage, map);
        return new PageResult<>(sysUsers.getRecords(), sysUsers.getTotal());
    }

    @Override
    public PageResult<CustomerVo> customerDetails(Map<String, Object> map) throws ParseException {
        long page = 1;
        long limit = 10;
        if (!StringUtils.isEmpty(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (!StringUtils.isEmpty(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (!StringUtils.isEmpty(map.get("startTime"))) {
            long startTime = format.parse(map.get("startTime").toString()).getTime() / 1000;
            map.put("startTime", startTime);
        }
        if (!StringUtils.isEmpty(map.get("endTime"))) {
            long endTime = format.parse(map.get("endTime").toString()).getTime() / 1000;
            map.put("endTime", endTime);
        }
        Page<String> paging = new Page<>(page, limit);
        IPage<String> customerDetails = this.imCustomerMsgMapper.customerDetails(paging, map);
        List<String> patientIds = customerDetails.getRecords();
        if (CollectionUtils.isEmpty(patientIds)) {
            return new PageResult<>(Lists.newArrayList(), customerDetails.getTotal());
        }
        // TODO: 跨域依赖（aihoo-domain-patient 待引入），此处使用 stub 返回空 list
        // 查询所有患者手机号
        // List<PatientUser> patientUsers = this.patientUserMapper.selectList(new QueryWrapper<PatientUser>().in("id", patientIds));
        List<Object> patientUsers = new ArrayList<>();
        // Map<String, PatientUser> patientUserMap = patientUsers.stream().collect(Collectors.toMap(PatientUser::getId, s -> s));
        // 临时用空 map 占位，待 patientUserMapper 迁移后恢复
        Map<String, Object> patientUserMap = new java.util.HashMap<>();
        List<CustomerVo> list = new ArrayList<>();
        patientIds.forEach(patientId -> {
            CustomerVo customerVo = new CustomerVo();
            // TODO: 跨域依赖，待 patientUser 域迁移后恢复 patientUserMap.get(patientId).getMobile()
            // customerVo.setPatientPhone(patientUserMap.get(patientId).getMobile());
            customerVo.setPatientPhone("");
            // 循环拼接分页的数据
            // 获取聊天客服与当前患者所有的聊天消息
            List<ImCustomerMsg> imCustomerMsgs = this.imCustomerMsgMapper.findStartTime(map.get("adminId").toString(), patientId);
            List<Long> collect = imCustomerMsgs.stream().map(ImCustomerMsg::getMsgTime).map(Long::parseLong).collect(Collectors.toList());
            //当前客服聊天开始时间
            Long start = collect.stream().sorted().collect(Collectors.toList()).get(0);
            customerVo.setStartTime(format.format(start * 1000));
            //当前客服聊天结束时间
            Long end = collect.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()).get(0);
            customerVo.setEndTime(format.format(end * 1000));
            // 客服id
            customerVo.setAdminId(map.get("adminId").toString());
            // 患者id
            customerVo.setPatientId(patientId);
            list.add(customerVo);
        });
        return new PageResult<>(list, customerDetails.getTotal());
    }

    @Override
    public List<ImCustomerMsg> chattingRecords(Map<String, Object> map) {
        // TODO: 跨域依赖（aihoo-domain-patient 待引入），旧代码用 patientUserMapper.selectById(patientId) 取头像
        // PatientUser patientUser = this.patientUserMapper.selectById(map.get("patientId").toString());
        // String headImg = patientUser == null ? "" : patientUser.getHeadImg();
        String headImg = "";
        // 获取聊天客服与当前患者所有的聊天消息
        List<ImCustomerMsg> imCustomerMsgs = this.imCustomerMsgMapper.findStartTime
                (map.get("adminId").toString(), map.get("patientId").toString());
        // 当前帅选时间 内客服与患者的聊天内容 按照聊天时间正序排序
        List<ImCustomerMsg> res = imCustomerMsgs.stream().sorted(Comparator.comparing(s -> Long.parseLong(s.getMsgTime()))).collect(Collectors.toList());
        //拼接前端需要的数据格式
        res.forEach(r -> {
            r.setTime(r.getMsgTime());
            r.setType(r.getPayloads().get(0).getMsgType());
            r.setPayload(r.getPayloads().get(0));
            r.setFrom(r.getFromAccount());
            r.setTo(r.getToAccount());
            if (r.getFromAccount().startsWith("ADMIN_")) {
                r.setAvatar(CUSTOMER_IMAGE);
            } else {
                r.setAvatar(headImg);
            }
            List<ImMsgCustomerContent> payload = r.getPayloads();
            payload.forEach(s -> {
                s.setData(s.getMsgContent());
            });
        });
        return res;
    }


    /**
     * mdt关键字搜索
     * 医生、医院、团队、疾病
     *
     * @param param
     * @return
     */
    @Override
    public List search(Map<String, Object> param) {
        int page = 0;
        int limit = 10;
        try {
            page = ObjectUtil.isEmpty(param.get("page")) ? 0 : Integer.parseInt(param.get("page").toString());
            limit = ObjectUtil.isEmpty(param.get("limit")) ? 10 : Integer.parseInt(param.get("limit").toString());
        } catch (Exception e) {
            log.error("分页条件错误--->page:" + param.get("page") + " limit:" + param.get("limit"));
        }
        // TODO: 跨域依赖（aihoo-domain-consultation 待创建），旧代码使用 mdtTeamMapper.teamList
        // IPage<Map> teamPage = new Page<>(page, limit, false);
        // List<MdtTeam> teamList;
        // if (null == param.get("keyword") || StrUtil.isBlank(param.get("keyword").toString())) {
        //     String mdtId = ObjectUtil.isEmpty(param.get("mdtId")) ? null : param.get("mdtId").toString();
        //     String tagId = ObjectUtil.isEmpty(param.get("tagId")) ? null : param.get("tagId").toString();
        //     teamList = mdtTeamMapper.teamList(mdtId, tagId, teamPage);
        // } else {
        //     String keyword = "%" + param.get("keyword").toString() + "%";
        //     teamList = mdtTeamMapper.teamListByKeyword(keyword, teamPage);
        // }
        // 临时返回空 list，待 mdtTeamMapper 迁移后恢复
        return Lists.newArrayList();

        /* 旧代码保留作迁移参考：
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
        */
    }

    @Override
    public JSONArray mdtList() {
        JSONArray mdtList = new JSONArray();
        // TODO: 跨域依赖（aihoo-domain-consultation 待创建），旧代码使用 mdtMapper + dMdtTagMapper
        // LambdaQueryWrapper<Mdt> mdtLambdaQueryWrapper = new QueryWrapper<Mdt>().lambda();
        // mdtLambdaQueryWrapper.eq(Mdt::getIsDelete, "0").eq(Mdt::getStatus, "1").orderByAsc(Mdt::getIndex);
        // List<Mdt> mdts = mdtMapper.selectList(mdtLambdaQueryWrapper);
        // for (Mdt mdt : mdts) {
        //     JSONObject jsonObject = new JSONObject();
        //     List<DMdtTag> mdtTagList = dMdtTagMapper.selectListByMdt(mdt.getId());
        //     jsonObject.put("tagList", mdtTagList);
        //     jsonObject.put("mdtId", mdt.getId() == null ? "" : mdt.getId());
        //     jsonObject.put("mdtName", mdt.getName() == null ? "" : mdt.getName());
        //     mdtList.add(jsonObject);
        // }
        return mdtList;
    }
}
