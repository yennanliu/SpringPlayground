package com.yen.mdblog.repository;

import com.yen.mdblog.entity.Author;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {

//    @Modifying
//    @Query("SELECT * FROM authors")
//    List<Author> getaAllAuthors();
}