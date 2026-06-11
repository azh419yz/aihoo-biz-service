package com.aihoo.domain.payment.model.mapper;

import com.aihoo.domain.payment.model.entity.OfflineHospital;
import com.aihoo.domain.payment.model.entity.OfflineOder;
import com.aihoo.domain.payment.model.vo.OfflineBooking;
import com.aihoo.domain.payment.model.vo.OfflineCost;
import com.aihoo.domain.payment.model.vo.OfflineDepartment;
import com.aihoo.domain.payment.model.vo.OfflineDistrict;
import com.aihoo.domain.payment.model.vo.OfflineNode;
import com.aihoo.domain.payment.model.vo.OfflineNodeDoctor;
import com.aihoo.domain.payment.model.vo.OfflineStaff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OfflineOderMapper extends BaseMapper<OfflineOder> {

    /**
     * 根据条件查询出所有的订单
     */
    List<OfflineOder> reportForm(Map<String, Object> map);

    /**
     * 查询出订单的总和
     */
    OfflineOder findCount(Map<String, Object> map);

    /**
     * 查询出订单中各个的总和
     */
    List<OfflineOder> findAll(Map<String, Object> map);

    /**
     * 查询医院下的科室
     */
    List<OfflineDepartment> findDepartment(Map<String, Object> map);

    /**
     * 查询院区的
     */
    List<OfflineDistrict> findHospitalList();

    /**
     * 查询 挂号费用的
     */
    List<OfflineCost> findCost();

    /**
     * 查询医院信息
     */
    List<OfflineHospital> selectHospitalCard();

    /**
     * 查询制单人
     */
    List<String> findPreparationNameList();

    /**
     *  获取华东医院的科室
     */
    List<OfflineNode> findNode();

    /**
     * 查询华山医院科室下的医生
     */
    List<OfflineStaff> findStaff(Map<String, Object> map);

    /**
     * 查询华东医院科室下的医生
     */
    List<OfflineNodeDoctor> findNodeDoctor(Map<String, Object> map);

    /**
     * 查询医院下的挂号类型
     */
    List<OfflineBooking> findBooking(Map<String, Object> map);

    /**
     *  查询医院和挂号类型下的科室
     */
    List<OfflineDepartment> queryDepart(Map<String, Object> map);

    /**
     * 查询华东医院下的科室
     */
    List<OfflineNode> queryNode(Map<String, Object> map);

    /**
     *  查询新增订单的时候  的id
     */
    OfflineOder selectOder();

    /**
     * 查询所属医院是否在数据库里
     */
    OfflineHospital findOneHospital(@Param("name") String hospitalName);
}