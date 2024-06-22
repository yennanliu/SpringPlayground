package com.yen.mdblog.mapper;

import com.yen.mdblog.entity.Dto.SearchRequest;
import com.yen.mdblog.entity.Po.Post;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostMapper {

  Post getById(@Param("id") long id);

  List<Post> getAllPosts();

  int getPostCount();

  // TODO : fix author_id parse (getter maybe ?) so can remove hardcode here
  // @Insert("INSERT INTO posts(`id`,`title`,`content`,`synopsis`,`author_id`, `dateTime`)
  // values(#{id}, #{title},  #{content}, #{synopsis}, #{author_id}, #{dateTime})")
  //    @Insert("INSERT INTO posts(`id`,`title`,`content`,`synopsis`,`author_id`, `date_time`)
  // values(#{id}, #{title},  #{content}, #{synopsis}, 1, #{dateTime})")
  //    @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertPost(@Param("Post") Post post);

  void updatePost(@Param("Post") Post post);

  List<Post> findById(@Param("authorId") Integer authorId);

  List<Post> findByKeyword(@Param("searchRequest") SearchRequest searchRequest);
}
