package com.yen.mdblog.controller;

import com.yen.mdblog.entity.Vo.CreateComment;
import com.yen.mdblog.service.CommentService;
import java.security.Principal;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/comment")
@Log4j2
public class CommentController {

  @Autowired CommentService commentService;

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String createComment(CreateComment request, Model model, Principal principal) {

    String userName = principal.getName();
    Long postId = request.getPostId();
    String commentContent = request.getCommentContent();
    Boolean createCommentSuccess = commentService.insertComment(userName, postId, commentContent);
    if (createCommentSuccess) {
      return "comment/comment_success";
    } else {
      return "create comment failed";
    }
  }
}
