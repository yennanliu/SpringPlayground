package com.yen.mdblog.controller;

import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.entity.Vo.CreateAuthor;
import com.yen.mdblog.mapper.AuthorMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;;import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("/register")
@Log4j2
public class AuthorRegisterController {

    @Autowired
    AuthorMapper authorMapper;

    @GetMapping("/create")
    public String register(Model model, Principal principal){

        model.addAttribute("CreateAuthor", new CreateAuthor());
        model.addAttribute("user", principal.getName());
        return "author/register";
    }

    @RequestMapping(value="/create", method= RequestMethod.POST)
    public String createUser(CreateAuthor request){

        log.info("create new user start ...");
        try{
            Author author = new Author();
            String name = request.getName();
            String email = request.getEmail();
            int id = authorMapper.getAuthorCount() + 1;
            author.setId(id);
            author.setName(name);
            author.setEmail(email);
            author.setCreateTime(new Date());
            author.setUpdateTime(new Date());
            log.info("author = " + author.toString());
            authorMapper.insertAuthor(author);
            log.info("create new user OK ...");
            return "author/success_register";
        }catch (Exception e){
            String errorMsg = "create new user failed ..." + e;
            log.error(errorMsg);
            return errorMsg;
        }
    }

}
