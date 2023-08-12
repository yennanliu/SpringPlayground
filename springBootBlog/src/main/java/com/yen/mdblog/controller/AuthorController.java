package com.yen.mdblog.controller;

import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.repository.AuthorRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/author")
@Log4j2
public class AuthorController {

    @Autowired
    AuthorRepository authorRepository;

    @GetMapping("/all")
    String getAllAuthor(Model model, Principal principal){

        Iterable<Author> authorOptional = authorRepository.findAll();
        model.addAttribute("authors", authorOptional);
        model.addAttribute("user", principal.getName());
        return "authors";
    }

    @GetMapping("/{id}")
    public String getAuthorById(@PathVariable Long id, Model model, Principal principal) {

        Optional<com.yen.mdblog.entity.Po.Author> authorOptional = authorRepository.findById(id);
        if (authorOptional.isPresent()) {
            model.addAttribute("author", authorOptional.get());
        } else {
            model.addAttribute("error", "");
        }
        model.addAttribute("user", principal.getName());
        return "author";
    }

}
