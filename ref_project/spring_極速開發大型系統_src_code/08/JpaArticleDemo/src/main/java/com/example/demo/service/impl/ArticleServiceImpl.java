package com.example.demo.service.impl;


import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 標注為服務類別
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    /**
     * Description: 重新定義service接口的實現，實現清單功能
     */
    @Override
    public List<Article> getArticleList() {
        return articleRepository.findAll();
    }

    /**
     * Description: 重新定義service接口的實現，實現根據id查詢物件功能。
     */
    @Override
    public Article findArticleById(long id) {
        return articleRepository.findById(id);
    }


}

