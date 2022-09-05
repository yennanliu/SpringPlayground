package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: QueryTest
 * Author:   longzhonghua
 * Date:     2019/5/5 13:52
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class QueryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    /**
     * Description: 根據方法名查詢。
     */
    public void queryByPriceBetween() {
        Iterable<Product> list = productRepository.findByPriceBetween(7.00, 8.00);

        for (Product product : list) {
            System.out.println(product);
        }

    }



    @Test
    /**
     * Description: 單一詞查詢
     */
    public void termQuery() {
        // 建構查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
        // 查詢詞,只能查詢一個中文字,或是一個英文單字
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.termQuery("name", "富"));
        // 搜尋，取得結果
        Page<Product> products = productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        // 總條數
        for (Product product : products) {
            System.out.println(product);
        }
    }

    @Test
    /**
     * Description: 多參數termsQuery
     */
    public void termsQuery() {
        // 建構查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
        // 查詢詞,只能查詢一個中文字,或是一個英文單字
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.termsQuery("name", "富","帥"));
        // 搜尋，取得結果
        Page<Product> products = productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        // 總條數
        for (Product product : products) {
            System.out.println(product);
        }
    }


    @Test
    /**
     * Description: matchQuery分詞查詢，采用預設的分詞器。
     */
    public void matchQuery() {
        // 查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
        // 查詢詞
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.matchQuery("name", "紅士"));
        // 搜尋，取得結果
        Page<Product>  products= productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        for (Product product : products) {
            System.out.println(product);
        }
    }

    @Test
    /**
     * Description: 多字段查詢
     */
    public void multiMatchQuery() {
        // 建構查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilder  = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery("紅富士金帥","name","body"));
        // 搜尋，取得結果
        Page<Product> products = productRepository.search(nativeSearchQueryBuilder.build());
        // 總條數
        for (Product product : products) {
            System.out.println(product);
        }
    }


}