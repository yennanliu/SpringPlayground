package com.yen.springBootPOC2AdminSystem.bean;

// https://www.youtube.com/watch?v=njvVPhCFH6o&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=66
// https://www.youtube.com/watch?v=pzL68_zvqK4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=67

/**
 *  User2 Bean (myBatis plus test)
 */

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user2") // default will find table in DB with same class name, but we can also declare it explicitly via `@TableName("user2")` annotation
@Data
public class User2 {

    // attr
    private Long id;
    private String name;
    private int age;
    private String email;
}
