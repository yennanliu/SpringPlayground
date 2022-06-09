package com.yen.SwaggerDemo.controller;

// https://www.gushiciku.cn/pl/pft6/zh-tw
// https://github.com/niumoo/springboot/blob/master/springboot-web-swagger/src/main/java/net/codingme/boot/controller/UserController.java

import com.yen.SwaggerDemo.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.Response;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserController {

    @RestController(value = "/v1")
    @Api(value = "用户操作 API(v1)", tags = "用户操作接口")
    public class userController {

        @ApiOperation(value = "新增用户")
        @PostMapping(value = "/user")
        public Boolean create(User user, BindingResult bindingResult) throws Exception {
            if (bindingResult.hasErrors()) {
                String message = bindingResult.getFieldError().getDefaultMessage();
                log.info(message);
                //return ResponseUtill.error(ResponseEnum.ERROR.getCode(), message);
                return false;
            } else {
                // 新增用户信息 do something
                //return ResponseUtill.success("用户[" + user.getUsername() + "]信息已新增");
                return true;
            }
        }

        @ApiOperation(value = "删除用户")
        @DeleteMapping(value = "/user/{username}")
        public void delete(@PathVariable("username")
                               @ApiParam(value = "用户名", required = true) String name) throws Exception {
            // 删除用户信息 do something
            //return ResponseUtill.success("用户[" + name + "]信息已删除");
            log.info(">>> delete " + "用户[" + name + "]信息已删除");
        }

        @ApiOperation(value = "修改用户")
        @PutMapping(value = "/user")
        public Boolean update(User user, BindingResult bindingResult) throws Exception {
            if (bindingResult.hasErrors()) {
                String message = bindingResult.getFieldError().getDefaultMessage();
                log.info(message);
                //return ResponseUtill.error(ResponseEnum.ERROR.getCode(), message);
                return false;
            } else {
                String username = user.getUsername();
                //return ResponseUtill.success("用户[" + username + "]信息已修改");
                log.info(">>> 用户[" + username + "]信息已修改");
                return true;
            }
        }

        @ApiOperation(value = "获取单个用户信息", tags = "用户查询")
        @GetMapping(value = "/user/{username}")
        public void get(@PathVariable("username")
                            @ApiParam(value = "用户名", required = true) String username) throws Exception {
            // 查询用户信息 do something
            User user = new User();
            user.setId(10000);
            user.setUsername(username);
            user.setAge(99);
            user.setSkills("cnp");
            //return ResponseUtill.success(user);
        }

        @ApiOperation(value = "获取用户列表", tags = "用户查询")
        @GetMapping(value = "/user")
        public String selectAll() throws Exception {
            // 查询用户信息列表 do something
            User user = new User();
            user.setId(10000);
            user.setUsername("未读代码");
            user.setAge(99);
            user.setSkills("cnp");
            List<User> userList = new ArrayList<>();
            userList.add(user);
            //return ResponseUtill.success(userList);
            return userList.toString();
        }
    }
}
