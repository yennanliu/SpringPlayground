package com.yen.mdblog.controller;

import com.yen.mdblog.entity.Vo.CreateAuthor;
import com.yen.mdblog.mapper.AuthorMapper;
import com.yen.mdblog.service.AuthorService;
import java.security.Principal;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/register")
@Log4j2
public class AuthorRegisterController {

  @Autowired AuthorMapper authorMapper;

  @Autowired AuthorService authorService;

  @GetMapping("/create")
  public String register(Model model, Principal principal) {

    model.addAttribute("CreateAuthor", new CreateAuthor());
    model.addAttribute("user", principal.getName());
    return "author/register";
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String createUser(CreateAuthor request) {

    String name = request.getName();
    String email = request.getEmail();
    Boolean createAuthorSuccess = authorService.createAuthor(name, email);
    if (createAuthorSuccess) {
      log.info("create new user OK ...");
      return "author/success_register";
    } else {
      return "create new user failed";
    }
  }
}
