package com.yen.mdblog.repositories;

import com.yen.mdblog.entities.Post;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
}
