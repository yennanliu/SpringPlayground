package com.yen.springBootPOC2AdminSystem.bean;

// https://www.youtube.com/watch?v=O8WUR5aSt8U&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=44
// https://www.youtube.com/watch?v=PpheT7laE_8&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=47

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  User Bean for login/logout management
 */

@AllArgsConstructor   // add all constructors WITH args
@NoArgsConstructor    // add all constructors WITHOUT args
@Data
public class User {
    private String userName;
    private String password;
}
