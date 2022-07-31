package com.yen.mdblog.repository;

import com.yen.mdblog.entity.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}