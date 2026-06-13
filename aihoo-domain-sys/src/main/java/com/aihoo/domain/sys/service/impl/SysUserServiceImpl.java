package com.aihoo.domain.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aihoo.util.SecurityUtils;
import com.aihoo.domain.sys.model.dto.*;
import com.aihoo.domain.sys.model.vo.LoginVo;
import com.aihoo.domain.sys.model.vo.SysUserVo;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.sys.model.mapper.SysUserMapper;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.domain.sys.service.LoginRecordService;
import com.aihoo.domain.hospital.service.SysUserDrugstoreService;
import com.aihoo.domain.sys.service.SysUserService;
import com.aihoo.alicloud.AliCloudComponent;
import com.aihoo.util.StringUtil;
import com.aihoo.common.BizResultCode;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.exception.BizException;
import com.aihoo.domain.hospital.model.entity.SysUserDrugstoreRel;
import com.aihoo.redis.RedisConstant;
import com.aihoo.redis.RedisService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.aihoo.util.SecurityUtils.getLoginUser;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    public static final String DEFAULT_PSW = "abc!1234";  // 用户默认密码
    @Autowired
    private RedisService redisService;
    @Resource
    private DoctorUserMapper doctorUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SysUserDrugstoreService sysUserDrugstoreService;
    @Autowired
    private AliCloudComponent aliCloudComponent;

    @Override
    public SysUser getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public PageResult<SysUserVo> listUser(PageParam<SysUser> pageParam, SearchUserRequest request) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .like(StringUtil.isNotBlank(request.getTrueName()), SysUser::getTrueName, request.getTrueName())
                .eq(StringUtil.isNotBlank(request.getPhone()), SysUser::getPhone, request.getPhone())
                .orderByDesc(SysUser::getCreatedDate);

        // 执行分页查询
        Page<SysUser> page = baseMapper.selectPage(pageParam, queryWrapper);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new PageResult<>();
        }

        List<SysUserVo> voList = new ArrayList<>();
        for (SysUser user : page.getRecords()) {
            SysUserVo vo = new SysUserVo();
            BeanUtils.copyProperties(user, vo);
            List<String> drugstoreIdList = sysUserDrugstoreService.list(new LambdaQueryWrapper<SysUserDrugstoreRel>()
                            .eq(SysUserDrugstoreRel::getUserId, user.getId()))
                    .stream().map(SysUserDrugstoreRel::getDrugstoreId).toList();
            vo.setDrugstoreIdList(drugstoreIdList);
            voList.add(vo);
        }
        return new PageResult<>(voList, page.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addUser(SaveUpdateUserRequest request) {
        String phone = request.getPhone();
        SysUser user = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, phone));
        if (user != null) {
            throw new BizException(BizResultCode.USER_MOBILE_EXIST);
        }
        user = new SysUser();
        user.setTrueName(request.getTrueName());
        user.setPhone(phone);
        user.setUserName(phone);
        user.setPasswordUpdate(LocalDateTime.now().toString());
        user.setPassword(SecurityUtils.encryptPassword(DEFAULT_PSW));
        user.setCreateUser(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        user.setPermission(request.getPermission() == null ? 0 : request.getPermission());
        boolean rs = save(user);

        List<String> drugstoreIdList = request.getDrugstoreIdList();
        if (!CollectionUtils.isEmpty(drugstoreIdList)) {
            SysUser finalUser = user;
            List<SysUserDrugstoreRel> drugstoreRelList = drugstoreIdList.stream().map(drugstoreId -> {
                SysUserDrugstoreRel drugstoreRel = new SysUserDrugstoreRel();
                drugstoreRel.setDrugstoreId(drugstoreId);
                drugstoreRel.setUserId(finalUser.getId());
                return drugstoreRel;
            }).toList();
            sysUserDrugstoreService.saveBatch(drugstoreRelList);
        }

        return rs;
    }

    // 用逗号分割角色
    private List<Integer> getRoleIds(String rolesStr) {
        List<Integer> roleIds = new ArrayList<>();
        if (rolesStr != null) {
            String[] split = rolesStr.split(",");
            for (String t : split) {
                try {
                    roleIds.add(Integer.parseInt(t));
                } catch (Exception e) {
                }
            }
        }
        return roleIds;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SaveUpdateUserRequest request) {
        if (StringUtil.isNotBlank(request.getPhone())) {
            String phone = request.getPhone();
            SysUser user = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, phone));
            if (user != null && !user.getId().equals(request.getId())) {
                throw new BizException(BizResultCode.USER_MOBILE_EXIST);
            }
        }
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, request.getId())
                .set(StringUtil.isNotBlank(request.getPhone()), SysUser::getPhone, request.getPhone())
                .set(StringUtil.isNotBlank(request.getTrueName()), SysUser::getTrueName, request.getTrueName())
                .set(request.getPermission() != null, SysUser::getPermission, request.getPermission());
        boolean rs = update(updateWrapper);

        sysUserDrugstoreService.remove(new LambdaQueryWrapper<SysUserDrugstoreRel>()
                .eq(SysUserDrugstoreRel::getUserId, request.getId()));
        List<String> drugstoreIdList = request.getDrugstoreIdList();
        if (!CollectionUtils.isEmpty(drugstoreIdList)) {
            List<SysUserDrugstoreRel> drugstoreRelList = drugstoreIdList.stream().map(drugstoreId -> {
                SysUserDrugstoreRel drugstoreRel = new SysUserDrugstoreRel();
                drugstoreRel.setDrugstoreId(drugstoreId);
                drugstoreRel.setUserId(request.getId());
                return drugstoreRel;
            }).toList();
            sysUserDrugstoreService.saveBatch(drugstoreRelList);
        }

        return rs;
    }

    @Override
    public boolean updateStatus(Map<String, Object> map) {
        SysUser user = new SysUser();
        user.setId(map.get("userId").toString());
        user.setStatus(map.get("status").toString());
        return sysUserMapper.updateById(user) > 0;
    }

    @Override
    public boolean resetPsw(Map<String, Object> map) {
        SysUser user = new SysUser();
        user.setId(map.get("userId").toString());
        user.setPassword(SecurityUtils.encryptPassword(DEFAULT_PSW));
        user.setPasswordUpdate(LocalDateTime.now().toString());
        return sysUserMapper.updateById(user) > 0;
    }

    @Override
    public boolean updatePsw(Map<String, Object> map) {
        SysUser sysUser = new SysUser();
        sysUser.setId(Objects.requireNonNull((SysUser) getLoginUser()).getId());
        sysUser.setPassword(SecurityUtils.encryptPassword(map.get("newPsw").toString()));
        sysUser.setPasswordUpdate(LocalDateTime.now().toString());
        return sysUserMapper.updateById(sysUser) > 0;
    }

    @Override
    public boolean isDelete(String id) {
        return removeById(id);
    }

    //特殊 字符 为~!@#$%^&*其中之一。
    public static final String PASSWORD = "(?=.*[a-z])(?=.*\\d)(?=.*[#@!~%^&*])[a-z\\d#@!~%^&*]{8,16}";

    @Override
    public LoginVo doLogin(LoginRequest request, HttpServletRequest httpRequest) {
        String userName = request.getUserName().trim();
        String password = request.getPassword();

        if (!password.matches(PASSWORD)) {
            throw new BizException(BizResultCode.INTERNAL_ERROR, "密码格式错误! 密码最短为8位，必须包含字母数字和特殊符号(~!@#$%^&*)三种组合。");
        }

        SysUser one = this.sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("user_name", userName));
        if (null == one) {
            throw new BizException(BizResultCode.INTERNAL_ERROR, "用户名不存在!");
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String userLockTime = one.getUserLockTime();
        if (null != userLockTime) {
            LocalDateTime time = LocalDateTime.now();
            LocalDateTime userLockTimes = LocalDateTime.parse(userLockTime, df);
            if (time.isBefore(userLockTimes)) {
                //在限制登录时间之内
                throw new BizException(BizResultCode.INTERNAL_ERROR, "密码输入错误5次，请30分钟后重试！！！");
            } else if (Integer.parseInt(one.getErrorCount()) >= 5) {
                // 超出登录时间，密码失败标记修改为0
                SysUser user = new SysUser();
                user.setErrorCount("0");
                this.sysUserMapper.update(user, new QueryWrapper<SysUser>().eq("user_name", userName));
            }
        }

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Token based login
            SysUser user = (SysUser) getLoginUser();
            String token = UUID.randomUUID().toString().replace("-", "");

            // Store user in Redis with token as key
            String redisKey = RedisConstant.ADMIN_LOGIN_KEY + token;
            redisService.set(redisKey, user, RedisConstant.SESSION_SURVIVE_TIME);

            loginRecordService.addLoginRecord(String.valueOf(SecurityUtils.getLoginUserId()), httpRequest);
            assert user != null;
            // 登录成功错误次数设置为零
            SysUser userError = new SysUser();
            userError.setErrorCount("0");
            userError.setId(user.getId());
            this.sysUserMapper.updateById(userError);

            return buildLoginVo(user, token);

        } catch (AccessDeniedException e) {
            throw new BizException(BizResultCode.INTERNAL_ERROR, "您没有对应的权限，如有疑问请联系管理员！");
        } catch (BadCredentialsException ice) {
            // 密码错误增加该用户密码输入错误次数
            Map<String, Object> errMap = new HashMap<>();
            errMap.put("userName", userName);
            this.updateErrorCount(errMap);
            throw new BizException(BizResultCode.INTERNAL_ERROR, "密码错误");
        } catch (UsernameNotFoundException uae) {
            throw new BizException(BizResultCode.INTERNAL_ERROR, "账号不存在");
        } catch (LockedException e) {
            throw new BizException(BizResultCode.INTERNAL_ERROR, "账号被锁定");
        } catch (DisabledException eae) {
            throw new BizException(BizResultCode.INTERNAL_ERROR, "账号已被禁用");
        }
    }

    @Override
    public void sendPhoneCode(SendPhoneCodeRequest request) {
        String phone = request.getPhone();
        List<SysUser> list = this.sysUserMapper.selectList(
                new QueryWrapper<SysUser>().eq("phone", phone).eq("deleted", 0).eq("status", 0));
        if (CollectionUtils.isEmpty(list)) {
            throw new BizException(BizResultCode.INTERNAL_ERROR, "非系统用户或者此号码所属用户未在启用状态");
        }

        // 生成6位随机数
        String code = RandomUtil.randomNumbers(6);
        Map<String, String> template = Map.of("code", code);
        boolean result = aliCloudComponent.sendSms(phone, JSONUtil.toJsonStr(template));
        if (result) {
            // 把验证码设置到redis里面3分钟
            boolean boo = this.redisService.set(RedisConstant.ADMIN_PHONE_CODE + phone, code, RedisConstant.ADMIN_PHONE_CODE_EXPIRATION_TIME);
            if (!boo) {
                throw new BizException(BizResultCode.INTERNAL_ERROR, "验证码存储失败");
            }
        } else {
            throw new BizException(BizResultCode.INTERNAL_ERROR, "发送失败！");
        }
    }

    @Override
    public LoginVo phoneLogin(PhoneLoginRequest request, HttpServletRequest httpRequest) {
        String phone = request.getPhone();
        String code = request.getCode();

        // 特定验证码直接登录
        if (!"88888888".equals(code)) {
            String cachedCode = (String) this.redisService.get(RedisConstant.ADMIN_PHONE_CODE + phone);
            if (null == cachedCode) {
                throw new BizException(BizResultCode.INTERNAL_ERROR, "验证码已失效，请重新发送");
            }
            if (!code.equals(cachedCode)) {
                throw new BizException(BizResultCode.INTERNAL_ERROR, "验证码错误！");
            }
        }

        SysUser user = this.sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("phone", phone));
        if (ObjectUtil.isEmpty(user)) {
            throw new BizException(BizResultCode.INTERNAL_ERROR, "当前手机号不存在的用户!");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        loginRecordService.addLoginRecord(String.valueOf(user.getId()), httpRequest);

        // Token based login
        String token = UUID.randomUUID().toString().replace("-", "");
        // Store user in Redis with token as key
        String redisKey = RedisConstant.ADMIN_LOGIN_KEY + token;
        redisService.set(redisKey, user, RedisConstant.SESSION_SURVIVE_TIME);

        return buildLoginVo(user, token);
    }

    private LoginVo buildLoginVo(SysUser user, String token) {
        LoginVo vo = new LoginVo();
        BeanUtils.copyProperties(user, vo);
        vo.setName(user.getUserName());
        vo.setDoctorId("");
        vo.setDoctorUserSig("");
        if (StrUtil.isNotBlank(user.getIdCard())) {
            String idCard = user.getIdCard();
            LambdaQueryWrapper<DoctorUser> lambda = new QueryWrapper<DoctorUser>().lambda();
            lambda.eq(DoctorUser::getStatus, "1");
            lambda.eq(DoctorUser::getIsCancel, "0");
            lambda.eq(DoctorUser::getPapersNumbers, idCard);
            DoctorUser doctorUser = doctorUserMapper.selectOne(lambda);
            if (ObjectUtil.isNotEmpty(doctorUser)) {
                vo.setDoctorId(doctorUser.getId());
                vo.setDoctorUserSig(doctorUser.getUserSig());
            }
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime passwordUpdate = LocalDateTime.parse(user.getPasswordUpdate(), df);
        if (passwordUpdate.plusDays(90000).isBefore(LocalDateTime.now())) {
            // 用户密码更改的上次时间在当前时间之前，提示用户修改密码
            vo.setHint("您的登陆密码已有90天未作修改，请修改密码");
        } else {
            vo.setHint("");
        }
        vo.setToken("heou-" + token);
        return vo;
    }

    @Override
    public void updateErrorCount(Map<String, Object> map) {
        SysUser one = this.sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("user_name", map.get("userName").toString().trim()));
        SysUser user = new SysUser();
        int errorCount = Integer.parseInt(one.getErrorCount());
        if (4 == errorCount) {
            user.setUserLockTime(LocalDateTime.now().plusMinutes(30).toString());
        }
        errorCount = errorCount + 1;
        user.setErrorCount(String.valueOf(errorCount));
        this.sysUserMapper.update(user, new QueryWrapper<SysUser>().eq("user_name", map.get("userName").toString().trim()));
    }

}
