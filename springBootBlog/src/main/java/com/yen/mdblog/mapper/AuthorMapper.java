package com.yen.mdblog.mapper;

import com.yen.mdblog.entities.Author;
import com.yen.mdblog.entities.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthorMapper {

    @Select("SELECT * FROM authors where id = #{id}")
    public Author getById(long id);

    @Insert("INSERT INTO authors(`id`,`email`,`name`,`url`) values(#{id}, #{email},  #{name}, #{url})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void insertAuthor(Author author);
}
