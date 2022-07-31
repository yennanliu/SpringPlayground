package com.yen.mdblog.service;

import com.yen.mdblog.entity.Post;

public interface PostService {

    Post getById(Long id);

    void savePost(Post post);

}
