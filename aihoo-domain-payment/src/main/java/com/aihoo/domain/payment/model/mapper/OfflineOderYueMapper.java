package com.aihoo.domain.payment.model.mapper;

import com.aihoo.domain.payment.model.entity.OfflineOderYue;
import com.aihoo.domain.payment.model.vo.OfflineDistrict;
import com.aihoo.domain.payment.model.vo.OfflineNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预约后的订单列表
 **/
public interface OfflineOderYueMapper extends BaseMapper<OfflineOderYue> {
    /**
     * 新增
     */
    int insertOderYue(@Param("oderYue") OfflineOderYue offlineOderYue);

    /**
     * 查询华东医院科室id
     */
    OfflineNode findNode(@Param("nodeCode") String nodeCode);

    /**
     * 查询华东医院小科室信息
     */
    List<OfflineNode> selectNodeList();

    /**
     * 查询新增订单后的主键id
     */
    OfflineOderYue findOderYueId();

    /**
     * 根据医院名称获取医院地址
     */
    OfflineDistrict findDistrictName(@Param("districtName") String districtName);
}