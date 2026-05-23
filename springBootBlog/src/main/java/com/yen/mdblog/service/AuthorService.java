package com.yen.mdblog.service;

import com.yen.mdblog.entity.Po.Author;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AuthorService {

  Author getById(Integer id) throws ExecutionException, InterruptedException;

  Author getByName(String name);

  List<Author> getAllAuthors() throws ExecutionException, InterruptedException;

  Boolean createAuthor(String name, String email);

  void updateAuthor(Author author);
}
