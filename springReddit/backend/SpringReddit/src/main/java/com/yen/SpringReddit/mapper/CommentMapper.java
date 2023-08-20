//package com.yen.SpringReddit.mapper;
//
//import com.yen.SpringReddit.dto.CommentsDto;
//import com.yen.SpringReddit.po.Comment;
//import com.yen.SpringReddit.po.Post;
//import com.yen.SpringReddit.po.User;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//
//// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/mapper/CommentMapper.java
//
//@Mapper(componentModel = "spring")
//public interface CommentMapper {
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "text", source = "commentsDto.text")
//    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
//    @Mapping(target = "post", source = "post")
//    @Mapping(target = "user", source = "user")
//    Comment map(CommentsDto commentsDto, Post post, User user);
//
//    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
//    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
//    CommentsDto mapToDto(Comment comment);
//}