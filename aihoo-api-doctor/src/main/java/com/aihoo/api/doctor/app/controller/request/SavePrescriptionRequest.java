package com.aihoo.api.doctor.app.controller.request;

import com.aihoo.domain.prescription.model.dto.PrescriptionConsultationFeeDTO;
import com.aihoo.domain.prescription.model.dto.PrescriptionDrugDTO;
import com.aihoo.domain.prescription.model.dto.PrescriptionInstructionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "开方请求")
public class SavePrescriptionRequest {

    @Schema(name = "id", description = "处方ID", example = "1")
    private String id;

    @Schema(name = "patientUserId", description = "患者用户ID", example = "1")
    @NotBlank(message = "患者用户ID不能为空")
    private String patientUserId;

    @Schema(name = "hosSickId", description = "就诊人ID", example = "1")
    @NotBlank(message = "就诊人ID不能为空")
    private String hosSickId;

    @Schema(name = "doctorUserId", description = "医生ID", example = "1")
    @NotBlank(message = "医生ID不能为空")
    private String doctorUserId;

    @Schema(name = "visitMdtNum", description = "订单编号", example = "V20260228161121005")
    @NotBlank(message = "订单编号不能为空")
    private String visitMdtNum;

    @Schema(name = "name", description = "姓名", example = "张三")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @Schema(name = "sex", description = "性别 0-女 1-男", example = "1")
    @NotBlank(message = "性别不能为空")
    private String sex;

    @Schema(name = "age", description = "年龄", example = "18")
    @NotBlank(message = "年龄不能为空")
    private String age;

    @Schema(name = "disease", description = "辨病", example = "失眠")
    @NotBlank(message = "辨病不能为空")
    private String disease;

    @Schema(name = "syndrome", description = "辩证", example = "风热,气虚")
    @NotBlank(message = "辩证不能为空")
    private String syndrome;

    @Schema(name = "drugstoreId", description = "药店ID", example = "1")
    @NotBlank(message = "药店ID不能为空")
    private String drugstoreId;

    @Schema(name = "medicineStatusCode", description = "药态 1:中药饮片-自煎 2:中药饮片-代煎 3:颗粒", example = "2")
    @NotBlank(message = "药态不能为空")
    private String medicineStatusCode;

    @Schema(name = "drugList", description = "药品", example = "[{'drugId': '1', 'name': '葛根', 'price': '2.15', 'number': '10'}]")
    @NotNull(message = "药品")
    private List<PrescriptionDrugDTO> drugList;

    @Schema(name = "instruction", description = "用法", example = "")
    private PrescriptionInstructionDTO instruction;

    @Schema(name = "consultationFee", description = "诊金", example = "")
    private PrescriptionConsultationFeeDTO consultationFee;



}
