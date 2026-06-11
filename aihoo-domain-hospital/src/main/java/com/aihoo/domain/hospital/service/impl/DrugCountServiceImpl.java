package com.aihoo.domain.hospital.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.entity.DrugCount;
import com.aihoo.domain.hospital.model.mapper.DrugCountMapper;
import com.aihoo.domain.hospital.service.DrugCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 药品对账表 服务实现类
 * </p>
 *
 * <p>当前状态：占位实现，方法签名已与旧代码对齐；具体业务逻辑待完整迁移。
 * 涉及 B 类工具类（ExcelUtils/DateUtil/StatusEnumUtil 等）。</p>
 *
 * @author lx
 * @since 2020-11-04
 */
@Service
public class DrugCountServiceImpl extends ServiceImpl<DrugCountMapper, DrugCount> implements DrugCountService {

    @Override
    public PageResult<DrugCount> page(Map<String, Object> map) {
        throw new UnsupportedOperationException("TODO: 迁移 DrugCountServiceImpl.page 完整实现");
    }

    @Override
    public void drugCountOutExecl(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("TODO: 迁移 DrugCountServiceImpl.drugCountOutExecl 完整实现");
    }
}
