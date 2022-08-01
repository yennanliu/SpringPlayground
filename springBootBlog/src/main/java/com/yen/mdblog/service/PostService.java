package com.yen.mdblog.service;

import com.yen.mdblog.entity.Post;

public interface PostService {

    Post getById(Long id);

    int getTotalPost();

    void savePost(Post post);

}
