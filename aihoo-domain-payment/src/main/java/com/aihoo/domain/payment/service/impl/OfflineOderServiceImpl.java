package com.aihoo.domain.payment.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aihoo.common.HospitalOrderResult;
import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.config.HospitalProperties;
import com.aihoo.domain.payment.model.entity.*;
import com.aihoo.domain.payment.model.mapper.*;
import com.aihoo.domain.payment.model.vo.*;
import com.aihoo.domain.payment.service.OfflineOderService;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.util.DateUtil;
import com.aihoo.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 线下订单 Service 实现
 *
 * <p>主要依赖本域 mapper + HospitalProperties（shared-kernel）。</p>
 *
 * @author lenovo
 */
@Service
@Slf4j
public class OfflineOderServiceImpl extends ServiceImpl<OfflineOderMapper, OfflineOder> implements OfflineOderService {
    @Resource
    private OfflineOderMapper oderMapper;

    @Resource
    private HospitalProperties properties;

    @Resource
    private OfflineTreatmentMapper treatmentMapper;

    @Resource
    private OfflineDetailsMapper detailsMapper;

    @Resource
    private OfflineCompanyMapper companyMapper;

    @Resource
    private OfflineClinicCardMapper clinicCard;

    @Resource
    private OfflineOderUserMapper userMapper;

    private static void transformationHospital(List<OfflineOder> records) {
        records.stream().forEach(x -> {
            if (!"".equals(x.getHospitalName()) && null != x.getHospitalName()) {
                switch (x.getHospitalName()) {
                    case "总院":
                        x.setHospitalName("复旦大学附属华山医院(总院)");
                        break;
                    case "江苏路分部":
                        x.setHospitalName("复旦大学附属华山医院(江苏路分部)");
                        break;
                    case "传染科大楼":
                        x.setHospitalName("复旦大学附属华山医院(传染科大楼)");
                        break;
                    case "西院":
                        x.setHospitalName("复旦大学附属华山医院(西院)");
                        break;
                }
            }
        });
    }

    /**
     * 查询订单列表
     */
    @Override
    public PageResult<OfflineOder> findAll(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<OfflineOder> wrapper = isField(map);
        IPage<OfflineOder> oderIPage = this.oderMapper.selectPage(new Page<>(page, limit), wrapper);
        transformationHospital(oderIPage.getRecords());
        return new PageResult<>(oderIPage.getRecords(), oderIPage.getTotal());
    }

    /**
     * 改价
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updPrice(Map<String, Object> map) {
        QueryWrapper<OfflineOder> wrapper = new QueryWrapper<>();
        wrapper.eq("id", map.get("id"));
        OfflineOder oder = new OfflineOder();
        oder.setPrice(map.get("price").toString());
        oder.setOnly("2");
        int update = this.oderMapper.update(oder, wrapper);
        return update == 1;
    }

    /**
     * 根据就诊人查询就诊人信息
     */
    @Override
    public List<OfflineOderUser> findCertificates(Map<String, Object> map) {
        QueryWrapper<OfflineOderUser> wrapper = new QueryWrapper<>();
        wrapper.eq("name", map.get("name"));
        List<OfflineOderUser> list = this.userMapper.selectList(wrapper);
        return list;
    }

    /**
     * 查询目前的医院
     */
    @Override
    public List<OfflineHospital> findHospitalCard() {
        return this.oderMapper.selectHospitalCard();
    }

    /**
     * 制单人
     */
    @Override
    public List<String> preparationNameList(Map<String, Object> map) {
        return oderMapper.findPreparationNameList();
    }

    /**
     * 根据的医院查询科室下的医生
     */
    @Override
    public List<OfflineStaff> findStaff(Map<String, Object> map) {
        if (!"0".equals(map.get("districtId"))) {
            return this.oderMapper.findStaff(map);
        } else {
            List<OfflineStaff> staffList = new ArrayList<>();
            List<OfflineNodeDoctor> nodeList = this.oderMapper.findNodeDoctor(map);
            nodeList.forEach(x -> {
                OfflineStaff staff = new OfflineStaff();
                staff.setStaffCode(x.getMapId());
                staff.setStaffName(x.getEntityName());
                staff.setCodeId(x.getNodeId());
                staff.setCodeName(x.getPoolId());
                staffList.add(staff);
            });
            return staffList;
        }
    }

    /**
     * 院区下的挂号类型
     */
    @Override
    public List<OfflineBooking> findBooking(Map<String, Object> map) {
        if (!"0".equals(map.get("districtId"))) {
            return this.oderMapper.findBooking(map);
        } else {
            List<OfflineBooking> bookingList = new ArrayList<>();
            OfflineBooking booking = new OfflineBooking();
            booking.setDistrictId("0");
            booking.setBookingId("2");
            booking.setBookingName("专家门诊");
            bookingList.add(booking);
            OfflineBooking booking1 = new OfflineBooking();
            booking1.setDistrictId("0");
            booking1.setBookingId("13");
            booking1.setBookingName("特需全自费");
            bookingList.add(booking1);
            return bookingList;
        }
    }

    /**
     * 院区和挂号类型下的 科室
     */
    @Override
    public List<OfflineDepartment> queryDepart(Map<String, Object> map) {
        if (!"0".equals(map.get("districtId"))) {
            return this.oderMapper.queryDepart(map);
        } else {
            List<OfflineDepartment> departmentList = new ArrayList<>();
            List<OfflineNode> nodeList = this.oderMapper.queryNode(map);
            nodeList.forEach(x -> {
                OfflineDepartment department = new OfflineDepartment();
                department.setCodeId(x.getNodeId());
                department.setCodeName(x.getNodeName());
                department.setBookingId(x.getPoolId());
                departmentList.add(department);
            });
            return departmentList;
        }
    }

    /**
     * 查询就诊卡列表
     */
    @Override
    public List<OfflineClinicCard> findClinicCard(Map<String, Object> map) {
        QueryWrapper<OfflineClinicCard> wrapper = new QueryWrapper<>();
        wrapper.eq("certificates_number", map.get("certificatesNumber"));
        wrapper.eq("is_delete", 0);
        return this.clinicCard.selectList(wrapper);
    }

    /**
     * 删除就诊卡
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCard(Map<String, Object> map) {
        QueryWrapper<OfflineClinicCard> wrapper = new QueryWrapper<>();
        wrapper.eq("id", map.get("id").toString());
        OfflineClinicCard clinicCard = new OfflineClinicCard();
        clinicCard.setIsDelete("1");
        return this.clinicCard.update(clinicCard, wrapper) == 1;
    }

    /**
     * 新增数据 模糊查询
     */
    @Override
    public List<String> likeOder(Map<String, Object> map) {
        QueryWrapper<OfflineOder> wrapper = new QueryWrapper<>();
        return ifListOder(map, wrapper);
    }

    /**
     * 录入就诊卡的信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createClinicCard(Map<String, Object> map) {
        OfflineClinicCard card = new OfflineClinicCard();
        if (null != map.get("hospitalId") && !"".equals(map.get("hospitalId"))) {
            card.setHospitalId(map.get("hospitalId").toString());
        }
        if (null != map.get("hospitalName") && !"".equals(map.get("hospitalName"))) {
            card.setHospitalName(map.get("hospitalName").toString());
        }
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            card.setName(map.get("name").toString());
        }
        if (null != map.get("sex") && !"".equals(map.get("sex"))) {
            card.setSex(map.get("sex").toString());
        }
        if (null != map.get("phone") && !"".equals(map.get("phone"))) {
            card.setPhone(map.get("phone").toString());
        }
        if (null != map.get("certificatesType") && !"".equals(map.get("certificatesType"))) {
            card.setCertificatesType(map.get("certificatesType").toString());
        }
        if (null != map.get("certificatesNumber") && !"".equals(map.get("certificatesNumber"))) {
            card.setCertificatesNumber(map.get("certificatesNumber").toString());
        }
        if (null != map.get("birth") && !"".equals(map.get("birth"))) {
            card.setBirth(map.get("birth").toString());
        }
        if (null != map.get("coordinate") && !"".equals(map.get("coordinate"))) {
            card.setCoordinate(map.get("coordinate").toString());
        }
        if (null != map.get("clinicName") && !"".equals(map.get("clinicName"))) {
            card.setClinicName(map.get("clinicName").toString());
        }
        return this.clinicCard.insert(card) == 1;
    }

    /**
     * 转换证件类型的
     */
    public static void transformationType(Map<String, Object> map) {
        if (null != map.get("certificatesType") && !"".equals(map.get("certificatesType"))) {
            switch (map.get("certificatesType").toString()) {
                case "身份证":
                    map.put("certificatesType", "1");
                    break;
                case "护照":
                    map.put("certificatesType", "2");
                    break;
                case "回乡证":
                    map.put("certificatesType", "3");
                    break;
                case "军官证":
                    map.put("certificatesType", "4");
                    break;
                case "港澳台身份证":
                    map.put("certificatesType", "5");
                    break;
                case "其他":
                    map.put("certificatesType", "0");
                    break;
                case "NULL":
                    map.put("certificatesType", "");
            }
        }
        if (null != map.get("mode") && !"".equals(map.get("mode"))) {
            switch (map.get("mode").toString()) {
                case "月结":
                    map.put("mode", "1");
                    break;
                case "预付":
                    map.put("mode", "2");
                    break;
                case "现金":
                    map.put("mode", "3");
                    break;
            }
        }
        if (null != map.get("visitTime") && !"".equals(map.get("visitTime"))) {
            String visitTime = map.get("visitTime").toString();
            if (visitTime.length() == 23) {
                String data = visitTime.substring(0, 16);
                map.put("visitTime", data);
            } else {
                map.put("visitTime", "");
            }
        }
        if (null != map.get("preparationTime") && !"".equals(map.get("preparationTime"))) {
            String preparationTime = map.get("preparationTime").toString();
            if (preparationTime.length() == 23) {
                String substring = preparationTime.substring(0, 19);
                map.put("preparationTime", substring);
            }
        }
        if (null != map.get("treatmentName") && !"".equals(map.get("treatmentName"))) {
            String treatmentName = map.get("treatmentName").toString();
            String substring = treatmentName.substring(treatmentName.length() - 2);
            if ("受理".equals(substring)) {
                map.put("treatmentName", treatmentName.substring(0, treatmentName.length() - 2));
            }
        }
        if (null != map.get("preparationName") && !"".equals(map.get("preparationName"))) {
            switch (map.get("preparationName").toString()) {
                case "202":
                    map.put("preparationName", "何菲");
                    break;
                case "201":
                    map.put("preparationName", "陈莉");
                    break;
                case "200":
                    map.put("preparationName", "陈莉");
                    break;
            }
        }
        if (null != map.get("managerName") && !"".equals(map.get("managerName"))) {
            String managerName = map.get("managerName").toString();
            String substring = null;
            String name = null;
            int indexOf = managerName.indexOf("（");
            int index = managerName.indexOf("(");
            if (indexOf != -1 || index != -1) {
                if (index != -1) {
                    if (managerName.indexOf(")") != -1) {
                        substring = managerName.substring(managerName.indexOf("(") + 1, managerName.indexOf(")"));
                        name = managerName.substring(0, managerName.indexOf("("));
                    } else {
                        substring = managerName.substring(managerName.indexOf("(") + 1, managerName.indexOf("）"));
                        name = managerName.substring(0, managerName.indexOf("("));
                    }
                } else if (indexOf != -1) {
                    if (managerName.indexOf("）") != -1) {
                        substring = managerName.substring(managerName.indexOf("（") + 1, managerName.indexOf("）"));
                        name = managerName.substring(0, managerName.indexOf("（"));
                    } else {
                        substring = managerName.substring(managerName.indexOf("（") + 1, managerName.indexOf(")"));
                        name = managerName.substring(0, managerName.indexOf("（"));
                    }
                }
                map.put("name", substring);
                map.put("managerName", name);
            } else {
                map.put("name", managerName);
                map.put("managerName", managerName);
            }
        }
        if (null != map.get("treatmentName") && !"".equals(map.get("treatmentName"))) {
            switch (map.get("treatmentName").toString()) {
                case "专家门诊":
                    map.put("treatmentId", "1");
                    break;
                case "体检":
                    map.put("treatmentId", "2");
                    break;
                case "优先检查":
                    map.put("treatmentId", "3");
                    break;
                case "手术住院":
                    map.put("treatmentId", "4");
                    break;
                case "会诊服务":
                    map.put("treatmentId", "5");
                    break;
                case "特需门诊":
                    map.put("treatmentId", "6");
                    break;
            }
        }
        if ("1".equals(map.get("certificatesType").toString())) {
            if (null != map.get("certificates") && !"".equals(map.get("certificates"))) {
                String certificates = map.get("certificates").toString();
                if (certificates.length() == 15 || certificates.length() == 18) {
                    String birthday = "";
                    String sex = "";
                    if (certificates.length() == 15) {
                        String substring = certificates.substring(8, 10);
                        String s = certificates.substring(10, 12);
                        if (Integer.parseInt(substring) < 12) {
                            if (Integer.parseInt(s) < 31) {
                                birthday = "19" + certificates.substring(6, 8) + "-" + certificates.substring(8, 10) + "-" + certificates.substring(10, 12);
                                map.put("birthday", birthday);
                                sex = Integer.parseInt(certificates.substring(14, 15)) % 2 != 0 ? "1" : "2";
                                map.put("sex", sex);
                            }
                        }
                    } else if (certificates.length() == 18) {
                        String substring = certificates.substring(10, 12);
                        if (Integer.parseInt(substring) < 12) {
                            String s = certificates.substring(12, 14);
                            if (Integer.parseInt(s) < 31) {
                                sex = Integer.parseInt(certificates.substring(16, 17)) % 2 != 0 ? "1" : "2";
                                map.put("sex", sex);
                                birthday = certificates.substring(6, 10) + "-" + certificates.substring(10, 12) + "-" + certificates.substring(12, 14);
                                map.put("birthday", birthday);
                            }
                        }
                    }
                }
            }
        }

        if (null != map.get("phone") && !"".equals(map.get("phone"))) {
            String phone = map.get("phone").toString();
            int length = phone.length();
            if (length > 11) {
                map.put("phone","");
            }
        }
    }

    /**
     * 导入订单数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bulkImport(List<Map<String, Object>> mapList, HttpServletRequest request) {
        for (Map<String, Object> map : mapList) {
            transformationType(map);
            OfflineOder offlineOder = new OfflineOder();
            if (null != map.get("manager") && !"".equals(map.get("manager"))) {
                offlineOder.setManager(map.get("manager").toString());
            }
            if (null != map.get("managerPhone") && !"".equals(map.get("managerPhone"))) {
                offlineOder.setManagerPhone(map.get("managerPhone").toString());
            }
            if (null != map.get("customerId") && !"".equals(map.get("customerId"))) {
                offlineOder.setCustomerId(map.get("customerId").toString());
            }
            if (null != map.get("companyName") && !"".equals(map.get("companyName"))) {
                offlineOder.setCompanyName(map.get("companyName").toString());
            }
            if (null != map.get("managerName") && !"".equals(map.get("managerName"))) {
                offlineOder.setManagerName(map.get("managerName").toString());
            }
            offlineOder.setName(map.get("name").toString());
            if (null != map.get("certificatesType") && !"".equals(map.get("certificatesType"))) {
                offlineOder.setCertificatesType(map.get("certificatesType").toString());
            }
            if (null != map.get("certificates") && !"".equals(map.get("certificates"))) {
                offlineOder.setCertificates(map.get("certificates").toString());
            }
            if (null != map.get("birthday") && !"".equals(map.get("birthday"))) {
                offlineOder.setBirth(map.get("birthday").toString());
            }
            if (null != map.get("sex") && !"".equals(map.get("sex"))) {
                offlineOder.setSex(map.get("sex").toString());
            }
            if (null != map.get("phone") && !"".equals(map.get("phone"))) {
                offlineOder.setPhone(map.get("phone").toString());
            }
            if (null != map.get("sparePhone") && !"".equals(map.get("sparePhone"))) {
                offlineOder.setSparePhone(map.get("sparePhone").toString());
            }
            if (null != map.get("insuranceCard") && !"".equals(map.get("insuranceCard"))) {
                offlineOder.setInsuranceCard(map.get("insuranceCard").toString());
            }
            if (null != map.get("statement") && !"".equals(map.get("statement"))) {
                offlineOder.setStatement(map.get("statement").toString());
            }
            if (null != map.get("treatmentId") && !"".equals(map.get("treatmentId"))) {
                offlineOder.setTreatmentId(map.get("treatmentId").toString());
            }
            if (null != map.get("treatmentName") && !"".equals(map.get("treatmentName"))) {
                offlineOder.setTreatmentName(map.get("treatmentName").toString());
            }
            if (null != map.get("symptom") && !"".equals(map.get("symptom"))) {
                offlineOder.setSymptom(map.get("symptom").toString());
            }
            if (null != map.get("doctorSpecialty") && !"".equals(map.get("doctorSpecialty"))) {
                offlineOder.setDoctorSpecialty(map.get("doctorSpecialty").toString());
            }
            if (null != map.get("hospitalName") && !"".equals(map.get("hospitalName"))) {
                offlineOder.setHospitalName(map.get("hospitalName").toString());
            }
            if (null != map.get("doctorName") && !"".equals(map.get("doctorName"))) {
                offlineOder.setDoctorName(map.get("doctorName").toString());
            }
            if (null != map.get("visitTime") && !"".equals(map.get("visitTime"))) {
                offlineOder.setVisitTime(map.get("visitTime").toString());
            }
            if (null != map.get("examination") && !"".equals(map.get("examination"))) {
                offlineOder.setExamination(map.get("examination").toString());
            }
            if (null != map.get("mode") && !"".equals(map.get("mode"))) {
                offlineOder.setMode(map.get("mode").toString());
            }
            if (null != map.get("price") && !"".equals(map.get("price"))) {
                offlineOder.setPrice(map.get("price").toString());
            }
            if (null != map.get("status") && !"".equals(map.get("status"))) {
                offlineOder.setStatus(map.get("status").toString());
            }
            offlineOder.setPreparationName(map.get("preparationName").toString());
            offlineOder.setPreparationTime(map.get("preparationTime").toString());
            this.oderMapper.insert(offlineOder);
            OfflineOderUser oderUser = new OfflineOderUser();
            if (null != map.get("companyName") && !"".equals(map.get("companyName"))) {
                oderUser.setCompanyName(map.get("companyName").toString());
            }
            if (null != map.get("customerId") && !"".equals(map.get("customerId"))) {
                oderUser.setCustomerId(map.get("customerId").toString());
            }
            if (null != map.get("manager") && !"".equals(map.get("manager"))) {
                oderUser.setManager(map.get("manager").toString());
            }
            if (null != map.get("managerPhone") && !"".equals(map.get("managerPhone"))) {
                oderUser.setManagerPhone(map.get("managerPhone").toString());
            }
            if (null != map.get("managerName") && !"".equals(map.get("managerName"))) {
                oderUser.setManagerName(map.get("managerName").toString());
            }
            if (null != map.get("name") && !"".equals(map.get("name"))) {
                oderUser.setName(map.get("name").toString());
            }
            if (null != map.get("phone") && !"".equals(map.get("phone"))) {
                oderUser.setPhone(map.get("phone").toString());
            }
            if (null != map.get("sparePhone") && !"".equals(map.get("sparePhone"))) {
                oderUser.setSparePhone(map.get("sparePhone").toString());
            }
            if (null != map.get("certificatesType") && !"".equals(map.get("certificatesType"))) {
                oderUser.setCertificatesType(map.get("certificatesType").toString());
            }
            if (null != map.get("certificates") && !"".equals(map.get("certificates"))) {
                oderUser.setCertificates(map.get("certificates").toString());
            }
            if (null != map.get("statement") && !"".equals(map.get("statement"))) {
                oderUser.setStatement(map.get("statement").toString());
            }
            if (null != map.get("birthday") && !"".equals(map.get("birthday"))) {
                oderUser.setBirth(map.get("birthday").toString());
            }
            if (null != map.get("sex") && !"".equals(map.get("sex"))) {
                oderUser.setSex(map.get("sex").toString());
            }
            if (null != map.get("insuranceCard") && !"".equals(map.get("insuranceCard"))) {
                oderUser.setInsuranceCard(map.get("insuranceCard").toString());
            }
            QueryWrapper<OfflineOderUser> wrapper = new QueryWrapper<>();
            wrapper.eq("certificates", map.get("certificates"));
            OfflineOderUser selectOne = this.userMapper.selectOne(wrapper);
            if (selectOne == null) {
                this.userMapper.insert(oderUser);
            } else {
                this.userMapper.update(oderUser, wrapper);
            }
            if (null != (map.get("clinicId")) && !"".equals(map.get("clinicId"))) {
                OfflineClinicCard clinicCard = new OfflineClinicCard();
                if (null != map.get("clinicName") && !"".equals(map.get("clinicName"))) {
                    clinicCard.setHospitalName(map.get("clinicName").toString());
                }
                if (null != map.get("name") && !"".equals(map.get("name"))) {
                    clinicCard.setName(map.get("name").toString());
                }
                if (null != map.get("phone") && !"".equals(map.get("phone"))) {
                    clinicCard.setPhone(map.get("phone").toString());
                }
                if (null != map.get("certificatesType") && !"".equals(map.get("certificatesType"))) {
                    clinicCard.setCertificatesType(map.get("certificatesType").toString());
                }
                if (null != map.get("certificates") && !"".equals(map.get("certificates"))) {
                    clinicCard.setCertificatesNumber(map.get("certificates").toString());
                }
                if (null != map.get("clinicId") && !"".equals(map.get("clinicId"))) {
                    clinicCard.setClinicName(map.get("clinicId").toString());
                }
                if (null != map.get("birthday") && !"".equals(map.get("birthday"))) {
                    clinicCard.setBirth(map.get("birthday").toString());
                }
                if (null != map.get("sex") && !"".equals(map.get("sex"))) {
                    clinicCard.setSex(map.get("sex").toString());
                }
                QueryWrapper<OfflineClinicCard> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("hospital_name", map.get("clinicName"));
                queryWrapper.eq("certificates_number", map.get("certificates"));
                OfflineClinicCard one = this.clinicCard.selectOne(queryWrapper);
                if (one == null) {
                    this.clinicCard.insert(clinicCard);
                } else {
                    this.clinicCard.update(clinicCard, queryWrapper);
                }
            }
        }
        return CollectionUtil.isNotEmpty(mapList);
    }

    /**
     * 新增数据 模糊查询
     */
    private List<String> ifListOder(Map<String, Object> map, QueryWrapper<OfflineOder> wrapper) {
        List<String> list = new ArrayList<>();
        if (null != map.get("manager") && !"".equals(map.get("manager"))) {
            wrapper.like("manager", map.get("manager"));
            wrapper.select("DISTINCT manager");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getManager()) || null != x.getManager()) {
                    list.add(x.getManager());
                }
            });
        }
        if (null != map.get("managerPhone") && !"".equals(map.get("managerPhone"))) {
            wrapper.like("manager_phone", map.get("managerPhone"));
            wrapper.select("DISTINCT manager_phone");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getManagerPhone()) || null != x.getManagerPhone()) {
                    list.add(x.getManagerPhone());
                }
            });
        }
        if (null != map.get("managerName") && !"".equals(map.get("managerName"))) {
            wrapper.like("manager_name", map.get("managerName"));
            wrapper.select("DISTINCT manager_name");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getManagerName()) || null != x.getManagerName()) {
                    list.add(x.getManagerName());
                }
            });
        }
        if (null != map.get("symptom") && !"".equals(map.get("symptom"))) {
            wrapper.like("symptom", map.get("symptom"));
            wrapper.select("DISTINCT symptom");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getSymptom()) || null != x.getSymptom()) {
                    list.add(x.getSymptom());
                }
            });
        }
        if (null != map.get("hospitalName") && !"".equals(map.get("hospitalName"))) {
            wrapper.like("hospital_name", map.get("hospitalName"));
            wrapper.select("DISTINCT hospital_name");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getHospitalName()) || null != x.getHospitalName()) {
                    list.add(x.getHospitalName());
                }
            });
        }
        if (null != map.get("doctorSpecialty") && !"".equals(map.get("doctorSpecialty"))) {
            wrapper.like("doctor_specialty", map.get("doctorSpecialty"));
            wrapper.select("DISTINCT doctor_specialty");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getDoctorSpecialty()) || null != x.getDoctorSpecialty()) {
                    list.add(x.getDoctorSpecialty());
                }
            });
        }
        if (null != map.get("doctorName") && !"".equals(map.get("doctorName"))) {
            wrapper.like("doctor_name", map.get("doctorName"));
            wrapper.select("DISTINCT doctor_name");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getDoctorName()) || null != x.getDoctorName()) {
                    list.add(x.getDoctorName());
                }
            });
        }
        if (null != map.get("operation") && !"".equals(map.get("operation"))) {
            wrapper.like("operation", map.get("operation"));
            wrapper.select("DISTINCT operation");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getOperation()) || null != x.getOperation()) {
                    list.add(x.getOperation());
                }
            });
        }
        if (null != map.get("department") && !"".equals(map.get("department"))) {
            wrapper.like("department", map.get("department"));
            wrapper.select("DISTINCT department");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getDepartment()) || null != x.getDepartment()) {
                    list.add(x.getDepartment());
                }
            });
        }
        if (null != map.get("examination") && !"".equals(map.get("examination"))) {
            wrapper.like("examination", map.get("examination"));
            wrapper.select("DISTINCT examination");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getExamination()) || null != x.getExamination()) {
                    list.add(x.getExamination());
                }
            });
        }
        if (null != map.get("statement") && !"".equals(map.get("statement"))) {
            wrapper.like("statement", map.get("statement"));
            wrapper.select("DISTINCT statement");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getStatement()) || null != x.getStatement()) {
                    list.add(x.getStatement());
                }
            });
        }
        if (null != map.get("preparationMode") && !"".equals(map.get("preparationMode"))) {
            wrapper.like("preparation_mode", map.get("preparationMode"));
            wrapper.select("DISTINCT preparation_mode");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getPreparationMode()) || null != x.getPreparationMode()) {
                    list.add(x.getPreparationMode());
                }
            });
        }
        if (null != map.get("status") && !"".equals(map.get("status"))) {
            wrapper.like("status", map.get("status"));
            wrapper.select("DISTINCT status");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getStatus()) || null != x.getStatus()) {
                    list.add(x.getStatus());
                }
            });
        }
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            wrapper.like("name", map.get("name"));
            wrapper.select("DISTINCT name");
            List<OfflineOder> oderList = oderMapper.selectList(wrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getName()) || null != x.getName()) {
                    list.add(x.getName());
                }
            });
        }
        if (null != map.get("companyName") && !"".equals(map.get("companyName"))) {
            QueryWrapper<OfflineCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", map.get("companyName"));
            List<OfflineCompany> oderList = companyMapper.selectList(queryWrapper);
            oderList.forEach(x -> {
                if (!"".equals(x.getName()) || null != x.getName()) {
                    list.add(x.getName());
                }
            });
        }
        return list;
    }

    /**
     * 查询所有的就诊项目
     */
    @Override
    public List<OfflineTreatment> findTreatment() {
        QueryWrapper<OfflineTreatment> wrapper = new QueryWrapper<>();
        return this.treatmentMapper.selectList(wrapper.eq("is_delete", 0));
    }

    /**
     * 查询就诊项目下的项目详情
     */
    @Override
    public List<OfflineDetails> findDetails(Map<String, Object> map) {
        QueryWrapper<OfflineDetails> wrapper = new QueryWrapper<>();
        wrapper.eq("treatment_id", map.get("treatmentId"));
        wrapper.eq("is_delete", 0);
        return this.detailsMapper.selectList(wrapper);
    }

    /**
     * 查询一条订单语句
     */
    @Override
    public OfflineOder findOderOne(Map<String, Object> map) {
        OfflineOder offlineOder = this.oderMapper.selectById(map.get("id").toString());
        if (!"".equals(offlineOder.getHospitalName()) && null != offlineOder.getHospitalName()) {
            switch (offlineOder.getHospitalName()) {
                case "总院":
                    offlineOder.setHospitalName("复旦大学附属华山医院(总院)");
                    break;
                case "江苏路分部":
                    offlineOder.setHospitalName("复旦大学附属华山医院(江苏路分部)");
                    break;
                case "传染科大楼":
                    offlineOder.setHospitalName("复旦大学附属华山医院(传染科大楼)");
                    break;
                case "西院":
                    offlineOder.setHospitalName("复旦大学附属华山医院(西院)");
                    break;
            }
        }
        return offlineOder;
    }

    /**
     * 列表订单
     */
    @Override
    public HospitalOrderResult<OfflineOder> reportForm(Map<String, Object> map) {
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            map.put("startTime", map.get("startTime") + " 00:00:00");
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            map.put("endTime", map.get("endTime") + " 23:59:59");
        }
        List<OfflineOder> oderList = this.oderMapper.reportForm(map);
        int size = oderList.size();
        OfflineOder oder = this.oderMapper.findCount(map);
        List<OfflineOder> oders = this.oderMapper.findAll(map);
        for (OfflineOder offlineOder : oders) {
            OfflineOder oder1 = new OfflineOder();
            oder1.setVisitTime("合计");
            oder1.setIsCountHospital(offlineOder.getIsCountHospital());
            oder1.setIsSumPrice(offlineOder.getIsSumPrice());
            oder1.setHospitalName(offlineOder.getHospitalName());
            oderList.add(oder1);
        }
        OfflineOder oder1 = new OfflineOder();
        oder1.setVisitTime("总计");
        oder1.setHospitalName("");
        oder1.setCountHospital(oder.getCountHospital());
        oder1.setSumPrice(oder.getSumPrice());
        oderList.add(oder1);
        oderList.sort(Comparator.comparing(OfflineOder::getHospitalName, Comparator.nullsFirst(String::compareTo)).reversed());
        transformationHospital(oderList);
        Integer page = 0;
        Integer limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Integer.parseInt((map.get("page").toString()));
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Integer.parseInt((map.get("limit").toString()));
        }
        List<OfflineOder> list = oderList.stream().skip((page - 1 + 1) * limit).limit(limit).collect(Collectors.toList());
        return new HospitalOrderResult<>(list, size ,oderList.size());
    }

    /**
     * 导出
     */
    @Override
    public void oderOutExcel(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        List<OfflineOder> oderList = findOderList(map);
        transformationHospital(oderList);
        for (OfflineOder oder : oderList) {
            if ("合计".equals(oder.getVisitTime())) {
                oder.setPrice(oder.getIsSumPrice());
                oder.setHospitalName(oder.getIsCountHospital());
            }
            if ("总计".equals(oder.getVisitTime())) {
                oder.setPrice(oder.getSumPrice());
                oder.setHospitalName(oder.getCountHospital());
            }
            if (null != oder.getMode() && !"".equals(oder.getMode())) {
                switch (oder.getMode()) {
                    case "1":
                        oder.setMode("月结");
                        break;
                    case "2":
                        oder.setMode("预支付");
                        break;
                    case "3":
                        oder.setMode("现金");
                        break;
                }
            }
        }
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "客户订单报表.xlsx";
        ExcelUtils.Excel(request, response, oderList, OfflineOder.class, fileName);
    }

    /**
     * 订单报表的数据
     */
    private List<OfflineOder> findOderList(Map<String, Object> map) {
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            map.put("startTime", map.get("startTime") + " 00:00:00");
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            map.put("endTime", map.get("endTime") + " 23:59:59");
        }
        List<OfflineOder> oderList = this.oderMapper.reportForm(map);
        OfflineOder oder = this.oderMapper.findCount(map);
        List<OfflineOder> oders = this.oderMapper.findAll(map);
        for (OfflineOder offlineOder : oders) {
            OfflineOder oder1 = new OfflineOder();
            oder1.setVisitTime("合计");
            oder1.setIsCountHospital(offlineOder.getIsCountHospital());
            oder1.setIsSumPrice(offlineOder.getIsSumPrice());
            oder1.setHospitalName(offlineOder.getHospitalName());
            oderList.add(oder1);
        }
        OfflineOder oder1 = new OfflineOder();
        oder1.setVisitTime("总计");
        oder1.setHospitalName("");
        oder1.setCountHospital(oder.getCountHospital());
        oder1.setSumPrice(oder.getSumPrice());
        oderList.add(oder1);
        oderList.sort(Comparator.comparing(OfflineOder::getHospitalName, Comparator.nullsFirst(String::compareTo)).reversed());
        return oderList;
    }

    /**
     * 获取华山病人就诊卡列表
     */
    @Override
    public List<String> getPatIDList(Map<String, Object> map) {
        String url = properties.getPaymentUrl();
        Document xml = XmlUtil.createXml();
        xml.setXmlStandalone(true);
        Element element = xml.createElement("SERVICES");
        element.setAttribute("sname", "HS.Payment.GetPatIDList");
        xml.appendChild(element);
        Element params = xml.createElement("PARAMS");
        element.appendChild(params);
        Element param = xml.createElement("PARAM");
        param.setAttribute("pname", "IdentifyID");
        param.setAttribute("pval", properties.getDistinguishKey());
        Element param1 = xml.createElement("PARAM");
        param1.setAttribute("pname", "PatNo");
        param1.setAttribute("pval", map.get("phone").toString());
        Element param2 = xml.createElement("PARAM");
        param2.setAttribute("pname", "PatName");
        param2.setAttribute("pval", map.get("name").toString());
        params.appendChild(param);
        params.appendChild(param1);
        params.appendChild(param2);
        String s = XmlUtil.toStr(xml, "gbk", false);
        log.info("华山医院获取就诊卡输出前-->{}", s);
        SoapClient client = SoapClient.create(url)
                .setMethod("getPatIDList", "http://services.shxh/HSIMPaymentService")
                .setParam("getPatIDListXml", s);
        String send = client.send(true);
        Document document = XmlUtil.parseXml(send);
        String textContent = XmlUtil.transElements(document.getElementsByTagName("getPatIDListResult")).get(0).getTextContent();
        JSONObject jsonObject = JSONUtil.xmlToJson(textContent);
        log.info("华山医院获取就诊卡输出后-->{}", jsonObject);
        JSONObject o = (JSONObject) jsonObject.get("HS.Payment.GetPatIDList");
        Object o1 = o.get("PARAMS");
        List<String> body = new ArrayList<>();
        if (o1 instanceof JSONObject) {
        } else if (o1 instanceof JSONArray) {
            JSONArray jsonArray = o.getJSONArray("PARAMS");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                JSONArray array = json.getJSONArray("PARAM");
                for (int j = 0; j < array.size(); j++) {
                    JSONObject object = array.getJSONObject(j);
                    String pval = object.get("pval").toString();
                    String pname = object.get("pname").toString();
                    if ("PatID".equals(pname)) {
                        body.add(pval);
                    }
                }
            }
        }
        return body;
    }


    /**
     * 创建就诊卡
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPatId(Map<String, Object> map) {
        if ("".equals(map.get("clinicName"))) {
            Map<String, Object> card = createCard(map);
            if ("1".equals(card.get("ResultCode"))) {
                OfflineClinicCard offlineClinicCard = insertClinicCard(map);
                QueryWrapper<OfflineClinicCard> wrapper = new QueryWrapper<>();
                wrapper.eq("hospital_name", map.get("hospitalName"));
                wrapper.eq("certificates_number", map.get("certificatesNumber"));
                OfflineClinicCard clinicCard = this.clinicCard.selectOne(wrapper);
                if (clinicCard == null) {
                    offlineClinicCard.setClinicName(card.get("PatID").toString());
                    return this.clinicCard.insert(offlineClinicCard) == 1;
                } else {
                    return this.clinicCard.update(offlineClinicCard, wrapper) == 1;
                }
            }
            return false;
        } else {
            OfflineClinicCard offlineClinicCard = insertHSCard(map);
            QueryWrapper<OfflineClinicCard> wrapper = new QueryWrapper<>();
            wrapper.eq("hospital_name", map.get("hospitalName"));
            wrapper.eq("certificates_number", map.get("certificatesNumber"));
            OfflineClinicCard card = this.clinicCard.selectOne(wrapper);
            if (card == null) {
                return this.clinicCard.insert(offlineClinicCard) == 1;
            } else {
                return this.clinicCard.update(offlineClinicCard, wrapper) == 1;
            }
        }
    }

    /**
     * 华山新增就诊卡表
     */
    private OfflineClinicCard insertClinicCard(Map<String, Object> map) {
        OfflineClinicCard card = new OfflineClinicCard();
        card.setHospitalId(map.get("hospitalId").toString());
        card.setHospitalName(map.get("hospitalName").toString());
        card.setName(map.get("name").toString());
        card.setSex(map.get("sex").toString());
        card.setPhone(map.get("phone").toString());
        if (null != map.get("certificatesType") && !"".equals(map.get("certificatesType"))) {
            card.setCertificatesType(map.get("certificatesType").toString());
        }
        card.setCertificatesNumber(map.get("certificatesNumber").toString());
        card.setBirth(map.get("birth").toString());
        card.setCoordinate(map.get("coordinate").toString());
        if (null != map.get("clinicName") && !"".equals(map.get("clinicName"))) {
            card.setClinicName(map.get("clinicName").toString());
        }
        return card;
    }

    /**
     * 华东 新增就诊卡表
     */
    private OfflineClinicCard insertHSCard(Map<String, Object> map) {
        OfflineClinicCard card = new OfflineClinicCard();
        if (null != map.get("hospitalId") && !"".equals(map.get("hospitalId"))) {
            card.setHospitalId(map.get("hospitalId").toString());
        }
        card.setHospitalName(map.get("hospitalName").toString());
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            card.setName(map.get("name").toString());
        }
        if (null != map.get("sex") && !"".equals(map.get("sex"))) {
            card.setSex(map.get("sex").toString());
        }
        if (null != map.get("phone") && !"".equals(map.get("phone"))) {
            card.setPhone(map.get("phone").toString());
        }
        if (null != map.get("certificatesType") && !"".equals(map.get("certificatesType"))) {
            card.setCertificatesType(map.get("certificatesType").toString());
        }
        card.setCertificatesNumber(map.get("certificatesNumber").toString());
        if (null != map.get("birth") && !"".equals(map.get("birth"))) {
            card.setBirth(map.get("birth").toString());
        }
        if (null != map.get("coordinate") && !"".equals(map.get("coordinate"))) {
            card.setCoordinate(map.get("coordinate").toString());
        }
        card.setClinicName(map.get("clinicName").toString());
        card.setIsDelete("0");
        return card;
    }

    /**
     * 华山医院创建就诊卡接口
     */
    private Map<String, Object> createCard(Map<String, Object> map) {
        String url = properties.getPaymentUrl();
        Document xml = XmlUtil.createXml();
        xml.setXmlStandalone(true);
        Element element = xml.createElement("SERVICES");
        element.setAttribute("sname", "HS.Payment.CreatePatID");
        xml.appendChild(element);
        Element params = xml.createElement("PARAMS");
        element.appendChild(params);
        Element param = xml.createElement("PARAM");
        param.setAttribute("pname", "IdentifyID");
        param.setAttribute("pval", properties.getDistinguishKey());
        Element param1 = xml.createElement("PARAM");
        param1.setAttribute("pname", "PatName");
        param1.setAttribute("pval", map.get("name").toString());
        Element param2 = xml.createElement("PARAM");
        param2.setAttribute("pname", "PatGender");
        param2.setAttribute("pval", map.get("sex").toString());
        Element param3 = xml.createElement("PARAM");
        param3.setAttribute("pname", "PatBirthday");
        param3.setAttribute("pval", map.get("birth").toString());
        Element param4 = xml.createElement("PARAM");
        param4.setAttribute("pname", "PatAddress");
        param4.setAttribute("pval", map.get("coordinate").toString());
        Element param5 = xml.createElement("PARAM");
        param5.setAttribute("pname", "PatTel");
        param5.setAttribute("pval", map.get("phone").toString());
        Element param6 = xml.createElement("PARAM");
        param6.setAttribute("pname", "PatNo");
        param6.setAttribute("pval", map.get("certificatesNumber").toString());
        params.appendChild(param);
        params.appendChild(param1);
        params.appendChild(param2);
        params.appendChild(param3);
        params.appendChild(param4);
        params.appendChild(param5);
        params.appendChild(param6);
        String s = XmlUtil.toStr(xml, "gbk", false);
        log.info("华山医院创建就诊卡输出前-->{}", s);
        SoapClient client = SoapClient.create(url)
                .setMethod("createPatID", "http://services.shxh/HSIMPaymentService")
                .setParam("createPatIDXml", s);
        String send = client.send(true);
        Document document = XmlUtil.parseXml(send);
        String textContent = XmlUtil.transElements(document.getElementsByTagName("createPatIDResult")).get(0).getTextContent();
        JSONObject jsonObject = JSONUtil.xmlToJson(textContent);
        log.info("华山医院创建就诊卡输出后->{}", jsonObject);
        JSONObject o = (JSONObject) jsonObject.get("HS.Payment.CreatePatID");
        Object params1 = o.get("PARAMS");
        Map<String, Object> hashMap = new HashMap<>();
        if (params1 instanceof JSONObject) {
            JSONObject oJSONObject = o.getJSONObject("PARAMS");
            JSONArray array = oJSONObject.getJSONArray("PARAM");
            for (int i = 0; i < array.size(); i++) {
                JSONObject arrayJSONObject = array.getJSONObject(i);
                String pname = arrayJSONObject.get("pname").toString();
                String pval = arrayJSONObject.get("pval").toString();
                hashMap.put(pname, pval);
            }
        } else if (params1 instanceof JSONArray) {
            JSONArray array = o.getJSONArray("PARAMS");
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = array.getJSONObject(i);
                Object o1 = json.get("PARAM");
                if (o1 instanceof JSONArray) {
                    JSONArray jsonArray = json.getJSONArray("PARAM");
                    for (int j = 0; j < jsonArray.size(); j++) {
                        JSONObject object = jsonArray.getJSONObject(j);
                        String pname = object.get("pname").toString();
                        String pval = object.get("pval").toString();
                        hashMap.put(pname, pval);
                    }
                } else if (o1 instanceof JSONObject) {
                    JSONObject jsonJSONObject = json.getJSONObject("PARAM");
                    String pname = jsonJSONObject.get("pname").toString();
                    String pval = jsonJSONObject.get("pval").toString();
                    hashMap.put(pname, pval);
                }
            }
        }
        return hashMap;
    }

    /**
     * 身份证绑定就诊卡
     */
    @Override
    public Map<String, Object> bindPatId(Map<String, Object> map) {
        String url = properties.getPaymentUrl();
        Document xml = XmlUtil.createXml();
        xml.setXmlStandalone(true);
        Element element = xml.createElement("SERVICES");
        element.setAttribute("sname", "HS.Payment.BindPatID");
        xml.appendChild(element);
        Element params = xml.createElement("PARAMS");
        element.appendChild(params);
        Element param = xml.createElement("PARAM");
        param.setAttribute("pname", "IdentifyID");
        param.setAttribute("pval", properties.getDistinguishKey());
        Element param1 = xml.createElement("PARAM");
        param1.setAttribute("pname", "PatName");
        param1.setAttribute("pval", map.get("patName").toString());
        Element param2 = xml.createElement("PARAM");
        param2.setAttribute("pname", "PatNo");
        param2.setAttribute("pval", map.get("patNo").toString());
        Element param3 = xml.createElement("PARAM");
        param3.setAttribute("pname", "PatID");
        param3.setAttribute("pval", map.get("patId").toString());
        params.appendChild(param);
        params.appendChild(param1);
        params.appendChild(param2);
        params.appendChild(param3);
        String s = XmlUtil.toStr(xml, "gbk", false);
        log.info("华山医院绑定就诊卡输出前-->{}", s);
        SoapClient client = SoapClient.create(url)
                .setMethod("bindPatID", "http://services.shxh/HSIMPaymentService")
                .setParam("bindPatIDXml", s);
        String send = client.send(true);
        Document document = XmlUtil.parseXml(send);
        String textContent = XmlUtil.transElements(document.getElementsByTagName("bindPatIDResult")).get(0).getTextContent();
        JSONObject jsonObject = JSONUtil.xmlToJson(textContent);
        log.info("华山医院绑定就诊卡输出后-->{}", jsonObject);
        JSONObject o = (JSONObject) jsonObject.get("HS.Payment.BindPatID");
        Object o1 = o.get("PARAMS");
        Map<String, Object> hashMap = new HashMap<>();
        if (o1 instanceof JSONObject) {
            JSONObject oJSONObject = o.getJSONObject("PARAMS");
            JSONArray array = oJSONObject.getJSONArray("PARAM");
            for (int i = 0; i < array.size(); i++) {
                JSONObject object = array.getJSONObject(i);
                String pname = object.get("pname").toString();
                String pval = object.get("pval").toString();
                hashMap.put(pname, pval);
            }
        }
        return hashMap;
    }

    /**
     * 新建
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertOder(Map<String, Object> map) {
        // TODO: 跨域依赖 - 等 sys 域迁入后 SecurityUtils.getLoginUser() 返回 SysUser
        // SysUser principal = SecurityUtils.getLoginUser();
        // String userName = principal.getTrueName();
        String userName = String.valueOf(map.getOrDefault("preparationName", ""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OfflineOder oder = creatOder(map);
        oder.setPreparationTime(dateFormat.format(new Date()));
        oder.setPreparationName(userName);
        int insert = this.oderMapper.insert(oder);
        QueryWrapper<OfflineOderUser> wrapper = new QueryWrapper<>();
        wrapper.eq("certificates", oder.getCertificates());
        OfflineOderUser oderUser = this.userMapper.selectOne(wrapper);
        if (null == oderUser) {
            OfflineOderUser user = createOfflineOderUser(oder);
            this.userMapper.insert(user);
        } else {
            OfflineOderUser user = createOfflineOderUser(oder);
            this.userMapper.update(user, wrapper);
        }
        if (insert == 1) {
            OfflineOder offlineOder = this.oderMapper.selectOder();
            return offlineOder.getId();
        }
        return null;
    }

    /**
     * 新增 订单用户数据
     */
    private OfflineOderUser createOfflineOderUser(OfflineOder oder) {
        OfflineOderUser offlineOderUser = new OfflineOderUser();
        if (null != oder.getCompanyName()) {
            offlineOderUser.setCompanyName(oder.getCompanyName());
        }
        if (null != oder.getCustomerId()) {
            offlineOderUser.setCustomerId(oder.getCustomerId());
        }
        if (null != oder.getManager()) {
            offlineOderUser.setManager(oder.getManager());
        }
        if (null != oder.getManagerPhone()) {
            offlineOderUser.setManagerPhone(oder.getManagerPhone());
        }
        if (null != oder.getManagerName()) {
            offlineOderUser.setManagerName(oder.getManagerName());
        }
        if (null != oder.getName()) {
            offlineOderUser.setName(oder.getName());
        }
        if (null != oder.getPhone()) {
            offlineOderUser.setPhone(oder.getPhone());
        }
        if (null != oder.getSparePhone()) {
            offlineOderUser.setSparePhone(oder.getSparePhone());
        }
        if (null != oder.getCertificatesType()) {
            offlineOderUser.setCertificatesType(oder.getCertificatesType());
        }
        if (null != oder.getCertificates()) {
            offlineOderUser.setCertificates(oder.getCertificates());
        }
        if (null != oder.getBirth()) {
            offlineOderUser.setBirth(oder.getBirth());
        }
        if (null != oder.getSex()) {
            offlineOderUser.setSex(oder.getSex());
        }
        if (null != oder.getInsuranceCard()) {
            offlineOderUser.setInsuranceCard(oder.getInsuranceCard());
        }
        if (null != oder.getStatement()) {
            offlineOderUser.setStatement(oder.getStatement());
        }
        return offlineOderUser;
    }

    /**
     * 重置底纹
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updOnly(List<String> ids) {
        List<OfflineOder> oderList = new ArrayList<>();
        ids.forEach(i -> {
            OfflineOder oder = new OfflineOder();
            oder.setId(i);
            oder.setOnly("1");
            oderList.add(oder);
        });
        return this.updateBatchById(oderList);
    }

    /**
     * 查询所有企业名称
     */
    @Override
    public List<OfflineCompany> findCompany(Map<String, Object> map) {
        QueryWrapper<OfflineCompany> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            wrapper.like("name", map.get("name"));
        }
        return this.companyMapper.selectList(wrapper);
    }

    /**
     * 查询所有院区的
     */
    @Override
    public List<OfflineDistrict> findHospital() {
        List<OfflineDistrict> list = oderMapper.findHospitalList();
        for (OfflineDistrict district : list) {
            switch (district.getDistrictName()) {
                case "复旦大学附属华山医院(总院)":
                    district.setDistrictName("华山");
                    break;
                case "复旦大学附属华山医院(江苏路分部)":
                    district.setDistrictName("华山(江苏路)");
                    break;
                case "复旦大学附属华山医院(传染科大楼)":
                    district.setDistrictName("华山(传染大楼)");
                    break;
                case "复旦大学附属华山医院(西院)":
                    district.setDistrictName("华山(西)");
                    break;
                case "复旦大学附属华东医院":
                    district.setDistrictName("华东");
                    break;
            }
        }
        return list;
    }

    /**
     * 查询院区下科室名称
     */
    @Override
    public List<OfflineDepartment> findDepartment(Map<String, Object> map) {
        if (!"0".equals(map.get("districtId"))) {
            return oderMapper.findDepartment(map);
        } else {
            List<OfflineNode> nodeList = oderMapper.findNode();
            List<OfflineDepartment> departmentList = new ArrayList<>();
            nodeList.forEach(x -> {
                OfflineDepartment offlineDepartment = new OfflineDepartment();
                offlineDepartment.setCodeId(x.getNodeId());
                offlineDepartment.setCodeName(x.getNodeName());
                departmentList.add(offlineDepartment);
            });
            return departmentList;
        }
    }

    /**
     * 修改订单数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOder(Map<String, Object> map) {
        OfflineOder oder = update(map);
        UpdateWrapper<OfflineOder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", map.get("id"));
        updateWrapper.set("company_id", oder.getCompanyId());
        updateWrapper.set("company_name", oder.getCompanyName());
        updateWrapper.set("customer_id", oder.getCustomerId());
        updateWrapper.set("manager", oder.getManager());
        updateWrapper.set("manager_phone", oder.getManagerPhone());
        updateWrapper.set("manager_name", oder.getManagerName());
        updateWrapper.set("name", oder.getName());
        updateWrapper.set("phone", oder.getPhone());
        updateWrapper.set("spare_phone", oder.getSparePhone());
        updateWrapper.set("certificates_type", oder.getCertificatesType());
        updateWrapper.set("certificates", oder.getCertificates());
        updateWrapper.set("birth", oder.getBirth());
        updateWrapper.set("sex", oder.getSex());
        updateWrapper.set("type", oder.getType());
        updateWrapper.set("visit_time", oder.getVisitTime());
        updateWrapper.set("treatment_id", oder.getTreatmentId());
        updateWrapper.set("treatment_name", oder.getTreatmentName());
        updateWrapper.set("symptom", oder.getSymptom());
        updateWrapper.set("hospital_name", oder.getHospitalName());
        updateWrapper.set("doctor_specialty", oder.getDoctorSpecialty());
        updateWrapper.set("doctor_name", oder.getDoctorName());
        updateWrapper.set("price", oder.getPrice());
        updateWrapper.set("operation", oder.getOperation());
        updateWrapper.set("department", oder.getDepartment());
        updateWrapper.set("examination", oder.getExamination());
        updateWrapper.set("statement", oder.getStatement());
        updateWrapper.set("mode", oder.getMode());
        updateWrapper.set("operation_time", oder.getOperation());
        updateWrapper.set("status", oder.getStatus());
        updateWrapper.set("preparation_mode", oder.getPreparationMode());
        updateWrapper.set("insurance_card", oder.getInsuranceCard());
        QueryWrapper<OfflineOderUser> wrapper = new QueryWrapper<>();
        wrapper.eq("certificates", oder.getCertificates());
        OfflineOderUser user = createOfflineOderUser(oder);
        this.userMapper.update(user, wrapper);
        return this.oderMapper.update(oder, updateWrapper) == 1;
    }

    /**
     * 修改订单
     */
    private OfflineOder update(Map<String, Object> map) {
        OfflineOder oder = new OfflineOder();
        if ("" == map.get("companyId")) {
            oder.setCompanyId(null);
        } else {
            oder.setCompanyId(map.get("companyId").toString());
        }
        if (null != map.get("companyName")) {
            oder.setCompanyName(map.get("companyName").toString());
        }
        if ("" == map.get("customerId")) {
            oder.setCustomerId(null);
        } else {
            oder.setCustomerId(map.get("customerId").toString());
        }
        if (null != map.get("manager")) {
            oder.setManager(map.get("manager").toString());
        }
        if (null != map.get("managerPhone")) {
            oder.setManagerPhone(map.get("managerPhone").toString());
        }
        if (null != map.get("managerName")) {
            oder.setManagerName(map.get("managerName").toString());
        }
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            oder.setName(map.get("name").toString());
        }
        if (null != map.get("phone")) {
            oder.setPhone(map.get("phone").toString());
        }
        if (null != map.get("sparePhone") && !"".equals(map.get("sparePhone"))) {
            oder.setSparePhone(map.get("sparePhone").toString());
        }
        if (null != map.get("certificatesType") && !"".equals(map.get("certificatesType"))) {
            oder.setCertificatesType(map.get("certificatesType").toString());
        }
        if (null != map.get("certificates") && !"".equals(map.get("certificates"))) {
            oder.setCertificates(map.get("certificates").toString());
        }
        if (null != map.get("birth") && !"".equals(map.get("birth"))) {
            oder.setBirth(map.get("birth").toString());
        }
        if (null != map.get("sex") && !"".equals(map.get("sex"))) {
            oder.setSex(map.get("sex").toString());
        }
        if (null != map.get("type")) {
            oder.setType(map.get("type").toString());
        }
        if (null != map.get("visitTime")) {
            oder.setVisitTime(map.get("visitTime").toString());
        }
        if ("" != map.get("treatmentId")) {
            oder.setTreatmentId(map.get("treatmentId").toString());
        } else {
            oder.setTreatmentId(null);
        }
        if (null != map.get("treatmentName")) {
            oder.setTreatmentName(map.get("treatmentName").toString());
        }
        if (null != map.get("symptom")) {
            oder.setSymptom(map.get("symptom").toString());
        }
        if (null == map.get("hospitalName")) {
            oder.setHospitalName("");
        } else {
            oder.setHospitalName(map.get("hospitalName").toString());
        }
        if (null != map.get("doctorSpecialty")) {
            oder.setDoctorSpecialty(map.get("doctorSpecialty").toString());
        }
        if (null != map.get("doctorName")) {
            oder.setDoctorName(map.get("doctorName").toString());
        }
        if ("" == map.get("price")) {
            oder.setPrice(null);
        } else {
            oder.setPrice(map.get("price").toString());
        }
        if (null != map.get("operation")) {
            oder.setOperation(map.get("operation").toString());
        }
        if (null != map.get("department")) {
            oder.setDepartment(map.get("department").toString());
        }
        if (null != map.get("examination")) {
            oder.setExamination(map.get("examination").toString());
        }
        if (null != map.get("statement")) {
            oder.setStatement(map.get("statement").toString());
        }
        if (null != map.get("mode")) {
            oder.setMode(map.get("mode").toString());
        }
        if (null != map.get("operationTime")) {
            oder.setOperationTime(map.get("operationTime").toString());
        }
        oder.setStatus("待定");
        if (null != map.get("status")) {
            oder.setStatus(map.get("status").toString());
        }
        if (null != map.get("preparationMode")) {
            oder.setPreparationMode(map.get("preparationMode").toString());
        }
        if (null != map.get("insuranceCard")) {
            oder.setInsuranceCard(map.get("insuranceCard").toString());
        }
        return oder;
    }

    /**
     * 创建订单
     */
    private OfflineOder creatOder(Map<String, Object> map) {
        OfflineOder oder = new OfflineOder();
        if (null != map.get("companyId") && !"".equals(map.get("companyId"))) {
            oder.setCompanyId(map.get("companyId").toString());
        }
        if (null != map.get("companyName") && !"".equals(map.get("companyName"))) {
            oder.setCompanyName(map.get("companyName").toString());
        }
        if (null != map.get("customerId") && !"".equals(map.get("customerId"))) {
            oder.setCustomerId(map.get("customerId").toString());
        }
        if (null != map.get("manager") && !"".equals(map.get("manager"))) {
            oder.setManager(map.get("manager").toString());
        }
        if (null != map.get("managerPhone") && !"".equals(map.get("managerPhone"))) {
            oder.setManagerPhone(map.get("managerPhone").toString());
        }
        if (null != map.get("managerName") && !"".equals(map.get("managerName"))) {
            oder.setManagerName(map.get("managerName").toString());
        }
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            oder.setName(map.get("name").toString());
        }
        if (null != map.get("phone") && !"".equals(map.get("phone"))) {
            oder.setPhone(map.get("phone").toString());
        }
        if (null != map.get("sparePhone") && !"".equals(map.get("sparePhone"))) {
            oder.setSparePhone(map.get("sparePhone").toString());
        }
        if (null != map.get("certificatesType") && !"".equals(map.get("certificatesType"))) {
            oder.setCertificatesType(map.get("certificatesType").toString());
        }
        if (null != map.get("certificates") && !"".equals(map.get("certificates"))) {
            oder.setCertificates(map.get("certificates").toString());
        }
        if (null != map.get("birth") && !"".equals(map.get("birth"))) {
            oder.setBirth(map.get("birth").toString());
        }
        if (null != map.get("sex") && !"".equals(map.get("sex"))) {
            oder.setSex(map.get("sex").toString());
        }
        if (null != map.get("type") && !"".equals(map.get("type"))) {
            oder.setType(map.get("type").toString());
        }
        if (null != map.get("visitTime") && !"".equals(map.get("visitTime"))) {
            oder.setVisitTime(map.get("visitTime").toString());
        }
        if (null != map.get("treatmentId") && !"".equals(map.get("treatmentId"))) {
            oder.setTreatmentId(map.get("treatmentId").toString());
        }
        if (null != map.get("treatmentName") && !"".equals(map.get("treatmentName"))) {
            oder.setTreatmentName(map.get("treatmentName").toString());
        }
        if (null != map.get("symptom") && !"".equals(map.get("symptom"))) {
            oder.setSymptom(map.get("symptom").toString());
        }
        if (null != map.get("hospitalName") && !"".equals(map.get("hospitalName"))) {
            oder.setHospitalName(map.get("hospitalName").toString());
        }
        if (null != map.get("doctorSpecialty") && !"".equals(map.get("doctorSpecialty"))) {
            oder.setDoctorSpecialty(map.get("doctorSpecialty").toString());
        }
        if (null != map.get("doctorName") && !"".equals(map.get("doctorName"))) {
            oder.setDoctorName(map.get("doctorName").toString());
        }
        if (null != map.get("price") && !"".equals(map.get("price"))) {
            oder.setPrice(map.get("price").toString());
        }
        if (null != map.get("operation") && !"".equals(map.get("operation"))) {
            oder.setOperation(map.get("operation").toString());
        }
        if (null != map.get("department") && !"".equals(map.get("department"))) {
            oder.setDepartment(map.get("department").toString());
        }
        if (null != map.get("examination") && !"".equals(map.get("examination"))) {
            oder.setExamination(map.get("examination").toString());
        }
        if (null != map.get("statement") && !"".equals(map.get("statement"))) {
            oder.setStatement(map.get("statement").toString());
        }
        if (null != map.get("mode") && !"".equals(map.get("mode"))) {
            oder.setMode(map.get("mode").toString());
        }
        if (null != map.get("operationTime") && !"".equals(map.get("operationTime"))) {
            oder.setOperationTime(map.get("operationTime").toString());
        }
        oder.setStatus("待定 ");
        if (null != map.get("status") && !"".equals(map.get("status"))) {
            oder.setStatus(map.get("status").toString());
        }
        if (null != map.get("preparationMode") && !"".equals(map.get("preparationMode"))) {
            oder.setPreparationMode(map.get("preparationMode").toString());
        }
        if (null != map.get("insuranceCard") && !"".equals(map.get("insuranceCard"))) {
            oder.setInsuranceCard(map.get("insuranceCard").toString());
        }
        return oder;
    }

    /**
     * 判断字段
     */
    private QueryWrapper<OfflineOder> isField(Map<String, Object> map) {
        QueryWrapper<OfflineOder> queryWrapper = new QueryWrapper<>();
        isLike(queryWrapper, map);
        isOrder(queryWrapper, map);
        if (null != map.get("companyName") && !"".equals(map.get("companyName"))) {
            queryWrapper.like("company_name", String.valueOf(map.get("companyName")));
        }
        if (null != map.get("status") && !"".equals(map.get("status"))) {
            queryWrapper.like("status", String.valueOf(map.get("status")));
        }
        if (null != map.get("hospitalName") && !"".equals(map.get("hospitalName"))) {
            queryWrapper.like("hospital_name", String.valueOf(map.get("hospitalName")));
        }
        if (null != map.get("treatmentName") && !"".equals(map.get("treatmentName"))) {
            queryWrapper.eq("treatment_name", String.valueOf(map.get("treatmentName")));
        }
        if (null != map.get("preparationName") && !"".equals(map.get("preparationName"))) {
            queryWrapper.eq("preparation_name", String.valueOf(map.get("preparationName")));
        }
        if (null != map.get("doctorSpecialty") && !"".equals(map.get("doctorSpecialty"))) {
            queryWrapper.like("doctor_specialty", String.valueOf(map.get("doctorSpecialty")));
        }
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            queryWrapper.gt("visit_time", map.get("startTime") + " 00:00:00");
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            queryWrapper.lt("visit_time", map.get("endTime") + " 23:59:59");
        }
        queryWrapper.eq("is_delete", 0);
        return queryWrapper;
    }

    /**
     * 排序
     */
    private void isOrder(QueryWrapper<OfflineOder> queryWrapper, Map<String, Object> map) {
        if (null == map.get("isStatus") && null == map.get("visitTime") && null == map.get("hospital")) {
            queryWrapper.orderByDesc("preparation_time");
        } else if ("".equals(map.get("isStatus")) && "".equals(map.get("visitTime")) && "".equals(map.get("hospital"))) {
            queryWrapper.orderByDesc("create_time");
        } else {
            if ("1".equals(map.get("isStatus"))) {
                queryWrapper.orderByAsc("status");
            }
            if ("2".equals(map.get("isStatus"))) {
                queryWrapper.orderByDesc("status");
            }
            if ("1".equals(map.get("visitTime"))) {
                queryWrapper.orderByAsc("visit_time");
            }
            if ("2".equals(map.get("visitTime"))) {
                queryWrapper.orderByDesc("visit_time");
            }
            if ("1".equals(map.get("hospital"))) {
                queryWrapper.orderByAsc("hospital_name");
            }
            if ("2".equals(map.get("hospital"))) {
                queryWrapper.orderByDesc("hospital_name");
            }
        }
    }

    /**
     * 判断是否要模糊
     */
    private void isLike(QueryWrapper<OfflineOder> queryWrapper, Map<String, Object> map) {
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            queryWrapper.like("name", String.valueOf(map.get("name")));
        }
        if (null != map.get("phone") && !"".equals(map.get("phone"))) {
            queryWrapper.like("phone", String.valueOf(map.get("phone")));
        }
        if (null != map.get("managerName") && !"".equals(map.get("managerName"))) {
            queryWrapper.like("manager_name", String.valueOf(map.get("managerName")));
        }
        if (null != map.get("manager") && !"".equals(map.get("manager"))) {
            queryWrapper.like("manager", String.valueOf(map.get("manager")));
        }
    }
}