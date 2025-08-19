package com.yen.mdblog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yen.mdblog.constant.PageConst;
import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.entity.Po.User;
import com.yen.mdblog.entity.Vo.CreateAuthor;
import com.yen.mdblog.entity.Vo.LoginRequest;
import com.yen.mdblog.service.AuthorService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@Log4j2
@RequestMapping("/author/edit")
public class AuthorEditController {

  @Autowired AuthorService authorService;

  @GetMapping("/pre_edit")
  public String preEdit(Model model, Principal principal) {

    log.info(">>> (Author) prePost start ...");
    User user = new User();
    PageInfo<Author> pageInfo = null;
    //List<Author> authorList = null;
    Flux<Author> authorList = null;

    user.setUserName("admin"); // TODO: get it from session
    if (!StringUtils.isEmpty(user.getUserName()) || user.getUserName().length() > 0) {
      try {
        // add blogs for editing blogs at admin-age
        PageHelper.startPage(PageConst.PAGE_NUM.getSize(), PageConst.PAGE_SIZE.getSize());

        //authorList = authorService.getAllAuthors();
        authorList = authorService.getAllAuthors();

        pageInfo = new PageInfo<Author>(authorList.toStream().collect(Collectors.toList()), PageConst.PAGE_SIZE.getSize());
        model.addAttribute("pageInfo", pageInfo);
      } finally {
        PageHelper.clearPage();
      }

      // TODO : fix this from hardcode (get request from spring security login session/cookie)
      LoginRequest request = new LoginRequest();
      request.setUserName("admin");
      model.addAttribute("authors", authorList);
      model.addAttribute("LoginRequest", request);
      //model.addAttribute("user", principal.getName());
      model.addAttribute("user", "admin");
    }
    return "author/author_pre_edit";
  }

  @GetMapping("/")
  public String editAuthor(Model model, Principal principal) {

    model.addAttribute("CreateAuthor", new CreateAuthor());
    //model.addAttribute("user", principal.getName());
    model.addAttribute("user", "admin");
    return "author/author_pre_edit";
  }

  @GetMapping("/{id}")
  public String getAuthorById(@PathVariable Integer id, Model model, Principal principal) {

    //Author author = authorService.getById(id);
    Mono<Author> author = authorService.getById(id);

    model.addAttribute("author", author);
    return "author/author_edit";
  }

  @PostMapping(value = "/update")
  public String updateAuthor(Author author, Principal principal, Model model) {

    log.info(">>> update author : {}", author);
    authorService.updateAuthor(author);
    //model.addAttribute("user", principal.getName());
    model.addAttribute("user", "admin");
    return "redirect:/author/all";
  }
}
