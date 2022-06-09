package com.yen.SwaggerDemo.bean;

// https://www.gushiciku.cn/pl/pft6/zh-tw
// https://github.com/niumoo/springboot/blob/master/springboot-web-swagger/src/main/java/net/codingme/boot/domain/User.java

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "User instance")
public class User {

    /**
     *  user id
     *
     * @Id : PK
     * @GeneratedValue : auto increase
     */
    @ApiModelProperty(value = "user id", required = true, example = "1000")
    private Integer id;

    /**
     *  user name
     */
    @ApiModelProperty(value = "user name", required = true)
    private String username;

    /**
     *  password
     */
    @ApiModelProperty(value = "user password", required = true)
    private String password;

    /**
     * age
     */
    @ApiModelProperty(value = "user age", example = "18")
    private Integer age;

    /**
     * birthday
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "user birthday")
    private Date birthday;

    /**
     * skill
     */
    @ApiModelProperty(value = "user skill")
    private String skills;

}
