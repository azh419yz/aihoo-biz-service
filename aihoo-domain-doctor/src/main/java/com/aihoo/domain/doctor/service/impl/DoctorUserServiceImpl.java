package com.aihoo.domain.doctor.service.impl;

import cn.hutool.http.HttpUtil;
import com.aihoo.common.PageResult;
import com.aihoo.constant.DictTypeEnum;
import com.aihoo.domain.consultation.model.mapper.MdtTeamDoctorMapper;
import com.aihoo.domain.doctor.model.entity.CommonLanguage;
import com.aihoo.domain.doctor.model.entity.DoctorAync;
import com.aihoo.domain.doctor.model.entity.DoctorBalance;
import com.aihoo.domain.doctor.model.entity.DoctorSet;
import com.aihoo.domain.doctor.model.entity.DoctorSetTimes;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.entity.DoctorUserLog;
import com.aihoo.domain.doctor.model.entity.DoctorVisitSet;
import com.aihoo.domain.doctor.model.entity.DoctorWelcomeMessageSet;
import com.aihoo.domain.doctor.model.excel.DoctorEntity;
import com.aihoo.domain.doctor.model.mapper.DoctorAyncMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorBalanceMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorSetMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorUserLogMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.doctor.model.vo.DoctorSetTimeVo;
import com.aihoo.domain.doctor.service.CommonLanguageService;
import com.aihoo.domain.doctor.service.DoctorAyncService;
import com.aihoo.domain.doctor.service.DoctorBalanceService;
import com.aihoo.domain.doctor.service.DoctorSetService;
import com.aihoo.domain.doctor.service.DoctorSetTimesService;
import com.aihoo.domain.doctor.service.DoctorUserLogService;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.doctor.service.DoctorVisitSetService;
import com.aihoo.domain.doctor.service.DoctorWelcomeMessageSetService;
import com.aihoo.domain.hospital.model.entity.Hospital;
import com.aihoo.domain.hospital.model.entity.HospitalDepartment;
import com.aihoo.domain.hospital.model.mapper.HospitalDepartmentMapper;
import com.aihoo.domain.hospital.model.mapper.HospitalMapper;
import com.aihoo.domain.hospital.service.DepartmentService;
import com.aihoo.domain.sys.model.entity.DicPractitioner;
import com.aihoo.domain.sys.model.entity.Dict;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.domain.sys.model.entity.TBase;
import com.aihoo.domain.sys.model.mapper.DiceMapper;
import com.aihoo.domain.sys.model.mapper.DicPractitionerMapper;
import com.aihoo.domain.sys.model.mapper.SysUserMapper;
import com.aihoo.domain.sys.service.DiceService;
import com.aihoo.domain.sys.service.DicPractitionerService;
import com.aihoo.domain.sys.service.TBaseService;
import com.aihoo.domain.sys.util.LoginRecordUtil;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.properties.CaProperties;
import com.aihoo.redis.RedisConstant;
import com.aihoo.redis.RedisService;
import com.aihoo.util.IdUtils;
import com.aihoo.util.IdentityCardUtils;
import com.aihoo.util.SecurityUtil;
import com.aihoo.util.SecurityUtils;
import com.aihoo.util.StringUtil;
import com.aihoo.util.UserAgentGetter;
import com.aihoo.domain.hospital.constant.ReportCodingEnum;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Classname DoctorUserServiceImpl
 * @Description hf
 * @Date 2020/9/16 10:45
 * @Created by ad
 */
@Service
@Slf4j
public class DoctorUserServiceImpl extends ServiceImpl<DoctorUserMapper, DoctorUser> implements DoctorUserService {
    @Autowired
    private RedisService redisService;

    @Resource
    private DoctorUserMapper doctorUserMapper;
    @Resource
    private MdtTeamDoctorMapper mdtTeamDoctorMapper;

    @Resource
    private HospitalDepartmentMapper hospitalDepartmentMapper;

    @Resource
    private DoctorAyncMapper doctorAyncMapper;


    @Resource
    private DoctorBalanceMapper doctorBalanceMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private DiceMapper diceMapper;

    @Resource
    private HospitalMapper hospitalMapper;

    @Resource
    private DoctorSetService doctorSetService;
    @Resource
    private DoctorSetMapper doctorSetMapper;

    @Resource
    private DoctorBalanceService doctorBalanceService;

    @Resource
    private DoctorAyncService doctorAyncService;

    @Resource
    private DoctorUserLogService doctorUserLogService;
    @Resource
    private CommonLanguageService commonLanguageService;

    @Resource
    private TBaseService tBaseService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private DiceService diceService;

    @Resource
    private DoctorUserLogMapper doctorUserLogMapper;

    @Resource
    private DicPractitionerMapper dicPractitionerMapper;

    @Resource
    private DicPractitionerService dicPractitionerService;
    @Resource
    private LoginRecordUtil loginRecordUtil;
    @Resource
    private DoctorSetTimesService doctorSetTimesService;
    @Autowired
    private CaProperties caProperties;
    @Autowired
    private DoctorVisitSetService doctorVisitSetService;
    @Autowired
    private DoctorWelcomeMessageSetService doctorWelcomeMessageSetService;

    @Override
    public List<DoctorUser> findDoctorUserAll() {
        QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        wrapper.eq("is_cancel", 0);
        return doctorUserMapper.selectList(wrapper);
    }

    @Override
    public DoctorUser findDoctorUserById(String otherId) {
        return doctorUserMapper.selectById(otherId);
    }

    @Override
    public List<DoctorUser> findDoctorUserByIds(List<String> updateIds, String hospitalId) {
        return doctorUserMapper.findDoctorUserByIds(updateIds, hospitalId);
    }

    @Override
    public PageResult<DoctorUser> list(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        Page<DoctorUser> setPage = new Page<>(page, limit);
        //列表排序规则 index 没有值排在后面， 已经认证通过的排在前面，然后按照index排列
        IPage<DoctorUser> iPage = this.doctorUserMapper.selectDoctorUserPage(setPage, map);
        List<DoctorUser> doctorUserList = iPage.getRecords();
        if (CollectionUtils.isEmpty(doctorUserList)) {
            return new PageResult<>(iPage.getRecords(), iPage.getTotal());
        }
        List<String> doctorUserIds = doctorUserList.stream().map(DoctorUser::getId).collect(Collectors.toList());

        List<DoctorAync> doctorAyncs = this.doctorAyncMapper.findDoctorAyncByUserIds(doctorUserIds);
        if (CollectionUtils.isEmpty(doctorAyncs)) {
            return new PageResult<>("根据医生的id查询不到对应的接诊统计");
        }
        List<DoctorSet> doctorSets = this.doctorSetMapper.selectList(new QueryWrapper<DoctorSet>().in("doctor_user_id", doctorUserIds));
        if (CollectionUtils.isEmpty(doctorAyncs)) {
            return new PageResult<>("根据医生的id查询不到对应的医生设置");
        }
        NumberFormat df = NumberFormat.getPercentInstance();
        df.setMaximumFractionDigits(0);

        Map<String, DoctorAync> doctorAyncMap = doctorAyncs.stream().collect(Collectors.toMap(DoctorAync::getDoctorUserId, s -> s));
        Map<String, DoctorSet> doctorSetMap = doctorSets.stream().collect(Collectors.toMap(DoctorSet::getDoctorUserId, s -> s));
        doctorUserList.forEach(s -> {
            s.setOrderNumber(doctorAyncMap.get(s.getId()).getOrderNumber());
            s.setHighOpinion(df.format(Double.parseDouble(doctorAyncMap.get(s.getId()).getHighOpinion())));
            s.setRealHighOpinion(df.format(Double.parseDouble(doctorAyncMap.get(s.getId()).getRealHighOpinion())));
            s.setIsImg(doctorSetMap.get(s.getId()).getIsImg());
            s.setIsVideo(doctorSetMap.get(s.getId()).getIsVideo());
            s.setIsVoice(doctorSetMap.get(s.getId()).getIsVoice());
            s.setIsRevisit(doctorSetMap.get(s.getId()).getIsRevisit());
            s.setIsMdt(doctorSetMap.get(s.getId()).getIsMdt());
        });
        return new PageResult<>(doctorUserList, iPage.getTotal());
    }


    @Override
    public Boolean enableDisable(Map<String, Object> map, HttpServletRequest request) {
        DoctorUser doctorUser = new DoctorUser();
        doctorUser.setId(map.get("id").toString());
        doctorUser.setStatus(map.get("status").toString());
        int i = this.doctorUserMapper.updateById(doctorUser);
        loginRecordUtil.saveLoginRecord(request, "医生启用与禁用");
        if ("0".equals(map.get("status").toString())) {
            String accessToken = this.doctorUserMapper.selectById(map.get("id").toString()).getToken();
            String redisKey = RedisConstant.DOCKER_LOGIN_KEY + accessToken;
            redisService.remove(redisKey);
        }
        return i > 0;
    }

    @Override
    public DoctorUser doctorDetails(String id) {
        DoctorUser doctorUser = this.doctorUserMapper.findDoctorDetailsById(id);
        DoctorAync doctorAync = this.doctorAyncMapper.selectOne
                (new QueryWrapper<DoctorAync>().eq("doctor_user_id", id));
        NumberFormat df = NumberFormat.getPercentInstance();
        df.setMaximumFractionDigits(0);
        if (StringUtils.isEmpty(doctorAync)) {
            throw new BizException("没有查询到id对应的接诊设置信息  id：" + id);
        } else {
            doctorUser.setOrderNumber(doctorAync.getOrderNumber());
            doctorUser.setHighOpinion(df.format(Double.parseDouble(doctorAync.getHighOpinion())));
            doctorUser.setRealHighOpinion(df.format(Double.parseDouble(doctorAync.getRealHighOpinion())));
        }
        DoctorBalance doctorBalance = doctorBalanceMapper.selectOne(new QueryWrapper<DoctorBalance>().eq("doctor_user_id", id));
        if (StringUtils.isEmpty(doctorBalance)) {
            throw new BizException("根据医生id没有查询到余额额表信息 id ： " + id);
        }
        doctorUser.setRevisitBill(doctorBalance.getRevisitAmount());
        doctorUser.setVisitBill(doctorBalance.getVisitAmount());
        BigDecimal revisitAmountBill = new BigDecimal(doctorBalance.getRevisitAmount());
        BigDecimal visitAmountBill = new BigDecimal(doctorBalance.getVisitAmount());
        doctorUser.setTotalBill(revisitAmountBill.add(visitAmountBill).toString());
        return doctorUser;
    }

    @Override
    public JSONArray hospitalDepartmentAll(String hospitalId) {
        JSONArray jsonArray = new JSONArray();
        QueryWrapper<HospitalDepartment> wrapper = new QueryWrapper<>();
        wrapper.eq("hospital_id", hospitalId);
        List<HospitalDepartment> list = this.hospitalDepartmentMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return jsonArray;
        }
        list.forEach(s -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", s.getId());
            jsonObject.put("departCode", s.getDepartCode());
            jsonObject.put("departName", s.getDepartName());
            jsonArray.add(jsonObject);
        });
        return jsonArray;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doctorBulkExport(String memberNum, String name, String hospitalName, String departName,
                                 HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
        if (null != memberNum && !"".equals(memberNum)) {
            wrapper.eq("member_num", memberNum);
        }
        if (null != name && !"".equals(name)) {
            wrapper.like("name", name);
        }
        if (null != hospitalName && !"".equals(hospitalName)) {
            wrapper.like("hospital_name", hospitalName);
        }
        if (null != departName && !"".equals(departName)) {
            wrapper.like("depart_name", departName);
        }
        wrapper.eq("is_cancel", 0);
        List<DoctorUser> doctorUsers = this.doctorUserMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(doctorUsers)) {
            throw new BizException("当前条件下没有医生数据");
        }
        List<String> userIds = doctorUsers.stream().map(DoctorUser::getCreateUserId).collect(Collectors.toList());
        //无论是否禁用都应该可以查出来，关联关系
        List<SysUser> sysUsers = this.sysUserMapper.selectBatchIds(userIds);
        if (CollectionUtils.isEmpty(doctorUsers)) {
            throw new BizException("根据用户的id没有查询系统用户的信息 ：" + userIds);
        }
        // 用户信息准备
        Map<String, SysUser> userMap = sysUsers.stream().collect(Collectors.toMap(SysUser::getId, s -> s));

        // 接诊设置信息
        List<String> doctorUserIds = doctorUsers.stream().map(DoctorUser::getId).collect(Collectors.toList());
        List<DoctorSet> doctorSets = this.doctorSetMapper.selectList(new QueryWrapper<DoctorSet>().in("doctor_user_id", doctorUserIds));
        Map<String, DoctorSet> doctorSetMap = doctorSets.stream().collect(Collectors.toMap(DoctorSet::getDoctorUserId, s -> s));

        List<DoctorEntity> doctorUserEntities = new ArrayList<>();
        doctorUsers.forEach(s -> {
            DoctorEntity doctorUserEntity = new DoctorEntity();
            if ("0".equals(s.getStatus())) {
                s.setStatus("停用");
            } else {
                s.setStatus("启用");
            }
            if ("1".equals(s.getSex())) {
                s.setSex("男");
            } else {
                s.setSex("女");
            }
            switch (s.getIsAuth()) {
                case "NONE":
                    s.setIsAuth("未认证");
                    break;
                case "WAIT":
                    s.setIsAuth("认证中");
                    break;
                case "PASS":
                    s.setIsAuth("已认证");
                    break;
                case "REJECT":
                    s.setIsAuth("认证失败");
                    break;
                default:
                    s.setIsAuth("未认证");
            }
            BeanUtils.copyProperties(s, doctorUserEntity);
            if ("ASSISTANT".equals(doctorUserEntity.getDoctorType())) {
                doctorUserEntity.setDoctorType("助理医生");
            } else {
                doctorUserEntity.setDoctorType("会诊医生");
            }
            /*doctorUserEntity.setCreateUserName(userMap.get(s.getCreateUserId()).getUserName());*/
            if (doctorSetMap.containsKey(s.getId())) {
                BeanUtils.copyProperties(doctorSetMap.get(s.getId()), doctorUserEntity);
                if ("1".equals(doctorUserEntity.getIsImg())) {
                    doctorUserEntity.setIsImg("开启");
                } else {
                    doctorUserEntity.setIsImg("未开");
                }
                if ("1".equals(doctorUserEntity.getIsRevisit())) {
                    doctorUserEntity.setIsRevisit("开启");
                } else {
                    doctorUserEntity.setIsRevisit("未开");
                }
                if ("1".equals(doctorUserEntity.getIsMdt())) {
                    doctorUserEntity.setIsMdt("开启");
                } else {
                    doctorUserEntity.setIsMdt("未开");
                }
                /*if ("1".equals(doctorUserEntity.getIsVideo())) {
                    doctorUserEntity.setIsVideo("开启");
                } else {
                    doctorUserEntity.setIsVideo("未开");
                }
                if ("1".equals(doctorUserEntity.getIsVoice())) {
                    doctorUserEntity.setIsVoice("开启");
                } else {
                    doctorUserEntity.setIsVoice("未开");
                }*/
            }
            doctorUserEntities.add(doctorUserEntity);
        });
        ExcelUtils.writeExcel(request, response, doctorUserEntities, DoctorEntity.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + "医生表格.xlsx");
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                loginRecordUtil.saveLoginRecord(request, "医生批量导出");
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONArray doctorBulkImport(List<DoctorEntity> doctorEntities, HttpServletRequest request) throws Exception {

        JSONArray res = new JSONArray();
        //查询本系统存在的所有医院对应的code
        List<Hospital> hospitals = this.hospitalMapper.selectList(new QueryWrapper<Hospital>().eq("status", 1).eq("is_delete", 0));
        if (CollectionUtils.isEmpty(hospitals)) {
            res.add("系统还未生成医院数据");
            return res;
        }
        Map<String, String> hospitalMap = hospitals.stream().collect(Collectors.toMap(Hospital::getHosName, Hospital::getId));
        // 查询本系统医院对应的存在的科室
        List<String> hospitalNames = doctorEntities.stream().map(DoctorEntity::getHospitalName).distinct().collect(Collectors.toList());
        List<Hospital> hosName = this.hospitalMapper.selectList(new QueryWrapper<Hospital>().in("hos_name", hospitalNames).eq("status", 1).eq("is_delete", 0));
        if (CollectionUtils.isEmpty(hosName)) {
            res.add("根据输入的医院名称，未查询到任意一个医院");
            return res;
        }
        List<String> hospitalIds = hosName.stream().map(Hospital::getId).collect(Collectors.toList());
        List<HospitalDepartment> departments = this.hospitalDepartmentMapper.selectList(new QueryWrapper<HospitalDepartment>().in("hospital_id", hospitalIds));
        if (CollectionUtils.isEmpty(departments)) {
            res.add("系统还未生成科室数据");
            return res;
        }
        Map<String, HospitalDepartment> departmentMap = departments.stream().collect(Collectors.toMap(HospitalDepartment::getDepartName, s -> s));


        //验证 code和name是否对应
        List<Dict> papersDicts = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", DictTypeEnum.PAPERS.getType()));
        Map<String, String> papersMap = papersDicts.stream().collect(Collectors.toMap(Dict::getName, Dict::getCode));

        List<Dict> doctTitleDict = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", DictTypeEnum.DOCT_TITLE.getType()));
        Map<String, String> doctTitleMap = doctTitleDict.stream().collect(Collectors.toMap(Dict::getName, Dict::getCode));

        List<Dict> rylbDict = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", DictTypeEnum.RYLB.getType()));
        Map<String, String> rylbMap = rylbDict.stream().collect(Collectors.toMap(Dict::getName, Dict::getCode));

        List<Dict> positionDict = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", DictTypeEnum.POSITION.getType()));
        Map<String, String> positionMap = positionDict.stream().collect(Collectors.toMap(Dict::getName, Dict::getCode));

        //本次提交是否有错标记
        boolean flag = false;
        for (int i = 0; i < doctorEntities.size(); i++) {
            DoctorEntity doctorEntity = doctorEntities.get(i);
            if (StringUtils.isEmpty(doctorEntity.getMobile())) {
                res.add("手机号：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            } else if (11 != doctorEntity.getMobile().length()) {
                res.add("手机号：" + "第 " + (i + 1) + " 行格式错误");
                flag = true;
            }
            if (StringUtils.isEmpty(doctorEntity.getName())) {
                res.add("姓名：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            }
            if (StringUtils.isEmpty(doctorEntity.getPapersName())) {
                res.add("证件名称：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            } else {
                if (!papersMap.containsKey(doctorEntity.getPapersName())) {
                    res.add("证件名称：" + "第 " + (i + 1) + " 行是系统不存在的证件");
                    flag = true;
                } else {
                    if (StringUtils.isEmpty(doctorEntity.getPapersNumbers())) {
                        res.add("证件号码：" + "第 " + (i + 1) + " 行为空");
                        flag = true;
                    } else {
                        if (!IdentityCardUtils.isIdCard(doctorEntity.getPapersNumbers())) {
                            res.add("证件号码：" + "第 " + (i + 1) + " 行证件号码格式错误");
                            flag = true;
                        }
                        doctorEntity.setPapersCode(papersMap.get(doctorEntity.getPapersName()));
                        Map<String, String> message = IdentityCardUtils.getCarMessage(doctorEntity.getPapersNumbers());
                        doctorEntity.setSex(message.get("sex"));
                        doctorEntity.setAge(message.get("age"));
                        doctorEntity.setBirthday(message.get("birthday"));
                    }
                }
            }
            /*if (StringUtils.isEmpty(doctorEntity.getMemberNum())) {
                res.add("工号：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            }*/
            if (StringUtils.isEmpty(doctorEntity.getHospitalName())) {
                res.add("医院名称：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            } else {
                if (!hospitalMap.containsKey(doctorEntity.getHospitalName())) {
                    res.add("医院名称：" + "第 " + (i + 1) + " 行在系统不存在");
                    flag = true;
                } else {
                    doctorEntity.setHospitalId(hospitalMap.get(doctorEntity.getHospitalName()));
                }
            }
            if (StringUtils.isEmpty(doctorEntity.getDepartName())) {
                res.add("科室名称：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            } else {
                if (!departmentMap.containsKey(doctorEntity.getDepartName())) {
                    res.add("科室名称：" + "第 " + (i + 1) + " 行该编码在系统不存在");
                    flag = true;
                } else {
                    doctorEntity.setDepartCode(departmentMap.get(doctorEntity.getDepartName()).getDepartCode());
                    doctorEntity.setDepartId(departmentMap.get(doctorEntity.getDepartName()).getId());
                }
            }
            if (StringUtils.isEmpty(doctorEntity.getOfficeHolderName())) {
                res.add("职称：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            } else {
                if (!doctTitleMap.containsKey(doctorEntity.getOfficeHolderName())) {
                    res.add("职称：" + "第 " + (i + 1) + " 行在系统不存在");
                    flag = true;
                } else {
                    doctorEntity.setOfficeHolderCode(doctTitleMap.get(doctorEntity.getOfficeHolderName()));
                }
            }
            if (StringUtils.isEmpty(doctorEntity.getBeGoodAtText())) {
                res.add("擅长：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            }
            if (StringUtils.isEmpty(doctorEntity.getIntroductionText())) {
                res.add("简介：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            }
            if (StringUtils.isEmpty(doctorEntity.getPersonTypeName())) {
                res.add("人员类别：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            } else {
                if (!rylbMap.containsKey(doctorEntity.getPersonTypeName())) {
                    res.add("人员类别：" + "第 " + (i + 1) + " 行在系统不存在");
                    flag = true;
                } else {
                    doctorEntity.setPersonTypeCode(rylbMap.get(doctorEntity.getPersonTypeName()));
                }
            }
            if (StringUtils.isEmpty(doctorEntity.getPositionName())) {
                res.add("职务：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            } else {
                if (!positionMap.containsKey(doctorEntity.getPositionName())) {
                    res.add("职务：" + "第 " + (i + 1) + " 在系统不存在");
                    flag = true;
                } else {
                    doctorEntity.setPositionCode(positionMap.get(doctorEntity.getPositionName()));
                }
            }
            List<String> codes = Stream.of("是", "否").collect(Collectors.toList());
            if (!StringUtils.isEmpty(doctorEntity.getIsImg())) {
                if (!codes.contains(doctorEntity.getIsImg())) {
                    res.add("是否开启专家咨询：" + "第 " + (i + 1) + " 行描述错误 例：【是，否】");
                    flag = true;
                } else {
                    if ("是".equals(doctorEntity.getIsImg())) {
                        if (StringUtils.isEmpty(doctorEntity.getImgPrice())) {
                            res.add("专家咨询价格：" + "第 " + (i + 1) + " 行不能为空（开启时）");
                            flag = true;
                        }
                        doctorEntity.setIsImg("1");
                    } else {
                        doctorEntity.setIsImg("0");
                        doctorEntity.setImgPrice(null);
                    }
                }
            } else {
                doctorEntity.setIsImg(null);
                doctorEntity.setImgPrice(null);
            }
            /*if (!StringUtils.isEmpty(doctorEntity.getIsVideo())) {
                if (!codes.contains(doctorEntity.getIsVideo())) {
                    res.add("视频问诊开启关闭：" + "第 " + (i + 1) + " 行描述错误 例：【是，否】");
                    flag = true;
                } else {
                    if ("是".equals(doctorEntity.getIsVideo())) {
                        if (StringUtils.isEmpty(doctorEntity.getVideoPrice())) {
                            res.add("视频问诊价格：" + "第 " + (i + 1) + " 行不能为空（开启时）");
                            flag = true;
                        }
                        doctorEntity.setIsVideo("1");
                    } else {
                        doctorEntity.setIsVideo("0");
                        doctorEntity.setVideoPrice(null);
                    }
                }
            } else {
                doctorEntity.setIsVideo(null);
                doctorEntity.setVideoPrice(null);
            }*/
            /*if (!StringUtils.isEmpty(doctorEntity.getIsVoice())) {
                if (!codes.contains(doctorEntity.getIsVoice())) {
                    res.add("语音问诊开启关闭：" + "第 " + (i + 1) + " 行描述错误 例：【是，否】");
                    flag = true;
                } else {
                    if ("是".equals(doctorEntity.getIsVoice())) {
                        if (StringUtils.isEmpty(doctorEntity.getVoicePrice())) {
                            res.add("语音问诊价格：" + "第 " + (i + 1) + " 行不能为空（开启时）");
                            flag = true;
                        }
                        doctorEntity.setIsVoice("1");
                    } else {
                        doctorEntity.setIsVoice("0");
                        doctorEntity.setVoicePrice(null);
                    }
                }
            } else {
                doctorEntity.setIsVoice(null);
                doctorEntity.setVoicePrice(null);
            }*/
            if (StringUtils.isEmpty(doctorEntity.getIsRevisit())) {
                doctorEntity.setIsRevisit(null);
            }
            /*if (!("1".equals(doctorEntity.getIsImg()) || "1".equals(doctorEntity.getIsVideo())
                    || "1".equals(doctorEntity.getIsRevisit()) || "1".equals(doctorEntity.getIsVoice()))){
                res.add("错误提示：" + "第 " + (i + 1) + "医生设置必填一个");
                flag = true;
            }*/
            if (StringUtils.isEmpty(doctorEntity.getHeadImg())) {
                doctorEntity.setHeadImg(null);
            }
            if (StringUtils.isEmpty(doctorEntity.getTag())) {
                doctorEntity.setTag(null);
            }
            if (StringUtils.isEmpty(doctorEntity.getAchievement())) {
                doctorEntity.setAchievement(null);
            }

        }
        if (flag) {
            // 有错误直接返回错误提示
            return res;
        } else {
            //接诊设置
            List<DoctorSet> doctorSets = new ArrayList<>();
            //余额表 初始化
            List<DoctorBalance> doctorBalances = new ArrayList<>();
            //接诊统计初始化
            List<DoctorAync> doctorAyncs = new ArrayList<>();
            //日志
            List<DoctorUserLog> doctorUserLogs = new ArrayList<>();
            // 常用语
            List<CommonLanguage> commonLanguages = new ArrayList<>();
            // 医生报表数据
            List<DicPractitioner> dicPractitioners = new ArrayList<>();

            String userId = String.valueOf(SecurityUtils.getLoginUserId());

            // 判断手机号是否已存在
            List<String> mobiles = doctorEntities.stream().map(DoctorEntity::getMobile).collect(Collectors.toList());
            List<DoctorUser> doctorUsers = this.doctorUserMapper.selectList(new QueryWrapper<DoctorUser>().in("mobile", mobiles).eq("is_cancel", 0));
            if (!CollectionUtils.isEmpty(doctorUsers)) {
                //不为空说明 有手机号已经注册了
                res.add("存在已注册的手机号： " + doctorUsers.stream().map(DoctorUser::getMobile).collect(Collectors.toList()));
                return res;
            }
           /* List<String> memberNums = doctorEntities.stream().map(DoctorEntity::getMemberNum).collect(Collectors.toList());
            List<DoctorUser> doctorUsers1 = this.doctorUserMapper.selectList(new QueryWrapper<DoctorUser>().in("member_num", memberNums).eq("is_cancel", 0));
            if (!CollectionUtils.isEmpty(doctorUsers1)){
                //不为空说明 有重复的工号
                res.add("已存在的工号： " + doctorUsers1.stream().map(DoctorUser::getMemberNum).collect(Collectors.toList()));
                return res;
            }*/
            List<String> papersNumbers = doctorEntities.stream().map(DoctorEntity::getPapersNumbers).collect(Collectors.toList());
            List<DoctorUser> doctorUsers2 = this.doctorUserMapper.selectList(new QueryWrapper<DoctorUser>().in("papers_numbers", papersNumbers).eq("is_cancel", 0));
            if (!CollectionUtils.isEmpty(doctorUsers2)) {
                //不为空说明有重复的身份证号码
                res.add("已存在的身份证号： " + doctorUsers2.stream().map(DoctorUser::getPapersNumbers).collect(Collectors.toList()));
                return res;
            }

            UserAgentGetter userAgentGetter = new UserAgentGetter(request);
            String os = userAgentGetter.getOS();
            String ipAddr = userAgentGetter.getIpAddr();
            List<TBase> tBases = this.tBaseService.list(new QueryWrapper<TBase>().eq("`key`", "USEFUL_EXPRESSIONS"));
            doctorEntities.forEach(s -> {
                DoctorUser doctorUser = new DoctorUser();
                BeanUtils.copyProperties(s, doctorUser);
                doctorUser.setCreateUserId(userId);
                doctorUser.setCreateTime(null);
                doctorUser.setUpdateTime(null);
                doctorUser.setStatus(null);
                doctorUser.setIsAuth(null);
                this.doctorUserMapper.insert(doctorUser);
                //插入系统规则的编码
                DoctorUser doctorUserExist = new DoctorUser();
                doctorUserExist.setId(doctorUser.getId());
                doctorUserExist.setMemberNum(IdUtils.getDoctorID(Integer.valueOf(doctorUser.getId())));
                this.doctorUserMapper.updateById(doctorUserExist);
                DoctorSet doctorSet = new DoctorSet();
                doctorSet.setDoctorUserId(doctorUser.getId());
                doctorSet.setIsImg(s.getIsImg());
                doctorSet.setImgPrice(s.getImgPrice());
               /* doctorSet.setIsVideo(s.getIsVideo());
                doctorSet.setIsVoice(s.getIsVoice());*/
                /*doctorSet.setIsRevisit(s.getIsRevisit());*/
                /*doctorSet.setVideoPrice(s.getVideoPrice());
                doctorSet.setVoicePrice(s.getVoicePrice());*/
                doctorSets.add(doctorSet);
                DoctorBalance doctorBalance = new DoctorBalance();
                doctorBalance.setDoctorUserId(doctorUser.getId());
                doctorBalances.add(doctorBalance);
                DoctorAync doctorAync = new DoctorAync();
                doctorAync.setDoctorUserId(doctorUser.getId());
                doctorAyncs.add(doctorAync);
                DoctorUserLog doctorUserLog = new DoctorUserLog();
                doctorUserLog.setDoctorUserId(doctorUser.getId());
                doctorUserLog.setActionType("REGISTER");
                doctorUserLog.setOsName(os);
                doctorUserLog.setIpAddress(ipAddr);
                doctorUserLog.setRemark("操作人id：" + userId + "新增医生成功，手机号为：" + s.getMobile());
                doctorUserLogs.add(doctorUserLog);
                // 医生上报数据插入
                DicPractitioner dicPractitioner = new DicPractitioner();
                dicPractitioner.setDoctorUserId(doctorUser.getId());
                dicPractitioner.setYljgdm(ReportCodingEnum.YLJGDM);
                dicPractitioner.setGh(doctorUserExist.getMemberNum());
                dicPractitioner.setSsks(doctorUser.getDepartCode());
                dicPractitioner.setWsjgdm(ReportCodingEnum.WSJGDM);
                dicPractitioner.setXm(doctorUser.getName());
                dicPractitioner.setZjlx(doctorUser.getPapersCode());
                dicPractitioner.setZjhm(doctorUser.getPapersNumbers());
                dicPractitioner.setZwdm(doctorUser.getPositionCode());
                dicPractitioner.setZcdm(doctorUser.getOfficeHolderCode());
                dicPractitioner.setLb(doctorUser.getPersonTypeCode());
                dicPractitioner.setXgbz(ReportCodingEnum.XGBZ);
                dicPractitioners.add(dicPractitioner);
                if (!CollectionUtils.isEmpty(tBases)) {
                    tBases.forEach(base -> {
                        CommonLanguage commonLanguage = new CommonLanguage();
                        commonLanguage.setContent(base.getContent());
                        commonLanguage.setDoctorId(doctorUser.getId());
                        commonLanguage.setType("DOCKER");
                        commonLanguage.setIndex(base.getIndex());
                        commonLanguages.add(commonLanguage);
                    });
                }
            });
            //接诊设置
            this.doctorSetService.saveBatch(doctorSets);
            //创建医生钱包数据
            this.doctorBalanceService.saveBatch(doctorBalances);
            //创建医生接诊量统计
            this.doctorAyncService.saveBatch(doctorAyncs);
            //医生创建日志
            this.doctorUserLogService.saveBatch(doctorUserLogs);
            //医生常用语创建
            if (!CollectionUtils.isEmpty(commonLanguages)) {
                this.commonLanguageService.saveBatch(commonLanguages);
            }
            // 医生数据批量写入报表
            this.dicPractitionerService.saveBatch(dicPractitioners);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    loginRecordUtil.saveLoginRecord(request, "医生批量导入");
                }
            });
        }
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doctorUserAdd(Map<String, Object> map, HttpServletRequest request) throws Exception {
        BizException exception = new BizException();
        //参数校验和赋值
        parameterCalibration(map, exception);
        DoctorSet doctorSet = new DoctorSet();
        doctorSetParameterCalibration(map, exception, doctorSet);
        // 医生表insert 数据填充
        DoctorUser doctorUser = new DoctorUser();
        doctorUserParameterCalibration(map, exception, doctorUser);
        // 插入时存在index字段时，已存在的index加一
        List<Integer> ids = null;
        if (null != map.get("index") && !"".equals(map.get("index").toString())) {
            ids = this.doctorUserMapper.selectDoctorIdByIndex(map.get("index").toString());
        }
        //医生数据插入
        this.doctorUserMapper.insert(doctorUser);
        // 更新符合条件的index
        if (!CollectionUtils.isEmpty(ids)) {
            this.doctorUserMapper.updateDoctorIdByIndex(ids);
        }
        // 写入相应规则的工号
        DoctorUser doctorUserNum = new DoctorUser();
        doctorUserNum.setId(doctorUser.getId());
        doctorUserNum.setMemberNum(IdUtils.getDoctorID(Integer.valueOf(doctorUser.getId())));
        this.doctorUserMapper.updateById(doctorUserNum);
        // 主表插入回写id之后保存关联表
        doctorSet.setDoctorUserId(doctorUser.getId());
        this.doctorSetService.save(doctorSet);
        // 当复诊开启时写入复诊设置数据
        if ("1".equals(map.get("isRevisit").toString())) {
            List<DoctorSetTimeVo> data = JSONArray.parseArray
                    (JSON.toJSON(map.get("doctorSetTimes")).toString(), DoctorSetTimeVo.class);
            if (CollectionUtils.isEmpty(data)) {
                exception.setMessage("开启复诊时，复诊时间必须设置！");
                throw exception;
            }
            String verify = verifyDoctorSetTime(data);
            if (null != verify) {
                exception.setMessage(verify);
                throw exception;
            }
            this.doctorSetTimesService.addAcceptsTime(data, doctorUser.getId(), "REVISIT");
        }
        //创建医生钱包数据
        DoctorBalance doctorBalance = new DoctorBalance();
        doctorBalance.setDoctorUserId(doctorUser.getId());
        this.doctorBalanceService.save(doctorBalance);
        //创建医生接诊量统计
        DoctorAync doctorAync = new DoctorAync();
        doctorAync.setDoctorUserId(doctorUser.getId());
        this.doctorAyncMapper.insert(doctorAync);
        //医生创建日志
        UserAgentGetter userAgentGetter = new UserAgentGetter(request);
        DoctorUserLog log = new DoctorUserLog();
        log.setActionType("REGISTER");
        log.setDoctorUserId(doctorUser.getId());
        log.setOsName(userAgentGetter.getOS());
        log.setIpAddress(userAgentGetter.getIpAddr());
        log.setRemark("操作人id：" + Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString() + "新增医生成功，手机号为：" + map.get("mobile").toString().trim());
        this.doctorUserLogService.save(log);
        // 医生常用语创建
        List<TBase> tBases = this.tBaseService.list(new QueryWrapper<TBase>().eq("`key`", "USEFUL_EXPRESSIONS"));
        if (!CollectionUtils.isEmpty(tBases)) {
            List<CommonLanguage> commonLanguages = new ArrayList<>();
            tBases.forEach(base -> {
                CommonLanguage commonLanguage = new CommonLanguage();
                commonLanguage.setContent(base.getContent());
                commonLanguage.setDoctorId(doctorUser.getId());
                commonLanguage.setType("DOCKER");
                commonLanguage.setIndex(base.getIndex());
                commonLanguages.add(commonLanguage);
            });
            this.commonLanguageService.saveBatch(commonLanguages);
        }
        // 医生上报数据插入
        DicPractitioner dicPractitioner = new DicPractitioner();
        dicPractitioner.setDoctorUserId(doctorUser.getId());
        dicPractitioner.setYljgdm(ReportCodingEnum.YLJGDM);
        dicPractitioner.setGh(doctorUserNum.getMemberNum());
        dicPractitioner.setSsks(doctorUser.getDepartCode());
        dicPractitioner.setWsjgdm(ReportCodingEnum.WSJGDM);
        dicPractitioner.setXm(doctorUser.getName());
        dicPractitioner.setZjlx(doctorUser.getPapersCode());
        dicPractitioner.setZjhm(doctorUser.getPapersNumbers());
        dicPractitioner.setZwdm(doctorUser.getPositionCode());
        dicPractitioner.setZcdm(doctorUser.getOfficeHolderCode());
        dicPractitioner.setLb(doctorUser.getPersonTypeCode());
        dicPractitioner.setXgbz(ReportCodingEnum.XGBZ);
        this.dicPractitionerService.save(dicPractitioner);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                loginRecordUtil.saveLoginRecord(request, "医生新增");
            }
        });
        // 问诊设置
        DoctorVisitSet doctorVisitSet = new DoctorVisitSet();
        doctorVisitSet.setDoctorUserId(doctorUser.getId());
        doctorVisitSetService.save(doctorVisitSet);
        // 欢迎语设置
        DoctorWelcomeMessageSet welcomeMessageSet = new DoctorWelcomeMessageSet();
        welcomeMessageSet.setDoctorUserId(doctorUser.getId());
        doctorWelcomeMessageSetService.save(welcomeMessageSet);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doctorUpdate(Map<String, Object> map, HttpServletRequest request) throws Exception {
        BizException exception = new BizException();
        if (null == map.get("id") || "".equals(map.get("id"))) {
            exception.setMessage("请携带医生id");
            throw exception;
        }
        DoctorUser doctorUser = new DoctorUser();
        verifyDoctorUpdate(map, exception, doctorUser);
        // 更新校验后的医生数据
        String index = this.doctorUserMapper.selectById(doctorUser.getId()).getIndex();
        this.doctorUserMapper.updateById(doctorUser);
        // 当更新了医生的排序字段，该序号后面的自动加一
        if (null != map.get("index") && !"".equals(map.get("index").toString())) {
            if (!(null != index && index.equals(map.get("index").toString()))) {
                List<Integer> ids = this.doctorUserMapper.selectDoctorIdByIndex(map.get("index").toString());
                ids.remove(Integer.valueOf(doctorUser.getId()));
                if (!CollectionUtils.isEmpty(ids)) {
                    this.doctorUserMapper.updateDoctorIdByIndex(ids);
                }
            }
        }
        DoctorSet doctorSet = new DoctorSet();
        verifyDoctorSet(map, exception, doctorSet);
        UpdateWrapper<DoctorSet> wrapper = new UpdateWrapper<>();
        wrapper.eq("doctor_user_id", map.get("id").toString());
        if (!com.aihoo.util.AllFieldIsNotNullUtils.objCheckIsNull(doctorSet)) {
            // 医生设置里面有值才进行更新
            this.doctorSetService.update(doctorSet, wrapper);
        }
        // 当复诊设置开启时，对应的医生复诊排班数据更新
        if ("1".equals(map.get("isRevisit").toString())) {
            List<DoctorSetTimeVo> data = JSONArray.parseArray
                    (JSON.toJSON(map.get("doctorSetTimes")).toString(), DoctorSetTimeVo.class);
            if (CollectionUtils.isEmpty(data)) {
                exception.setMessage("开启复诊时，复诊时间必须设置！");
                throw exception;
            }
            String verify = verifyDoctorSetTime(data);
            if (null != verify) {
                exception.setMessage(verify);
                throw exception;
            }
            this.doctorSetTimesService.addAcceptsTime(data, doctorUser.getId(), "REVISIT");
        }
        // 更新对应的医生上报数据
        // 2022-02-17发现数据丢失问题
        doctorUser = getById(doctorUser.getId());
        DicPractitioner dicPractitioner = new DicPractitioner();
        dicPractitioner.setGh(doctorUser.getMemberNum());
        dicPractitioner.setSsks(doctorUser.getDepartCode());
        dicPractitioner.setXm(doctorUser.getName());
        dicPractitioner.setZjlx(doctorUser.getPapersCode());
        dicPractitioner.setZjhm(doctorUser.getPapersNumbers());
        dicPractitioner.setZwdm(doctorUser.getPositionCode());
        dicPractitioner.setZcdm(doctorUser.getOfficeHolderCode());
        dicPractitioner.setLb(doctorUser.getPersonTypeCode());
        QueryWrapper<DicPractitioner> dicPractitionerWrapper = new QueryWrapper<>();
        dicPractitionerWrapper.eq("doctor_user_id", doctorUser.getId());
        if (!this.dicPractitionerService.update(dicPractitioner, dicPractitionerWrapper)) {
            dicPractitioner.setDoctorUserId(doctorUser.getId());
            dicPractitioner.setYljgdm(ReportCodingEnum.YLJGDM);
            dicPractitioner.setWsjgdm(ReportCodingEnum.WSJGDM);
            dicPractitioner.setXgbz(ReportCodingEnum.XGBZ);
            dicPractitioner.setCazsxlm(StringUtil.isBlank(doctorUser.getCaNumber()) ? "" : doctorUser.getCaNumber());
            this.dicPractitionerService.save(dicPractitioner);
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                loginRecordUtil.saveLoginRecord(request, "医院批量导入");
            }
        });
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancel(Map<String, Object> map, HttpServletRequest request) {
        BizException exception = new BizException();
        List<DoctorUser> doctorUsers;
        if (null == map.get("mobile") || "".equals(map.get("mobile"))) {
            exception.setMessage("医生手机号必填");
            throw exception;
        } else {
            if (11 != map.get("mobile").toString().length()) {
                exception.setMessage("医生手机号格式不正确");
                throw exception;
            }
            QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
            wrapper.eq("is_cancel", 0);
            wrapper.eq("mobile", map.get("mobile").toString().trim());
            doctorUsers = this.doctorUserMapper.selectList(wrapper);
            if (CollectionUtils.isEmpty(doctorUsers)) {
                exception.setMessage("该手机号还未注册");
                throw exception;
            }
        }
        int count = this.doctorUserMapper.cancel(map.get("mobile").toString().trim());
        if (count > 0) {
            //医生注销日志
            UserAgentGetter userAgentGetter = new UserAgentGetter(request);
            DoctorUserLog log = new DoctorUserLog();
            log.setActionType("CANCEL");
            log.setDoctorUserId(doctorUsers.get(0).getId());
            log.setOsName(userAgentGetter.getOS());
            log.setIpAddress(userAgentGetter.getIpAddr());
            log.setRemark("操作人id：" + Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString() + "注销医生成功，手机号为：" + map.get("mobile").toString());
            this.doctorUserLogService.save(log);

            Map<String, Object> rm = new HashMap();
            rm.put("doctor_user_id", doctorUsers.get(0).getId());
            mdtTeamDoctorMapper.deleteByMap(rm);

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    loginRecordUtil.saveLoginRecord(request, "医生注销");
                }
            });
        } else {
            exception.setMessage("注销失败");
            throw exception;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean doctorCA() {
        QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
        wrapper.eq("is_cancel", "0");
        List<DoctorUser> doctorUserList = doctorUserMapper.selectList(wrapper);
        for (DoctorUser doctorUser : doctorUserList) {
            String papersNumbers = doctorUser.getPapersNumbers();
            Map<String, String> data = getCert(papersNumbers);
            String cert = "";
            String certSn = "";
            if (null != data) {
                cert = data.get("cert");
                certSn = data.get("certSn");
                if ("PASS".equals(StringUtil.getStr(doctorUser.getIsAuth()))) {
                    DoctorUser updateDoctorUser = new DoctorUser();
                    updateDoctorUser.setIsAuth("PASS");
                    updateDoctorUser.setCaNumber(certSn);
                    updateDoctorUser.setCaCert(cert);
                    updateDoctorUser.setId(doctorUser.getId());
                    updateDoctorUser.setToken(null);
                    redisService.remove(RedisConstant.DOCKER_LOGIN_KEY + doctorUser.getToken());

                    DoctorUserLog doctorUserLog = new DoctorUserLog();
                    doctorUserLog.setDoctorUserId(doctorUser.getId());
                    doctorUserLog.setActionType("CASIGN");
                    doctorUserLog.setRemark("认证成功");
                    doctorUserMapper.updateById(updateDoctorUser);
                    doctorUserLogMapper.insert(doctorUserLog);
                }
            } else {
                DoctorUser updateDoctorUser = new DoctorUser();
                updateDoctorUser.setIsAuth("NONE");
                updateDoctorUser.setCaNumber("");
                updateDoctorUser.setCaCert("");
                updateDoctorUser.setId(doctorUser.getId());
                DoctorUserLog doctorUserLog = new DoctorUserLog();
                doctorUserLog.setDoctorUserId(doctorUser.getId());
                doctorUserLog.setActionType("CASIGN");
                doctorUserLog.setRemark("认证失败");
                doctorUserMapper.updateById(updateDoctorUser);
                doctorUserLogMapper.insert(doctorUserLog);
            }
            /*更新统计表*/
            UpdateWrapper<DicPractitioner> up = new UpdateWrapper();
            up.setSql("`update_time` = NOW()").set("CAZSXLM", certSn).set("ZJHM", papersNumbers);
            up.eq("doctor_user_id", doctorUser.getId());
            if (!dicPractitionerService.update(up)) {
                DicPractitioner dicPractitioner = new DicPractitioner();
                dicPractitioner.setDoctorUserId(doctorUser.getId());
                dicPractitioner.setCazsxlm(certSn);
                dicPractitioner.setZjhm(papersNumbers);
                dicPractitioner.setGh(doctorUser.getMemberNum());
                dicPractitioner.setSsks(doctorUser.getDepartCode());
                dicPractitioner.setXm(doctorUser.getName());
                dicPractitioner.setZjlx(doctorUser.getPapersCode());
                dicPractitioner.setZwdm(doctorUser.getPositionCode());
                dicPractitioner.setZcdm(doctorUser.getOfficeHolderCode());
                dicPractitioner.setLb(doctorUser.getPersonTypeCode());
                dicPractitioner.setYljgdm(ReportCodingEnum.YLJGDM);
                dicPractitioner.setWsjgdm(ReportCodingEnum.WSJGDM);
                dicPractitioner.setXgbz(ReportCodingEnum.XGBZ);
                dicPractitionerMapper.insert(dicPractitioner);
            }
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            String str = "MIIC5DCCAoigAwIBAgIQYYEgXgG19lXnfJkWNNFiNDAMBggqgRzPVQGDdQUAMDQxCzAJBgNVBAYTAkNOMREwDwYDVQQKDAhVbmlUcnVzdDESMBAGA1UEAwwJU0hFQ0EgU00yMB4XDTIxMDczMDAyMTU0MloXDTIyMDczMDE1NTk1OVowHjELMAkGA1UEBhMCQ04xDzANBgNVBAMMBuWRqOWEkjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABF/pcc9WWMrxVBHbhDT3s5k86ckuCwbO///cuQQ0Pao5hN5MoWrceXTzL8x7p3Q14A7U9yUQEsoZKhUKO9K1UMejggGOMIIBijAiBgNVHSMBAf8EGDAWgBSJMQSRe0Oqqpq/hB2bhu7wuHCZoDAgBgNVHQ4BAf8EFgQUHIf6UVg/9Wi6JR7e1tKaU7GpmRswDgYDVR0PAQH/BAQDAgeAMCkGCCqBHIbvOoEUBB0xMDNANTAwOFNGMDMzMDIwMzE5ODUwMzMwMDY3NDAkBgUqVhUBAQQbM0A1MDA4U0YwMzMwMjAzMTk4NTAzMzAwNjc0MIHgBgNVHR8EgdgwgdUwgZmggZaggZOGgZBsZGFwOi8vbGRhcDIuc2hlY2EuY29tOjM4OS9jbj1DUkw1NDguY3JsLG91PVJBMjAxNjEwMTIsb3U9Q0E5MSxvdT1jcmwsbz1VbmlUcnVzdD9jZXJ0aWZpY2F0ZVJldm9jYXRpb25MaXN0P2Jhc2U/b2JqZWN0Q2xhc3M9Y1JMRGlzdHJpYnV0aW9uUG9pbnQwN6A1oDOGMWh0dHA6Ly9sZGFwMi5zaGVjYS5jb20vQ0E5MS9SQTIwMTYxMDEyL0NSTDU0OC5jcmwwDAYIKoEcz1UBg3UFAANIADBFAiEAjyjXwufHeTq3Oj5vBG3D3emgMKxsIEcXoXkDQ7I1W8ECIEt8ZM6YodzJ6ExZgIxahLRp54+QSfZlIe2xmJs5+xf3";
            System.out.println(URLEncoder.encode(str, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用CA认证接口
     *
     * @param id
     * @return
     */
    private Map<String, String> getCert(String id) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("appId", caProperties.getAppId());
            String msg = "appId=" + caProperties.getAppId() + "&id=" + id;
            String signature = SecurityUtil.signature(caProperties.getPrivateStr(), msg);
            map.put("sign", signature);
            String response = HttpUtil.post(caProperties.getOpenUrl() + "v1/user/cert", map);
            String decode = URLDecoder.decode(response, "UTF-8");
            Map responseMap = JSON.parseObject(decode, Map.class);
            String ret = responseMap.get("ret").toString();
            if ("success".equals(ret)) {
                return JSON.parseObject(responseMap.get("data").toString(), Map.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 医生复诊时间数据的插入
    private String verifyDoctorSetTime(List<DoctorSetTimeVo> data) throws ParseException {
        // 排班时间校验，不能重叠
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        for (DoctorSetTimeVo vo : data) {
            List<DoctorSetTimeVo.SetTime> setTimes = vo.getSetTimes();
            for (DoctorSetTimeVo.SetTime s1 : setTimes) {
                for (DoctorSetTimeVo.SetTime s2 : setTimes) {
                    Date beginNowTime = df.parse(s1.getStartTime());
                    Date endNowTime = df.parse(s1.getEndTime());
                    Date beginTime = df.parse(s2.getStartTime());
                    Date endTime = df.parse(s2.getEndTime());
                    boolean isPass = belongCalendar(beginNowTime, beginTime, endTime);
                    boolean isPass1 = belongCalendar(endNowTime, beginTime, endTime);
                    if (isPass) {
                        return ("存在叠加时间，请重新编辑排班时间。重叠时间为："
                                + vo.getWeekCode() + "的" + s1.getStartTime() + "---" + s1.getEndTime() +
                                "与" + s2.getStartTime() + "---" + s2.getEndTime()
                        );
                    }
                    if (isPass1) {
                        return ("存在叠加时间，请重新编辑排班时间。重叠时间为："
                                + vo.getWeekCode() + "的" + s1.getStartTime() + "---" + s1.getEndTime() +
                                "与" + s2.getStartTime() + "---" + s2.getEndTime()
                        );
                    }
                }
            }
        }
        return null;
    }

    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        //设置结束时间
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        //处于开始时间之后，和结束时间之前的判断
        return date.after(begin) && date.before(end);
    }

    private void doctorUserParameterCalibration(Map<String, Object> map, BizException exception, DoctorUser doctorUser) throws Exception {
        Map<String, String> message = IdentityCardUtils.getCarMessage(map.get("papersNumbers").toString());
        doctorUser.setSex(message.get("sex"));
        doctorUser.setAge(message.get("age"));
        doctorUser.setBirthday(message.get("birthday"));
        // 获取登陆人id
        String userId = Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString();
        doctorUser.setCreateUserId(userId);
        doctorUser.setMobile(map.get("mobile").toString().trim());
        if (null != map.get("headImg") && !"".equals(map.get("headImg").toString())) {
            doctorUser.setHeadImg(map.get("headImg").toString().trim());
        }
        doctorUser.setName(map.get("name").toString().trim());
        if (null != map.get("tag") && !"".equals(map.get("tag"))) {
            doctorUser.setTag(map.get("tag").toString().trim());
        }
        /*doctorUser.setMemberNum(map.get("memberNum").toString().trim());*/
        doctorUser.setHospitalId(map.get("hospitalId").toString().trim());
        Hospital hospitalId = this.hospitalMapper.selectById(map.get("hospitalId").toString().trim());
        if (StringUtils.isEmpty(hospitalId)) {
            exception.setMessage("未查询到id对应医院名称 id :" + map.get("hospitalId").toString());
            throw exception;
        }
        doctorUser.setHospitalName(hospitalId.getHosName());
        doctorUser.setDepartId(map.get("departId").toString().trim());
        doctorUser.setDepartCode(map.get("departCode").toString().trim());
        String departName = this.departmentService.findDepartmentNameByCode(map.get("departCode").toString().trim());
        if (StringUtils.isEmpty(departName)) {
            exception.setMessage("未查询到departCode对应科室名称");
            throw exception;
        }
        doctorUser.setDepartName(departName);
        doctorUser.setOfficeHolderCode(map.get("officeHolderCode").toString().trim());
        String officeHolderCode = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.DOCT_TITLE.getType(), map.get("officeHolderCode").toString().trim());
        if (StringUtils.isEmpty(officeHolderCode)) {
            exception.setMessage("未查询到officeHolderCode对应的职称");
            throw exception;
        }
        doctorUser.setOfficeHolderName(officeHolderCode);
        doctorUser.setBeGoodAtText(map.get("beGoodAtText").toString().trim());
        doctorUser.setIntroductionText(map.get("introductionText").toString().trim());
        doctorUser.setPersonTypeCode(map.get("personTypeCode").toString().trim());
        String personTypeName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.RYLB.getType(), map.get("personTypeCode").toString().trim());
        if (StringUtils.isEmpty(personTypeName)) {
            exception.setMessage("未找到编码对应的人员类别");
            throw exception;
        }
        doctorUser.setPersonTypeName(personTypeName);
        doctorUser.setPositionCode(map.get("positionCode").toString().trim());
        String positionName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.POSITION.getType(), map.get("positionCode").toString().trim());
        if (StringUtils.isEmpty(positionName)) {
            exception.setMessage("未找到对应的职务类型");
            throw exception;
        }
        doctorUser.setPositionName(positionName);
        doctorUser.setPapersCode(map.get("papersCode").toString().trim());
        String papersName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.PAPERS.getType(), map.get("papersCode").toString().trim());
        if (StringUtils.isEmpty(papersName)) {
            exception.setMessage("未找到对应的证件类型");
            throw exception;
        }
        doctorUser.setPapersName(papersName);
        doctorUser.setPapersNumbers(map.get("papersNumbers").toString().trim());
        // index 医生排序字段 非必填
        if (null != map.get("index") && !"".equals(map.get("index").toString())) {
            doctorUser.setIndex(map.get("index").toString());
        }
        // 医生成就 非必填
        if (null != map.get("achievement") && !"".equals(map.get("achievement").toString())) {
            doctorUser.setAchievement(map.get("achievement").toString());
        }

        //  写入会诊医生类型 有值直接写入
        if (null != map.get("doctorType") && !"".equals(map.get("doctorType").toString())) {
            doctorUser.setDoctorType(map.get("doctorType").toString().trim());
        }

        if (null != map.get("medicalLicensePageOne") && !"".equals(map.get("medicalLicensePageOne").toString())) {
            doctorUser.setMedicalLicensePageOne(map.get("medicalLicensePageOne").toString().trim());
        }
        if (null != map.get("medicalLicensePageTwo") && !"".equals(map.get("medicalLicensePageTwo").toString())) {
            doctorUser.setMedicalLicensePageTwo(map.get("medicalLicensePageTwo").toString().trim());
        }
        if (null != map.get("medicalLicenseNo") && !"".equals(map.get("medicalLicenseNo").toString())) {
            doctorUser.setMedicalLicenseNo(map.get("medicalLicenseNo").toString().trim());
        }
        if (null != map.get("medicalLicenseIssueDate") && !"".equals(map.get("medicalLicenseIssueDate").toString())) {
            doctorUser.setMedicalLicenseIssueDate(map.get("medicalLicenseIssueDate").toString().trim());
        }
        if (null != map.get("practiceCertificatePageOne") && !"".equals(map.get("practiceCertificatePageOne").toString())) {
            doctorUser.setPracticeCertificatePageOne(map.get("practiceCertificatePageOne").toString().trim());
        }
        if (null != map.get("practiceCertificatePageTwo") && !"".equals(map.get("practiceCertificatePageTwo").toString())) {
            doctorUser.setPracticeCertificatePageTwo(map.get("practiceCertificatePageTwo").toString().trim());
        }
        if (null != map.get("practiceCertificateNo") && !"".equals(map.get("practiceCertificateNo").toString())) {
            doctorUser.setPracticeCertificateNo(map.get("practiceCertificateNo").toString().trim());
        }
        if (null != map.get("practiceCertificateIssueDate") && !"".equals(map.get("practiceCertificateIssueDate").toString())) {
            doctorUser.setPracticeCertificateIssueDate(map.get("practiceCertificateIssueDate").toString().trim());
        }
        if (null != map.get("area") && !"".equals(map.get("area").toString())) {
            doctorUser.setArea(map.get("area").toString().trim());
        }
    }

    private void doctorSetParameterCalibration(Map<String, Object> map, BizException exception, DoctorSet doctorSet) {
        if (null == map.get("isImg") || "".equals(map.get("isImg"))) {
            exception.setMessage("是否开启专家咨询必填");
            throw exception;
        } else {
            doctorSet.setIsImg(map.get("isImg").toString());
            if ("1".equals(map.get("isImg").toString())) {
                if (null == map.get("imgPrice") || "".equals(map.get("imgPrice").toString())) {
                    exception.setMessage("开启专家咨询时，专家咨询价格必填");
                    throw exception;
                }
                doctorSet.setImgPrice(map.get("imgPrice").toString());
            }
        }
        /*if (null == map.get("isVoice") || "".equals(map.get("isVoice"))) {
            exception.setMessage("是否开启语音问诊必填");
            throw exception;
        } else {
            doctorSet.setIsVoice(map.get("isVoice").toString());
            if ("1".equals(map.get("isVoice").toString())) {
                if (null == map.get("voicePrice") || "".equals(map.get("voicePrice"))) {
                    exception.setMessage("开启语音问诊时，语音问诊价格必填");
                    throw exception;
                }
                doctorSet.setVoicePrice(map.get("voicePrice").toString());
            }
        }*/
        /*if (null == map.get("isVideo") || "".equals(map.get("isVideo"))) {
            exception.setMessage("是否开启视频问诊必填");
            throw exception;
        } else {
            doctorSet.setIsVideo(map.get("isVideo").toString());
            if ("1".equals(map.get("isVideo").toString())) {
                if (null == map.get("videoPrice") || "".equals(map.get("videoPrice").toString())) {
                    exception.setMessage("开启视频问诊时，视频问诊价格必填");
                    throw exception;
                }
                doctorSet.setVideoPrice(map.get("videoPrice").toString());
            }
        }*/
        if (null == map.get("isRevisit") || "".equals(map.get("isRevisit").toString())) {
            exception.setMessage("是否开启复诊必填");
            throw exception;
        } else {
            if ("1".equals(map.get("isRevisit").toString())) {
                //开启复诊 复诊时间必须设置
                if (null == map.get("doctorSetTimes")) {
                    exception.setMessage("开启复诊时，复诊时间必须设置！");
                    throw exception;
                }
            }
            doctorSet.setIsRevisit(map.get("isRevisit").toString());
        }

        if (null == map.get("isMdt") || "".equals(map.get("isMdt").toString())) {
            exception.setMessage("是否开启会诊必填");
            throw exception;
        } else {
            if ("1".equals(map.get("isMdt").toString())) {
                //开启会诊时，医生类型必填
                if (null == map.get("doctorType") || "".equals(map.get("doctorType").toString())) {
                    exception.setMessage("开启会诊时，医生类型必填");
                    throw exception;
                }
            }
            doctorSet.setIsMdt(map.get("isMdt").toString());
        }

        if (!("1".equals(doctorSet.getIsImg()) || "1".equals(doctorSet.getIsRevisit()) || "1".equals(doctorSet.getIsMdt()))) {
            exception.setMessage("医生设置中必须设置一个!");
            throw exception;
        }
        if ("1".equals(map.get("isCombination")) || "0".equals(map.get("isCombination"))) {
            doctorSet.setIsCombination(map.get("isCombination").toString());
        }
    }

    private void parameterCalibration(Map<String, Object> map, BizException exception) {
        // 医生表参数校验
        if (null == map.get("name") || "".equals(map.get("name"))) {
            exception.setMessage("请填写医生姓名");
            throw exception;
        }
        if (null == map.get("mobile") || "".equals(map.get("mobile"))) {
            exception.setMessage("请填写手机号");
            throw exception;

        } else {
            if (11 != map.get("mobile").toString().length()) {
                exception.setMessage("请填写11位正确的手机号码");
                throw exception;
            }
            QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
            wrapper.eq("is_cancel", 0);
            wrapper.eq("mobile", map.get("mobile").toString().trim());
            List<DoctorUser> list = this.doctorUserMapper.selectList(wrapper);
            if (!CollectionUtils.isEmpty(list)) {
                exception.setMessage("该手机号已经注册");
                throw exception;

            }
        }
       /* if (null == map.get("headImg") || "".equals(map.get("headImg"))) {
            exception.setMessage("请上传图片");
            throw exception;
        }*/
       /* if (null == map.get("memberNum") || "".equals(map.get("memberNum"))) {
            exception.setMessage("请上传工号");
            throw exception;
        }else {
            List<DoctorUser> doctorUsers = this.doctorUserMapper.
                    selectList(new QueryWrapper<DoctorUser>().eq("member_num", map.get("memberNum").toString().trim()).eq("is_cancel",0));
            if (!CollectionUtils.isEmpty(doctorUsers)){
                exception.setMessage("医生的工号不能重复");
                throw exception;
            }
        }*/
        if (null == map.get("hospitalId") || "".equals(map.get("hospitalId"))) {
            exception.setMessage("请上传医院id");
            throw exception;
        }
        if (null == map.get("departId") || "".equals(map.get("departId"))) {
            exception.setMessage("请上传科室id");
            throw exception;
        }
        if (null == map.get("departCode") || "".equals(map.get("departCode"))) {
            exception.setMessage("请上传科室编码");
            throw exception;
        }
        if (null == map.get("officeHolderCode") || "".equals(map.get("officeHolderCode"))) {
            exception.setMessage("请上传职称编码");
            throw exception;
        }
        if (null == map.get("beGoodAtText") || "".equals(map.get("beGoodAtText"))) {
            exception.setMessage("请上传擅长");
            throw exception;
        }
        if (null == map.get("introductionText") || "".equals(map.get("introductionText"))) {
            exception.setMessage("请上传简介");
            throw exception;
        }
        if (null == map.get("personTypeCode") || "".equals(map.get("personTypeCode"))) {
            exception.setMessage("请上传人员类别编码");
            throw exception;
        }
        if (null == map.get("positionCode") || "".equals(map.get("positionCode"))) {
            exception.setMessage("请上传职务编码");
            throw exception;
        }
        if (null == map.get("papersCode") || "".equals(map.get("papersCode"))) {
            exception.setMessage("请上传证件类型编码");
            throw exception;
        }
        if (null == map.get("papersNumbers") || "".equals(map.get("papersNumbers"))) {
            exception.setMessage("请输入证件号码");
            throw exception;
        } else {
            if (!IdentityCardUtils.isIdCard(map.get("papersNumbers").toString().trim())) {
                exception.setMessage("身份证格式不正确");
                throw exception;
            }
            List<DoctorUser> doctorUsers = this.doctorUserMapper.selectList
                    (new QueryWrapper<DoctorUser>().eq("papers_numbers", map.get("papersNumbers").toString().trim()).eq("is_cancel", 0));
            if (!CollectionUtils.isEmpty(doctorUsers)) {
                exception.setMessage("证件号码重复");
                throw exception;
            }
        }
    }

    private void verifyDoctorUpdate(Map<String, Object> map, BizException exception, DoctorUser doctorUser) throws Exception {

        doctorUser.setId(map.get("id").toString());
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            doctorUser.setName(map.get("name").toString().trim());
        }
        if (null != map.get("mobile") && !"".equals(map.get("mobile"))) {
            if (11 != map.get("mobile").toString().length()) {
                exception.setMessage("请填写11位正确的手机号码");
                throw exception;
            }
            //查询之前的用户手机号
            DoctorUser one = this.doctorUserMapper.selectById(map.get("id").toString().trim());
            if (!one.getMobile().equals(map.get("mobile").toString().trim())) {
                QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
                wrapper.eq("is_cancel", 0);
                wrapper.eq("mobile", map.get("mobile").toString().trim());
                List<DoctorUser> list = this.doctorUserMapper.selectList(wrapper);
                if (!CollectionUtils.isEmpty(list)) {
                    exception.setMessage("该手机号已经注册");
                    throw exception;
                }
                doctorUser.setMobile(map.get("mobile").toString().trim());
            }
        }
        if (null != map.get("departCode") && !"".equals(map.get("departCode"))) {
            String name = this.departmentService.findDepartmentNameByCode(map.get("departCode").toString());
            doctorUser.setDepartCode(map.get("departCode").toString().trim());
            doctorUser.setDepartName(name);
        }
        if (null != map.get("departId") && !"".equals(map.get("departId").toString())) {
            doctorUser.setDepartId(map.get("departId").toString());
        }
        if (null != map.get("beGoodAtText") && !"".equals(map.get("beGoodAtText").toString())) {
            doctorUser.setBeGoodAtText(map.get("beGoodAtText").toString());
        }
        // 标签可以更新为空

        if (null == map.get("tag") || "".equals(map.get("tag").toString())) {
            doctorUser.setTag(null);
        } else {
            doctorUser.setTag(map.get("tag").toString());
        }
        // 排序字段
        if (null != map.get("index") && !"".equals(map.get("index").toString())) {
            doctorUser.setIndex(map.get("index").toString());
        }

        if (null != map.get("introductionText") && !"".equals(map.get("introductionText").toString())) {
            doctorUser.setIntroductionText(map.get("introductionText").toString());
        }
        if (null != map.get("headImg") && !"".equals(map.get("headImg").toString())) {
            doctorUser.setHeadImg(map.get("headImg").toString());
        }
        if (null != map.get("hospitalId") && !"".equals(map.get("hospitalId"))) {
            Hospital hospital = this.hospitalMapper.selectById(map.get("hospitalId").toString().trim());
            if (StringUtils.isEmpty(hospital)) {
                exception.setMessage("未查询到id对应医院名称 id :" + map.get("hospitalId").toString());
                throw exception;
            }
            doctorUser.setHospitalName(hospital.getHosName());
            doctorUser.setHospitalId(hospital.getId());
        }
        if (null != map.get("officeHolderCode") && !"".equals(map.get("officeHolderCode"))) {
            String officeHolderCode = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.DOCT_TITLE.getType(), map.get("officeHolderCode").toString().trim());
            if (StringUtils.isEmpty(officeHolderCode)) {
                exception.setMessage("未查询到officeHolderCode对应的职称");
                throw exception;
            }
            doctorUser.setOfficeHolderName(officeHolderCode);
            doctorUser.setOfficeHolderCode(map.get("officeHolderCode").toString().trim());
        }
        if (null != map.get("personTypeCode") && !"".equals(map.get("personTypeCode"))) {
            String personTypeName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.RYLB.getType(), map.get("personTypeCode").toString().trim());
            if (StringUtils.isEmpty(personTypeName)) {
                exception.setMessage("未找到编码对应的人员类别");
                throw exception;
            }
            doctorUser.setPersonTypeName(personTypeName);
            doctorUser.setPersonTypeCode(map.get("personTypeCode").toString().trim());
        }

        if (null != map.get("positionCode") && !"".equals(map.get("positionCode"))) {
            String positionName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.POSITION.getType(), map.get("positionCode").toString().trim());
            if (StringUtils.isEmpty(positionName)) {
                exception.setMessage("未找到对应的职务类型");
                throw exception;
            }
            doctorUser.setPositionName(positionName);
            doctorUser.setPositionCode(map.get("positionCode").toString().trim());
        }

        if (null != map.get("papersCode") && !"".equals(map.get("papersCode"))) {
            String papersName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.PAPERS.getType(), map.get("papersCode").toString().trim());
            if (StringUtils.isEmpty(papersName)) {
                exception.setMessage("未找到证件对应的名称");
                throw exception;
            }
            doctorUser.setPapersName(papersName);
            doctorUser.setPapersCode(map.get("papersCode").toString().trim());
            if (!IdentityCardUtils.isIdCard(map.get("papersNumbers").toString())) {
                exception.setMessage("身份证格式不正确");
                throw exception;
            }
            DoctorUser user = this.doctorUserMapper.selectById(doctorUser.getId());
            if (!user.getPapersNumbers().equals(map.get("papersNumbers").toString().trim())) {
                List<DoctorUser> doctorUsers = this.doctorUserMapper.selectList
                        (new QueryWrapper<DoctorUser>().eq("papers_numbers", map.get("papersNumbers").toString().trim()).eq("is_cancel", 0));
                if (!CollectionUtils.isEmpty(doctorUsers)) {
                    exception.setMessage("证件号码重复");
                    throw exception;
                }
            }
            Map<String, String> message = IdentityCardUtils.getCarMessage(map.get("papersNumbers").toString());
            doctorUser.setSex(message.get("sex"));
            doctorUser.setAge(message.get("age"));
            doctorUser.setBirthday(message.get("birthday"));
            doctorUser.setPapersNumbers(map.get("papersNumbers").toString().trim());
        }

        // 主要成就非必填
        if (null != map.get("achievement") && !"".equals(map.get("achievement").toString())) {
            doctorUser.setAchievement(map.get("achievement").toString());
        }

        // 会诊医生类型有值时 写入会诊医生类型
        if (null != map.get("doctorType") && !"".equals(map.get("doctorType").toString())) {
            doctorUser.setDoctorType(map.get("doctorType").toString().trim());
        }

        if (null != map.get("medicalLicensePageOne") && !"".equals(map.get("medicalLicensePageOne").toString())) {
            doctorUser.setMedicalLicensePageOne(map.get("medicalLicensePageOne").toString().trim());
        }
        if (null != map.get("medicalLicensePageTwo") && !"".equals(map.get("medicalLicensePageTwo").toString())) {
            doctorUser.setMedicalLicensePageTwo(map.get("medicalLicensePageTwo").toString().trim());
        }
        if (null != map.get("medicalLicenseNo") && !"".equals(map.get("medicalLicenseNo").toString())) {
            doctorUser.setMedicalLicenseNo(map.get("medicalLicenseNo").toString().trim());
        }
        if (null != map.get("medicalLicenseIssueDate") && !"".equals(map.get("medicalLicenseIssueDate").toString())) {
            doctorUser.setMedicalLicenseIssueDate(map.get("medicalLicenseIssueDate").toString().trim());
        }
        if (null != map.get("practiceCertificatePageOne") && !"".equals(map.get("practiceCertificatePageOne").toString())) {
            doctorUser.setPracticeCertificatePageOne(map.get("practiceCertificatePageOne").toString().trim());
        }
        if (null != map.get("practiceCertificatePageTwo") && !"".equals(map.get("practiceCertificatePageTwo").toString())) {
            doctorUser.setPracticeCertificatePageTwo(map.get("practiceCertificatePageTwo").toString().trim());
        }
        if (null != map.get("practiceCertificateNo") && !"".equals(map.get("practiceCertificateNo").toString())) {
            doctorUser.setPracticeCertificateNo(map.get("practiceCertificateNo").toString().trim());
        }
        if (null != map.get("practiceCertificateIssueDate") && !"".equals(map.get("practiceCertificateIssueDate").toString())) {
            doctorUser.setPracticeCertificateIssueDate(map.get("practiceCertificateIssueDate").toString().trim());
        }
        if (null != map.get("area") && !"".equals(map.get("area").toString())) {
            doctorUser.setArea(map.get("area").toString().trim());
        }

    }

    private void verifyDoctorSet(Map<String, Object> map, BizException exception, DoctorSet doctorSet) {
        if (null != map.get("isImg") && !"".equals(map.get("isImg"))) {
            doctorSet.setIsImg(map.get("isImg").toString());
            if ("1".equals(map.get("isImg"))) {
                if (null == map.get("imgPrice") || "".equals(map.get("isImg").toString())) {
                    exception.setMessage("当专家咨询开启时，专家咨询价格必填");
                    throw exception;
                }
                doctorSet.setImgPrice(map.get("imgPrice").toString().trim());
            }
        }
       /* if (null != map.get("isVoice") && !"".equals(map.get("isVoice"))) {
            doctorSet.setIsVoice(map.get("isVoice").toString());
            if ("1".equals(map.get("isVoice"))) {
                if (null == map.get("voicePrice") || "".equals(map.get("voicePrice"))) {
                    exception.setMessage("当语音问诊开启时，语音问诊价格 必填");
                    throw exception;
                }
                doctorSet.setVoicePrice(map.get("voicePrice").toString().trim());
            }
        }*/
       /* if (null != map.get("isVideo") && !"".equals(map.get("isVideo"))) {
            doctorSet.setIsVideo(map.get("isVideo").toString());
            if ("1".equals(map.get("isVideo"))) {
                if (null == map.get("videoPrice") || "".equals(map.get("videoPrice"))) {
                    exception.setMessage("当视频问诊开启时,视频问诊价格必填");
                    throw exception;
                }
                doctorSet.setVideoPrice(map.get("videoPrice").toString().trim());
            }
        }*/

        if (null != map.get("isRevisit") && !"".equals(map.get("isRevisit"))) {
            if ("1".equals(map.get("isRevisit").toString())) {
                //开启复诊 复诊时间必须设置
                if (null == map.get("doctorSetTimes")) {
                    exception.setMessage("开启复诊时，复诊时间必须设置！");
                    throw exception;
                }
            }
            doctorSet.setIsRevisit(map.get("isRevisit").toString());
        }
        if (null != map.get("isMdt") && !"".equals(map.get("isMdt").toString())) {
            if ("1".equals(map.get("isMdt").toString())) {
                //开启会诊 是否是助理医生必填
                if (null == map.get("doctorType") || "".equals(map.get("doctorType").toString())) {
                    exception.setMessage("开启会诊 医生类型必填！");
                    throw exception;
                }
            }
            doctorSet.setIsMdt(map.get("isMdt").toString());
        }

        if (!("1".equals(doctorSet.getIsImg()) || "1".equals(doctorSet.getIsRevisit()) || "1".equals(doctorSet.getIsMdt()))) {
            exception.setMessage("设置中必须设置一个!");
            throw exception;
        }

        if ("1".equals(map.get("isCombination").toString()) || "0".equals(map.get("isCombination").toString())) {
            doctorSet.setIsCombination(map.get("isCombination").toString());
        }
    }
}
