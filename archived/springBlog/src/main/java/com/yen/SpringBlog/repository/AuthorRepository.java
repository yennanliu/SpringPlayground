package com.yen.SpringBlog.repository;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-2.html

import com.yen.SpringBlog.entities.Author;
import org.springframework.data.repository.CrudRepository;

/**
 *  The CrudRepository interface provides various methods for performing CRUD operations on an entity.
 *  On the other hand, the PagingAndSortingRepository is an extension of the CrudRepository
 *  that provides additional methods for retrieving entities using the paging and sorting abstraction.
 */
public interface AuthorRepository extends CrudRepository<Author, Long> {
}
