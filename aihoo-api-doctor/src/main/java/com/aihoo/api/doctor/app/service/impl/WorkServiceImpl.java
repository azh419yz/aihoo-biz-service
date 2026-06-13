package com.aihoo.api.doctor.app.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.aihoo.util.AdderssUtils;
import com.aihoo.util.CardUtil;
import com.aihoo.util.SmsUtils;
import com.aihoo.util.WeekUtil;
import com.aihoo.redis.RedisConstant;
import com.aihoo.redis.RedisService;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aihoo.api.doctor.app.controller.vo.ChangeBalance;
import com.aihoo.api.doctor.app.controller.vo.DoctorWorkVo;
import com.aihoo.api.doctor.app.controller.vo.HosOrderList;
import com.aihoo.domain.hospital.model.entity.HomepageMessage;
import com.aihoo.domain.im.model.mapper.MessageMapper;
import com.aihoo.domain.im.model.entity.Message;
import com.aihoo.domain.doctor.model.mapper.DoctorUserLogMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorSetMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorAyncMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorBalanceMapper;
import com.aihoo.domain.im.model.mapper.CancelMapper;
import com.aihoo.domain.consultation.model.mapper.MdtOrderMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.visit.model.mapper.HosRevisitMapper;
import com.aihoo.domain.visit.model.mapper.HosVisitMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorSetTimesMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorBalanceLogMapper;

import com.aihoo.api.doctor.app.service.WorkService;
import com.aihoo.api.doctor.common.utils.AuthUtil;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.util.UserAgentGetter;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import com.aihoo.domain.doctor.model.entity.DoctorSet;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.entity.DoctorUserLog;
import com.aihoo.domain.doctor.model.entity.DoctorAync;
import com.aihoo.domain.doctor.model.entity.DoctorBalance;
import com.aihoo.domain.doctor.model.entity.DoctorBalanceLog;
import com.aihoo.domain.doctor.model.entity.DoctorSetTimes;
import com.aihoo.domain.consultation.model.entity.MdtOrder;
import com.aihoo.domain.im.model.entity.PushMessage;
import com.aihoo.domain.im.model.entity.Cancel;
import com.aihoo.redis.RedisService;
import java.util.*;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/21 16:25
 * @description：工作台详细信息
 */
@Service
public class WorkServiceImpl implements WorkService {

    //首页提示信息表
    @Autowired
    private MessageMapper messageMapper;
    //接诊设置表
    @Autowired
    private DoctorAyncMapper doctorAyncMapper;
    //在线问诊信息表
    @Autowired
    private HosVisitMapper hosVisitMapper;
    //复诊信息表
    @Autowired
    private HosRevisitMapper hosRevisitMapper;
    //远程会诊
    @Autowired
    private MdtOrderMapper mdtOrderMapper;
    //接诊设置表
    @Autowired
    private DoctorSetMapper doctorSetMapper;
    //接诊设置时间表
    @Autowired
    private DoctorSetTimesMapper doctorSetTimesMapper;
    @Autowired
    private DoctorBalanceMapper doctorBalanceMapper;
    @Autowired
    private DoctorUserMapper doctorUserMapper;
    @Autowired
    private DoctorBalanceLogMapper doctorBalanceLogMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CancelMapper cancelMapper;
    @Autowired
    private DoctorUserLogMapper doctorUserLogMapper;

    /**
     * 保存医生的擅长和简介
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject saveContent(Map<String, String> map) {
        String beGoodAtText = map.get("beGoodAtText");//擅长
        String introductionText = map.get("introductionText");//简介
        DoctorUser doctorUser = new DoctorUser();
        DoctorUser loginUser = AuthUtil.getLoginUser();
        String id = loginUser.getId();
        doctorUser.setId(id);
        if (!StringUtil.isNullOrEmpty(beGoodAtText)) {
            doctorUser.setBeGoodAtText(beGoodAtText);
        }
        if (!StringUtil.isNullOrEmpty(introductionText)) {
            doctorUser.setIntroductionText(introductionText);
        }
        int i = doctorUserMapper.updateById(doctorUser);
        JSONObject jsonObject = new JSONObject();
        if (i == 0) {
            jsonObject.put("is_succ", "error");
        }
        DoctorUser docUser = doctorUserMapper.selectById(id);
        String accessToken = docUser.getToken();
        String redisKey = RedisConstant.DOCKER_LOGIN_KEY + accessToken;
        redisService.remove(redisKey);
        redisService.set(redisKey, docUser, RedisConstant.TOKEN_SURVIVE_TIME);
        AuthUtil.clear();
        AuthUtil.setLoginUser(docUser);
        return jsonObject;
    }

    /**
     * @Description: 医生余额
     * @Date: 2020/9/29
     * @Return: com.aihoo.common.JsonResult
     **/
    @Override
    public List<ChangeBalance> balanceLog(Map<String, String> map) {
        String type = "%%";
        if (null != map.get("type") && !map.get("type").equals("ALL")) {
            type = map.get("type") + "%";
        }
        String date = map.get("date");
        //一月的开始
        Date beginOfMonth;
        //一月的结束
        Date endOfMonth;
        try {
            if (StrUtil.isBlank(date)) {
                beginOfMonth = DateUtil.beginOfMonth(DateUtil.date());
                endOfMonth = DateUtil.endOfMonth(DateUtil.date());
            } else {
                DateTime dateTime = DateUtil.parse(date, "yyyy-MM");
                beginOfMonth = DateUtil.beginOfMonth(dateTime);
                endOfMonth = DateUtil.endOfMonth(dateTime);
            }
        } catch (Exception e) {
            beginOfMonth = DateUtil.beginOfMonth(DateUtil.date());
            endOfMonth = DateUtil.endOfMonth(DateUtil.date());
        }
        /*统计本月余额*/
        LambdaQueryWrapper<DoctorBalanceLog> lambda = new QueryWrapper<DoctorBalanceLog>().lambda();
        lambda.select(DoctorBalanceLog::getType, DoctorBalanceLog::getChangeAmount);
        lambda.eq(DoctorBalanceLog::getDoctorUserId, AuthUtil.getLoginUser().getId());
        lambda.between(DoctorBalanceLog::getCreateTime, beginOfMonth, endOfMonth);
        List<DoctorBalanceLog> balanceLogList = doctorBalanceLogMapper.selectList(lambda);
        BigDecimal conut = new BigDecimal(0);
        balanceLogList.forEach(balanceLog -> {
            if (balanceLog.getType().contains("ADD")) {
                conut.add(new BigDecimal(balanceLog.getChangeAmount()));
            } else if (balanceLog.getType().contains("LOSE")) {
                conut.subtract(new BigDecimal(balanceLog.getChangeAmount()));
            }
        });
        Integer page;
        Integer limit;
        try {
            page = Integer.parseInt(map.get("page"));
            limit = Integer.parseInt(map.get("limit"));
        } catch (Exception e) {
            page = 1;
            limit = 10;
            e.printStackTrace();
        }
        IPage<DoctorBalanceLog> iPage = new Page<>(page, limit);
        IPage<DoctorBalanceLog> resultPage = doctorBalanceLogMapper.selectByDoctorId(AuthUtil.getLoginUser().getId(), type, beginOfMonth, endOfMonth, iPage);
        List<DoctorBalanceLog> doctorBalanceLogList = resultPage.getRecords();
        List<ChangeBalance> changeBalanceList = new ArrayList<>();
        for (DoctorBalanceLog doctorBalanceLog : doctorBalanceLogList) {
            String doctorBalanceType = doctorBalanceLog.getType();
            ChangeBalance changeBalance = new ChangeBalance();
            changeBalance.setCreatTime(doctorBalanceLog.getCreateTime());
//            操作类型  REVISIT_ADD-复诊增加 VISIT_ADD-问诊增加 REVISIT_LOSE-复诊减少 VISIT_LOSE-问诊减少
            String code = "-";
            if (doctorBalanceLog.getType().contains("ADD")) {
                code = "+";
            }
            changeBalance.setChangeAmount(code + doctorBalanceLog.getChangeAmount());
            changeBalance.setType(doctorBalanceType);
            changeBalance.setRemark(doctorBalanceLog.getRemark());
            changeBalanceList.add(changeBalance);
        }
        return changeBalanceList;
    }

    @Override
    public List balanceLogType(Map<String, String> param) {
        List types = new ArrayList();
        Map<Object, Object> map = new HashMap<>();
        Map<Object, Object> map1 = new HashMap<>();
        Map<Object, Object> map2 = new HashMap<>();
        Map<Object, Object> map3 = new HashMap<>();
        map.put("code", "ALL");
        map.put("name", "全部");
        map1.put("code", "REVISIT");
        map1.put("name", "复诊购药");
        map2.put("code", "MDT");
        map2.put("name", "会诊订单");
        map3.put("code", "VISIT");
        map3.put("name", "在线复诊");
        types.add(map);
        types.add(map1);
        types.add(map2);
        types.add(map3);
        return types;
    }

    @Override
    public JSONObject doctorSet() {
        JSONObject jsonObject = new JSONObject();
        DoctorUser loginUser = AuthUtil.getLoginUser();
        DoctorSet doctorSet = doctorSetMapper.selectDoctorUserId(loginUser.getId());
        jsonObject.put("isImg", doctorSet.getIsImg());//是否开启图文问诊 0-未开 1-开启
//        jsonObject.put("isVoice", doctorSet.getIsVoice());//是否开启语音问诊 0-未开 1-开启
//        jsonObject.put("isVideo", doctorSet.getIsVideo());//是否开启视频问诊 0-未开 1-开启
        jsonObject.put("isRevisit", doctorSet.getIsRevisit());//是否开启复诊 0-未开 1-开启
        jsonObject.put("isMdt", doctorSet.getIsMdt());//是否开启mdt 0-未开 1-开启
        return jsonObject;
    }

    @Override
    public List doctorSetTimes(Map<String, String> map) {
        //VOICE-语音问诊 VIDEO-视频问诊 REVISIT-复诊
        String type = map.get("type");
        List<Map> doctorSetTimesList = doctorSetTimesMapper.selectListByTypeAndDoctorId(AuthUtil.getLoginUser().getId(), type);
        List list = new ArrayList(Collections.nCopies(doctorSetTimesList.size(), new HashMap()));
        for (Map docSetTime : doctorSetTimesList) {
            docSetTime.put("setTimes", String.valueOf(docSetTime.get("setTimes")).split("[,]"));
            String weekCode = String.valueOf(docSetTime.get("weekCode"));
            switch (weekCode) {
                case "Monday":
                    list.set(0, docSetTime);
                    break;
                case "Tuesday":
                    list.set(1, docSetTime);
                    break;
                case "Wednesday":
                    list.set(2, docSetTime);
                    break;
                case "Thursday":
                    list.set(3, docSetTime);
                    break;
                case "Friday":
                    list.set(4, docSetTime);
                    break;
                case "Saturday":
                    list.set(5, docSetTime);
                    break;
                case "Sunday":
                    list.set(6, docSetTime);
                    break;
                default:
            }
        }
        return list;
    }

    /**
     * 工作台页面
     *
     * @return
     */
    @Override
    public Map<String, Object> getWorkbench() {
        DoctorUser loginUser = AuthUtil.getLoginUser();
        String id = loginUser.getId();
        DoctorUser doctorUser = doctorUserMapper.selectById(id);
        DoctorWorkVo doctorWorkVo = new DoctorWorkVo();
        doctorWorkVo.setId(id);
        doctorWorkVo.setName(doctorUser.getName());
        doctorWorkVo.setHeadImg(doctorUser.getHeadImg());
        doctorWorkVo.setDepartName(doctorUser.getDepartName());
        doctorWorkVo.setHospitalName(doctorUser.getHospitalName());
        doctorWorkVo.setOfficeHolderName(doctorUser.getOfficeHolderName());
        //可否接诊判断
        doctorWorkVo.setWorkStatus("不可接诊");
        QueryWrapper<DoctorSet> doctorSetQueryWrapper = new QueryWrapper<>();
        doctorSetQueryWrapper.eq("doctor_user_id", id);
        DoctorSet doctorSet = doctorSetMapper.selectOne(doctorSetQueryWrapper);
        String isImg = doctorSet.getIsImg();
        String isRevisit = doctorSet.getIsRevisit();
        String isMdt = doctorSet.getIsMdt();
        doctorWorkVo.setIsImg(isImg);
        doctorWorkVo.setIsRevisit(isRevisit);
        doctorWorkVo.setIsMdt(isMdt);
        DoctorBalance doctorBalance = doctorBalanceMapper.selectByDoctorId(id);
        doctorWorkVo.setAmount(doctorBalance.getAvailableAmount());
        //判断医生接诊状态
        /*String isVideo = doctorSet.getIsVideo();
        String isVoice = doctorSet.getIsVoice();
        if (isImg.equals("1")) {
            doctorWorkVo.setWorkStatus("可接诊");
        } else {
            //判断星期
            String weekCode = WeekUtil.todayWeek();
            QueryWrapper<DoctorSetTimes> doctorSetTimesQueryWrapper = new QueryWrapper<>();
            doctorSetTimesQueryWrapper.eq("doctor_user_id", id)
                    .eq("week_code", weekCode);
            List<DoctorSetTimes> doctorSetTimesList = doctorSetTimesMapper.selectList(doctorSetTimesQueryWrapper);
            if (doctorSetTimesList != null && doctorSetTimesList.size() != 0) {
                for (DoctorSetTimes doctorSetTimes : doctorSetTimesList) {
                    String startTime = doctorSetTimes.getStartTime();
                    String endTime = doctorSetTimes.getEndTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    String now = simpleDateFormat.format(new Date());
                    boolean b = WeekUtil.inTime(now, startTime, endTime);
                    if (b) {
                        String type = doctorSetTimes.getType();
                        if (type.equals("VOICE") && isVoice.equals("1")) {
                            doctorWorkVo.setWorkStatus("可接诊");
                            break;
                        } else if (type.equals("VIDEO") && isVideo.equals("1")) {
                            doctorWorkVo.setWorkStatus("可接诊");
                            break;
                        } else if (type.equals("REVISIT") && isRevisit.equals("1")) {
                            doctorWorkVo.setWorkStatus("可接诊");
                            break;
                        }
                    }
                }
            }
        }*/
        HashMap<String, Object> map = new HashMap<>();
        map.put("doctorData", getDoctorData(id, doctorWorkVo));
        map.put("homeMessageData", getHomeMessageData());
//        map.put("orderData", getOrderList(id));
        return map;
    }

    /**
     * 获取工作台上方卡片信息
     *
     * @return 医生信息
     * @Param id 医生id
     */
    public Object getDoctorData(String id, DoctorWorkVo doctorWorkVo) {
        QueryWrapper<DoctorAync> wrapper = new QueryWrapper<>();
        wrapper.eq("doctor_user_id", id);
        DoctorAync doctorAync = doctorAyncMapper.selectOne(wrapper);
        String highOpinion = null;
        String orderNumber = null;
        if (doctorAync != null) {
            highOpinion = doctorAync.getHighOpinion();
            orderNumber = doctorAync.getOrderNumber();
        }
        doctorWorkVo.setHighOpinion(getStar(highOpinion));
        if (orderNumber == null) {
            doctorWorkVo.setOrderNumber("50");
        } else {
            doctorWorkVo.setOrderNumber(orderNumber + "");
        }
        return doctorWorkVo;
    }

    /**
     * 首页提示信息
     *
     * @return
     */
    public Object getHomeMessageData() {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("type", "DOCKER")
                .eq("is_delete", "0")
                .select("title")
                .orderByAsc(true, "create_time");
        List<Message> homePageMessageList = messageMapper.selectList(wrapper);
        List<String> titleList = new ArrayList<>();
        for (Message homepageMessage : homePageMessageList) {
            titleList.add(homepageMessage.getTitle());
        }
        return titleList;
    }

    /**
     * 工作台下面的复诊购药，图文问诊，远程会诊
     *
     * @param id
     * @return
     */
    public Object getOrderList(String id) {
        HashMap<String, Object> map = new HashMap<>();
        //图文问诊
        LambdaQueryWrapper<HosVisit> hosVisitQueryWrapper = new QueryWrapper<HosVisit>().lambda();
//        pushMessageLambdaQueryWrapper.eq(PushMessage::getIsDelete, "0")
//                .and(wrapper -> wrapper.eq(PushMessage::getType, "DOCKER").or().eq(PushMessage::getType, "ALL"))
//                .or(wrapper -> wrapper.eq(PushMessage::getType, "DOCKER_PERSONAL").eq(PushMessage::getPesronalId, AuthUtil.getLoginUser().getId()))
//                .orderByDesc(PushMessage::getCreateTime);
        hosVisitQueryWrapper.eq(HosVisit::getDoctorUserId, id)
                .and(wrapper -> wrapper.eq(HosVisit::getStatus, "PAY")/*.or().eq(HosVisit::getStatus, "HAVE").or().eq(HosVisit::getStatus, "START")*/)
                .orderByDesc(HosVisit::getCreateTime);
        List<HosVisit> hosVisitList = hosVisitMapper.selectList(hosVisitQueryWrapper);
        ArrayList<HosOrderList> hosOrderListArrayList = new ArrayList<>();
        //获取创建时间最近的
        if (hosVisitList.size() != 0) {
            for (HosVisit hosVisit : hosVisitList) {
                HosOrderList hosOrderList = new HosOrderList();
                hosOrderList.setId(hosVisit.getId());
                hosOrderList.setName(hosVisit.getName());
                hosOrderList.setAge(hosVisit.getAge());
                String type = hosVisit.getType();
                hosOrderList.setType(type);
                hosOrderList.setTypeName(StatusEnumUtil.getVisitTypeName(type));
                if (hosVisit.getSex().equals("0")) {
                    hosOrderList.setSex("女");
                } else {
                    hosOrderList.setSex("男");
                }
                hosOrderListArrayList.add(hosOrderList);
            }

        }
//        hosOrder1.setCount(hosVisitList.size());
//        map.put("hosVisit", hosOrder1);
        //复诊购药
        LambdaQueryWrapper<HosRevisit> hosRevisitQueryWrapper = new QueryWrapper<HosRevisit>().lambda();
        hosRevisitQueryWrapper.eq(HosRevisit::getDoctorUserId, id)
                .and(wapper -> wapper.eq(HosRevisit::getStatus, "PAY").or()
                        /*.eq(HosRevisit::getStatus, "HAVE").or()
                        .eq(HosRevisit::getStatus, "START")*/)
                .orderByDesc(HosRevisit::getRevisitStartTime);
        List<HosRevisit> hosRevisitList = hosRevisitMapper.selectList(hosRevisitQueryWrapper);
        if (hosRevisitList.size() != 0) {
            for (HosRevisit hosRevisit : hosRevisitList) {
                HosOrderList hosOrderList = new HosOrderList();
                hosOrderList.setId(hosRevisit.getId());
                hosOrderList.setName(hosRevisit.getName());
                hosOrderList.setAge(hosRevisit.getAge());
                hosOrderList.setType("REVISIT");
                hosOrderList.setTypeName("复诊购药");
                if (hosRevisit.getSex().equals("0")) {
                    hosOrderList.setSex("女");
                } else {
                    hosOrderList.setSex("男");
                }
                hosOrderListArrayList.add(hosOrderList);
            }

        }
//        hosOrder2.setCount(hosRevisitList.size());
//        map.put("hosRevisit", hosOrder2);
        //远程会诊
        LambdaQueryWrapper<MdtOrder> mdtOrderQueryWrapper = new QueryWrapper<MdtOrder>().lambda();
        mdtOrderQueryWrapper.eq(MdtOrder::getDoctorUserId, id)
                .and(wapper -> wapper.eq(MdtOrder::getStatus, "PAY")/*.or()
                        .eq(MdtOrder::getStatus, "HAVE")*/)
                .orderByDesc(MdtOrder::getCreateTime);
        List<MdtOrder> mdtOrderList = mdtOrderMapper.selectList(mdtOrderQueryWrapper);
        if (mdtOrderList.size() != 0) {
            for (MdtOrder mdtOrder : mdtOrderList) {
                HosOrderList hosOrderList = new HosOrderList();
                hosOrderList.setId(mdtOrder.getId());
                hosOrderList.setName(mdtOrder.getName());
                hosOrderList.setAge(mdtOrder.getAge());
                hosOrderList.setType("MDT");
                hosOrderList.setTypeName("远程会诊");
                if (mdtOrder.getSex().equals("0")) {
                    hosOrderList.setSex("女");
                } else {
                    hosOrderList.setSex("男");
                }
                hosOrderListArrayList.add(hosOrderList);
            }

        }
//        hosList3.setCount(mdtOrderList.size());
//        map.put("mdtOrder", hosList3);
        map.put("hosOrder", hosOrderListArrayList);
        return map;
    }

    /**
     * @Author: 李圣龙
     * @Description: 医查询生个人信息
     * @Date: 2020/9/28
     * @Return: com.aihoo.common.JsonResult
     **/
    @Override
    public JSONObject doctorMessage() {
        DoctorUser loginUser = AuthUtil.getLoginUser();

        //医生信息
        JSONObject docter = new JSONObject();
        String papersNumbers = loginUser.getPapersNumbers();
        try {
            Map<String, String> carInfo = CardUtil.getCarInfo(papersNumbers);
            docter.put("sex", carInfo.get("sex"));//性别
            docter.put("birthday", carInfo.get("birthday"));//生日
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginUser = doctorUserMapper.selectById(loginUser.getId());
        docter.put("id", loginUser.getId());//姓名
        docter.put("name", loginUser.getName());//姓名
        docter.put("headImg", loginUser.getHeadImg());//头像
        docter.put("departName", loginUser.getDepartName());//所在科室
        docter.put("mobile", loginUser.getMobile());//所在科室
        docter.put("hospitalName", loginUser.getHospitalName());//医院名称
        docter.put("beGoodAtText", loginUser.getBeGoodAtText());//擅长
        docter.put("introductionText", loginUser.getIntroductionText());//简介
        docter.put("officeHolderName", loginUser.getOfficeHolderName());//职称
        String doctorId = loginUser.getId();
        docter.put("highOpinion", "100%");//好评率
        docter.put("orderNumber", "50");//接单量
        try {
            DoctorAync doctorAync = doctorAyncMapper.selectByDoctorId(doctorId);
            String highOpinion = doctorAync.getHighOpinion();
            docter.put("highOpinion", getStar(highOpinion));//好评率
            String orderNumber = doctorAync.getOrderNumber();
            docter.put("orderNumber", getOrderNumber(orderNumber));//接单量
        } catch (Exception e) {
            System.out.println("未创建好评率表");
        }
        String format = "0.00";
        try {
            DoctorBalance doctorBalance = doctorBalanceMapper.selectByDoctorId(doctorId);
            double availableAmount = Double.parseDouble(doctorBalance.getAvailableAmount());
            double lockAmount = Double.parseDouble(doctorBalance.getLockAmount());
            format = String.format("%.2f", availableAmount + lockAmount);
        } catch (Exception e) {
            System.out.println("未创建医生钱包");
        }
        docter.put("balance", format);//钱包余额
        return docter;
    }

    /**
     * 接单量
     *
     * @param orderNumber
     * @return
     */
    public String getOrderNumber(String orderNumber) {
        if (StringUtil.isNullOrEmpty(orderNumber)) {
            return "50";
        } else if (orderNumber.equals("0")) {
            return "50";
        } else {
            return orderNumber;
        }
    }

    /**
     * 计算好评率
     *
     * @param num
     * @return
     */
    public String getStar(String num) {
        if (StringUtil.isNullOrEmpty(num)) {
            return "100%";
        }
        try {
            if (Double.parseDouble(num) == 0) {
                return "100%";
            }
            String[] split = num.split("[.]");
//        System.out.println(JSON.toJSONString(split));
            if (split[0].equals("1")) {
                return split[0] + "00%";
            } else {
                if (split[1].substring(0, 1).equals("0")) {
                    return split[1].substring(1) + "%";
                }
                return split[1] + "%";
            }
        } catch (Exception e) {
            return "100%";
        }

    }

    @Override
    public boolean logout(HttpServletRequest request) {
        DoctorUser doctorUser = AuthUtil.getLoginUser();
        String id = doctorUser.getId();
        try {
            redisService.remove(RedisConstant.DOCKER_LOGIN_KEY + doctorUser.getToken());
            DoctorUser updete = new DoctorUser();
            updete.setId(id);
            updete.setToken("LOGOUT" + doctorUser.getToken());
            doctorUserMapper.updateById(updete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DoctorUserLog log = new DoctorUserLog();
        log.setActionType("LOGOUT");
        log.setDoctorUserId(id);
        UserAgentGetter userAgentGetter = new UserAgentGetter(request);
        log.setOsName(userAgentGetter.getOS());
        log.setIpAddress(userAgentGetter.getIpAddr());
        log.setRemark("登出成功");
        String cityNameByTaoBaoAPI = AdderssUtils.getCityNameByTaoBaoAPI(userAgentGetter.getIpAddr());
        log.setCity(cityNameByTaoBaoAPI);
        doctorUserLogMapper.insert(log);
        return true;
    }

    @Override
    public JSONObject sendCancelCode(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        DoctorUser doctorUser = AuthUtil.getLoginUser();
        if (null == doctorUser) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "请登录");
            return jsonObject;
        }


        // 生成6位随机数
        String code = RandomUtil.randomNumbers(6);
        String key = "doctor_cancel_" + doctorUser.getMobile();
        Map<String, Object> sendMap = new HashMap<>();
        sendMap.put("mobile", doctorUser.getMobile());
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
        redisService.set(key, code, 180);
        jsonObject.put("is_succ", 1);
        return jsonObject;
    }

    @Override
    public JSONObject doctorUserCancel(Map<String, Object> map, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        DoctorUser doctorUser = AuthUtil.getLoginUser();
        if (null == doctorUser) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "请登录");
            return jsonObject;
        }
        String mobile = doctorUser.getMobile();
        String code = map.get("code").toString();
        String sendCodeKey = "doctor_cancel_" + mobile;

        if (!redisService.exists(sendCodeKey)) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "验证码已过期");
            return jsonObject;
        }
        String codes = redisService.get(sendCodeKey).toString();

        //手机号和验证码都是正确的则登录
        if (!codes.equals(code)) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "验证码不正确");
            return jsonObject;
        } else {
            Cancel cancel = new Cancel();
            cancel.setType("DOCKER");
            cancel.setDoctorUserId(doctorUser.getId());
            cancel.setRemark(map.get("remark").toString());
            cancelMapper.insert(cancel);
        }
        jsonObject.put("is_succ", 1);
        return jsonObject;
    }

    @Override
    public List visitOrderList() {
        HashMap<String, Object> map = new HashMap<>();
        //图文问诊
        LambdaQueryWrapper<HosVisit> hosVisitQueryWrapper = new QueryWrapper<HosVisit>().lambda();
        hosVisitQueryWrapper.eq(HosVisit::getDoctorUserId, AuthUtil.getLoginUser().getId())
                .and(wrapper -> wrapper.eq(HosVisit::getStatus, "PAY").or().eq(HosVisit::getStatus, "HAVE").or().eq(HosVisit::getStatus, "START"))
                .orderByDesc(HosVisit::getCreateTime);
        List<HosVisit> hosVisitList = hosVisitMapper.selectList(hosVisitQueryWrapper);
        ArrayList<HosOrderList> hosOrderListArrayList = new ArrayList<>();
        //获取创建时间最近的
        if (hosVisitList.size() != 0) {
            for (HosVisit hosVisit : hosVisitList) {
                HosOrderList hosOrderList = new HosOrderList();
                hosOrderList.setId(hosVisit.getId());
                hosOrderList.setName(hosVisit.getName());
                hosOrderList.setAge(hosVisit.getAge());
                String type = hosVisit.getType();
                hosOrderList.setType(type);
                hosOrderList.setTypeName(StatusEnumUtil.getVisitTypeName(type));
                if (hosVisit.getSex().equals("0")) {
                    hosOrderList.setSex("女");
                } else {
                    hosOrderList.setSex("男");
                }
                hosOrderListArrayList.add(hosOrderList);
            }
        }
        return hosOrderListArrayList;
    }

    @Override
    public List revisitOrderList() {
        //复诊购药
        LambdaQueryWrapper<HosRevisit> hosRevisitQueryWrapper = new QueryWrapper<HosRevisit>().lambda();
        hosRevisitQueryWrapper.eq(HosRevisit::getDoctorUserId, AuthUtil.getLoginUser().getId())
                .and(wapper -> wapper.eq(HosRevisit::getStatus, "PAY").or()
                        .eq(HosRevisit::getStatus, "HAVE").or()
                        .eq(HosRevisit::getStatus, "START"))
                .orderByDesc(HosRevisit::getRevisitStartTime);
        List<HosRevisit> hosRevisitList = hosRevisitMapper.selectList(hosRevisitQueryWrapper);
        ArrayList<HosOrderList> hosOrderListArrayList = new ArrayList<>();
        if (hosRevisitList.size() != 0) {
            for (HosRevisit hosRevisit : hosRevisitList) {
                HosOrderList hosOrderList = new HosOrderList();
                hosOrderList.setId(hosRevisit.getId());
                hosOrderList.setName(hosRevisit.getName());
                hosOrderList.setAge(hosRevisit.getAge());
                hosOrderList.setType("REVISIT");
                hosOrderList.setTypeName("复诊购药");
                if (hosRevisit.getSex().equals("0")) {
                    hosOrderList.setSex("女");
                } else {
                    hosOrderList.setSex("男");
                }
                hosOrderListArrayList.add(hosOrderList);
            }
        }
        return hosOrderListArrayList;
    }

    @Override
    public Map<String, Object> getWorkbenchTest() {
        DoctorUser loginUser = AuthUtil.getLoginUser();
        String id = loginUser.getId();
        DoctorWorkVo doctorWorkVo = new DoctorWorkVo();
        doctorWorkVo.setId(id);
        doctorWorkVo.setName(loginUser.getName());
        doctorWorkVo.setHeadImg(loginUser.getHeadImg());
        doctorWorkVo.setDepartName(loginUser.getDepartName());
        doctorWorkVo.setHospitalName(loginUser.getHospitalName());
        doctorWorkVo.setOfficeHolderName(loginUser.getOfficeHolderName());
        //可否接诊判断
        doctorWorkVo.setWorkStatus("不可接诊");
        QueryWrapper<DoctorSet> doctorSetQueryWrapper = new QueryWrapper<>();
        doctorSetQueryWrapper.eq("doctor_user_id", id);
        DoctorSet doctorSet = doctorSetMapper.selectOne(doctorSetQueryWrapper);
        String isImg = doctorSet.getIsImg();
        String isRevisit = doctorSet.getIsRevisit();
        String isVideo = doctorSet.getIsVideo();
        String isVoice = doctorSet.getIsVoice();
        if (isImg.equals("1")) {
            doctorWorkVo.setWorkStatus("可接诊");
        } else {
            //判断星期
            String weekCode = WeekUtil.todayWeek();
            QueryWrapper<DoctorSetTimes> doctorSetTimesQueryWrapper = new QueryWrapper<>();
            doctorSetTimesQueryWrapper.eq("doctor_user_id", id)
                    .eq("week_code", weekCode);
            List<DoctorSetTimes> doctorSetTimesList = doctorSetTimesMapper.selectList(doctorSetTimesQueryWrapper);
            if (doctorSetTimesList != null && doctorSetTimesList.size() != 0) {
                for (DoctorSetTimes doctorSetTimes : doctorSetTimesList) {
                    String startTime = doctorSetTimes.getStartTime();
                    String endTime = doctorSetTimes.getEndTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    String now = simpleDateFormat.format(new Date());
                    boolean b = WeekUtil.inTime(now, startTime, endTime);
                    if (b) {
                        String type = doctorSetTimes.getType();
                        if (type.equals("VOICE") && isVoice.equals("1")) {
                            doctorWorkVo.setWorkStatus("可接诊");
                            break;
                        } else if (type.equals("VIDEO") && isVideo.equals("1")) {
                            doctorWorkVo.setWorkStatus("可接诊");
                            break;
                        } else if (type.equals("REVISIT") && isRevisit.equals("1")) {
                            doctorWorkVo.setWorkStatus("可接诊");
                            break;
                        }
                    }
                }
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("doctorData", getDoctorData(id, doctorWorkVo));
        map.put("revisitData", getRevisitDataTest());
        map.put("orderData", getOrderList(id));
        return map;
    }

    /**
     * 首页提示信息
     *
     * @return
     */
    public Object getRevisitDataTest() {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("type", "DOCKER")
                .eq("is_delete", "0")
                .select("title")
                .orderByAsc(true, "create_time");
        List<Message> homePageMessageList = messageMapper.selectList(wrapper);
        List<String> titleList = new ArrayList<>();
        for (Message homepageMessage : homePageMessageList) {
            titleList.add(homepageMessage.getTitle());
        }
        return titleList;
    }
}
