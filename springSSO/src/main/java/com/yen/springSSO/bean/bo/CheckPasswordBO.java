package com.yen.springSSO.bean.bo;

// book p.3-30

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckPasswordBO {

    private String userName;
    private String passWord;
    private String salt; // encrypt key
    private String authorities; // user authorities
}
