package com.aihoo.api.doctor.app.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aihoo.enums.PushMessageType;
import com.aihoo.util.OssFileUtils;
import com.aihoo.util.SmsUtils;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.aihoo.domain.consultation.model.mapper.MdtOrderMapper;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDrugMapper;
import com.aihoo.domain.prescription.model.mapper.HosPreDrugOrderMapper;
import com.aihoo.domain.prescription.model.entity.HosPreDrugOrder;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDiseaseMapper;
import com.aihoo.domain.visit.model.mapper.HosRevisitMapper;
import com.aihoo.domain.visit.model.mapper.HosVisitMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionLogMapper;
import com.aihoo.domain.payment.model.mapper.OrderMapper;
import com.aihoo.domain.payment.model.mapper.TbYlYppsxxMapper;

import com.aihoo.api.doctor.app.service.ApiService;
import com.aihoo.api.doctor.app.service.IMService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDisease;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrug;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionLog;
import com.aihoo.domain.payment.model.entity.Order;
import com.aihoo.domain.payment.model.entity.TbYlYppsxx;

/**
 * @program: aihoo-root
 * @description: 回调接口
 * @author: Mr.Li
 * @create: 2020-11-10 17:48
 **/
@Service
public class ApiServiceImpl implements ApiService {
    private final Log log = LogFactory.get();
    @Resource
    private IMService imService;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private HosVisitMapper hosVisitMapper;
    @Resource
    private MdtOrderMapper mdtOrderMapper;
    @Resource
    private HosRevisitMapper hosRevisitMapper;
    @Resource
    private DoctorUserMapper doctorUserMapper;
    @Resource
    private HosPrescriptionMapper hosPrescriptionMapper;
    @Resource
    private HosPrescriptionDrugMapper hosPrescriptionDrugMapper;
    @Resource
    private HosPrescriptionDiseaseMapper hosPrescriptionDiseaseMapper;
    @Resource
    private HosPreDrugOrderMapper hosPreDrugOrderMapper;
    @Resource
    private PushMessageServiceImpl pushMessageServiceImpl;
    @Resource
    private HosPrescriptionLogMapper hosPrescriptionLogMapper;
    @Resource
    private TbYlYppsxxMapper ylYppsxxMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean getPushSign(String mobile, String bizSn, String cert) {
        QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        wrapper.eq("mobile", mobile).eq("is_cancel", "0");
        DoctorUser doctorUser = doctorUserMapper.selectOne(wrapper);
        String caNumber = doctorUser.getCaNumber();
        if (caNumber != null && caNumber.equals(cert)) {
            //修改处方状态为已认证
            QueryWrapper<HosPrescription> hosPrescriptionQueryWrapper = new QueryWrapper<>();
            hosPrescriptionQueryWrapper.eq("prescription_num", bizSn);
            HosPrescription hosPrescription = hosPrescriptionMapper.selectOne(hosPrescriptionQueryWrapper);
            HosPrescription update = new HosPrescription();
            update.setId(hosPrescription.getId());
            update.setCheckStatus("SIGN");
            hosPrescriptionMapper.updateById(update);
            return true;
        }
        return false;
    }

    /**
     * @param prescripNo     处方单编号
     * @param prescripStatus 处方单状态
     *                       (10-处方上传成功初始状态（默认）；
     *                       40:已备货; 43:已配送；50:已签收; 98:已退货)
     *                       正向处理：益药宝未发送已备货状态，无退费限制；一旦发送给已备货状态，则不可退费；
     *                       逆向处理：接收到 98-已退货，可进行退费处理
     *                       0->40:订单状态 -已配货，待配送
     *                       1->43:订单状态 -已配送，待签收
     *                       2->50:订单状态 -已收款
     *                       3->98:订单状态 -用户已退货
     * @param dealDate       处理日期
     * @return
     */
    @Override
    public boolean getPrescriptionLogistics(String prescripNo, String prescripStatus, String dealDate) {
        QueryWrapper<HosPrescription> wrapper = new QueryWrapper<>();
        wrapper.eq("order_num", prescripNo);
        wrapper.eq("status", "PAY");
        wrapper.eq("check_status", "PASS");
        wrapper.eq("is_pay", "0");
        HosPrescription hosPrescription = hosPrescriptionMapper.selectOne(wrapper);
        if (null != hosPrescription) {
            if("1 2".contains(prescripStatus)){
                String status = "1".equals(prescripStatus) ? "SHIPPED" : "END";
                serviceTheGoodsRemind(hosPrescription);
                hosPrescriptionMapper.update(null, new UpdateWrapper<HosPrescription>().eq("order_num", prescripNo).set("status",status));
                hosPreDrugOrderMapper.update(null, new UpdateWrapper<HosPreDrugOrder>().eq("order_num", prescripNo).set("status",status).setSql((("1".equals(prescripStatus) ? "shippend_start_time" : "shippend_end_time") + " = NOW()")));
                ylYppsxxMapper.update(null,new UpdateWrapper<TbYlYppsxx>().set("PSZT","END".equals(status) ? 1 : 0 ).set("END".equals(status) ? "PSJSSJ" : "PSKSSJ" , "NOW()" ));

                Boolean x = "1".equals(prescripStatus) ? serviceDistributionRemind(hosPrescription) : serviceTheGoodsRemind(hosPrescription);
                
                // 插入配送开始结束日志
                String remark = "1".equals(prescripStatus) ? "快递正在配送中，感谢您的耐心等待，查看订单和物流信息" : "药品已签收。";
                HosPrescriptionLog pLog = new HosPrescriptionLog();
                pLog.setType("DOCKER");
                pLog.setHosPrescriptionId(hosPrescription.getId());
                pLog.setPatientUserId(hosPrescription.getPatientUserId());
                pLog.setDoctorUserId(hosPrescription.getDoctorUserId());
                pLog.setStatus(status);
                pLog.setRemark(remark);
                pLog.setSickId(hosPrescription.getHosSickId());
                hosPrescriptionLogMapper.insert(pLog);
            }
            return true;
        }
        return false;
    }

    /**
     * 患者完成支付，未确认收货 服务提醒
     *
     * @param hosPrescription
     * @return
     */
    private boolean serviceDistributionRemind(HosPrescription hosPrescription) {
        try {
            pushMessageServiceImpl.insertPatient("药品配送", hosPrescription.getPatientUserId(), "快递正在配送中，感谢您的耐心等待，查看订单和物流信息",
                    PushMessageType.messageType_DRUG, hosPrescription.getId(), "快递正在配送中，感谢您的耐心等待，查看订单和物流信息", "0");
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    /**
     * 确认收货服务提醒
     *
     * @param hosPrescription
     * @return
     */
    private boolean serviceTheGoodsRemind(HosPrescription hosPrescription) {
        try {
            pushMessageServiceImpl.insertPatient("药品配送", hosPrescription.getPatientUserId(), "您购买的药品已签收。",
                    PushMessageType.messageType_DRUG, hosPrescription.getId(), "您购买的药品已签收。", "0");
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    /**
     * 审方回调
     *
     * @param prescrJson Certificate=验证信息&userID=医生ID&code=审核结果代码& prescrJson=审核信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean judgeResult(String certificate, String userID, String code, Map<String, Object> prescrJson) throws Exception {
        String orderNum = prescrJson.get("hisUuId").toString();
        /*通过处方的订单编号查询处方以及关联的药品和疾病*/
        LambdaQueryWrapper<HosPrescription> hosPrescriptionLambdaQueryWrapper = new QueryWrapper<HosPrescription>().lambda();
        hosPrescriptionLambdaQueryWrapper.eq(HosPrescription::getOrderNum, orderNum);
        HosPrescription hosPrescription = hosPrescriptionMapper.selectOne(hosPrescriptionLambdaQueryWrapper);
        if (null == hosPrescription) {
            log.error("没有查到当前处方单" + orderNum);
            return false;
        }
        String hosPrescriptionId = hosPrescription.getId();
        LambdaQueryWrapper<HosPrescriptionDrug> hosPrescriptionDrugLambdaQueryWrapper = new QueryWrapper<HosPrescriptionDrug>().lambda();
        hosPrescriptionDrugLambdaQueryWrapper.eq(HosPrescriptionDrug::getHosPrescriptionId, hosPrescriptionId);
        List<HosPrescriptionDrug> hosPrescriptionDrugs = hosPrescriptionDrugMapper.selectList(hosPrescriptionDrugLambdaQueryWrapper);
        LambdaQueryWrapper<HosPrescriptionDisease> hosPrescriptionDiseaseLambdaQueryWrapper = new QueryWrapper<HosPrescriptionDisease>().lambda();
        hosPrescriptionDiseaseLambdaQueryWrapper.eq(HosPrescriptionDisease::getHosPrescriptionId, hosPrescriptionId);
        List<HosPrescriptionDisease> hosPrescriptionDiseases = hosPrescriptionDiseaseMapper.selectList(hosPrescriptionDiseaseLambdaQueryWrapper);
        /*判断审方状态*/
        /*药师信息*/
        Object auditorPrescrHandle = prescrJson.get("auditorPrescrHandle");
        /*获取药师信息*/
        HashMap auditorPrescrHandleMap = JSON.parseObject(auditorPrescrHandle.toString(), HashMap.class);
        /*审核药师名称*/
        String handleUserName = auditorPrescrHandleMap.get("handleUserName").toString();
        /*审核药师id*/
        String handleUserId = auditorPrescrHandleMap.get("handleUserId").toString();
        /*审核时间*/
        String handleDateTime = auditorPrescrHandleMap.get("handleDateTime").toString();
        /*审核意见*/
        String content = auditorPrescrHandleMap.get("content").toString();
        /*审核驳回修改*/
        if ("100".equals(code)) {
            createOrders(hosPrescription);
            HashMap<Object, Object> map = new HashMap<>();
            map.put("msgType", IMMsgType.PrescribeCheckSuccessCard);
            String type = hosPrescription.getVisitMdtNum();
            String visitMdtNum = hosPrescription.getVisitMdtNum();
            switch (type) {
                case "REVISIT":
                    LambdaQueryWrapper<HosRevisit> lambda1 = new QueryWrapper<HosRevisit>().lambda();
                    lambda1.eq(HosRevisit::getOrderNum, visitMdtNum);
                    HosRevisit hosRevisit = hosRevisitMapper.selectOne(lambda1);
                    map.put("orderId", hosRevisit.getId());
                    map.put("orderNum", hosPrescription.getOrderNum());
                    map.put("businessID", "MSHCustomMsg");
                    imService.sendPostHttpRequest("DOCTOR_" + hosPrescription.getDoctorUserId(), "PATIENT_" + hosPrescription.getPatientUserId(), map, "已开具电子处方，请查收", "", "");
                    break;
                case "VISIT":
                    LambdaQueryWrapper<HosVisit> lambda2 = new QueryWrapper<HosVisit>().lambda();
                    lambda2.eq(HosVisit::getOrderNum, visitMdtNum);
                    HosVisit hosVisit = hosVisitMapper.selectOne(lambda2);
                    map.put("orderId", hosVisit.getId());
                    map.put("orderNum", hosPrescription.getOrderNum());
                    map.put("businessID", "MSHCustomMsg");
                    imService.sendPostHttpRequest("DOCTOR_" + hosPrescription.getDoctorUserId(), "PATIENT_" + hosPrescription.getPatientUserId(), map, "已开具电子处方，请查收", "", "");
                    break;
                case "MDT":
                    break;
            }

            hosPrescription.setCheckPharmaceutist(handleUserName);
            hosPrescription.setCheckPharmaceutistId(handleUserId);
            String url = makePrescriptionImg(hosPrescription, hosPrescriptionDiseases, hosPrescriptionDrugs);
            HosPrescription updateHosPrescription = new HosPrescription();
            updateHosPrescription.setManualCheckReturn(prescrJson.toString());
            updateHosPrescription.setManualCheckContent(content);
            updateHosPrescription.setManualCheckTime(handleDateTime);
            updateHosPrescription.setManualCheckPharmaceutist(handleUserName);
            updateHosPrescription.setManualCheckPharmaceutistId(handleUserId);
            updateHosPrescription.setCheckStatus("PASS");
            updateHosPrescription.setImg(url);
            updateHosPrescription.setId(hosPrescription.getId());
            hosPrescriptionMapper.updateById(updateHosPrescription);
            log.info("********************审核通过********************");
            serviceJudgeSuccessRemind(orderNum);
            return true;
        } else {
            HosPrescription updateHosPrescription = new HosPrescription();
            updateHosPrescription.setId(hosPrescription.getId());
            updateHosPrescription.setCheckStatus("REJECT");
            updateHosPrescription.setManualCheckTime(handleDateTime);
            updateHosPrescription.setManualCheckReturn(JSON.toJSONString(prescrJson));
            updateHosPrescription.setManualCheckContent(content);
            hosPrescriptionMapper.updateById(updateHosPrescription);
            log.info("********************审核驳回********************");
            serviceJudgeErrorRemind(orderNum, content);
            return true;
        }
    }

    /**
     * 审方失败服务提醒
     *
     * @param orderNum orderNum
     * @param content content
     */
    private void serviceJudgeErrorRemind(String orderNum, String content) {
        try {
            QueryWrapper<HosPrescription> wrapper = new QueryWrapper<>();
            wrapper.eq("order_num", orderNum);
            HosPrescription hosPrescription = hosPrescriptionMapper.selectOne(wrapper);
            if (null == hosPrescription) {
                return;
            }
            log.info("处方服务提醒开始");
            String title = "处方审核";
            String intro = "您提交的患者" + hosPrescription.getName() + "处方审核失败，" + content + "，请尽快前往处理。";
            String messageType = PushMessageType.messageType_PRESCRIPTION;
            String otherId = hosPrescription.getId();
            String isPush = "0";
            pushMessageServiceImpl.insertDoctor(title, hosPrescription.getDoctorUserId() ,intro, messageType, otherId, intro, isPush);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("处方服务提醒失败");
        }
    }

    /**
     * 审方成功服务提醒
     *
     * @param orderNum 订单号
     */
    private void serviceJudgeSuccessRemind(String orderNum) {
        try {
            QueryWrapper<HosPrescription> wrapper = new QueryWrapper<>();
            wrapper.eq("order_num", orderNum);
            HosPrescription hosPrescription = hosPrescriptionMapper.selectOne(wrapper);
            if (null == hosPrescription) {
                return;
            }
            DoctorUser doctorUser = doctorUserMapper.selectOne(new QueryWrapper<DoctorUser>().eq("id", hosPrescription.getDoctorUserId()));
            if (null == doctorUser) {
                return;
            }

            log.info("处方服务提醒开始");
            String title = "处方审核";
            String intro = "温馨提醒：" + doctorUser.getName() + "医生开具的处方已审核通过，请尽快完成支付。药品物流费用根据物流公司标准结算，送药上门时您自行支付";
            String patientId = hosPrescription.getPatientUserId();
            String messageType = PushMessageType.messageType_PRESCRIPTION;
            String otherId = hosPrescription.getId();
            String content = "该处方已审核通过，请在3日内购买，以免过期失效";
            String isPush = "0";
            pushMessageServiceImpl.insertPatient(title, patientId, intro, messageType, otherId, content, isPush);
            log.info(SmsUtils.offlineSend("{$var}医生开具的处方已审核通过，请尽快完成支付。", hosPrescription.getMobile(), doctorUser.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("处方服务提醒失败");
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
        int insert1 = orderMapper.insert(order);
        HosPreDrugOrder hosPreDrugOrder = new HosPreDrugOrder();
        hosPreDrugOrder.setIsPre("1");
        hosPreDrugOrder.setType(hosPrescription.getType());
        hosPreDrugOrder.setHosPrescriptionId(hosPrescription.getId());
        hosPreDrugOrder.setOrderNum(hosPrescription.getOrderNum());
        hosPreDrugOrder.setStatus("WAIT");
        hosPreDrugOrder.setPatientUserId(hosPrescription.getPatientUserId());
        int insert2 = hosPreDrugOrderMapper.insert(hosPreDrugOrder);
        if (insert1 != 0 && insert2 != 0) {
            serviceDrugOrderCreateRemind(hosPrescription);
        }
    }

    /**
     * 审方通过创建药品订单服务提醒
     *
     * @param hosPrescription hosPrescription
     */
    private void serviceDrugOrderCreateRemind(HosPrescription hosPrescription) {
        try {
            DoctorUser doctorUser = doctorUserMapper.selectOne(new QueryWrapper<DoctorUser>().eq("id", hosPrescription.getDoctorUserId()));
            if (null == doctorUser) {
                return;
            }
            String name = doctorUser.getName();
            pushMessageServiceImpl.insertPatient("处方审核：", hosPrescription.getPatientUserId(),"温馨提醒：" +  name + "医生开具的处方已审核通过，请尽快完成支付。药品物流费用根据物流公司标准结算，送药上门时您自行支付",
                    PushMessageType.messageType_DRUG, hosPrescription.getId(), "该处方已审核通过，请在3日内购买，以免过期失效", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 审方成功生成处方笺
     *
     * @param hosPrescription hosPrescription
     * @param diseaseList     diseaseList
     * @param drugList        drugList
     * @return String
     * @throws Exception e
     */
    public String makePrescriptionImg(HosPrescription hosPrescription, List<HosPrescriptionDisease> diseaseList, List<HosPrescriptionDrug> drugList) throws Exception {
        /*行间距*/
        int addY = 55;
        int addX = 30;
        /*图片宽*/
        int width = 1280;
        /*图片高*/
        int height = 720 + (drugList.size() > 3 ? addY * (drugList.size() - 2) : 0);
        /*得到图片缓冲区 INT精确度达到一定,RGB三原色，高度70,宽度150*/
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        /*得到它的绘制环境(这张图片的笔)*/
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        /*设置背景颜色*/
        g2.setColor(Color.WHITE);
        /*填充整张图片(其实就是设置背景颜色)*/
        g2.fillRect(0, 0, width, height);
        /*设置字体颜色*/
        g2.setColor(Color.black);
        /*边框加粗*/
        g2.setStroke(new BasicStroke(2.0f));
        /*画边框就是黑边框*/
        g2.drawRect(1, 1, width - 2, height - 2);
        /*边框不需要加粗*/
        g2.setStroke(new BasicStroke(0.0f));
        /*右上角方块，普通药品处方*/
        /*正方形的边长*/
        int squareWeight = 90;
        /*左上角的x轴*/
        int squareX = 1160;
        /*左上角的Y轴*/
        int squareY = 30;
        /*上*/
        g2.drawLine(squareX, squareY, squareX + squareWeight, squareY);
        /*左*/
        g2.drawLine(squareX, squareY, squareX, squareY + squareWeight);
        /*下*/
        g2.drawLine(squareX, squareY + squareWeight, squareX + squareWeight, squareY + squareWeight);
        /*右*/
        g2.drawLine(squareX + squareWeight, squareY, squareX + squareWeight, squareY + squareWeight);
        /*文字*/
        Font squareFont = new Font("宋体", Font.PLAIN, squareWeight / 4);
        g2.setFont(squareFont);
        /*抗锯齿*/
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics squarefm = g2.getFontMetrics(squareFont);
        g2.drawString("普通药", squareX + ((squareWeight - squarefm.stringWidth("普通药")) / 2), squareY + squareWeight / 2);
        g2.drawString("品处方", squareX + ((squareWeight - squarefm.stringWidth("品处方")) / 2), squareY + squareWeight * 3 / 4);

        /* 设置标题的字体,字号,大小 */
        Font titleFont = new Font("黑体", Font.BOLD, 40);
        g2.setFont(titleFont);
        /* 抗锯齿 */
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        /* 计算文字长度,计算居中的X点坐标 */
        FontMetrics fm = g2.getFontMetrics(titleFont);
        int titleWidth = fm.stringWidth("处方笺");
        int titleWidthX = (width - titleWidth) / 2;
        g2.drawString("处方笺", titleWidthX, squareY + squareWeight * 3 / 4);


        /* 第一行y轴 */
        int Y1 = squareY + squareWeight * 3 / 2;
        int X1 = 70;
        // 第五行（使用上面的文字格式，所以不要动）
        g2.drawString("RP", X1, Y1 + addY * 4);
        g2.setFont(new Font("宋体", Font.PLAIN, addX));
        /* 第一行 */
        g2.drawString("NO：" + hosPrescription.getPrescriptionNum(), X1, Y1);
        g2.drawString("开方日期：" + hosPrescription.getCreateTime().substring(0, 10), X1 + addX * 28, Y1);
        /* 2 */
        g2.drawString("姓名：" + hosPrescription.getName(), X1, Y1 + addY);
        g2.drawString("性别：" + (hosPrescription.getSex().equals("1") ? "男" : "女"), X1 + addX * 11, Y1 + addY);
        g2.drawString("年龄：" + hosPrescription.getAge(), X1 + addX * 22, Y1 + addY);
        g2.drawString("费别：" + (hosPrescription.getFeeType().equals("SELF") ? "自费" : ""), X1 + addX * 33, Y1 + addY);
        /* 3 */
        g2.drawString("联系电话：" + hosPrescription.getMobile(), X1, Y1 + addY * 2);
        g2.drawString("科室：" + hosPrescription.getDepartName(), X1 + addX * 22, Y1 + addY * 2);
        /* 4 */
        String disease = "临床诊断：";
        for (HosPrescriptionDisease hosPrescriptionDisease : diseaseList) {
            if ("临床诊断：".equals(disease)) {
                disease += hosPrescriptionDisease.getDiseaseName();
            } else {
                disease += "、" + hosPrescriptionDisease.getDiseaseName();
            }
        }
        g2.drawString(disease, X1, Y1 + addY * 3);

        /* 6 */
        g2.drawString("名称", X1 + addX, Y1 + addY * 5);
        g2.drawString("规格", X1 + addX * 9, Y1 + addY * 5);
        g2.drawString("数量", X1 + addX * 18, Y1 + addY * 5);
        g2.drawString("用法用量", X1 + addX * 27, Y1 + addY * 5);

        for (int i = 0; i < drugList.size(); i++) {
            HosPrescriptionDrug hosPrescriptionDrug = drugList.get(i);
            g2.drawString(hosPrescriptionDrug.getName(), X1 + addX, Y1 + addY * (6 + i));
            g2.drawString(hosPrescriptionDrug.getSize(), X1 + addX * 9, Y1 + addY * (6 + i));
            g2.drawString(hosPrescriptionDrug.getNumber(), X1 + addX * 18, Y1 + addY * (6 + i));
            g2.drawString(hosPrescriptionDrug.getFreqMedName() + " " + hosPrescriptionDrug.getDosage() + " " + hosPrescriptionDrug.getRouteAdmiName(), X1 + addX * 27, Y1 + addY * (6 + i));
        }

        /*药品*/
        g2.drawString("医生签章：", X1, height - addY);
        URL url = new URL(hosPrescription.getDoctorSignet());
        Image image = ImageIO.read(url);
        g2.drawImage(image, X1 + addX * 5, height - addY * 2, 200, 75, null);
        g2.drawString("审方药师：" + hosPrescription.getCheckPharmaceutist(), X1 + addX * 18, height - addY);
        /* 释放对象*/
        g2.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean flag = ImageIO.write(bi, "jpg", out);
        if (!flag) {
            log.info("图片转换流失败");
            return null;
        }
        byte[] b = out.toByteArray();
        String fileName = "Prescription_" + System.currentTimeMillis() + ".jpg";
        MultipartFile file = new MockMultipartFile(fileName, fileName, "", b);
        if (null == file) {
            file.getSize();
        }
        String resultUrl = OssFileUtils.uploadFile(file);
        log.info("上传成功URL:" + resultUrl);
        return resultUrl;
    }
}
