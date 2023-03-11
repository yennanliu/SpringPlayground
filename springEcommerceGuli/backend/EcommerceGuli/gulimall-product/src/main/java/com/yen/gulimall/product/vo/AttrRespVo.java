package com.yen.gulimall.product.vo;

// https://youtu.be/7dVkoxElUvU?t=338

import lombok.Data;

@Data
public class AttrRespVo extends AttrVo{

    private String catelogName;
    private String groupName;

    // https://youtu.be/kCjMunm_9Ig?t=59
    private Long[] catelogPath;

}
