package com.yen.mdblog.service.impl;

import com.yen.mdblog.mapper.PostMapper;
import com.yen.mdblog.service.PostService;
import com.yen.mdblog.entity.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostMapper postMapper;

    @Override
    public Post getById(Long id) {

        return postMapper.getById(id);
    }

    @Override
    public int getTotalPost() {
        return postMapper.getPostCount();
    }

    @Override
    public void savePost(Post post) {

        postMapper.insertPost(post);
    }

}
