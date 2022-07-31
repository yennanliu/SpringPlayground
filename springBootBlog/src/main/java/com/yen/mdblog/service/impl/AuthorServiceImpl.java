package com.yen.mdblog.service.impl;

import com.yen.mdblog.entity.Author;
import com.yen.mdblog.mapper.AuthorMapper;
import com.yen.mdblog.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorMapper authorMapper;

    @Override
    public Author getById(Long id) {
        return authorMapper.getById(id);
    }

    @Override
    public void saveAuthor(Author author) {
        authorMapper.insertAuthor(author);
    }

}
