package com.yen.resourceServer.bean.bo;

// book p.3-51

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckPassWordBO {

    private String userName;
    private String passWord;
    private String salt;
    private String authorities;
}
