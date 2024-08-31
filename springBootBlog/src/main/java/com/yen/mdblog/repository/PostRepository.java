package com.yen.mdblog.repository;

import com.yen.mdblog.entity.Po.Post;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

//@Component
//public interface PostRepository extends PagingAndSortingRepository<Post, Long> {}

@Component
public interface PostRepository extends R2dbcRepository<Post, Long> {}
