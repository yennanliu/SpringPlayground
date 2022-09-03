package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.SolrService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: SolrControllerTest
 * Author:   longzhonghua
 * Date:     2019/5/9 11:10
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrControllerTest {
    @Autowired
    private SolrService solrService;

    @Autowired
    private SolrClient solrClient;
    @Test
    public void addUser() throws IOException, SolrServerException {
        // 建構一篇文件
        //往doc中加入字段，在用戶端這邊加入的字段必須在服務端的組態檔中有定義
        User user = new User();
        user.setId("8888888");
        user.setName("龍知然");
        solrClient.addBean(user);
        solrClient.commit();
    }

    //根據di查詢
    @Test
    public void getByIdFromSolr() throws IOException, SolrServerException {
        //根據id查詢內容
        String id="8888888";
        SolrDocument solrDocument = solrClient.getById(id);
        //取得filedName
        Collection<String> fieldNames = solrDocument.getFieldNames();
        //取得file名和內容
        Map<String, Object> fieldValueMap = solrDocument.getFieldValueMap();
        List<SolrDocument> childDocuments = solrDocument.getChildDocuments();
        String results = solrDocument.toString();
        System.out.println(results);

    }


    @Test
    public void updateUser()  throws IOException, SolrServerException {
        User user = new User();
        user.setId("8888888");
        user.setName("知然");
        solrClient.addBean(user);
        solrClient.commit();
    }

    @Test
    public void delById()  throws IOException, SolrServerException {
        //根據id移除訊息
        UpdateResponse updateResponse = solrClient.deleteById("8888888");
        //執行的時間
        long elapsedTime = updateResponse.getElapsedTime();
        int qTime = updateResponse.getQTime();
        //請求位址
        String requestUrl = updateResponse.getRequestUrl();
        //請求的結果{responseHeader={status=0,QTime=2}}
        NamedList<Object> response = updateResponse.getResponse();
        //請求結果的頭{status=0,QTime=2}
        NamedList responseHeader = updateResponse.getResponseHeader();
        //請求的狀態 0
        solrClient.commit();
        int status = updateResponse.getStatus();
        //成功則傳回0.要是沒有文件被移除也會傳回0,代表根本沒有

    }


    @Test
    public void queryAllOne() {
    }

    @Test
    public void queryAll() throws IOException, SolrServerException {


        //第二種模式
        SolrQuery solrQuery = new SolrQuery();
        // 設定預設搜尋域
        solrQuery.setQuery("*:*");
//        solrQuery.addField("*");
        solrQuery.set("q", "知然");
        solrQuery.add("q", "name:然");
        // 設定傳回結果的排序規則
        solrQuery.setSort("id", SolrQuery.ORDER.asc);
        //設定查詢的條數
        solrQuery.setRows(50);
        //設定查詢的開始
        solrQuery.setStart(0);
        // 設定分頁參數
        solrQuery.setStart(0);
        solrQuery.setRows(20);
        //設定反白
        solrQuery.setHighlight(true);
        //設定反白的字段
        solrQuery.addHighlightField("name");
        //設定反白的型態
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        System.out.println(solrQuery);
        QueryResponse response = solrClient.query(solrQuery);
        //傳回反白顯示結果
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        //response.getResults();查詢傳回的結果

        SolrDocumentList documentList = response.getResults();
        long numFound = documentList.getNumFound();
        System.out.println("總共查詢到的文件數量： " + numFound);
        for (SolrDocument solrDocument : documentList) {
            System.out.println(solrDocument);
            System.out.println(solrDocument.get("name"));
        }
        System.out.println(highlighting);
    }
}