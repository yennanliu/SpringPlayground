package com.yen.mdblog.mapper;

import com.yen.mdblog.entity.Dto.SearchRequest;
import com.yen.mdblog.entity.Po.Post;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {

    public Post getById(@Param("id") long id);

    public List<Post> getAllPosts();

    public int getPostCount();

    // TODO : fix author_id parse (getter maybe ?) so can remove hardcode here
    //@Insert("INSERT INTO posts(`id`,`title`,`content`,`synopsis`,`author_id`, `dateTime`) values(#{id}, #{title},  #{content}, #{synopsis}, #{author_id}, #{dateTime})")
//    @Insert("INSERT INTO posts(`id`,`title`,`content`,`synopsis`,`author_id`, `date_time`) values(#{id}, #{title},  #{content}, #{synopsis}, 1, #{dateTime})")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void insertPost(@Param("Post") Post post);

    public void updatePost(@Param("Post") Post post);

    List<Post> findById(@Param("authorId")  Integer authorId);

    List<Post> findByKeyword(@Param("searchRequest") SearchRequest searchRequest);
}
