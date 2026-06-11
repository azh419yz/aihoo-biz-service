package com.aihoo.domain.payment.model.mapper;

import com.aihoo.domain.payment.model.vo.OfflineAppointment;
import com.aihoo.domain.payment.model.vo.OfflineCost;
import com.aihoo.domain.payment.model.vo.OfflineDepartment;
import com.aihoo.domain.payment.model.vo.OfflineNodeDoctor;
import com.aihoo.domain.payment.model.vo.OfflineTreatmentTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 线下预约
 **/
public interface OfflineAppointmentMapper extends BaseMapper<OfflineAppointment> {

    List<OfflineTreatmentTime> findAppointment(@Param("map") Map<String, Object> map);

    List<OfflineDepartment> thisDepartmentList();

    /**
     * 查询华山的预约挂号的数据
     */
    List<OfflineTreatmentTime> findStaff();

    /**
     * 查询华东的预约挂号的数据
     */
    List<OfflineNodeDoctor> findNodeDoctor();

    /**
     * 查询华山医院医生和科室
     */
    List<OfflineTreatmentTime> selectStaff(@Param("map") Map<String, Object> map);

    /**
     * 查询加个
     * @param groupId 挂号id
     * @return OfflineCost
     */
    OfflineCost findCost(@Param("groupId") String groupId);
}