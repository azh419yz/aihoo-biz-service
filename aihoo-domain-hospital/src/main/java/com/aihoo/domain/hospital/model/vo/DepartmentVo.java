package com.aihoo.domain.hospital.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @Classname DepartmentVo
 * @Description hf
 * @Date 2020/9/17 11:37
 * @Created by ad
 */
@Data
public class DepartmentVo {
    private String code;
    private String title;
    private List<DepartmentVo> children;
}
