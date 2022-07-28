package com.yen.mdblog.repositories;

import com.yen.mdblog.entities.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}