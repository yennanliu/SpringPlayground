package com.yen.mdblog.repository;

import com.yen.mdblog.entity.Po.Author;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

//public interface AuthorRepository extends CrudRepository<Author, Integer> {
//
//  //    @Modifying
//  //    @Query("SELECT * FROM authors")
//  //    List<Author> getaAllAuthors();
//}

@Component
public interface AuthorRepository extends R2dbcRepository<Author, Integer> {

}
