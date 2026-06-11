package com.aihoo.api.doctor.app.controller.vo;

import com.aihoo.api.doctor.app.model.HosPrescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/6 15:18
 */
@Data
@Schema(description = "问诊单 VO")
public class HosVisitVo {
    @Schema(name = "visitNo", description = "订单号", example = "1")
    private String visitNo;
    @Schema(name = "visitId", description = "订单id", example = "1")
    private Long visitId;
    @Schema(name = "content", description = "自述", example = "1")
    private String content;
    @Schema(name = "createTime", description = "创建时间")
    private String createTime;
    @Schema(name = "hosPrescriptions", description = "处方")
    List<HosPrescription> hosPrescriptions;
}
