package com.example.demo.model;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import java.io.Serializable;

/**
 * @author longzhonghua
 * @data 2/24/2019 11:33 PM
 */
@Data
/**
 * 必須實現可序列化接口，要在網路上傳輸
 */
public class User implements Serializable {

    @Field("id")
    /**
     * 使用這個注解，裡面的名字是根據在solr中組態的來決定
     */
    private String id;

    @Field("name")
    private String name;

}

