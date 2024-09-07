package com.yen.mdblog.service.impl;

import com.yen.mdblog.entity.Po.Author;
//import com.yen.mdblog.mapper.AuthorMapper;
import com.yen.mdblog.repository.AuthorRepository;
import com.yen.mdblog.service.AuthorService;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class AuthorServiceImpl implements AuthorService {

  //@Autowired AuthorMapper authorMapper;

  @Autowired
  AuthorRepository authorRepository;

  @Override
  public Mono<Author> getById(Integer id) {

    //return authorMapper.getById(id);
    return authorRepository.findById(id);
  }

  @Override
  public Mono<Author> getByName(String name) {

    // TODO : fix below
    //return authorMapper.getByName(name);
    return authorRepository.findById(1);
  }

  @Override
  public Flux<Author> getAllAuthors() {

    //return authorMapper.getAllAuthors();
    return authorRepository.findAll();
  }

  @Override
  public Boolean createAuthor(String name, String email) {
    try {
      Author newAuthor = new Author();
      //int id = authorMapper.getAuthorCount() + 1;
      //authorRepository.findAll().toStream().collect(Collectors.toList()).size();

      // TODO : fix below
      newAuthor.setId(1);

      newAuthor.setName(name);
      newAuthor.setEmail(email);
      newAuthor.setCreateTime(new Date());
      newAuthor.setUpdateTime(new Date());
      log.info("save author = " + newAuthor);

      //authorMapper.insertAuthor(newAuthor);
      authorRepository.save(newAuthor);

      log.info("create new user OK ...");
      return true;
    } catch (Exception e) {
      log.error("createAuthor failed : " + e);
      return false;
    }
  }

  @Override
  public void updateAuthor(Author author) {

    // TODO : optimize below
    //authorMapper.updateAuthor(author);
    authorRepository.deleteById(author.getId());
    authorRepository.save(author);
  }
}
