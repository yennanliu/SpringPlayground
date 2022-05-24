package com.yen.springBootPOC2AdminSystem.controller;

// https://www.youtube.com/watch?v=O8WUR5aSt8U&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=44
// https://www.youtube.com/watch?v=HcZCvC7jBlU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=70

/** Controller for landing page
 *
 *  - login page (login.html)
 *  - main page (main.html)
 */

import com.yen.springBootPOC2AdminSystem.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

// NOTE !!! don't forget @Controller annotation for a controller
@Slf4j
@Controller
public class IndexController {

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     *  Non - login page
     *
     *  request to endpoint = "/" or "/login"
     *  will be passed to login.html
     */
    @GetMapping(value = {"/", "/login"})
    public String loginPage(){
        return "login";
    }

    /**
     *  Already-login page
     */
    @PostMapping("/login")
    public String main(User user, HttpSession session, Model model){

        /**
         *  logic check if user login success
         *  if there is non-null username and non-null password -> login success
         *  and we save user instance in session as well
         */
        // if ( StringUtils.isEmpty(user.getUserName()) && StringUtils.hasLength(user.getPassword()) )
        if ( StringUtils.hasLength(user.getUserName()) && "123".equals(user.getPassword()) ){
            session.setAttribute("loginUser", user);
            //return "main";

            /**
             *  Re-direct : avoid "resend request" to a form.
             *      NOTE !!! if login success, redirect to main.html (instead of send a new request to main.html everytime)
             */
            return "redirect:/main.html";
        }else{
            // if login failed, back to login page
            model.addAttribute("msg", "account or pwd wrong");
            return "login";
        }
    }

    /**
     *  Main page
     *
     *  so, once login success, every request will be redirected to main.html (instead of login)
     */
    @GetMapping("/main.html")
    public String mainPage(HttpSession session, Model model){

        log.info(">>> current method : mainPage");

        /**
         *  check if already login,
         *  in formal dev, we should use Interceptor (攔截器), or filter (過濾器)
         *  however, we now hardcode here as easy development
         */

        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null){

            // show visit count via redis
            ValueOperations<String, String> operations =  redisTemplate.opsForValue();
            String c1 = operations.get("/main.html");
            String c2 = operations.get("/sql");

            model.addAttribute("mainCount", c1);
            model.addAttribute("sqlCount", c2);

            return "main";
        }else{
            // if login failed, back to login page
            model.addAttribute("msg", "not login, plz login");
            return "login";
        }
    }

}
