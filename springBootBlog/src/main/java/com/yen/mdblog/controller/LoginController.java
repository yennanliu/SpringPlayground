package com.yen.mdblog.controller;

import com.yen.mdblog.entity.Post;
import com.yen.mdblog.entity.User;
import com.yen.mdblog.entity.request.LoginRequest;

import com.yen.mdblog.repository.PostRepository;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@Log4j2
public class LoginController {

    @Autowired
    PostRepository postRepository;

    @GetMapping(value = {"/login"})
    public String loginInit(Model model){
        // return LoginRequest instance as placeholder, so login html can use it and pass var to login post method (as below)
        model.addAttribute("LoginRequest", new LoginRequest());

        return "login";
    }

    @RequestMapping(value="/login", method= RequestMethod.POST)
    public String login(LoginRequest request, Model model){

        log.info(">>> login start ...");
        log.info(">>> loginRequest = " + request);

        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassWord(request.getPassWord());

        // check login account, pwd // TODO : validate its from info from DB
        if (StringUtils.hasLength(user.getUserName()) && "123".equals(user.getPassWord())){
            //return "redirect:/login";

            // add blogs for editing blogs at admin-age
            // TODO : fix below
            final int page = 0;
            final int size = 100;
            Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "DateTime"));
            Page<Post> postsPage = postRepository.findAll(pageRequest);
            List<Post> posts = postsPage.toList();

            model.addAttribute("posts", posts);
            model.addAttribute("LoginRequest", request);
            return "login_success";
        }else{
            log.info(">>> login failed, plz try again ...");
            return "redirect:/login";
        }
    }

    @GetMapping("/login_success")
    public String login_success(LoginRequest request){

        log.info(">>> username = " + request.getUserName());
        return "login_success";
    }

}
