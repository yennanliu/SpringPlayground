package com.yen.mdblog.mapper;

import com.yen.mdblog.entity.Po.Comment;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

  List<Comment> getCommentByPostId(Long postId);

  void insertComment(Comment comment);
}
