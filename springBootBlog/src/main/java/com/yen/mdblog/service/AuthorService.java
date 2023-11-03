package com.yen.mdblog.service;

import com.yen.mdblog.entity.Po.Author;

import java.util.List;

public interface AuthorService {

    Author getById(Integer id);

    Author getByName(String name);

    List<Author> getAllAuthors();

    void saveAuthor(Author author);

    void updateAuthor(Author author);
}
