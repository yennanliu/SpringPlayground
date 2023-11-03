package com.yen.mdblog.controller;

import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.entity.Po.Comment;
import com.yen.mdblog.entity.Po.Post;
import com.yen.mdblog.entity.Vo.CreateComment;
import com.yen.mdblog.entity.Vo.CreatePost;
import com.yen.mdblog.service.CommentService;
import com.yen.mdblog.util.PostUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comment")
@Log4j2
public class CommentController {

    @Autowired
    CommentService commentService;

//    @GetMapping("/{postId}")
//    public String getComments(){
//
//    }

    @RequestMapping(value="/create", method= RequestMethod.POST)
    public String createComment(CreateComment request, Model model, Principal principal){

        log.info(">>> create comment start ...");
        Comment comment = new Comment();
//        comment.setAuthorId(request.getAuthorId());
        comment.setPostId(request.getPostId());
        comment.setCommentContent(request.getCommentContent());
        comment.setCreateTime(new Date()); //?

        System.out.println(">>> (CommentController) create new comment = " + comment.toString());
        log.info(">>>> create post end ...");
        commentService.insertComment(comment);
        model.addAttribute("user", principal.getName());
        return "comment_success";
    }

}
