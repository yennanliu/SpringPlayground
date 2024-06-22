package com.yen.mdblog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yen.mdblog.constant.PageConst;
import com.yen.mdblog.entity.Po.Post;
import com.yen.mdblog.entity.Po.User;
import com.yen.mdblog.entity.Vo.CreatePost;
import com.yen.mdblog.entity.Vo.LoginRequest;
import com.yen.mdblog.mapper.PostMapper;
import com.yen.mdblog.repository.PostRepository;
import com.yen.mdblog.service.PostService;
import com.yen.mdblog.util.PostUtil;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
@RequestMapping("/posts/edit")
public class PostEditController {

  private final int PAGINATIONSIZE = 3;
  @Autowired PostService postService;
  @Autowired PostRepository postRepository;
  @Autowired PostMapper postMapper;

  @GetMapping("/pre_edit")
  public String prePost(
      @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
      @RequestParam(value = "pageSize", defaultValue = "0" + PAGINATIONSIZE) int pageSize,
      Model model,
      Principal principal) {

    log.info(">>> prePost start ...");
    User user = new User();
    PageInfo<Post> pageInfo = null;
    List<Post> postList = null;
    List<Post> posts = null;
    user.setUserName("admin"); // TODO: get it from session
    if (!StringUtils.isEmpty(user.getUserName()) || user.getUserName().length() > 0) {
      try {
        // add blogs for editing blogs at admin-age
        Pageable pageRequest =
            PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "DateTime"));
        PageHelper.startPage(pageNum, pageSize);
        Page<Post> postsPage = postRepository.findAll(pageRequest);
        posts = postsPage.toList();
        postList = postMapper.getAllPosts();
        PageHelper.startPage(PageConst.PAGE_NUM.getSize(), PageConst.PAGE_SIZE.getSize());
        pageInfo = new PageInfo<Post>(postList, PageConst.PAGE_SIZE.getSize());
        model.addAttribute("pageInfo", pageInfo);
      } finally {
        PageHelper.clearPage();
      }

      // TODO : fix this from hardcode (get request from spring security login session/cookie)
      LoginRequest request = new LoginRequest();
      request.setUserName("admin");
      model.addAttribute("posts", posts);
      model.addAttribute("LoginRequest", request);
    }

    model.addAttribute("user", principal.getName());
    return "post/post_pre_edit";
  }

  @GetMapping("/")
  public String EditPost(Model model, Principal principal) {
    model.addAttribute("CreatePost", new CreatePost());
    model.addAttribute("user", principal.getName());
    return "post/post_edit";
  }

  @GetMapping("/{id}")
  public String getPostById(@PathVariable long id, Model model, Principal principal) {
    Optional<Post> postOptional = postRepository.findById(id);

    if (postOptional.isPresent()) {
      model.addAttribute("post", postOptional.get());
    } else {
      model.addAttribute("error", "no-post");
    }

    model.addAttribute("user", principal.getName());
    return "post/post_edit";
  }

  @PostMapping(value = "/update")
  public String update(Post post, Principal principal, Model model) {

    post.setSynopsis(PostUtil.getSynopsis(post.getContent()));
    log.info(">>> update post : {}", post);
    postService.updatePost(post);
    log.info(">>> update professor : return to professor/list page");
    model.addAttribute("user", principal.getName());
    return "redirect:/posts/all";
  }
}
