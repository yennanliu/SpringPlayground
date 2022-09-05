package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

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
public class ByPageTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    /**
     * Description: 分頁查詢
     */
    public void termQuery() {
        // 分頁：
        int page = 0;
        int size = 1;//每頁文件數

        // 建構查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
        // 查詢詞,只能查詢一個中文字,或是一個英文單字
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.termQuery("name", "富"));
        // 搜尋，取得結果
        nativeSearchQueryBuilderQueryBuilder.withPageable(PageRequest.of(page, size));
        Page<Product> products = productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        // 總條數
          for (Product product : products) {
            System.out.println(product);
        }

    }


    @Test
    /**
     * Description: 分頁查詢+排序
     */
    public void searchByPageAndSort() {
        // 分頁：
        int page = 0;
        int size = 5;//每頁文件數

        // 建構查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
        // 查詢詞,只能查詢一個中文字,或是一個英文單字
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.termQuery("name", "富"));
        // 搜尋，取得結果
        nativeSearchQueryBuilderQueryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
        nativeSearchQueryBuilderQueryBuilder.withPageable(PageRequest.of(page, size));
        Page<Product> products = productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        // 總條數
        for (Product product : products) {
            System.out.println(product);
        }

    }


}