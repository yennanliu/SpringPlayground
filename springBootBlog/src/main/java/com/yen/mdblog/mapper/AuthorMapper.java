package com.yen.mdblog.mapper;

import com.yen.mdblog.entity.Po.Author;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AuthorMapper {

    public Author getById(@Param("id") Integer id);

    public Author getByName(@Param("name") String name);

    public List<Author> getAllAuthors();

    public int getAuthorCount();

//    @Insert("INSERT INTO authors(`id`,`email`,`name`,`url`) values(#{id}, #{email},  #{name}, #{url})")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void insertAuthor(Author author);

    public void updateAuthor(Author author);
}
