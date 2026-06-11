package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.controller.vo.AreaVo;
import com.aihoo.api.doctor.app.mapper.AreaMapper;
import com.aihoo.api.doctor.app.model.Area;
import com.aihoo.api.doctor.app.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 地区表 服务实现类
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
@Service
@RequiredArgsConstructor
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
    private final AreaMapper areaMapper;

    @Override
    public String getAreaByCode(String code) {
        QueryWrapper<Area> wrapper = new QueryWrapper<>();
        wrapper.eq("area_code", code);
        Area area = this.areaMapper.selectList(wrapper).get(0);
        return area.getName();
    }

    @Override
    public List<AreaVo> l3List() {
        List<Area> areas = this.areaMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(areas)) {
            return List.of();
        }
        Map<String, List<Area>> areasMap = areas.stream().collect(Collectors.groupingBy(Area::getType));
        List<Area> firstLevel = null;
        List<Area> secondLevel = null;
        List<Area> threeLevel = null;
        if (CollectionUtils.isEmpty(areasMap)) {
            return List.of();
        }
        for (Map.Entry<String, List<Area>> entry : areasMap.entrySet()) {
            if ("PROVINCE".equals(entry.getKey())) {
                firstLevel = entry.getValue();
            }
            if ("CITY".equals(entry.getKey())) {
                secondLevel = entry.getValue();
            }
            if ("DISTRICT".equals(entry.getKey())) {
                threeLevel = entry.getValue();
            }
        }
        assert firstLevel != null;
        assert secondLevel != null;
        assert threeLevel != null;
        Map<String, List<Area>> secondLevelMap = secondLevel.stream().collect(Collectors.groupingBy(Area::getParentAreaCode));
        Map<String, List<Area>> threeLevelMap = threeLevel.stream().collect(Collectors.groupingBy(Area::getParentAreaCode));
        List<AreaVo> respArray = new ArrayList<>();
        firstLevel.forEach(area -> {
            AreaVo resJson = new AreaVo();
            resJson.setName(area.getName());
            resJson.setAreaCode(area.getAreaCode());
            List<Area> firstSecondRelation = secondLevelMap.get(area.getAreaCode());
            List<AreaVo> secondJsonArray = new ArrayList<>();
            if (!CollectionUtils.isEmpty(firstSecondRelation)) {
                firstSecondRelation.forEach(s -> {
                    AreaVo jsonSecond = new AreaVo();
                    jsonSecond.setName(s.getName());
                    jsonSecond.setAreaCode(s.getAreaCode());
                    List<Area> secondThreeRelation = threeLevelMap.get(s.getAreaCode());
                    List<AreaVo> threeJsonArray = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(secondThreeRelation)) {
                        secondThreeRelation.forEach(area1 -> {
                            AreaVo jsonObject = new AreaVo();
                            jsonObject.setName(area1.getName());
                            jsonObject.setAreaCode(area1.getAreaCode());
                            threeJsonArray.add(jsonObject);
                        });
                    }
                    jsonSecond.setChildren(threeJsonArray);
                    secondJsonArray.add(jsonSecond);
                });
            }
            resJson.setChildren(secondJsonArray);
            respArray.add(resJson);
        });
        return respArray;
    }

    @Override
    public List<AreaVo> l2List() {
        List<Area> areas = this.areaMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(areas)) {
            return List.of();
        }
        Map<String, List<Area>> areasMap = areas.stream().collect(Collectors.groupingBy(Area::getType));
        if (CollectionUtils.isEmpty(areasMap)) {
            return List.of();
        }
        List<Area> firstLevel = null;
        List<Area> secondLevel = null;
        for (Map.Entry<String, List<Area>> entry : areasMap.entrySet()) {
            if ("PROVINCE".equals(entry.getKey())) {
                firstLevel = entry.getValue();
            }
            if ("CITY".equals(entry.getKey())) {
                secondLevel = entry.getValue();
            }
        }
        assert firstLevel != null;
        assert secondLevel != null;
        Map<String, List<Area>> secondLevelMap = secondLevel.stream().collect(Collectors.groupingBy(Area::getParentAreaCode));
        List<AreaVo> respArray = new ArrayList<>();
        firstLevel.forEach(area -> {
            AreaVo resJson = new AreaVo();
            resJson.setName(area.getName());
            resJson.setAreaCode(area.getAreaCode());
            List<Area> firstSecondRelation = secondLevelMap.get(area.getAreaCode());
            List<AreaVo> secondJsonArray = new ArrayList<>();
            if (!CollectionUtils.isEmpty(firstSecondRelation)) {
                firstSecondRelation.forEach(s -> {
                    AreaVo jsonSecond = new AreaVo();
                    jsonSecond.setName(s.getName());
                    jsonSecond.setAreaCode(s.getAreaCode());
                    secondJsonArray.add(jsonSecond);
                });
            }
            resJson.setChildren(secondJsonArray);
            respArray.add(resJson);
        });
        return respArray;
    }

    @Override
    public List<Area> provincesList() {
        return areaMapper.selectList(new LambdaQueryWrapper<Area>().eq(Area::getType, "PROVINCE"));
    }

    @Override
    public List<Area> cityList(String parentAreaCode) {
        return areaMapper.selectList(new LambdaQueryWrapper<Area>()
                .eq(Area::getType, "CITY")
                .eq(Area::getParentAreaCode, parentAreaCode)
        );
    }

    @Override
    public List<Area> districtList(String parentAreaCode) {
        return areaMapper.selectList(new LambdaQueryWrapper<Area>()
                .eq(Area::getType, "DISTRICT")
                .eq(Area::getParentAreaCode, parentAreaCode)
        );
    }
}
