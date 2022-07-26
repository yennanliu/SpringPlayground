package com.yen.springMybatisDemo1.mapper;

import com.yen.springMybatisDemo1.bean.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    public Comment select1(int id);
    public List<Comment> selectAll();
}
