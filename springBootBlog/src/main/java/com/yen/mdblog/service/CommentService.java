package com.yen.mdblog.service;

import com.yen.mdblog.entity.Po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getCommentsByPostId(Long postId);

    Boolean insertComment(String userName, Long postId, String commentContent);
}
