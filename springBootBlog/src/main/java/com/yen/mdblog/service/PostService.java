package com.yen.mdblog.service;

import com.yen.mdblog.entity.Po.Post;

import java.util.List;

public interface PostService {

    List<Post> getPostsById(Integer id);

    List<Post> getAllPost();

    int getTotalPost();

    void savePost(Post post);

    void updatePost(Post post);

}
