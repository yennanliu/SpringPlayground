package com.yen.mdblog.mapper;

import com.yen.mdblog.entity.Author;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthorMapper {

    @Select("SELECT * FROM authors where id = #{id}")
    public Author getById(long id);

    @Select("SELECT * FROM authors")
    public List<Author> getAllAuthors();

    @Insert("INSERT INTO authors(`id`,`email`,`name`,`url`) values(#{id}, #{email},  #{name}, #{url})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void insertAuthor(Author author);
}
