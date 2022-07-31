package com.yen.mdblog.service;

import com.yen.mdblog.entity.Author;

public interface AuthorService {

    Author getById(Long id);

    void saveAuthor(Author author);

}
