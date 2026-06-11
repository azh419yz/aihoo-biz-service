package com.aihoo.domain.prescription.service.impl;

import com.aihoo.common.BizResultCode;
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
import com.aihoo.domain.prescription.stub.DoctorUser;
import com.aihoo.domain.prescription.stub.DoctorUserService;
import com.aihoo.domain.prescription.stub.MdtOrder;
import com.aihoo.domain.prescription.stub.MdtOrderService;
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

    // TODO: 跨域依赖。doctor 域尚未迁移，使用本域 stub 类型。
    private final DoctorUserService doctorUserService;
    private final PrescriptionInstructionService prescriptionInstructionService;
    private final HosPreDrugService hosPreDrugService;
    private final DrugstoreService drugstoreService;
    // TODO: 跨域依赖。consultation 域尚未迁移，使用本域 stub 类型。
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
