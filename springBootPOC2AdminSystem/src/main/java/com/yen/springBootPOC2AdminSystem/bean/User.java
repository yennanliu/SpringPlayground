package com.yen.springBootPOC2AdminSystem.bean;

// https://www.youtube.com/watch?v=O8WUR5aSt8U&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=44

import lombok.Data;

/**
 *  User Bean for login/logout management
 */

@Data
public class User {
    private String userName;
    private String password;
}
