package com.yen.mdblog.service.impl;

import com.yen.mdblog.entity.Po.Comment;
import com.yen.mdblog.mapper.CommentMapper;
import com.yen.mdblog.service.CommentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {

        return commentMapper.getCommentByPostId(postId);
    }

    @Override
    public Boolean insertComment(String userName, Long postId, String commentContent) {

        try{
            Comment comment = new Comment();
            comment.setUserName(userName);
            comment.setPostId(postId);
            comment.setCommentContent(commentContent);
            comment.setCreateTime(new Date());
            log.info("create comment OK");
            commentMapper.insertComment(comment);
            return true;
        }catch (Exception e){
            log.warn("create comment failed : " + e);
            return false;
        }
    }

}
