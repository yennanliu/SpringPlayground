package com.yen.resourceServer.service;

// book p.3-59

import com.yen.resourceServer.bean.bo.GetUserInfoBO;
import com.yen.resourceServer.bean.dto.GetUserInfoDTO;

public interface UserResourceService {

    GetUserInfoBO getUserInfo(GetUserInfoDTO getUserInfoDTO);
}
