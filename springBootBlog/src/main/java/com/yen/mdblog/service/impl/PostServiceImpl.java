package com.yen.mdblog.service.impl;

import com.yen.mdblog.entity.Dto.SearchRequest;
import com.yen.mdblog.entity.Po.Post;
import com.yen.mdblog.mapper.PostMapper;
import com.yen.mdblog.service.PostService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

  @Autowired PostMapper postMapper;

  @Override
  public List<Post> getPostsById(Integer authorId) {

    return postMapper.findById(authorId);
  }

  @Override
  public List<Post> getAllPost() {

    return postMapper.getAllPosts();
  }

  @Override
  public List<Post> getPostByKeyword(SearchRequest request) {

    return postMapper.findByKeyword(request);
  }

  @Override
  public int getTotalPost() {

    return postMapper.getPostCount();
  }

  @Override
  public void savePost(Post post) {

    postMapper.insertPost(post);
  }

  @Override
  public void updatePost(Post post) {

    // log.info(">>> updatePost : post = {}", post);
    postMapper.updatePost(post);
  }
}
