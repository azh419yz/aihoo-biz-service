package com.aihoo.api.doctor.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.controller.vo.DoctorDirectoryVo;
import com.aihoo.api.doctor.app.model.DoctorDirectory;

import java.util.List;


public interface DoctorDirectoryService extends IService<DoctorDirectory> {

    /**
     * 查询医生通讯录
     *
     * @param sickName 患者名称 模糊搜索
     * @return 页面展示类
     */
    List<DoctorDirectoryVo> findDoctorDirectoryList(String sickName);
}
