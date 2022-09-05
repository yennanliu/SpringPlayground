package com.example.demo.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;

/**
 * Author:   longzhonghua
 * Date:     3/21/2019 9:38 PM
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class HelloControllerTest {
    //啟用web上下文
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception{
        //使用上下文建構mockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    public void hello() throws Exception {
       // 得到MvcResult自訂驗證
      // 執行請求
        MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.get("/hello")
        //.post("/hello") 傳送post請求
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                //傳導入參數數
                .param("name","longzhonghua")
               // .accept(MediaType.TEXT_HTML_VALUE))
                //接收的型態
                .accept(MediaType.APPLICATION_JSON_UTF8))
                //等同於Assert.assertEquals(200,status);
                //判斷接收到的狀態是否是200
                .andExpect(MockMvcResultMatchers.status().isOk())
                 //等同於 Assert.assertEquals("hello longzhonghua",content);
                .andExpect(MockMvcResultMatchers.content().string("hello longzhonghua"))
                .andDo(MockMvcResultHandlers.print())
        //傳回MvcResult
        .andReturn();
        //得到傳回程式碼
        int status=mvcResult.getResponse().getStatus();
        //得到傳回結果
        String content=mvcResult.getResponse().getContentAsString();
        //斷言，判斷傳回程式碼是否正確
        Assert.assertEquals(200,status);
        //斷言，判斷傳回的值是否正確
        Assert.assertEquals("hello longzhonghua",content);
    }
}
