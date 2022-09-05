package com.yen.mdblog.service;

import com.yen.mdblog.entity.Author;

import java.util.List;

public interface AuthorService {

    Author getById(Long id);

    List<Author> getAllAuthors();

    void saveAuthor(Author author);

}
