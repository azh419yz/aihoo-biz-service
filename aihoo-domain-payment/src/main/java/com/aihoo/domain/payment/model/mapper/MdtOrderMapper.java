package com.aihoo.domain.payment.model.mapper;

import com.aihoo.domain.payment.model.entity.MdtOrder;
import com.aihoo.domain.payment.model.vo.MdtOrderTradeInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Classname MdtOrderMapper
 * @Description hf
 * @Date 2020/9/30 13:18
 * @Created by ad
 */
@Mapper
public interface MdtOrderMapper extends BaseMapper<MdtOrder> {
    /**
     * 查询会诊订单交易信息
     */
    List<MdtOrderTradeInfoVo> getInfos(@Param("map") Map<String, Object> map);

    /**
     * 查询总数
     */
    int getTotal(@Param("map") Map<String, Object> map);
}