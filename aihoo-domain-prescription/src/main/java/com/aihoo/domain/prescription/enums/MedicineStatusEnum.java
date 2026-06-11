package com.aihoo.domain.prescription.enums;

import lombok.Getter;

@Getter
public enum MedicineStatusEnum {


    SELF_BOIL(1, "中药饮片·自煎"),
    SERVICE_BOIL(2, "中药饮片·代煎"),
    GRANULE(3, "中药颗粒");

    private final int code;
    private final String desc;

    MedicineStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MedicineStatusEnum fromCode(int code) {
        for (MedicineStatusEnum s : values()) {
            if (s.code == code) return s;
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
