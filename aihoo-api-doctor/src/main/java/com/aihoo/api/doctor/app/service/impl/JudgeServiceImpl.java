package com.aihoo.api.doctor.app.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aihoo.enums.PushMessageType;
import com.aihoo.util.OssFileUtils;
import com.aihoo.util.SmsUtils;
import com.aihoo.util.StringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aihoo.api.doctor.app.mapper.*;
import com.aihoo.api.doctor.app.model.*;
import com.aihoo.api.doctor.app.service.IMService;
import com.aihoo.api.doctor.app.service.JudgeService;
import com.aihoo.properties.CheckingProperties;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.api.doctor.common.utils.HisResultFormat;
import com.aihoo.api.doctor.common.utils.IMMsgType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.List;
import java.util.*;

/**
 * @program: aihoo-root
 * @description:
 * @author: Mr.Li
 * @create: 2020-11-05 11:44
 **/
@Service
public class JudgeServiceImpl implements JudgeService {
    private final Log log = LogFactory.get();
    @Resource
    private IMService imService;
    @Resource
    private DrugMapper drugMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private MdtTeamMapper mdtTeamMapper;
    @Resource
    private MdtOrderMapper mdtOrderMapper;
    @Resource
    private HosVisitMapper hosVisitMapper;
    @Resource
    private DoctorUserMapper doctorUserMapper;
    @Resource
    private HosRevisitMapper hosRevisitMapper;
    @Resource
    private CheckingProperties checkingProperties;
    @Resource
    private HosPreDrugOrderMapper hosPreDrugOrderMapper;
    @Resource
    private HosPrescriptionMapper hosPrescriptionMapper;
    @Resource
    private PushMessageServiceImpl pushMessageServiceImpl;
    @Resource
    private HosPrescriptionDrugMapper hosPrescriptionDrugMapper;
    @Resource
    private HosPrescriptionDiseaseMapper hosPrescriptionDiseaseMapper;

    /**
     * 审方
     *
     * @param msg 处方id
     * @throws Exception e
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void judge(String msg) {
        try {
            LambdaQueryWrapper<HosPrescriptionDrug> hosPrescriptionDrugLambdaQueryWrapper = new QueryWrapper<HosPrescriptionDrug>().lambda();
            hosPrescriptionDrugLambdaQueryWrapper.eq(HosPrescriptionDrug::getHosPrescriptionId, msg);
            LambdaQueryWrapper<HosPrescriptionDisease> hosPrescriptionDiseaseLambdaQueryWrapper = new QueryWrapper<HosPrescriptionDisease>().lambda();
            hosPrescriptionDiseaseLambdaQueryWrapper.eq(HosPrescriptionDisease::getHosPrescriptionId, msg);
            HosPrescription hosPrescription = hosPrescriptionMapper.selectById(msg);
            List<HosPrescriptionDrug> hosPrescriptionDrugs = hosPrescriptionDrugMapper.selectList(hosPrescriptionDrugLambdaQueryWrapper);
            List<HosPrescriptionDisease> hosPrescriptionDiseases = hosPrescriptionDiseaseMapper.selectList(hosPrescriptionDiseaseLambdaQueryWrapper);

            String result = startJudge(hosPrescription, hosPrescriptionDiseases, hosPrescriptionDrugs, "1");
            if (result == null) {
                log.error("审方接口调用失败");
                return;
            }
            String content = "";
            String code = "";
            String time = "";
            String handleUserName = "";
            String handleUserId = "";
            String handleUserMobile = "";
            String isDisable = "0";
            try {
                HashMap resultMap = JSON.parseObject(result, HashMap.class);
                code = resultMap.get("code").toString();
                if (hosPrescription.getIsCanForce().equals("1") && "0".equals(hosPrescription.getIsDisable())) {
                    log.info("强制执行成功");
                    content = "强制执行成功";
                } else {
                    HashMap prescrJson = JSON.parseObject(resultMap.get("prescrJson").toString(), HashMap.class);
                    HashMap auditorPrescrHandle = JSON.parseObject(prescrJson.get("auditorPrescrHandle").toString(), HashMap.class);
                    for (Map<String, String> prescrProblems : HisResultFormat.getMaps(prescrJson.get("auditorPrescrProblems"))) {
                        if ("3".equals(prescrProblems.get("errorLevel"))) {
                            isDisable = "1";
                        }
                    }
                    content = auditorPrescrHandle.get("content").toString();
                    time = resultMap.get("time").toString();
                    handleUserName = auditorPrescrHandle.get("handleUserName").toString();
                    handleUserId = auditorPrescrHandle.get("handleUserId").toString();
                    if(null != auditorPrescrHandle.get("handleUserMobile")){
                        handleUserMobile = auditorPrescrHandle.get("handleUserMobile").toString();
                    }
                }
            } catch (Exception e) {
                log.error(e, "审方结果异常，请查看相应处方返回值");
                HosPrescription updateHosPrescription = new HosPrescription();
                updateHosPrescription.setId(hosPrescription.getId());
                updateHosPrescription.setCheckReturn(result);
                hosPrescriptionMapper.updateById(updateHosPrescription);
                return;
            }
            /*
             *         code  100就是通过  102就弹窗  103执行等待的时间
             *         首先102他调用继续执行的接口。
             *         看返回结果是100就通过，
             *         是103就是药师审核，这个时候看time字段，然后倒计时等待我们调他的回掉接口告诉他通过退回还是超时通过。
             *         时间到了之后我们还没有调他的接过他就调用我们的超时通过接口，然后保存
             */
            String type = hosPrescription.getType();
            String visitMdtNum = hosPrescription.getVisitMdtNum();
            String orderId = "";
            String doctorUserName = "";
            switch (type) {
                case "REVISIT":
                    LambdaQueryWrapper<HosRevisit> lambda1 = new QueryWrapper<HosRevisit>().lambda();
                    lambda1.eq(HosRevisit::getOrderNum, visitMdtNum);
                    HosRevisit hosRevisit = hosRevisitMapper.selectOne(lambda1);
                    orderId = hosRevisit.getId();
                    doctorUserName = doctorUserMapper.selectById(hosPrescription.getDoctorUserId()).getName() + "医生";
                    break;
                case "VISIT":
                    LambdaQueryWrapper<HosVisit> lambda3 = new QueryWrapper<HosVisit>().lambda();
                    lambda3.eq(HosVisit::getOrderNum, visitMdtNum);
                    HosVisit hosVisit = hosVisitMapper.selectOne(lambda3);
                    orderId = hosVisit.getId();
                    doctorUserName = doctorUserMapper.selectById(hosPrescription.getDoctorUserId()).getName() + "医生";
                    break;
                case "MDT":
                    LambdaQueryWrapper<MdtOrder> lambda2 = new QueryWrapper<MdtOrder>().lambda();
                    lambda2.eq(MdtOrder::getOrderNum, visitMdtNum);
                    MdtOrder mdtOrder = mdtOrderMapper.selectOne(lambda2);
                    doctorUserName = mdtTeamMapper.selectById(mdtOrder.getMdtTeamId()).getName();
                    break;
            }
            if ("100".equals(code)) {
                createOrders(hosPrescription);
                //img发送 会诊不发，在线复诊和复诊发
                if ("REVISIT".equals(type) || "VISIT".equals(type)) {
                    HashMap<Object, Object> map = new HashMap<>();
                    map.put("msgType", IMMsgType.PrescribeCheckSuccessCard);
                    map.put("orderId", hosPrescription.getId());
                    map.put("orderNum", hosPrescription.getOrderNum());
                    map.put("businessID", "MSHCustomMsg");
                    imService.sendPostHttpRequest("DOCTOR_" + hosPrescription.getDoctorUserId(), "PATIENT_" + hosPrescription.getPatientUserId(), map, "已开具电子处方，请查收", "", "");
                }
                hosPrescription.setCheckPharmaceutist(handleUserName);
                String url = makePrescriptionImg(hosPrescription, hosPrescriptionDiseases, hosPrescriptionDrugs);
                HosPrescription updateHosPrescription = new HosPrescription();
                updateHosPrescription.setCheckReturn(result);
                updateHosPrescription.setCheckContent(content);
                updateHosPrescription.setCheckTime(DateUtil.now());
                updateHosPrescription.setCheckPharmaceutist(handleUserName);
                updateHosPrescription.setCheckPharmaceutistId(handleUserId);
                updateHosPrescription.setCheckStatus(StatusEnumUtil.PASS);
                updateHosPrescription.setImg(url);
                updateHosPrescription.setId(hosPrescription.getId());
                hosPrescriptionMapper.updateById(updateHosPrescription);
                pushMessageServiceImpl.insertPatient("处方审核 ：", hosPrescription.getPatientUserId(), "温馨提醒：" + doctorUserName + "开具的处方已审核通过，请尽快完成支付。药品物流费用根据物流公司标准结算，送药上门时您自行支付",
                        PushMessageType.messageType_PRESCRIPTION, hosPrescription.getId(), "该处方已审核通过，请在3日内购买，以免过期失效", "0", hosPrescription.getCheckTime());
                log.info("********************审核通过********************");
            } else if ("102".equals(code)) {
                hosPrescription.setCheckPharmaceutist(handleUserName);
                HosPrescription updateHosPrescription = new HosPrescription();
                updateHosPrescription.setCheckReturn(result);
                updateHosPrescription.setCheckContent(content);
                updateHosPrescription.setCheckTime(DateUtil.now());
                updateHosPrescription.setCheckPharmaceutist(handleUserName);
                updateHosPrescription.setCheckPharmaceutistId(handleUserId);
                updateHosPrescription.setCheckStatus(StatusEnumUtil.REJECT);
                updateHosPrescription.setId(hosPrescription.getId());
                updateHosPrescription.setIsDisable(isDisable);
                hosPrescriptionMapper.updateById(updateHosPrescription);
                pushMessageServiceImpl.insertDoctor("处方审核 ：", hosPrescription.getDoctorUserId(), "您提交的患者" + hosPrescription.getPatientUserId() + "处方审核失败，" + content + "，请尽快前往处理。",
                        PushMessageType.messageType_PRESCRIPTION, hosPrescription.getId(), "您提交的患者" + hosPrescription.getPatientUserId() + "处方审核失败，" + content + "，请尽快前往处理。", "0");
                log.info("********************审核失败********************");
            } else if ("103".equals(code)) {
                HosPrescription updateHosPrescription = new HosPrescription();
                Date date = DateUtil.date();
                Date lastDate = DateUtil.offsetSecond(date, Integer.parseInt(time));
                String format = DateUtil.format(lastDate, "yyyy-MM-dd HH:mm:ss");
                updateHosPrescription.setCheck_timeout(format);
                updateHosPrescription.setCheckStatus(StatusEnumUtil.MANUALAUDIT);
                updateHosPrescription.setId(hosPrescription.getId());
                hosPrescriptionMapper.updateById(updateHosPrescription);
                pushMessageServiceImpl.insertDoctor("处方审核 ：", hosPrescription.getDoctorUserId(), "您提交的患者" + hosPrescription.getPatientUserId() + "处方已转为人工审核，" + content + "，请耐心等待。",
                        PushMessageType.messageType_PRESCRIPTION, hosPrescription.getId(), "您提交的患者" + hosPrescription.getPatientUserId() + "处方已转为人工审核，" + content + "，请耐心等待。", "0");
                log.info("********************等待人工审方********************");
                // TODO: 需第三方提供字段
                if(!StringUtil.isBlank(handleUserMobile)){
                    log.info(SmsUtils.offlineSend("{$var}向您提交人工处方审核请求，请尽快前往处理。", handleUserMobile, doctorUserName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 审方成功后创建药品处方订单
     *
     * @param hosPrescription 处方单
     */
    public void createOrders(HosPrescription hosPrescription) {
        Order order = new Order();
        order.setOrderType(hosPrescription.getType());
        order.setOrderNum(hosPrescription.getOrderNum());
        order.setPatientUserId(hosPrescription.getPatientUserId());
        order.setOtherId(hosPrescription.getOtherId());
        order.setTotalPrice(hosPrescription.getTotalPrice());
        order.setPayStatus("WAIT");
        orderMapper.insert(order);
        HosPreDrugOrder hosPreDrugOrder = new HosPreDrugOrder();
        hosPreDrugOrder.setIsPre("1");
        hosPreDrugOrder.setType(hosPrescription.getType());
        hosPreDrugOrder.setHosPrescriptionId(hosPrescription.getId());
        hosPreDrugOrder.setOrderNum(hosPrescription.getOrderNum());
        hosPreDrugOrder.setStatus("WAIT");
        hosPreDrugOrder.setPatientUserId(hosPrescription.getPatientUserId());
        hosPreDrugOrderMapper.insert(hosPreDrugOrder);
    }

    /**
     * 审方成功生成处方笺
     *
     * @param hosPrescription 处方
     * @param diseaseList     疾病
     * @param drugList        药品
     * @return String
     * @throws Exception e
     */
    public String makePrescriptionImg(HosPrescription hosPrescription, List<HosPrescriptionDisease> diseaseList, List<HosPrescriptionDrug> drugList) throws Exception {
        //行间距
        int addY = 55;
        int addX = 30;
        int width = 1280; // 图片宽
        int height = 720 + (drugList.size() > 3 ? addY * (drugList.size() - 2) : 0);// 图片高
        // 得到图片缓冲区
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);// INT精确度达到一定,RGB三原色，高度70,宽度150
        // 得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setColor(Color.WHITE); // 设置背景颜色
        g2.fillRect(0, 0, width, height);// 填充整张图片(其实就是设置背景颜色)
        g2.setColor(Color.black);// 设置字体颜色
        g2.setStroke(new BasicStroke(2.0f)); // 边框加粗
        g2.drawRect(1, 1, width - 2, height - 2); // 画边框就是黑边框
        g2.setStroke(new BasicStroke(0.0f)); // 边框不需要加粗
        //右上角方块，普通药品处方
        int squareWeight = 90;//正方形的边长
        int squareX = 1160;//左上角的x轴
        int squareY = 30;//左上角的Y轴
        g2.drawLine(squareX, squareY, squareX + squareWeight, squareY); // 上
        g2.drawLine(squareX, squareY, squareX, squareY + squareWeight); // 左
        g2.drawLine(squareX, squareY + squareWeight, squareX + squareWeight, squareY + squareWeight); // 下
        g2.drawLine(squareX + squareWeight, squareY, squareX + squareWeight, squareY + squareWeight); // 右
        //文字
        Font squareFont = new Font("宋体", Font.PLAIN, squareWeight / 4);
        g2.setFont(squareFont);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 抗锯齿
        FontMetrics squarefm = g2.getFontMetrics(squareFont);
        g2.drawString("普通药", squareX + ((squareWeight - squarefm.stringWidth("普通药")) / 2), squareY + squareWeight / 2);
        g2.drawString("品处方", squareX + ((squareWeight - squarefm.stringWidth("品处方")) / 2), squareY + squareWeight * 3 / 4);

        // 设置标题的字体,字号,大小
        Font titleFont = new Font("黑体", Font.BOLD, 40);
        g2.setFont(titleFont);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 抗锯齿
        // 计算文字长度,计算居中的X点坐标
        FontMetrics fm = g2.getFontMetrics(titleFont);
        int titleWidth = fm.stringWidth("处方笺");
        int titleWidthX = (width - titleWidth) / 2;
        g2.drawString("处方笺", titleWidthX, squareY + squareWeight * 3 / 4);


        // 第一行y轴
        int Y1 = squareY + squareWeight * 3 / 2;
        int X1 = 70;
        // 第五行（使用上面的文字格式，所以不要动）
        g2.drawString("RP", X1, Y1 + addY * 4);
        g2.setFont(new Font("宋体", Font.PLAIN, addX));
        // 第一行
        g2.drawString("NO：" + hosPrescription.getPrescriptionNum(), X1, Y1);
        g2.drawString("开方日期：" + hosPrescription.getCreateTime().substring(0, 10), X1 + addX * 28, Y1);
        // 2
        g2.drawString("姓名：" + hosPrescription.getName(), X1, Y1 + addY);
        g2.drawString("性别：" + (hosPrescription.getSex().equals("1") ? "男" : "女"), X1 + addX * 11, Y1 + addY);
        g2.drawString("年龄：" + hosPrescription.getAge(), X1 + addX * 22, Y1 + addY);
        g2.drawString("费别：" + (hosPrescription.getFeeType().equals("SELF") ? "自费" : ""), X1 + addX * 33, Y1 + addY);
        // 3
        g2.drawString("联系电话：" + hosPrescription.getMobile(), X1, Y1 + addY * 2);
        g2.drawString("科室：" + hosPrescription.getDepartName(), X1 + addX * 22, Y1 + addY * 2);
        // 4
        String disease = "临床诊断：";
        for (HosPrescriptionDisease hosPrescriptionDisease : diseaseList) {
            if ("临床诊断：".equals(disease)) {
                disease += hosPrescriptionDisease.getDiseaseName();
            } else {
                disease += "、" + hosPrescriptionDisease.getDiseaseName();
            }
        }
        g2.drawString(disease, X1, Y1 + addY * 3);

        // 6
        g2.drawString("名称", X1 + addX, Y1 + addY * 5);
        g2.drawString("规格", X1 + addX * 9, Y1 + addY * 5);
        g2.drawString("数量", X1 + addX * 18, Y1 + addY * 5);
        g2.drawString("用法用量", X1 + addX * 27, Y1 + addY * 5);
        //药品名称过长
        Font drugNameFont = new Font("宋体", Font.PLAIN, squareWeight / 6);
        for (int i = 0; i < drugList.size(); i++) {
            g2.setFont(drugNameFont);
            HosPrescriptionDrug hosPrescriptionDrug = drugList.get(i);
            g2.setFont(squareFont);
            g2.drawString(hosPrescriptionDrug.getName(), X1 + addX, Y1 + addY * (6 + i));
            g2.drawString(hosPrescriptionDrug.getSize(), X1 + addX * 9, Y1 + addY * (6 + i));
            g2.drawString(hosPrescriptionDrug.getNumber(), X1 + addX * 18, Y1 + addY * (6 + i));
            g2.drawString(hosPrescriptionDrug.getFreqMedName() + " " + hosPrescriptionDrug.getDosage() + " " + hosPrescriptionDrug.getRouteAdmiName(), X1 + addX * 27, Y1 + addY * (6 + i));
        }

        // 药品
        g2.drawString("医生签章：", X1, height - addY);
        URL url = new URL(hosPrescription.getDoctorSignet());
        Image image = ImageIO.read(url);
        g2.drawImage(image, X1 + addX * 5, height - addY * 2, 200, 75, null);
        g2.drawString("审方药师：" + hosPrescription.getCheckPharmaceutist(), X1 + addX * 18, height - addY);

        g2.dispose(); // 释放对象
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean flag = ImageIO.write(bi, "jpg", out);
        if (!flag) {
            log.info("图片转换流失败");
            return null;
        }
        byte[] b = out.toByteArray();
        String fileName = System.currentTimeMillis() + ".jpg";
        MultipartFile file = new MockMultipartFile(fileName, fileName, "", b);
        String resultUrl = OssFileUtils.uploadFile(file);
        log.info("上传成功URL:" + resultUrl);
        return resultUrl;
    }

    /**
     * 调用审方
     *
     * @return String
     */
    public String startJudge(HosPrescription hosPrescription, List<HosPrescriptionDisease> diseaseList, List<HosPrescriptionDrug> drugList, String submitCount) {
        try {
            if(null == hosPrescription) return null;
            String doctorUserId = hosPrescription.getDoctorUserId();
            DoctorUser doctorUser = doctorUserMapper.selectById(doctorUserId);
            // 处方信息
            Map<String, Object> prescrJson = new HashMap<>();
            prescrJson.put("patientId", hosPrescription.getIdCard());
            prescrJson.put("serialNo", hosPrescription.getVisitMdtNum());
            prescrJson.put("visitId", "1");
            prescrJson.put("patName", hosPrescription.getName());
            prescrJson.put("age", hosPrescription.getAge());
            prescrJson.put("sex", hosPrescription.getSex());
            prescrJson.put("birthday", DateUtil.format(IdcardUtil.getBirthDate(hosPrescription.getIdCard()), "yyyy-MM-dd"));
            prescrJson.put("admissionId", hosPrescription.getOrderNum());
            prescrJson.put("admissionDateTime", hosPrescription.getCreateTime());
            prescrJson.put("hisUuId", hosPrescription.getOrderNum());
            prescrJson.put("doctorCode", doctorUser.getId());
            prescrJson.put("doctorName", doctorUser.getName());
            prescrJson.put("startDateTime", hosPrescription.getCreateTime());
            prescrJson.put("deptCode", hosPrescription.getDepartCode());
            prescrJson.put("deptName", hosPrescription.getDepartName());
            prescrJson.put("submitCount", submitCount);
            prescrJson.put("hospitalCode", "");
            prescrJson.put("hospitalName", "名仕汇医院");
            prescrJson.put("isInternet", "Y");
            prescrJson.put("dllVersion", "HTTP");
            prescrJson.put("preType", "");
            prescrJson.put("kidneyStatus", hosPrescription.getKidneyStatus());
            prescrJson.put("liverStatus", hosPrescription.getLiverStatus());
            prescrJson.put("womanStatus", hosPrescription.getWomanStatus());
            prescrJson.put("allegeCode", "");
            prescrJson.put("allegeName", hosPrescription.getAllegeName());
            prescrJson.put("patientType", "");
            prescrJson.put("totalAmount", "");
            prescrJson.put("height", "");
            prescrJson.put("weight", "");
            List<Object> auditorICDs = new ArrayList<>();
            for (HosPrescriptionDisease prescriptionDisease : diseaseList) {
                HashMap<String, Object> auditorICD = new HashMap<>();
                auditorICD.put("hisUuid", hosPrescription.getOrderNum());
                auditorICD.put("iCDCode", prescriptionDisease.getDiseaseCode());
                auditorICD.put("iCDName", prescriptionDisease.getDiseaseName());
                auditorICDs.add(auditorICD);
            }
            prescrJson.put("auditorICDs", auditorICDs);
            List<Object> auditorPrescrs = new ArrayList<>();
            for (HosPrescriptionDrug prescriptionDrug : drugList) {
                HashMap<String, Object> auditorPrescr = new HashMap<>();
                auditorPrescr.put("patientId", hosPrescription.getIdCard());
                auditorPrescr.put("serialNo", hosPrescription.getVisitMdtNum());
                auditorPrescr.put("visitId", "1");
                auditorPrescr.put("hisUuId", hosPrescription.getOrderNum());
                auditorPrescr.put("prescrNo", hosPrescription.getOrderNum());
                auditorPrescr.put("groupNo", "");
                auditorPrescr.put("groupSubNo", "");
                auditorPrescr.put("deptCode", hosPrescription.getDepartCode());
                auditorPrescr.put("deptName", hosPrescription.getDepartName());
                auditorPrescr.put("doctorCode", doctorUserId);
                auditorPrescr.put("doctorName", doctorUser.getName());
                auditorPrescr.put("startDateTime", hosPrescription.getCreateTime());
                auditorPrescr.put("isCurrent", "0");
                Drug drug = drugMapper.selectById(prescriptionDrug.getDrugId());
                String drugCode = drug.getId();
                auditorPrescr.put("drugCode", drugCode);
                auditorPrescr.put("drugName", prescriptionDrug.getName());
                auditorPrescr.put("beginUseDate", "");
                auditorPrescr.put("endUseDate", "");
                auditorPrescr.put("drugSpec", drug.getSize());
                String dosage = prescriptionDrug.getDosage();
                String doseUnit = drug.getDoseUnit();
                auditorPrescr.put("dosage", dosage.replaceAll(doseUnit, ""));
                auditorPrescr.put("dosageUnits", doseUnit);
                auditorPrescr.put("qnty", drug.getUnitMeasure());
                auditorPrescr.put("qntyUnit", drug.getPackUnitName());
                auditorPrescr.put("total", "1");
                auditorPrescr.put("totalUnit", drug.getPackUnitName());
                auditorPrescr.put("freqTimes", "1");
                auditorPrescr.put("price", "");
                auditorPrescr.put("administrationCode", prescriptionDrug.getRouteAdmiCode());
                auditorPrescr.put("administration", prescriptionDrug.getRouteAdmiName());
                auditorPrescr.put("remark", "");
                auditorPrescr.put("longOnceFlag", "0");
                auditorPrescr.put("frequencyCode", prescriptionDrug.getFreqMedCode());
                auditorPrescr.put("frequencyName", prescriptionDrug.getFreqMedName());
                auditorPrescrs.add(auditorPrescr);
            }
            prescrJson.put("auditorPrescrs", auditorPrescrs);
            // 使用base64加密
            Map<String, Object> map = new HashMap<>();
            String s = JSONObject.toJSONString(prescrJson, JSONWriter.Feature.WriteNullStringAsEmpty);
            map.put("prescrJson", Base64.encode(s));
            map.put("strPreType", "1");
            map.put("updateFlag", "0");
            String result = null;
            if (hosPrescription.getIsCanForce().equals("1") && "0".equals(hosPrescription.getIsDisable())) {
                log.info("【强制执行审方】\n{}\nprescrJson入参：{}\n最终入参：{}",checkingProperties.getSaveForceUrl(), s, map);
                result = HttpUtil.post(checkingProperties.getSaveForceUrl(), map);
            } else {
                log.info("【首次执行审方】\n{}\nprescrJson入参：{}\n最终入参：{}",checkingProperties.getSaveUrl(), s, map);
                result = HttpUtil.post(checkingProperties.getSaveUrl(), map);
            }
            log.debug(result);
            return result;
        } catch (Exception e) {
            log.error(e, "审方接口调取失败");
            return null;
        }
    }


    public static void main(String[] args) {
        String a = "100mg".replaceAll("mg", "");
        System.out.println(a);
    }
}
