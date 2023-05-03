package com.yen.gulimall.search.config;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;

// https://youtu.be/EIymTNQn8XE?t=1094

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticConfigTest extends TestCase {

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testGetEsClient(){
        System.out.println(client); // should be sth like this : org.elasticsearch.client.RestHighLevelClient@4682eba5
    }

    /**
     *  https://youtu.be/YXSti7iDv8Y?t=135
     *      - test save data to ES index
     */
    @Test
    public void testSaveToIndex() throws IOException {

        String index = "myindex3";
        IndexRequest indexRequest = new IndexRequest(index);
        //indexRequest.id("10");
        // approach 1) : use raw json
//        request.source("userName", "iori",
//                       "age", 20,
//                       "gender", "m", XContentType.JSON);

        // approach 2) use json string from bean (user defined class)
        User user = new User("JOLIN", 26);
        String userJsonStr = JSON.toJSONString(user);
        indexRequest.source(userJsonStr, XContentType.JSON); // data saved to ES

        // run op
        IndexResponse indexResp = client.index(indexRequest, ElasticConfig.COMMON_OPTIONS);
        System.out.println(">>> IndexResponse = " + indexResp.toString());
    }

    // https://youtu.be/Rx8VJAL78QY?t=7
    @Test
    public void testQueryES() throws IOException {

        String index = "myindex3";

        // step 1) create query request
        SearchRequest searchRequest = new SearchRequest();
        // query which index
        searchRequest.indices(index);
        // search condition
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // construct search conditions
        // https://youtu.be/Rx8VJAL78QY?t=443
//        searchSourceBuilder.query();
//        searchSourceBuilder.from();
//        searchSourceBuilder.size();
//        searchSourceBuilder.aggregation();
        searchSourceBuilder.query(
                QueryBuilders.matchQuery("age", "26")
        );
        System.out.println(">>> searchSourceBuilder = " + searchSourceBuilder);

        searchRequest.source(searchSourceBuilder);
        // construct search conditions



        // step 2) run query
        // https://youtu.be/Rx8VJAL78QY?t=206
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


        System.out.println(">>> searchResponse = " + searchResponse);

        // step 3) analyze result
        RestStatus status = searchResponse.status();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();


    }


    @Data
    class User{
        private String name;
        private Integer age;

        public User(String name, Integer age){
            this.name = name;
            this.age = age;
        }
    }

}