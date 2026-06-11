package com.aihoo.domain.im.model.mapper;

import com.aihoo.domain.im.model.entity.ImCustomerMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Classname ImCustomerMsgMapper
 * @Description hf
 * @Date 2020/11/11 13:09
 * @Created by ad
 */
public interface ImCustomerMsgMapper extends BaseMapper<ImCustomerMsg> {
    IPage<String> customerDetails(Page<String> page, @Param("map") Map<String, Object> map);

    List<ImCustomerMsg> findStartTime(@Param("adminId") String customerId, @Param("patientId") String patientId);
}
