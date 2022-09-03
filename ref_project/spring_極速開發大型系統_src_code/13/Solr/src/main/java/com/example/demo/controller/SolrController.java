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
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author longzhonghua
 * @data 2/24/2019 11:36 PM
 */
@RestController
public class SolrController {
    @Autowired
    private SolrService solrService;

    @Autowired
    private SolrClient solrClient;

    //批次增加
    @RequestMapping("/addUsers")
    public void addUsers() throws IOException, SolrServerException {
        List<User> users = solrService.addUser();
        solrClient.addBeans(users);
        solrClient.commit();
    }

    //增加
    @RequestMapping("/addUser")
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
    @RequestMapping("/getById/{id}")
    public String getByIdFromSolr(@PathVariable("id") String id) throws IOException, SolrServerException {

        //根據id查詢內容
        SolrDocument solrDocument = solrClient.getById(id);
        //取得filedName
        Collection<String> fieldNames = solrDocument.getFieldNames();
        //取得file名和內容
        Map<String, Object> fieldValueMap = solrDocument.getFieldValueMap();

//            int childDocumentCount = solrDocument.getChildDocumentCount();

        List<SolrDocument> childDocuments = solrDocument.getChildDocuments();
        String results = solrDocument.toString();
        //fieldNames
        // fieldValueMap
        // childDocuments;
        return results;

    }

    //改
    @RequestMapping("/updateUser")
    public void updateUser() throws IOException, SolrServerException {
        User user = new User();
        user.setId("8888888");
        user.setName("知然");

        solrClient.addBean(user);
        solrClient.commit();
    }

    //根據di移除
    @RequestMapping("/delById/{id}")
    public String delById(@PathVariable("id") String id) throws IOException, SolrServerException {
        //根據id移除訊息
        UpdateResponse updateResponse = solrClient.deleteById(id);
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
        return status == 0 ? "success" : "failed";
    }

    @RequestMapping("/delAll")
    public void delAll() throws IOException, SolrServerException {
        solrClient.deleteByQuery("*:*");
        UpdateResponse response = solrClient.commit();

    }

    @RequestMapping("/queryAllOne")
    public String queryAllOne() throws IOException, SolrServerException {
 /*  //第一種模式
        Map<String, String> queryParamMap = new HashMap<String, String>();
        queryParamMap.put("q", "*:*");
        //queryParamMap.put("f1", "id,name");
        queryParamMap.put("f1", "id:88");
        queryParamMap.put("sort", "id asc");
        MapSolrParams mapSolrParams = new MapSolrParams(queryParamMap);
        solrClient.query(mapSolrParams);

        for (Map.Entry<String, String[]> mapSolrParam : mapSolrParams) {

            System.out.println("solrDocument==============" + mapSolrParam);
        }

        return mapSolrParams;*/

        SolrQuery query = new SolrQuery();

        // 給query設定一個主查詢條件：關鍵詞
        query.setQuery("*:*");
        query.add("q", "name:然");

        QueryResponse response = solrClient.query(query);

        SolrDocumentList docs = response.getResults();

        long numFound = docs.getNumFound();

        System.out.println("總共查詢到的文件數量： " + numFound);

        return docs.toString();

    }


    @RequestMapping("/queryAll")
    public Object queryAll() throws IOException, SolrServerException {


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
            System.out.println("solrDocument==============" + solrDocument);
            System.out.println("solrDocument==============" + solrDocument.get("name"));
        }
        return highlighting;
    }
}
