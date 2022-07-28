package com.yen.springMybatisDemo1.service;

import com.yen.springMybatisDemo1.bean.Comment;
import com.yen.springMybatisDemo1.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentMapper commentMapper;

    public Comment getOneComment(int id){

        return commentMapper.select1(id);
    }

    public List<Comment> getAllComment(){
        return commentMapper.selectAll();
    }

    public String selectCommentDate(){
        return commentMapper.selectCommentDate();
    }

}
