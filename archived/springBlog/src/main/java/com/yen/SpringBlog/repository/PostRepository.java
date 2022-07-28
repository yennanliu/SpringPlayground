package com.yen.SpringBlog.repository;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-2.html

import com.yen.SpringBlog.entities.Post;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *  Since we don't need to implement paging or sorting for the Author entity,
 *  we can extend CrudRepository for AuthorRepository.
 *  On the other hand, since we will be retrieving posts using the paging and sorting abstraction,
 *  we need to extend PagingAndSortingRepository for PostRepository.
 */
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
}
