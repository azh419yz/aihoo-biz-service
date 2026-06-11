package com.aihoo.domain.prescription.stub;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 占位 Service：原 DoctorUserService。
 * 该域尚未迁移，本地占位以让 HosPrescriptionServiceImpl 编译通过。
 * 待 doctor 域迁移完成后删除此文件并改用正式类型。
 */
public interface DoctorUserService extends IService<DoctorUser> {
}
