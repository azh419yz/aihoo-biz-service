package com.aihoo.domain.im.model.mapper;

import com.aihoo.domain.im.model.entity.ImCustomerMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Mapper
public interface ImCustomerMsgMapper extends BaseMapper<ImCustomerMsg> {
    List<ImCustomerMsg> findStartTime(@Param("adminId") String adminId, @Param("patientId") String patientId);
    IPage<String> customerDetails(IPage page, @Param("map") Map<String, Object> map);
}
