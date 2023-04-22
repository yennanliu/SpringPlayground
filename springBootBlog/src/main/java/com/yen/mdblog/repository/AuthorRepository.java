package com.yen.mdblog.repository;

import com.yen.mdblog.entity.Po.Author;

import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {

//    @Modifying
//    @Query("SELECT * FROM authors")
//    List<Author> getaAllAuthors();
}