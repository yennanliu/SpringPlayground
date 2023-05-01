package com.yen.gulimall.search.config;

import junit.framework.TestCase;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

}