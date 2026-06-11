package com.aihoo.api.doctor.common.constant;

/**
 * @Classname DictTypeEnum
 * @Description hf
 * @Date 2020/9/18 14:21
 * @Created by ad
 */
public enum DictTypeEnum {
    BANNER("BRAND类型", "BRAND_TYPE"),
    HOS_LEVEL("医院等级", "HOS_LEVEL"),
    HOS_ATT("医院属性", "HOS_ATT"),
    DOCT_TITLE("医生职称", "DOCT_TITLE"),
    RYLB("人员类别", "RYLB"),
    POSITION("职务编码", "POSITION"),
    PAPERS("职务编码", "PAPERS"),
    WEEK("星期", "WEEK"),
    HOS_CATE("医院类型", "HOS_CATE"),
    DRUG_DOS("药品剂型编码", "DRUG_DOS"),
    PACK_UNIT("包装单位编码", "PACK_UNIT"),
    YYPC("默认用药频次编码", "YYPC"),
    ROUTE_ADMI("默认用药途径编码", "ROUTE_ADMI");
    private String typeName;
    private String type;

    DictTypeEnum(String typeName, String type) {
        this.typeName = typeName;
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
