package com.aihoo.domain.sys.service.impl;

import com.aihoo.enums.MdtTypeEnum;
import com.aihoo.domain.sys.model.mapper.DiceMapper;
import com.aihoo.domain.sys.model.entity.Dict;
import com.aihoo.domain.sys.service.DiceService;
import com.aihoo.common.JsonResult;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname DiceServiceImpl
 * @Description hf
 * @Date 2020/9/15 21:23
 * @Created by ad
 */
@Service
public class DiceServiceImpl extends ServiceImpl<DiceMapper, Dict> implements DiceService {

    @Resource
    private DiceMapper diceMapper;

    @Override
    public JSONArray getDoctorType(String type) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.orderByAsc("`index`");
        List<Dict> dicts = diceMapper.selectList(wrapper);
        JSONArray jsonArray = new JSONArray();
        if (CollectionUtils.isEmpty(dicts)) {
            return jsonArray;
        }
        dicts.forEach(dict -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("typeName", dict.getTypeName());
            jsonObject.put("code", dict.getCode());
            jsonObject.put("name", dict.getName());
            jsonArray.add(jsonObject);

        });
        return jsonArray;
    }

    @Override
    public Dict getDoctorNameByCodeAndType(String type) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("type", "BRAND_TYPE");
        wrapper.eq("code", type);
        return this.diceMapper.selectList(wrapper).get(0);
    }

    @Override
    public String getDoctorNameByTypeAndCode(String type, String code) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.eq("code", code);
        Dict dict = this.diceMapper.selectOne(wrapper);
        return null == dict ? null : dict.getName();
    }

    @Override
    public String getDoctorNameByTypeAndName(String type, String name) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.eq("name", name);
        Dict dict = this.diceMapper.selectOne(wrapper);
        return null == dict ? null : dict.getCode();
    }

    @Override
    public List<Map<String, String>> getMdtTypeList() {
        LambdaQueryWrapper<Dict> lambda = new QueryWrapper<Dict>().lambda();
        lambda.eq(Dict::getType, MdtTypeEnum.DICTCODE.getCode());
        List<Dict> dicts = this.diceMapper.selectList(lambda);
        List<Map<String, String>> mapList = new ArrayList<>();
        dicts.forEach(dict -> {
            Map<String, String> map = new HashMap<>();
            map.put("code", dict.getCode());
            map.put("name", dict.getName());
            mapList.add(map);
        });
        return mapList;
    }

    @Override
    public JsonResult listByType(String type) {
        List<Map> maps = diceMapper.selectByType(type);
        return JsonResult.ok().putData(maps);
    }


}
