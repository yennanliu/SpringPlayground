package com.yen.mdblog.service;

import com.yen.mdblog.entity.Po.Author;

import java.util.List;

public interface AuthorService {

    Author getById(Integer id);

    Author getByName(String name);

    List<Author> getAllAuthors();

    Boolean createAuthor(String name, String email);

    void updateAuthor(Author author);
}
