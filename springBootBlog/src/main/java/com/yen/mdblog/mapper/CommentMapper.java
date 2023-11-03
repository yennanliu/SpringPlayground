package com.yen.mdblog.mapper;

import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.entity.Po.Comment;
import com.yen.mdblog.entity.Po.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    public List<Comment> getCommentByPostId(Long postId);

    public void insertComment(Comment comment);
}
