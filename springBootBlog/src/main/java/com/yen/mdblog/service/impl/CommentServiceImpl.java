package com.yen.mdblog.service.impl;

import com.yen.mdblog.entity.Po.Comment;
import com.yen.mdblog.mapper.CommentMapper;
import com.yen.mdblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {

        return commentMapper.getCommentByPostId(postId);
    }

    @Override
    public void insertComment(Comment comment) {

        commentMapper.insertComment(comment);
    }

}
