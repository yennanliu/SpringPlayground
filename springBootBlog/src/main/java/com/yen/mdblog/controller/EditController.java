package com.yen.mdblog.controller;

import com.yen.mdblog.entity.Post;
import com.yen.mdblog.entity.request.CreatePost;
import com.yen.mdblog.repository.PostRepository;
import com.yen.mdblog.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;

@Controller
@Log4j2
@RequestMapping("/edit")
public class EditController {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/")
    public String EditPost(Model model){
        model.addAttribute("CreatePost", new CreatePost());
        return "edit_post";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable long id, Model model) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isPresent()) {
            model.addAttribute("post", postOptional.get());
        } else {
            model.addAttribute("error", "no-post");
        }

        return "edit_post";
    }

    @PostMapping(value="/update")
    public String update(Post post) {

        log.info(">>> update post : {}", post);
        postService.updatePost(post);
        log.info(">>> update professor : return to professor/list page");

        // TODO : fix this
        return "redirect:/edit/update";
    }

}
