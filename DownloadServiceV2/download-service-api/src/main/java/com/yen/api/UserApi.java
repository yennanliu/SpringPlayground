package com.yen.api;

import com.yen.bean.User;
import org.springframework.web.bind.annotation.GetMapping;

public interface UserApi {

    @GetMapping("/user")
    public User getUser();

}
