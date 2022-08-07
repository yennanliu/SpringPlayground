package com.yen.mdblog.repository;

import com.yen.mdblog.entity.Post;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
}
