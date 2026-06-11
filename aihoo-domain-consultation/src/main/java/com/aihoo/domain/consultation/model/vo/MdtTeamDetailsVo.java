package com.aihoo.domain.consultation.model.vo;

import com.aihoo.domain.consultation.model.entity.DMdtTag;
import com.aihoo.domain.consultation.model.entity.MdtTeam;
import lombok.Data;


import java.util.List;
import java.util.Map;

/**
 * @Classname MdtTeamDetailsVo
 * @Description hf
 * @Date 2020/12/25 10:49
 * @Created by ad
 */
@Data
public class MdtTeamDetailsVo extends MdtTeam {
    // mdt 团队关联的会诊医生集合
    private List<mdtDoctor> consultantDoctors;
    // mdt 个人会诊医生
    private mdtDoctor consultantDoctor;
    // mdt 个人会诊和团队会诊的助理医生
    private mdtDoctor assistantDoctor;
    // mdt 组合医生集合
    private List<Map<String, mdtDoctor>> combinationDoctors;
    // mdt 团队关联的标签集合
    private List<DMdtTag> dMdtTags;
    // mdt 会诊模式
    private String mdtType;

    @Data
    public static class mdtDoctor {
        //医生id
        private String doctorUserId;
        // 医生姓名
        private String name;
        // 会诊费用
        private String doctorType;
        // 是否领衔医生
        private String isMain;
        // 医生的会诊价格
        private String price;

    }
}