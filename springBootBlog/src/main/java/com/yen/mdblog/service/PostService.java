package com.yen.mdblog.service;

import com.yen.mdblog.entities.Post;

public interface PostService {

    Post getById(Long id);

    void savePost(Post post);

}
