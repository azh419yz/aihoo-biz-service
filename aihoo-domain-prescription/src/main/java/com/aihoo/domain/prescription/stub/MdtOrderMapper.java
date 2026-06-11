package com.aihoo.domain.prescription.stub;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 占位 Mapper：原 MdtOrderMapper（t_mdt_order）。
 * 来自 admin 的 mapper 扫描包。该域尚未迁移，本地占位以让 HosPreDrugServiceImpl 编译通过。
 * 待 consultation 域迁移完成后删除此文件并改用正式类型。
 */
public interface MdtOrderMapper extends BaseMapper<MdtOrder> {
}
