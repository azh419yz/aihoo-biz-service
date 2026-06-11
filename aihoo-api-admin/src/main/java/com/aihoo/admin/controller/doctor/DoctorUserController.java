package com.aihoo.admin.controller.doctor;

import com.aihoo.common.BaseController;
import com.aihoo.common.PageResult;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorUserController extends BaseController {

    @Resource
    private DoctorUserService doctorUserService;

    @PostMapping("/list")
    public PageResult<DoctorUser> list(@RequestBody Map<String, Object> map) {
        try {
            return this.doctorUserService.list(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("医生分页查询出错");
        }
    }
}
