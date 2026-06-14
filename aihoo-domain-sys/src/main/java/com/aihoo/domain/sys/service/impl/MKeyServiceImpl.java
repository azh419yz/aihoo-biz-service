package com.aihoo.domain.sys.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aihoo.constant.ImUserPrefix;
import com.aihoo.properties.CaProperties;
import com.aihoo.properties.TencentProperties;
import com.aihoo.enums.LoginErrorCodeEnum;
import com.aihoo.enums.UserRoleEnum;
import com.aihoo.util.SecurityUtils;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.sys.model.mapper.SysUserMapper;
import com.aihoo.domain.sys.model.mapper.SysUserRoleMapper;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.domain.sys.model.entity.SysUserRole;
import com.aihoo.domain.sys.service.LoginRecordService;
import com.aihoo.domain.sys.service.MKeyService;
import com.aihoo.util.ImUtils;
import com.aihoo.common.JsonResult;
import com.aihoo.exception.BizException;
import com.aihoo.redis.RedisService;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.aihoo.domain.sys.service.impl.SysUserServiceImpl.DEFAULT_PSW;

/**
 * @author Lenovo
 */
@Service
public class MKeyServiceImpl implements MKeyService {
    private Log log = LogFactory.get();
    @Resource
    private CaProperties caProperties;
    @Resource
    private RedisService redisService;
    @Resource
    private DoctorUserMapper doctorUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private LoginRecordService loginRecordService;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private TencentProperties tencentProperties;
    @Autowired
    private UserDetailsService userDetailsService;

    //生成成功
    private final static String bizSnHead = "QRCODE_";
    //扫码成功
    private final static String confirmHead = "CONFIRM_";
    //登录成功
    private final static String bizSnLogin = "QRCODE_LOGIN_";
    //过期时间
    private final static Integer redisSecond = 300;

    /**
     * 获取二维码
     *
     * @return
     */
    @Override
    public JSONObject qrCode(HttpServletRequest request) throws UnsupportedEncodingException {
        String url = caProperties.getOpenUrl() + "/v1/qrcode/gen";
        String appId = caProperties.getAppId();
        String bizSn = UUID.randomUUID().toString();
        JSONObject msg = new JSONObject();
        msg.put("bizSn", bizSn);
        msg.put("date", DateUtil.now());
        msg.put("ip", request.getRemoteAddr());
        Map<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        param.put("action", "login");
        param.put("bizSn", bizSn);
        param.put("msg", Base64.encode(msg.toJSONString()));
        param.put("msgWrapper", "0");
        //todo 测试
//        param.put("url", "https://testadmin.mshhospital.com/api/v1/mkey/login");
        //todo 正式
        param.put("url", caProperties.getCallBack() + "/api/v1/mkey/login");
        // 2022-03-01 用云医签扫码，提示证书登录失败，第三方建议：把forword改成redirect试下
        param.put("mode", "redirect");
        // param.put("mode", "forward");
        String response = HttpUtil.get(url, param);
        log.info("response == {}", response);
        if (null == response) {
            log.error("无响应");
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject != null) {
            String ret = jsonObject.get("ret").toString();
            if (ret.equals("success")) {
                JSONObject data = JSONObject.parseObject(jsonObject.get("data").toString());
                JSONObject result = new JSONObject();
                result.put("bizSn", bizSn);
                redisService.set(bizSnHead + bizSn, result.toJSONString(), redisSecond);
                result.put("qrcode", "data:image/png;base64," + URLDecoder.decode(data.get("qrcode").toString(), "UTF-8"));
                log.info(JSONObject.toJSONString(redisService.get(bizSnHead + bizSn)));
                return result;
            }
        }
        return null;
    }

    @Override
    public void qrcodeLogin(String bizSn, String action, String cert, String signAlg, String signValue, String id) {
        log.info("bizSn == " + bizSn);
        log.info("action == " + action);
        log.info("cert == " + cert);
        log.info("signAlg == " + signAlg);
        log.info("signValue == " + signValue);
        log.info("id == " + id);
        Object o = redisService.get(bizSnHead + bizSn);
        if (null == o) {
            log.error("扫码登录回调：二维码已过期，请重新获取二维码");
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(o.toString());
        jsonObject.put("bizSn", bizSn);
        jsonObject.put("action", action);
        jsonObject.put("cert", cert);
        jsonObject.put("signAlg", signAlg);
        jsonObject.put("signValue", signValue);
        jsonObject.put("id", id);
        if (action.equals("confirm")) {
            redisService.set(confirmHead + bizSn, jsonObject.toJSONString(), redisSecond);
            log.info("扫码类型：已扫码通知");
            return;
        } else if (!action.equals("login") || (null == bizSn || null == action || null == cert || null == signAlg || null == signValue)) {
            throw new BizException("二维码登录错误");
        }
        log.info("扫码类型：登录扫码");
        redisService.set(bizSnLogin + bizSn, jsonObject.toJSONString(), redisSecond);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult qrcodeStatus(String bizSn, HttpServletRequest request) {
        Object o = redisService.get(bizSnLogin + bizSn);
        if (null != o) {
            log.info("扫码回调成功！");
        } else if (null != redisService.get(confirmHead + bizSn)) {
            return JsonResult.error(LoginErrorCodeEnum.QRCODE_CONFIRM, "扫码成功，请授权登录!");
        } else if (null != redisService.get(bizSnHead + bizSn)) {
            return JsonResult.error(LoginErrorCodeEnum.QRCODE_WAIT, "请扫码登录!");
        } else {
            return JsonResult.error(LoginErrorCodeEnum.QRCODE_TIMEOUT, "二维码已过期，请重新获取!");
        }
        JSONObject jsonObject = JSONObject.parseObject(o.toString());
        String id = jsonObject.get("id").toString();
        LambdaQueryWrapper<DoctorUser> lambda = new QueryWrapper<DoctorUser>().lambda();
        lambda.eq(DoctorUser::getPapersNumbers, id);
        lambda.eq(DoctorUser::getIsCancel, "0");
        DoctorUser doctorUser = doctorUserMapper.selectOne(lambda);
        if (null == doctorUser) {
            return JsonResult.error(LoginErrorCodeEnum.QRCODE_ERROR, "未创建或已注销,请联系管理员!");
        } else if (doctorUser.getStatus().equals("0")) {
            return JsonResult.error(LoginErrorCodeEnum.QRCODE_ERROR, "已被禁用,请联系管理员!");
        }
        if (!doctorUser.getIsAuth().equals("PASS")) {
            DoctorUser update = new DoctorUser();
            update.setIsAuth("PASS");
            LambdaQueryWrapper<DoctorUser> updateWrapper = new QueryWrapper<DoctorUser>().lambda();
            updateWrapper.eq(DoctorUser::getId, doctorUser.getId());
            doctorUserMapper.update(update, updateWrapper);
            log.info("医生认证状态更新为：已认证");
        }
        //查询医生对应的系统账户
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new QueryWrapper<SysUser>().lambda();
        sysUserLambdaQueryWrapper.eq(SysUser::getIdCard, doctorUser.getPapersNumbers()).eq(SysUser::getDeleted, "0");
        SysUser user = sysUserMapper.selectOne(sysUserLambdaQueryWrapper);
        if (StringUtils.isEmpty(user)) {
            log.info("当前医生不存在,自动创建用户!");
            user = addUser(doctorUser);
        }
        if (user.getStatus().equals("1")) {
            return JsonResult.error(LoginErrorCodeEnum.QRCODE_ERROR, "当前用户已冻结，请联系管理员!");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        loginRecordService.addLoginRecord(String.valueOf(SecurityUtils.getLoginUserId()), request);
        return JsonResult.ok().put("data", objectToJson(user));
    }


    public SysUser addUser(DoctorUser doctorUser) {
        SysUser user = new SysUser();
        user.setTrueName(doctorUser.getName());
        user.setUserName(doctorUser.getMobile());
        user.setPhone(doctorUser.getMobile());
        user.setPasswordUpdate(DateUtil.now());
        user.setNickName(doctorUser.getName());
        user.setPassword(SecurityUtils.encryptPassword(DEFAULT_PSW));
        user.setEmailVerified("1");
        user.setAvatar(doctorUser.getHeadImg());
        user.setSex(doctorUser.getSex().equals("0") ? "女" : "男");
        user.setIdCard(doctorUser.getPapersNumbers());
        user.setBirthday(doctorUser.getBirthday());
        user.setCreateUser("-1");
        boolean rs = sysUserMapper.insert(user) > 0;
        if (rs) {
            String sysUserId = user.getId();
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(Integer.parseInt(UserRoleEnum.HZZLYS.getCode()));
            sysUserRole.setUserId(Integer.parseInt(sysUserId));
            int insert = sysUserRoleMapper.insert(sysUserRole);
            String imUserId = String.format(ImUserPrefix.USER_ID_FORMAT, ImUserPrefix.ADMIN, user.getId());
            String userSig = ImUtils.genUserSig(imUserId, null, tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
            SysUser sysUser = new SysUser();
            sysUser.setUserSig(userSig);
            sysUser.setId(user.getId());
            int i = sysUserMapper.updateById(sysUser);
            if (0 == i) {
                throw new BizException("userSig生成失败");
            }
            if (insert == 0) {
                throw new BizException("添加失败，请重试");
            }
            user.setUserSig(userSig);
            user.setStatus("0");
            return user;
        } else {
            throw new BizException("用户创建失败");
        }
    }

    private JSONObject objectToJson(SysUser user) {
        JSONObject res = new JSONObject();
        res.put("id", user.getId());
        res.put("name", user.getUserName());
        res.put("nickName", user.getNickName());
        res.put("avatar", user.getAvatar());
        res.put("sex", user.getSex());
        res.put("userSig", user.getUserSig());
        res.put("doctorId", "");
        res.put("doctorUserSig", "");
        if (StrUtil.isNotBlank(user.getIdCard())) {
            String idCard = user.getIdCard();
            LambdaQueryWrapper<DoctorUser> lambda = new QueryWrapper<DoctorUser>().lambda();
            lambda.eq(DoctorUser::getStatus, "1");
            lambda.eq(DoctorUser::getIsCancel, "0");
            lambda.eq(DoctorUser::getPapersNumbers, idCard);
            DoctorUser doctorUser = doctorUserMapper.selectOne(lambda);
            if (ObjectUtil.isNotEmpty(doctorUser)) {
                res.put("doctorId", doctorUser.getId());
                res.put("doctorUserSig", doctorUser.getUserSig());
            }
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime passwordUpdate = LocalDateTime.parse(user.getPasswordUpdate(), df);
        if (passwordUpdate.plusDays(9000).isBefore(LocalDateTime.now())) {
            // 用户密码更改的上次时间在当前时间之前，提示用户修改密码
            res.put("hint", "您的登陆密码已有90天未作修改，请修改密码");
        } else {
            res.put("hint", "");
        }
        return res;
    }

}

