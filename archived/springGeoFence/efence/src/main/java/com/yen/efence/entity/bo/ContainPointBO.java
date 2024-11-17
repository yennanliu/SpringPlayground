package com.yen.efence.entity.bo;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/entity/bo/ContainPointBO.java

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContainPointBO implements Serializable {

    private Boolean result;
}