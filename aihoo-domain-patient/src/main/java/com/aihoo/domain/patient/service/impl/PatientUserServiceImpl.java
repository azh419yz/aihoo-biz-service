package com.aihoo.domain.patient.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;

import com.aihoo.common.PageResult;
import com.aihoo.domain.patient.config.PaProperties;
import com.aihoo.domain.patient.model.entity.Address;
import com.aihoo.domain.patient.model.entity.HosSickRef;
import com.aihoo.domain.patient.model.entity.PatientUser;
import com.aihoo.domain.patient.model.entity.PatientUserLog;
import com.aihoo.domain.patient.model.entity.PatientUserVo;
import com.aihoo.domain.patient.model.excel.PatientUserEntity;
import com.aihoo.domain.patient.model.mapper.AddressMapper;
import com.aihoo.domain.patient.model.mapper.PatientUserLogMapper;
import com.aihoo.domain.patient.model.mapper.PatientUserMapper;
import com.aihoo.domain.patient.model.mapper.PatientUserVoMapper;
import com.aihoo.domain.patient.service.PatientUserLogService;
import com.aihoo.domain.patient.service.PatientUserService;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.redis.RedisConstant;
import com.aihoo.redis.RedisService;
import com.aihoo.util.CardUtil;
import com.aihoo.util.StringUtil;
import com.aihoo.util.UserAgentGetter;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 患者用户表 服务实现类
 * </p>
 *
 * <p>迁移说明：原 admin 模块 PatientUserServiceImpl，1:1 复制业务逻辑；
 * 跨域依赖处理：
 * <ul>
 *   <li>LoginRecordUtil 已迁入 domain-sys（com.aihoo.domain.sys.util.LoginRecordUtil），需添加 aihoo-domain-sys 依赖</li>
 *   <li>HosSick 跨域实体内联为 HosSickRef 简化 DTO（com.aihoo.domain.patient.model.entity.HosSickRef）</li>
 *   <li>Area/SysUser 仅出现在 LoginRecordUtil 内部，patient 域不直接依赖</li>
 * </ul>
 * </p>
 *
 * @author mcp
 * @since 2020-10-08
 */
@Service
@Slf4j
public class PatientUserServiceImpl extends ServiceImpl<PatientUserMapper, PatientUser> implements PatientUserService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private PatientUserMapper patientUserMapper;
    @Autowired
    private PatientUserVoMapper patientUserVoMapper;
    @Autowired
    private PatientUserLogMapper patientUserLogMapper;
    @Lazy
    @Autowired
    private PatientUserLogService patientUserLogService;
    @Autowired
    private AddressMapper addressMapper;

    /**
     * 操作记录写入器。
     * <p>跨域引用：原 com.aihoo.admin.common.utils.LoginRecordUtil 已迁入
     * com.aihoo.domain.sys.util.LoginRecordUtil（依赖 domain-sys）。</p>
     */
    @Autowired
    private com.aihoo.domain.sys.util.LoginRecordUtil loginRecordUtil;
    @Autowired
    private PaProperties paProperties;

    @Override
    public JSONArray patientUserList(Map<String, Object> map) {
        int limit = Integer.parseInt(map.get("limit").toString());
        int page = Integer.parseInt(map.get("page").toString());
        int startNum = (page - 1) * limit;
        JSONArray jsonArray = new JSONArray();
        map.put("startNum", startNum);
        map.put("limit", limit);
        List<PatientUser> patientUsers = patientUserMapper.patientUserList(map);
        if (CollectionUtils.isEmpty(patientUsers)) {
            return jsonArray;
        }
        for (PatientUser patientUser : patientUsers) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", patientUser.getId());
            jsonObject.put("mobile", patientUser.getMobile());
            jsonObject.put("createTime", patientUser.getCreateTime());
            jsonObject.put("status", patientUser.getStatus());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


    @Override
    public PageResult<PatientUserVo> page(Map<String, Object> map) {

        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }

        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }

        QueryWrapper<PatientUserVo> wrapper = new QueryWrapper<>();
        if (null != map.get("mobile") && !"".equals(map.get("mobile"))) {
            wrapper.eq("mobile", map.get("mobile"));
        }
        wrapper.eq("is_cancel", 0);
        wrapper.orderByDesc("create_time");
        IPage<PatientUserVo> iPage = this.patientUserVoMapper.selectPage(new Page<>(page, limit), wrapper);

        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public JSONObject patientUserDetails(String id) {
        JSONObject jsonObject = new JSONObject();
        PatientUser patientUser = patientUserMapper.patientUserDetails(id);
        if (null == patientUser) {
            return jsonObject;
        }
        QueryWrapper<PatientUserLog> wrapper = new QueryWrapper<>();
        wrapper.eq("patient_user_id", patientUser.getId());
        wrapper.eq("action_type", "LOGIN");
        wrapper.orderByDesc("create_time");
        List<PatientUserLog> patientUserLogs = patientUserLogMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(patientUserLogs)) {
            jsonObject.put("ipAddress", "");
            jsonObject.put("city", "");
        }
        for (PatientUserLog patientUserLog : patientUserLogs) {
            jsonObject.put("ipAddress", patientUserLog.getIpAddress());
            jsonObject.put("city", patientUserLog.getCity());
        }
        jsonObject.put("id", patientUser.getId());
        jsonObject.put("createTime", patientUser.getCreateTime());
        if (null == patientUser.getIdCard() || "".equals(patientUser.getIdCard())) {
            if (null == patientUser.getBirthDay() || "".equals(patientUser.getBirthDay())) {
                jsonObject.put("age", "0");
            } else {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    //使用SimpleDateFormat的parse()方法生成Date
                    Date date = sf.parse(patientUser.getBirthDay());
                    String ageByBirth = String.valueOf(CardUtil.getAgeByBirth(date));
                    jsonObject.put("age", ageByBirth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            String ageByIdCard = String.valueOf(IdcardUtil.getAgeByIdCard(patientUser.getIdCard()));
            jsonObject.put("age", ageByIdCard);
        }
        jsonObject.put("name", patientUser.getName());
        jsonObject.put("nickName", patientUser.getNickName());
        jsonObject.put("mobile", patientUser.getMobile());
        jsonObject.put("headImg", patientUser.getHeadImg());
        jsonObject.put("idCard", patientUser.getIdCard());
        jsonObject.put("sex", patientUser.getSex());
        jsonObject.put("status", patientUser.getStatus());
        jsonObject.put("isCancel", patientUser.getIsCancel());
        jsonObject.put("isAuth", patientUser.getIsAuth());
        jsonObject.put("birthDay", patientUser.getBirthDay());
        if (null == patientUser.getUnionId() || "".equals(patientUser.getUnionId())) {
            jsonObject.put("weChar", "0");
        } else {
            jsonObject.put("weChar", "1");
        }
        if (null == patientUser.getAlipayOpenId() || "".equals(patientUser.getAlipayOpenId())) {
            jsonObject.put("aliCloud", "0");
        } else {
            jsonObject.put("aliCloud", "1");
        }
        if (null == patientUser.getAppleId() || "".equals(patientUser.getAppleId())) {
            jsonObject.put("apple", "0");
        } else {
            jsonObject.put("apple", "1");
        }
        // 就诊人集合：原 admin 引用 com.aihoo.admin.system.model.HosSick，
        // 此处改用 patient 域内的简化 DTO HosSickRef，字段名/结构 1:1 对齐。
        List<HosSickRef> hosSicks = patientUser.getHosSicks();
        if (hosSicks == null) {
            hosSicks = new ArrayList<>();
        }
        JSONArray hosSickArray = new JSONArray();
        for (HosSickRef hosSick : hosSicks) {
            JSONObject hosSicksObject = new JSONObject();
            hosSicksObject.put("hosSickId", hosSick.getId());
            hosSicksObject.put("hosSickName", hosSick.getName());
            hosSicksObject.put("hosSickSex", hosSick.getSex());
            hosSicksObject.put("hosSickAge", hosSick.getAge());
            hosSicksObject.put("hosSickIdCard", hosSick.getIdCard());
            hosSicksObject.put("hosSickMobile", hosSick.getMobile());
            hosSickArray.add(hosSicksObject);
        }
        jsonObject.put("hosSicks", hosSickArray);

        List<Address> addresses = patientUser.getAddresses();
        if (addresses == null) {
            addresses = new ArrayList<>();
        }
        JSONArray addressArray = new JSONArray();
        for (Address address : addresses) {
            JSONObject addressObject = new JSONObject();
            addressObject.put("addressId", address.getId());
            addressObject.put("addressName", address.getName());
            addressObject.put("addresses", address.getProvince() + "" + address.getCity() + "" + address.getDistrict());
            addressObject.put("addressMobile", address.getMobile());
            addressArray.add(addressObject);
        }
        jsonObject.put("addresses", addressArray);
        return jsonObject;
    }

    @Override
    public Boolean enableDisable(Map<String, Object> map, HttpServletRequest request) {
        PatientUser patientUser = new PatientUser();
        patientUser.setId(map.get("id").toString());
        patientUser.setStatus(map.get("status").toString());
        int i = this.patientUserMapper.updateById(patientUser);
        loginRecordUtil.saveLoginRecord(request, "用户启用与禁用");
        if ("0".equals(map.get("status").toString())) {
            String accessToken = this.patientUserMapper.selectById(map.get("id").toString()).getToken();
            String redisKey = RedisConstant.PATIENT_LOGIN_KEY + accessToken;
            redisService.remove(redisKey);
        }
        return i > 0;
    }

    @Override
    public int cancel(String mobile) {
        return this.patientUserMapper.cancel(mobile);
    }

    @Override
    public int getCount(Map<String, Object> map) {
        int count = patientUserMapper.getCount(map);
        return count;
    }

    @Override
    public void patientUserBulkExport(String mobile, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<PatientUser> wrapper = new QueryWrapper<PatientUser>();
        if (null != mobile && !"".equals(mobile)) {
            wrapper.eq("mobile", mobile);
        }
        List<PatientUser> patientUsers = this.patientUserMapper.selectList(wrapper);
        if (org.springframework.util.CollectionUtils.isEmpty(patientUsers)) {
            throw new BizException("当前条件无数据");
        }
        List<PatientUserEntity> list = new ArrayList<>();
        patientUsers.forEach(s -> {

            if ("1".equals(s.getSex())) {
                s.setSex("男");
            } else {
                s.setSex("女");
            }
            if ("".equals(s.getUnionId()) || null == s.getUnionId()) {
                s.setUnionId("否");
            } else {
                s.setUnionId("是");
            }
            if ("".equals(s.getAlipayOpenId()) || null == s.getAlipayOpenId()) {
                s.setAlipayOpenId("否");
            } else {
                s.setAlipayOpenId("是");
            }
            if ("".equals(s.getAppleId()) || null == s.getAppleId()) {
                s.setAppleId("否");
            } else {
                s.setAppleId("是");
            }
            if ("0".equals(s.getStatus())) {
                s.setStatus("停用");
            } else {
                s.setStatus("启用");
            }
            if ("0".equals(s.getIsCancel())) {
                s.setIsCancel("否");
            } else {
                s.setIsCancel("是");
            }
            if (null == s.getIsAuth()) {
                s.setIsAuth("未认证");
            } else {
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
            }
            PatientUserEntity entity = new PatientUserEntity();
            QueryWrapper<PatientUserLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("patient_user_id", s.getId());
            queryWrapper.eq("action_type", "LOGIN");
            queryWrapper.orderByDesc("create_time");
            List<PatientUserLog> patientUserLogs = patientUserLogMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(patientUserLogs)) {
                entity.setIpAddress("");
                entity.setCity("");
            }
            for (PatientUserLog patientUserLog : patientUserLogs) {
                entity.setIpAddress(patientUserLog.getIpAddress());
                entity.setCity(patientUserLog.getCity());
            }
            if (null == s.getIdCard() || "".equals(s.getIdCard())) {
                if (null == s.getBirthDay() || "".equals(s.getBirthDay())) {
                    entity.setAge("0");
                } else {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        //使用SimpleDateFormat的parse()方法生成Date
                        Date date = sf.parse(s.getBirthDay());
                        String ageByBirth = String.valueOf(CardUtil.getAgeByBirth(date));
                        entity.setAge(ageByBirth);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                String ageByIdCard = String.valueOf(IdcardUtil.getAgeByIdCard(s.getIdCard()));
                entity.setAge(ageByIdCard);
            }
            BeanUtils.copyProperties(s, entity);
            list.add(entity);
        });
        ExcelUtils.writeExcel(request, response, list, PatientUserEntity.class, "用户管理表格.xlsx");
    }


    @Override
    public boolean patientApprove(Map<String, Object> map) {
        QueryWrapper<PatientUser> qw = new QueryWrapper();
        qw.apply("is_cancel = '0' and status = '1' and is_auth = 'WAIT'");
        List<PatientUser> patientUserList = list(qw);
        List<PatientUserLog> logList = new ArrayList();
        if (patientUserList.size() > 0) {
            for (PatientUser patientUser : patientUserList) {
                String name = patientUser.getName();
                String idCard = patientUser.getIdCard();
                boolean b = getCert(idCard, name);
                PatientUserLog uLog = new PatientUserLog();
                logList.add(uLog);
                uLog.setPatientUserId(patientUser.getId());
                uLog.setActionType("APPROVESIGN");
                if (b) {
                    int sex = IdcardUtil.getGenderByIdCard(idCard);
                    String birthDay = DateUtil.formatDateTime(IdcardUtil.getBirthDate(idCard));
                    patientUser.setIsAuth("PASS");
                    patientUser.setBirthDay(birthDay);
                    patientUser.setSex(String.valueOf(sex));
                    patientUser.setToken(null);
                    uLog.setRemark("认证成功");
                    // redisService.remove(RedisConstant.PATIENT_LOGIN_KEY + patientUser.getToken());
                } else {
                    patientUser.setIsAuth("REJECT");
                    uLog.setRemark("认证失败");
                }
                patientUser.setAuthTime(LocalDateTime.now().toString());
            }
            saveOrUpdateBatch(patientUserList);
            patientUserLogService.saveOrUpdateBatch(logList);

            // 认证成功 刷新缓存
            // List<String> okList = patientUserList.stream().map(x->x.getId()).collect(Collectors.toList());
            // if(okList.size()>0){
            //     List<PatientUser> userList = list(new QueryWrapper<PatientUser>().apply("FIND_IN_SET(id,{0}) AND LENGTH(IFNULL(token,'')) > 0",String.join(",", okList)));
            //     if(userList.size()>0){
            //         for (PatientUser user : userList) {
            //             redisService.set(RedisConstant.PATIENT_LOGIN_KEY + user.getToken(), user, RedisConstant.TOKEN_SURVIVE_TIME);
            //         }
            //     }
            // }
        }
        return true;
    }


    /**
     * 多源身份证认证
     *
     * @param idNo 身份证号
     * @param name 姓名
     * @return boolean
     */
    private boolean getCert(String idNo, String name) {
        try {
            String accessToken = getToken();
            if (StringUtil.isNotBlank(accessToken)) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("idNo", idNo);
                paramMap.put("name", name);
                paramMap.put("transactionId", SecureUtil.md5(name + idNo));//业务流水号

                String body = HttpRequest.post(paProperties.getUrl())
                        //头信息，多个头信息多次调用此方法即可
                        .header(Header.AUTHORIZATION, accessToken)
                        .header(Header.CONTENT_TYPE, "application/json")
                        //表单内容
                        .body(JSON.toJSONString(paramMap))
                        .execute().body();
                //            {"status":200,"result":{"authResult":true,"msg":"认证通过","englishName":null,"name":"牛堃","gjCn":null}}
                //            { "status": 500, "error": "0001", "msg": "身份证号格式错误", "errorCode": "ID_NO_ERROR", "time": "2020-09-27 10:04:24" }
                Map<String, Object> resultBody = JSON.parseObject(body, HashMap.class);
                if (null != resultBody.get("status") && resultBody.get("status").equals(200)) {
                    String result = String.valueOf(resultBody.get("result"));
                    Map<String, Object> hashMap = JSON.parseObject(result, HashMap.class);
                    return Boolean.parseBoolean(hashMap.get("authResult").toString());
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 多源接口获取token
     */
    private String getToken() {
        String accessToken = "";
        try {
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("app_id", paProperties.getAppId());
            paramMap.put("app_secret", paProperties.getAppSecret());
            paramMap.put("client", "p_autht");

            String body = HttpRequest.post(paProperties.getTokenUrl())
                    //头信息，多个头信息多次调用此方法即可
                    .header(Header.CONTENT_TYPE, "application/json")
                    //表单内容
                    .body(JSON.toJSONString(paramMap))
                    .execute().body();
            log.info("\n获取token结果\n{}", body);
            Map<String, String> result = JSON.parseObject(body, HashMap.class);
            String access_token = result.get("access_token");
            if (StringUtil.isNotBlank(access_token)) {
                accessToken = access_token;
                log.info("获取token成功:)token:" + access_token);
            } else {
                log.error("获取token失败");
            }
        } catch (Exception e) {
            log.error("获取token失败", e.getMessage());
            e.printStackTrace();
        }
        return accessToken;
    }
}
