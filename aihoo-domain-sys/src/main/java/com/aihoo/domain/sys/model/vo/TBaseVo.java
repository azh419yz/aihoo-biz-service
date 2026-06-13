package com.aihoo.domain.sys.model.vo;

import lombok.Data;
import java.io.Serializable;

@Data
public class TBaseVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String key;
    private String title;
    private String content;
    private String index;
}
