package com.yen.mdblog.service.impl;

import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.mapper.AuthorMapper;
import com.yen.mdblog.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorMapper authorMapper;

    @Override
    public Author getById(Long id) {
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
    public void saveAuthor(Author author) {
        authorMapper.insertAuthor(author);
    }

    @Override
    public void updateAuthor(Author author) {
        authorMapper.updateAuthor(author);
    }

}
