package com.aihoo.domain.prescription.service.impl;

import com.aihoo.common.BizResultCode;
import com.aihoo.domain.consultation.model.entity.MdtOrder;
import com.aihoo.domain.consultation.service.MdtOrderService;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.hospital.model.entity.Drugstore;
import com.aihoo.domain.hospital.service.DrugstoreService;
import com.aihoo.domain.prescription.enums.MedicineStatusEnum;
import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrug;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionInstruction;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionMapper;
import com.aihoo.domain.prescription.model.vo.HosPrescriptionInnerVo;
import com.aihoo.domain.prescription.model.vo.PrescriptionDrugVo;
import com.aihoo.domain.prescription.service.HosPreDrugService;
import com.aihoo.domain.prescription.service.HosPrescriptionService;
import com.aihoo.domain.prescription.service.PrescriptionInstructionService;
import com.aihoo.exception.BizException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname HosPrescriptionServiceImpl
 * @Description hf
 * @Date 2020/9/24 10:11
 * @Created by ad
 */
@Service
@RequiredArgsConstructor
public class HosPrescriptionServiceImpl extends ServiceImpl<HosPrescriptionMapper, HosPrescription> implements HosPrescriptionService {

    private final DoctorUserService doctorUserService;
    private final PrescriptionInstructionService prescriptionInstructionService;
    private final HosPreDrugService hosPreDrugService;
    private final DrugstoreService drugstoreService;
    @Autowired
    @Lazy
    private MdtOrderService mdtOrderService;

    @Override
    public HosPrescriptionInnerVo getPrescriptionInnerVo(String id) {
        HosPrescription prescription = baseMapper.selectById(id);
        if (prescription == null) {
            throw new BizException(BizResultCode.DATA_NOT_FOUND);
        }
        HosPrescriptionInnerVo prescriptionInnerVo = new HosPrescriptionInnerVo();
        BeanUtils.copyProperties(prescription, prescriptionInnerVo);
        prescriptionInnerVo.setDiseaseSyndrome(prescription.getDisease() + "; " + prescription.getSyndrome());
        prescriptionInnerVo.setSex("1".equals(prescription.getSex()) ? "男" : "女");

        DoctorUser doctorUser = doctorUserService.getById(prescription.getDoctorUserId());
        prescriptionInnerVo.setDoctorName(doctorUser.getName());
        prescriptionInnerVo.setDepartName(doctorUser.getDepartName());

        HosPrescriptionInstruction prescriptionInstruction = prescriptionInstructionService.getOne(new QueryWrapper<HosPrescriptionInstruction>()
                .eq("hos_prescription_id", prescription.getId()));
        if (prescriptionInstruction != null) {
            prescriptionInnerVo.setAdvice(prescriptionInstruction.getAdvice());
            String desc = MedicineStatusEnum.fromCode(Integer.parseInt(prescription.getMedicineStatusCode())).getDesc();
            String usage = "1".equals(prescriptionInstruction.getUsage()) ? "内服" : "外用";
            prescriptionInnerVo.setMedicineStatusCode(usage + desc);
            String method = "共%s剂，每日%s剂，分%s次服用";
            prescriptionInnerVo.setMethod(String.format(method,
                    prescriptionInstruction.getDoseNumber(), prescriptionInstruction.getDose(), prescriptionInstruction.getTimes()));

        }

        List<HosPrescriptionDrug> drugList = hosPreDrugService.list(new QueryWrapper<HosPrescriptionDrug>()
                .eq("hos_prescription_id", prescription.getId()));
        List<PrescriptionDrugVo> prescriptionDrugVoList = drugList.stream().map(drug -> {
            PrescriptionDrugVo prescriptionDrugVo = new PrescriptionDrugVo();
            prescriptionDrugVo.setName(drug.getName());
//            prescriptionDrugVo.setMethod(drug.getMethod());
            prescriptionDrugVo.setNumber(drug.getNumber());
            return prescriptionDrugVo;
        }).toList();
        prescriptionInnerVo.setDrugVoList(prescriptionDrugVoList);

        MdtOrder mdtOrder = mdtOrderService.getOne(new QueryWrapper<MdtOrder>()
                .eq("pre_id", prescription.getId()));
        if (mdtOrder != null) {
            prescriptionInnerVo.setReceiveMsg(mdtOrder.getReceiveName() + " " + mdtOrder.getReceivePhone() + " " + mdtOrder.getReceiveAddress());
            prescriptionInnerVo.setHosSickRemark(mdtOrder.getHosSickRemark());
        }

        Drugstore drugstore = drugstoreService.getById(prescription.getDrugstoreId());
        if (drugstore != null) {
            prescriptionInnerVo.setDrugstoreName(drugstore.getName());
        }

        return prescriptionInnerVo;
    }
}
