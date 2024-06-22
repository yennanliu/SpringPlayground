package com.yen.mdblog.service.impl;

import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.mapper.AuthorMapper;
import com.yen.mdblog.service.AuthorService;
import java.util.Date;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AuthorServiceImpl implements AuthorService {

  @Autowired AuthorMapper authorMapper;

  @Override
  public Author getById(Integer id) {

    return authorMapper.getById(id);
  }

  @Override
  public Author getByName(String name) {

    return authorMapper.getByName(name);
  }

  @Override
  public List<Author> getAllAuthors() {

    return authorMapper.getAllAuthors();
  }

  @Override
  public Boolean createAuthor(String name, String email) {
    try {
      Author newAuthor = new Author();
      int id = authorMapper.getAuthorCount() + 1;
      newAuthor.setId(id);
      newAuthor.setName(name);
      newAuthor.setEmail(email);
      newAuthor.setCreateTime(new Date());
      newAuthor.setUpdateTime(new Date());
      log.info("save author = " + newAuthor);
      authorMapper.insertAuthor(newAuthor);
      log.info("create new user OK ...");
      return true;
    } catch (Exception e) {
      log.error("createAuthor failed : " + e);
      return false;
    }
  }

  @Override
  public void updateAuthor(Author author) {

    authorMapper.updateAuthor(author);
  }
}
