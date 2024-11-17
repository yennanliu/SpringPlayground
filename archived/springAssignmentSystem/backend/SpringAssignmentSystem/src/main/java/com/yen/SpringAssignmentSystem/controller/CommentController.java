package com.yen.SpringAssignmentSystem.controller;

import java.util.Set;

import com.yen.SpringAssignmentSystem.domain.Comment;
import com.yen.SpringAssignmentSystem.domain.User;
import com.yen.SpringAssignmentSystem.dto.CommentDto;
import com.yen.SpringAssignmentSystem.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowCredentials = "true")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // TODO : fix below
    @PostMapping("")
    //public ResponseEntity<Comment> createComment (@RequestBody CommentDto commentDto, @AuthenticationPrincipal User user)
    public ResponseEntity<Comment> createComment (@RequestBody CommentDto commentDto, User user) {

        Comment comment = commentService.save(commentDto, user);
        return ResponseEntity.ok(comment);
    }

    // TODO : fix below
    @PutMapping("{commentId}")
    //public ResponseEntity<Comment> updateComment (@RequestBody CommentDto commentDto, @AuthenticationPrincipal User user) {
    public ResponseEntity<Comment> updateComment (@RequestBody CommentDto commentDto, User user) {

        Comment comment = commentService.save(commentDto, user);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("")
    public ResponseEntity<Set<Comment>> getCommentsByAssignment(@RequestParam Long assignmentId) {

        Set<Comment> comments = commentService.getCommentsByAssignmentId(assignmentId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<?> deleteComment (@PathVariable Long commentId) {

        try {
            commentService.delete(commentId);
            return ResponseEntity.ok("Comment deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
