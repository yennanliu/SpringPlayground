package com.yen.mdblog.controller;

import com.yen.mdblog.entity.Po.Post;
import com.yen.mdblog.entity.Po.User;
import com.yen.mdblog.entity.Vo.CreatePost;
import com.yen.mdblog.entity.Vo.LoginRequest;
import com.yen.mdblog.repository.PostRepository;
import com.yen.mdblog.service.PostService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
@RequestMapping("/edit")
public class EditController {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/pre_edit")
    public String prePost(Model model) {

        log.info(">>> prePost start ...");
        User user = new User();
        user.setUserName("admin"); // TODO: get it from session
        // check login account, pwd // TODO : validate its info from DB
        if (!StringUtils.isEmpty(user.getUserName()) || user.getUserName().length() > 0) {
            // add blogs for editing blogs at admin-age
            // TODO : fix below
            final int page = 0;
            final int size = 100;
            Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "DateTime"));
            System.out.println(">>> pageRequest = " + pageRequest);
            Page<Post> postsPage = postRepository.findAll(pageRequest);
            List<Post> posts = postsPage.toList();

            // TODO : fix this from hardcode (get request from spring security login session/cookie)
            LoginRequest request = new LoginRequest();
            request.setUserName("admin");

            model.addAttribute("posts", posts);
            model.addAttribute("LoginRequest", request);
        }
        return "login_success";
    }

    @GetMapping("/")
    public String EditPost(Model model) {
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
        //return "redirect:/edit/";
        return "redirect:/posts/all";
    }

}
