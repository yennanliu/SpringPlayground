package com.yen.mdblog.controller;

import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.service.AuthorService;
import java.security.Principal;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/author")
@Log4j2
public class AuthorController {

  @Autowired AuthorService authorService;

  @GetMapping("/all")
  String getAllAuthor(Model model, Principal principal) {

    //List<Author> authors = authorService.getAllAuthors();
    Flux<Author> authors = authorService.getAllAuthors();

    model.addAttribute("authors", authors);
    //model.addAttribute("user", principal.getName());
    model.addAttribute("user", "admin");
    return "author/authors";
  }

  @GetMapping("/{id}")
  public String getAuthorById(@PathVariable Integer id, Model model, Principal principal) {

    //Author author = authorService.getById(id);
    Mono<Author> author = authorService.getById(id);

    model.addAttribute("author", author);
    //model.addAttribute("user", principal.getName());
    model.addAttribute("user", "admin");
    return "author/author";
  }
}
