package com.aihoo.domain.visit.service.impl;


import com.aihoo.common.PageResult;
import com.aihoo.domain.visit.model.entity.HosSick;
import com.aihoo.domain.visit.model.excel.HoSickEntity;
import com.aihoo.domain.visit.model.mapper.HosSickMapper;
import com.aihoo.domain.visit.model.mapper.HosSickVoMapper;
import com.aihoo.domain.visit.model.mapper.RevisitOrderMapper;
import com.aihoo.domain.visit.model.mapper.VisitOrderMapper;
import com.aihoo.domain.visit.model.vo.HosSickVo;
import com.aihoo.domain.visit.service.HosSickService;
import com.aihoo.domain.consultation.model.entity.MdtOrder;
import com.aihoo.domain.consultation.model.mapper.MdtOrderMapper;
import com.aihoo.domain.patient.model.entity.PatientUser;
import com.aihoo.domain.patient.model.mapper.PatientUserMapper;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 就诊人信息表 服务实现类
 * </p>
 *
 * @author mcp
 * @since 2020-10-08
 */
@Service
public class HosSickServiceImpl extends ServiceImpl<HosSickMapper, HosSick> implements HosSickService {
    @Resource
    private HosSickMapper hosSickMapper;
    @Resource
    private HosSickVoMapper hosSickVoMapper;
    @Resource
    private PatientUserMapper patientUserMapper;
    @Resource
    private VisitOrderMapper visitOrderMapper;
    @Resource
    private RevisitOrderMapper revisitOrderMapper;
    @Resource
    private MdtOrderMapper mdtOrderMapper;

    @Override
    public int getCount(Map<String, Object> map) {
        int count = hosSickMapper.getCount(map);
        return count;
    }

    @Override
    public JSONArray hosSickList(Map<String, Object> map) {
        int limit = Integer.parseInt(map.get("limit").toString());
        int page = Integer.parseInt(map.get("page").toString());
        int startNum = (page - 1) * limit;
        JSONArray jsonArray = new JSONArray();
        map.put("startNum", startNum);
        map.put("limit", limit);
        List<HosSick> hosSicks = hosSickMapper.hosSickList(map);
        if(CollectionUtils.isEmpty(hosSicks)){
            return jsonArray;
        }
        for (HosSick hosSick : hosSicks) {
            PatientUser patientUser = patientUserMapper.selectOne(new QueryWrapper<PatientUser>().eq("id", hosSick.getPatientUserId()));
            if (null==patientUser){
                return jsonArray;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",hosSick.getId());
            jsonObject.put("mobile",hosSick.getMobile());
            jsonObject.put("createTime",hosSick.getCreateTime());
            jsonObject.put("name",hosSick.getName());
            jsonObject.put("idCard",hosSick.getIdCard());
            jsonObject.put("patientUser",patientUser.getMobile());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

	@Override
	public PageResult<HosSickVo> page(Map<String, Object> map) {

		    long page = 1;
	        long limit = 10;

	        if (null != map.get("page") && !"".equals(map.get("page"))) {
	            page = Long.parseLong(map.get("page").toString());
	        }

	        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
	            limit = Long.parseLong(map.get("limit").toString());
	        }

	        QueryWrapper<HosSickVo> wrapper = new QueryWrapper<>();

            if (null !=  map.get("name") && !"".equals( map.get("name"))) {
                wrapper.like("name", map.get("name"));
            }
            if (null !=  map.get("mobile") && !"".equals( map.get("mobile"))) {
                wrapper.eq("mobile", map.get("mobile"));
            }
            wrapper.orderByDesc("create_time");

	        IPage<HosSickVo> iPage = this.hosSickVoMapper.selectPage(new Page<>(page, limit), wrapper);

	        List<HosSickVo> hosSickVos = iPage.getRecords();

	        if(CollectionUtils.isEmpty(hosSickVos)){
	            return new PageResult<HosSickVo>();
	        }

	        hosSickVos.stream().map(v -> {
	        	PatientUser patientUser = patientUserMapper.selectOne(new QueryWrapper<PatientUser>().eq("id", v.getPatientUserId()));
	        	if (patientUser==null) {
	        		patientUser=new PatientUser();
				}
	        	v.setPatientUser(patientUser.getMobile());
	        	return v;
	        }).collect(Collectors.toList());

	        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
	}


    @Override
    public JSONObject hosSickDetails(String id) {
        JSONObject jsonObject = new JSONObject();
        HosSick hosSick = hosSickMapper.hosSickDetails(id);
        if (null==hosSick){
            return jsonObject;
        }
        PatientUser patientUser = patientUserMapper.selectOne(new QueryWrapper<PatientUser>().eq("id", hosSick.getPatientUserId()));
        if (null == patientUser) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "查不到该用户，请核实");
            return jsonObject;
        }
        Long hosVisit = visitOrderMapper.selectCount(new QueryWrapper<com.aihoo.domain.visit.model.entity.HosVisit>().eq("hos_sick_id",hosSick.getId()));
        Long hosRevisit = revisitOrderMapper.selectCount(new QueryWrapper<com.aihoo.domain.visit.model.entity.HosRevisit>().eq("hos_sick_id", hosSick.getId()));
        Long mdtOrder = mdtOrderMapper.selectCount(new QueryWrapper<MdtOrder>().eq("hos_sick_id", hosSick.getId()));

        Long sumNumber = hosRevisit+hosVisit+mdtOrder;

        jsonObject.put("id",hosSick.getId());
        jsonObject.put("name",hosSick.getName());
        jsonObject.put("mobile",hosSick.getMobile());
        jsonObject.put("idCard",hosSick.getIdCard());
        jsonObject.put("sex",hosSick.getSex());
        jsonObject.put("isDelete",hosSick.getIsDelete());
        jsonObject.put("createTime",hosSick.getCreateTime());
        jsonObject.put("age",hosSick.getAge());
        jsonObject.put("patientUser",patientUser.getMobile());
        jsonObject.put("sumNumber",String.valueOf(sumNumber));
        jsonObject.put("is_succ", 1);
        return jsonObject;
    }

    @Override
    public void hosSickBulkExport(String name, String mobile, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<HosSick> wrapper = new QueryWrapper<HosSick>();
        if (null != mobile && !"".equals(mobile)){
            wrapper.eq("mobile",mobile);
        }
        if (null != name && !"".equals(name)){
            wrapper.eq("mobile",name);
        }
        List<HosSick> hosSicks = this.hosSickMapper.selectList(wrapper);
        if (org.springframework.util.CollectionUtils.isEmpty(hosSicks)){
            throw new BizException("当前条件无数据");
        }
        List<HoSickEntity> list = new ArrayList<>();
        hosSicks.forEach(s->{
            if ("1".equals(s.getSex())){
                s.setSex("男");
            }else {
                s.setSex("女");
            }
            if ("0".equals(s.getIsDelete())){
                s.setIsDelete("否");
            }else {
                s.setIsDelete("是");
            }
            HoSickEntity entity = new HoSickEntity();
            PatientUser patientUser = patientUserMapper.selectOne(new QueryWrapper<PatientUser>().eq("id",s.getPatientUserId()));
            entity.setPatientUser(patientUser.getMobile());
            Long hosVisit = visitOrderMapper.selectCount(new QueryWrapper<com.aihoo.domain.visit.model.entity.HosVisit>().eq("hos_sick_id",s.getId()));
            Long hosRevisit = revisitOrderMapper.selectCount(new QueryWrapper<com.aihoo.domain.visit.model.entity.HosRevisit>().eq("hos_sick_id", s.getId()));
            Long mdtOrder = mdtOrderMapper.selectCount(new QueryWrapper<MdtOrder>().eq("hos_sick_id", s.getId()));
            Long sumNumber = hosRevisit+hosVisit+mdtOrder;
            entity.setSumNumber(String.valueOf(sumNumber));
            BeanUtils.copyProperties(s,entity);
            list.add(entity);
        });
        ExcelUtils.writeExcel(request,response,list,HoSickEntity.class,"就诊人管理表格.xlsx");
    }
}
