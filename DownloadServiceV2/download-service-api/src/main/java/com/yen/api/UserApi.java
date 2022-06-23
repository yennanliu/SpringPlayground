package com.yen.api;

import org.springframework.web.bind.annotation.GetMapping;

public interface UserApi {

    @GetMapping("/user")
    public String getUser();

}
