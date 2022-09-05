package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: QueryTest
 * Author:   longzhonghua
 * Date:     2019/5/5 13:52
 * Description:模糊查詢
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FuzzySearchTest {
    @Autowired
    private ProductRepository productRepository;
    @Test
    /**
     * Description: 左右模糊。
     */
    public void queryStringQuery() {
        // 查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
        //  左右模糊
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.queryStringQuery("我覺得紅富士好吃").field("name"));
        // 搜尋，取得結果
        Page<Product>  products= productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        for (Product product : products) {
            System.out.println(product);
        }
    }

    @Test
    /**
     * Description: 前綴查詢prefixQuery。
     */
    public void prefixQuery() {
        // 查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
        //  左右模糊
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.prefixQuery("name","士"));
        // 搜尋，取得結果
        Page<Product>  products= productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        for (Product product : products) {
            System.out.println(product);
        }

    }

    @Test
    /**
     * Description: 分詞模糊查詢。
     */
    public void fuzzyQuery() {
        // 查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.fuzzyQuery("name","士").fuzziness(Fuzziness.ONE));
        // 搜尋，取得結果
        Page<Product>  products= productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        for (Product product : products) {
            System.out.println(product);
        }

    }
    @Test
    /**
     * Description: 通配符查詢wildcard query。
     */
    public void wildcardQuery() {
        // 查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.wildcardQuery("name","金*"));
        // 搜尋，取得結果
        Page<Product>  products= productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        for (Product product : products) {
            System.out.println(product);
        }
    }

    @Test
    /**
     * Description: 通配符查詢wildcard query。
     */
    public void wildcardQuery2() {
        // 查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.wildcardQuery("name","金?"));
        // 搜尋，取得結果
        Page<Product>  products= productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        for (Product product : products) {
            System.out.println(product);
        }
    }

    @Test
    /**
     * Description:相似內容查詢。
     */
    public void moreLikeThisQuery() {
        // 查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
        //  左右模糊
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.moreLikeThisQuery(new String[]{"name"}, new String[]{"紅"}, null));
        // 搜尋，取得結果
        Page<Product> products = productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        for (Product product : products) {
            System.out.println(product);
        }
    }
    @Test
    /**
     * Description:相似內容查詢。
     */
    public void multiMoreLikeThisQuery() {
        // 查詢條件
        NativeSearchQueryBuilder nativeSearchQueryBuilderQueryBuilder = new NativeSearchQueryBuilder();
        //  左右模糊
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.moreLikeThisQuery(new String[]{"name"}, new String[]{"紅"}, null));
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.moreLikeThisQuery(new String[]{"name"}, new String[]{"紅"}, null));
        nativeSearchQueryBuilderQueryBuilder.withQuery(QueryBuilders.moreLikeThisQuery(new String[]{"name"}, new String[]{"紅"}, null));
        // 搜尋，取得結果
        Page<Product> products = productRepository.search(nativeSearchQueryBuilderQueryBuilder.build());
        for (Product product : products) {
            System.out.println(product);
        }
    }

}