package com.aihoo.domain.doctor.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.aihoo.domain.doctor.component.AliCloudComponent;
import com.aihoo.constant.ImUserPrefix;
import com.aihoo.properties.TencentProperties;
import com.aihoo.properties.TestProperties;
import com.aihoo.util.*;
import com.aihoo.common.BizResultCode;
import com.aihoo.exception.BizException;
import com.aihoo.redis.RedisConstant;
import com.aihoo.redis.RedisService;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.doctor.dto.DoctorVisitSetRequest;
import com.aihoo.domain.doctor.dto.DoctorWelcomeMessageRequest;
import com.aihoo.domain.doctor.dto.DoctorUserVo;
import com.aihoo.domain.sys.model.mapper.DVersionMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorSetMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorUserLogMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;


import com.aihoo.security.AuthUtil;
import com.aihoo.util.ValueFormatUtils;
import com.aihoo.util.UserAgentGetter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.entity.DoctorSet;
import com.aihoo.domain.doctor.model.entity.DoctorVisitSet;
import com.aihoo.domain.doctor.model.entity.DoctorWelcomeMessageSet;
import com.aihoo.domain.doctor.model.entity.DoctorUserLog;
import com.aihoo.domain.sys.model.entity.DVersion;
import com.aihoo.redis.RedisService;
import com.aihoo.domain.doctor.service.DoctorVisitSetService;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.visit.service.HosVisitService;
import com.aihoo.domain.prescription.service.PrescriptionService;
import com.aihoo.domain.doctor.service.DoctorWelcomeMessageSetService;
import com.aihoo.domain.im.service.ProposalService;


/**
 * <p>
 * 医生用户表 服务实现类
 * </p>
 *
 * @author mcp
 * @since 2020-09-15
 */
@Log4j2
@Service
public class DoctorUserServiceImpl extends ServiceImpl<DoctorUserMapper, DoctorUser> implements DoctorUserService {

    @Resource
    private DoctorUserMapper doctorUserMapper;
    @Autowired(required = false)
    private RedisService redisService;
    @Resource
    private DoctorSetMapper doctorSetMapper;
    @Resource
    private DoctorUserLogMapper doctorUserLogMapper;
    @Resource
    private DVersionMapper dVersionMapper;
    @Autowired
    private TencentProperties tencentProperties;
    @Autowired
    private TestProperties testProperties;

    @Autowired
    private AliCloudComponent aliCloudComponent;
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private HosVisitService hosVisitService;
    @Autowired
    private ProposalService proposalService;
    @Autowired
    private DoctorVisitSetService doctorVisitSetService;
    @Autowired
    private DoctorWelcomeMessageSetService doctorWelcomeMessageSetService;

    @Override
    public DoctorUser selectMobile(String mobile) {
//        return doctorUserMapper.selectMobile(mobile);
        QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile).eq("is_cancel", "0");
        return doctorUserMapper.selectOne(wrapper);
    }

    @Override
    public DoctorUserVo loginUser(String mobile, HttpServletRequest request) {
        DoctorUser doctorUser = selectMobile(mobile);
        String oldToken = doctorUser.getToken();
        String accessToken = UUID.randomUUID().toString().replace("-", "");
        UpdateWrapper<DoctorUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("mobile", mobile);
        updateWrapper.eq("status", 1);
        updateWrapper.eq("is_auth", "PASS");
        updateWrapper.eq("is_cancel", "0");
        updateWrapper.set("token", accessToken);
        if (StringUtil.isBlank(doctorUser.getUserSig())) {
            String imUserId = String.format(ImUserPrefix.USER_ID_FORMAT, ImUserPrefix.DOCTOR, doctorUser.getId());
            String userSig = ImUtils.genUserSig(imUserId, null, tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
            updateWrapper.set("user_sig", userSig);
        }
        doctorUserMapper.update(updateWrapper);
        // 添加记录
        UserAgentGetter userAgentGetter = new UserAgentGetter(request);
        DoctorUserLog userLog = new DoctorUserLog();
        userLog.setActionType("LOGIN");
        userLog.setDoctorUserId(doctorUser.getId());
        userLog.setOsName(userAgentGetter.getOS());
        userLog.setIpAddress(userAgentGetter.getIpAddr());
        userLog.setRemark("登陆成功");
        String cityNameByTaoBaoAPI = AdderssUtils.getCityNameByTaoBaoAPI(userAgentGetter.getIpAddr());
        userLog.setCity(cityNameByTaoBaoAPI);
        doctorUserLogMapper.insert(userLog);
        // 刷新数据
        doctorUser = doctorUserMapper.selectOne(new QueryWrapper<DoctorUser>().eq("id", doctorUser.getId()));

        // 缓存数据
        String redisKey = RedisConstant.DOCKER_LOGIN_KEY + accessToken;
        redisService.set(redisKey, doctorUser, RedisConstant.TOKEN_SURVIVE_TIME);
        log.info("存入用户数据,key:{},data:{}", redisKey, JSONObject.toJSONString(doctorUser));
        if (StringUtil.isNotBlank(oldToken)) {
            redisService.remove(RedisConstant.DOCKER_LOGIN_KEY + oldToken);
        }
        // 返回VO
        return convert2Vo(doctorUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject sendCode(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        //用户输入的手机号码
        String mobile = (String) map.get("mobile");
        // 生成6位随机数
        // String code = RandomUtil.randomNumbers(6);
        String code = "111111";
        String key = "doctor_login_" + mobile;
        Map<String, Object> sendMap = new HashMap<>();
        sendMap.put("mobile", mobile);
        sendMap.put("msg", "【合偶平方】您好，您的验证码是" + code + ",3分钟内有效");
        // 推送短信
        String body = SmsUtils.send(sendMap);
        JSONObject sendObject = JSONObject.parseObject(body);
        String sendCode = sendObject.get("code").toString();
        if (!"0".equals(sendCode)) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "发送短信失败");
        }
        //设置到redis里面把验证码-3分钟
        jsonObject.put("is_succ", 1);
        redisService.set(key, code, 180);
        jsonObject.put("msg", "短信验证码已发送");
        return jsonObject;

    }

    /**
     * @param map
     * @Author: 14891
     * @Description: 登录
     * @Date: 2020/9/20
     * @Return: com.alibaba.fastjson2.JSONObject
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject phoneLogin(Map<String, Object> map, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        DoctorUser doctorUser = new DoctorUser();
        String accessToken = UUID.randomUUID().toString().replace("-", "");
        String redisKey = RedisConstant.DOCKER_LOGIN_KEY + accessToken;
        String oldToken = "";
        //把验证码与手机号和用户发送的作比较
        String mobile = (String) map.get("mobile");
        String code = (String) map.get("code");
        String checkCode;
        String sendCodeKey = "doctor_login_" + mobile;
        if (testProperties.getMobile().equals(mobile)) {
            checkCode = testProperties.getCode();
        } else {
            if (!redisService.exists(sendCodeKey)) {
                jsonObject.put("is_succ", 0);
                jsonObject.put("msg", "验证码已过期");
                return jsonObject;
            }
            //接收redis中的验证码
            checkCode = (String) redisService.get(sendCodeKey);
        }
        if (null == checkCode || "".equals(checkCode)) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "验证码已过期");
        } else if (!code.equals(checkCode)) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "验证码错误");
        } else {
            QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
            wrapper.eq("mobile", mobile).eq("is_cancel", "0");
            doctorUser = doctorUserMapper.selectOne(wrapper);
            if (StringUtils.isEmpty(doctorUser.getStatus()) || doctorUser.getStatus().equals("0")) {
                jsonObject.put("is_succ", 0);
                jsonObject.put("msg", "您已经被禁用");
            } else if ("PASS".equals(doctorUser.getIsAuth())) {
                oldToken = doctorUser.getToken();

                //转JSON格式
                jsonObject.put("id", ValueFormatUtils.toString(doctorUser.getId()));
                jsonObject.put("createTime", ValueFormatUtils.toString(doctorUser.getCreateTime()));
                jsonObject.put("updateTime", ValueFormatUtils.toString(doctorUser.getUpdateTime()));
                jsonObject.put("mobile", ValueFormatUtils.toString(CodeUtils.stringSixMask(doctorUser.getMobile())));
                jsonObject.put("headImg", ValueFormatUtils.toString(doctorUser.getHeadImg()));
                jsonObject.put("name", ValueFormatUtils.toString(doctorUser.getName()));
                jsonObject.put("tag", ValueFormatUtils.toString(doctorUser.getTag()));
                jsonObject.put("hospitalName", ValueFormatUtils.toString(doctorUser.getHospitalName()));
                jsonObject.put("beGoodAtText", ValueFormatUtils.toString(doctorUser.getBeGoodAtText()));
                jsonObject.put("introductionText", ValueFormatUtils.toString(doctorUser.getIntroductionText()));
                jsonObject.put("token", accessToken);
                jsonObject.put("idCard", CodeUtils.idCardMask(doctorUser.getPapersNumbers()));
                QueryWrapper<DoctorSet> doctorSetQueryWrapper = new QueryWrapper<>();
                doctorSetQueryWrapper.eq("doctor_user_id", doctorUser.getId());
                DoctorSet doctorSet = doctorSetMapper.selectOne(doctorSetQueryWrapper);
                if (doctorSet == null) {
                    jsonObject.put("isImg", "0");
                    jsonObject.put("isRevisit", "0");
                    jsonObject.put("isMdt", "0");
                    jsonObject.put("imgPrice", null);
                } else {
                    jsonObject.put("isImg", doctorSet.getIsImg());
                    jsonObject.put("isRevisit", doctorSet.getIsRevisit());
                    jsonObject.put("isMdt", doctorSet.getIsMdt());
                    jsonObject.put("imgPrice", doctorSet.getImgPrice());
                }
                //缓存到redis里面
                redisService.set(redisKey, doctorUser, RedisConstant.TOKEN_SURVIVE_TIME);

                UpdateWrapper<DoctorUser> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("mobile", mobile);
                updateWrapper.eq("status", 1);
                updateWrapper.eq("is_auth", "PASS");
                updateWrapper.eq("is_cancel", "0");
                updateWrapper.set("token", accessToken);
                if (null == doctorUser.getUserSig() || doctorUser.getUserSig().isEmpty()) {
                    String imUserId = String.format(ImUserPrefix.USER_ID_FORMAT, ImUserPrefix.DOCTOR, doctorUser.getId());
                    String userSig = ImUtils.genUserSig(imUserId, null, tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
                    DoctorUser saveUserSig = new DoctorUser();
                    saveUserSig.setUserSig(userSig);
                    doctorUserMapper.update(saveUserSig, updateWrapper);
                } else {
                    doctorUserMapper.update(null, updateWrapper);
                }
                doctorUser = doctorUserMapper.selectOne(new QueryWrapper<DoctorUser>().eq("id", doctorUser.getId()));
                if (null == doctorUser) {
                    jsonObject.put("is_succ", 0);
                    jsonObject.put("msg", "查不到此用户信息");
                    return jsonObject;
                }
                jsonObject.put("userSig", doctorUser.getUserSig());
                //将redis中的验证码进行删除
                redisService.remove(sendCodeKey);
                if (!"".equals(oldToken)) {
                    //将redis中的键进行删除
                    redisService.remove(RedisConstant.DOCKER_LOGIN_KEY + oldToken);
                }
                jsonObject.put("token", accessToken);
                jsonObject.put("is_succ", 1);
                UserAgentGetter userAgentGetter = new UserAgentGetter(request);
                DoctorUserLog log = new DoctorUserLog();
                log.setActionType("LOGIN");
                log.setDoctorUserId(doctorUser.getId());
                log.setOsName(userAgentGetter.getOS());
                log.setIpAddress(userAgentGetter.getIpAddr());
                log.setRemark("登陆成功");
                String cityNameByTaoBaoAPI = AdderssUtils.getCityNameByTaoBaoAPI(userAgentGetter.getIpAddr());
                log.setCity(cityNameByTaoBaoAPI);
                doctorUserLogMapper.insert(log);
            } else {
                jsonObject.put("is_succ", 0);
                jsonObject.put("msg", "此账号还未注册或者未进行CA认证");
            }
        }
        return jsonObject;
    }

    @Override
    public JSONObject versionsUpdate(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        DVersion dVersion = dVersionMapper.versionsUpdate(map);
        if (null == dVersion) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "未查到版本信息");
            return jsonObject;
        }
        int result = dVersion.getNumber().compareTo(map.get("number").toString());
        if (result > 0) {
            jsonObject.put("hasUpdate", "1");
        } else {
            jsonObject.put("hasUpdate", "0");
        }
        jsonObject.put("id", dVersion.getId());
        jsonObject.put("type", dVersion.getType());
        jsonObject.put("number", dVersion.getNumber());
        jsonObject.put("name", dVersion.getName());
        jsonObject.put("isForce", dVersion.getIsForce());
        jsonObject.put("appType", dVersion.getAppType());
        jsonObject.put("downloadUrl", dVersion.getDownloadUrl());
        jsonObject.put("content", dVersion.getContent());
        jsonObject.put("pushed", dVersion.getPushed());
        jsonObject.put("publishTime", dVersion.getPublishTime());
        jsonObject.put("is_succ", 1);
        return jsonObject;
    }


    @Override
    public boolean sendCode(String mobile) {
        // 生成6位随机数
        String code = RandomUtil.randomNumbers(6);
        Map<String, String> template = Map.of("code", code);
        boolean result = aliCloudComponent.sendSms(mobile, JSONUtil.toJson(template));
        if (result) {
            // 把验证码设置到redis里面3分钟
            String key = "send_code_" + mobile;
            redisService.set(key, code, 180);
        }
        return result;
    }

    @Override
    public DoctorUserVo phoneLogin(String mobile, String code, HttpServletRequest request) {

        String checkCode;
        String sendCodeKey = "send_code_" + mobile;
        if (testProperties.getCode().equals(code)) {
            checkCode = testProperties.getCode();
        } else {
            if (!redisService.exists(sendCodeKey)) {
                throw new BizException(BizResultCode.SMS_CODE_EXPIRED);
            }
            //接收redis中的验证码
            checkCode = (String) redisService.get(sendCodeKey);
        }
        if (!code.equals(checkCode)) {
            throw new BizException(BizResultCode.SMS_CODE_ERROR);
        }

        //将redis中的验证码进行删除
        redisService.remove(sendCodeKey);

        QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile).eq("is_cancel", "0");
        DoctorUser doctorUser = doctorUserMapper.selectOne(wrapper);
        // 校验状态
        if (null == doctorUser) {
            throw new BizException(BizResultCode.DOCTOR_MOBILE_NOT_BOUND);
        }
        if ("0".equals(doctorUser.getStatus())) {
            throw new BizException(BizResultCode.DOCTOR_ACCOUNT_DISABLED);
        }
        if (!"PASS".equals(doctorUser.getIsAuth())) {
            throw new BizException(BizResultCode.DOCTOR_ACCOUNT_NO_AUTH);
        }

        // 更新token
        String oldToken = doctorUser.getToken();
        String accessToken = UUID.randomUUID().toString().replace("-", "");
        UpdateWrapper<DoctorUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("mobile", mobile);
        updateWrapper.eq("status", 1);
        updateWrapper.eq("is_auth", "PASS");
        updateWrapper.eq("is_cancel", "0");
        updateWrapper.set("token", accessToken);
        if (StringUtil.isBlank(doctorUser.getUserSig())) {
            String imUserId = String.format(ImUserPrefix.USER_ID_FORMAT, ImUserPrefix.DOCTOR, doctorUser.getId());
            String userSig = ImUtils.genUserSig(imUserId, null, tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
            updateWrapper.set("user_sig", userSig);
        }
        doctorUserMapper.update(updateWrapper);

        // 添加记录
        UserAgentGetter userAgentGetter = new UserAgentGetter(request);
        DoctorUserLog log = new DoctorUserLog();
        log.setActionType("LOGIN");
        log.setDoctorUserId(doctorUser.getId());
        log.setOsName(userAgentGetter.getOS());
        log.setIpAddress(userAgentGetter.getIpAddr());
        log.setRemark("登陆成功");
        String cityNameByTaoBaoAPI = AdderssUtils.getCityNameByTaoBaoAPI(userAgentGetter.getIpAddr());
        log.setCity(cityNameByTaoBaoAPI);
        doctorUserLogMapper.insert(log);
        // 刷新数据
        doctorUser = doctorUserMapper.selectOne(new QueryWrapper<DoctorUser>().eq("id", doctorUser.getId()));

        // 缓存数据
        String redisKey = RedisConstant.DOCKER_LOGIN_KEY + accessToken;
        redisService.set(redisKey, doctorUser, RedisConstant.TOKEN_SURVIVE_TIME);
        if (StringUtil.isNotBlank(oldToken)) {
            redisService.remove(RedisConstant.DOCKER_LOGIN_KEY + oldToken);
        }
        // 返回VO
        return convert2Vo(doctorUser);
    }

    private DoctorUserVo convert2Vo(DoctorUser doctorUser) {
        DoctorUserVo userVo = new DoctorUserVo();
        BeanUtils.copyProperties(doctorUser, userVo);
        // 手机号
        userVo.setMobile(CodeUtils.stringSixMask(doctorUser.getMobile()));
        // 身份证
//        doctorUser.setPapersNumbers(CodeUtils.idCardMask(doctorUser.getPapersNumbers()));

        // 开方数
        userVo.setPrescriptionCount(prescriptionService.countByDoctorUserId(doctorUser.getId()));
        // 患者数
        userVo.setVisitCount(hosVisitService.countByDoctorUserId(doctorUser.getId()));
        // 评价数
        userVo.setProposalCount(proposalService.countByDoctorUserId(doctorUser.getId()));
        return userVo;
    }

    @Override
    public DoctorVisitSet getVisitSet() {
        String loginUserId = AuthUtil.getLoginUserId();
        return doctorVisitSetService.getOne(new LambdaQueryWrapper<DoctorVisitSet>()
                .eq(DoctorVisitSet::getDoctorUserId, loginUserId));
    }

    @Override
    public DoctorVisitSet setVisit(DoctorVisitSetRequest request) {
        String loginUserId = AuthUtil.getLoginUserId();
        DoctorVisitSet doctorVisitSet = doctorVisitSetService.getOne(new LambdaQueryWrapper<DoctorVisitSet>()
                .eq(DoctorVisitSet::getDoctorUserId, loginUserId));

        if (ObjectUtils.isEmpty(doctorVisitSet)) {
            doctorVisitSet = new DoctorVisitSet();
        }
        BeanUtils.copyProperties(request, doctorVisitSet);
        doctorVisitSetService.saveOrUpdate(doctorVisitSet);
        return doctorVisitSet;
    }

    @Override
    public DoctorWelcomeMessageSet getWelcomeMessage() {
        String loginUserId = AuthUtil.getLoginUserId();
        return doctorWelcomeMessageSetService.getOne(new LambdaQueryWrapper<DoctorWelcomeMessageSet>()
                .eq(DoctorWelcomeMessageSet::getDoctorUserId, loginUserId));
    }

    @Override
    public DoctorWelcomeMessageSet setWelcomeMessage(DoctorWelcomeMessageRequest request) {
        String loginUserId = AuthUtil.getLoginUserId();
        DoctorWelcomeMessageSet welcomeMessageSet = doctorWelcomeMessageSetService.getOne(new LambdaQueryWrapper<DoctorWelcomeMessageSet>()
                .eq(DoctorWelcomeMessageSet::getDoctorUserId, loginUserId));

        if (ObjectUtils.isEmpty(welcomeMessageSet)) {
            welcomeMessageSet = new DoctorWelcomeMessageSet();
        }
        BeanUtils.copyProperties(request, welcomeMessageSet);
        doctorWelcomeMessageSetService.saveOrUpdate(welcomeMessageSet);
        return welcomeMessageSet;
    }

    @Override
    public DoctorUserVo detail(String id) {
        DoctorUser loginUser = null;
        if (AuthUtil.getLoginUser() != null) {
            loginUser = doctorUserMapper.selectById(AuthUtil.getLoginUser().getId());
        }
        if (loginUser == null) {
            loginUser = doctorUserMapper.selectById(id);
        }
        return convert2Vo(loginUser);
    }

    @Override
    public Object list(Map<String, Object> map) {
        return null;
    }

    @Override
    public DoctorUser findDoctorUserById(String id) {
        return doctorUserMapper.selectById(id);
    }

    @Override
    public List<DoctorUser> findDoctorUserAll() {
        return doctorUserMapper.selectList(null);
    }
}
