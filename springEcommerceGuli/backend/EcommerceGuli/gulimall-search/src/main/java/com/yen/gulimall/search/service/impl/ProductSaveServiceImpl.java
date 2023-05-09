package com.yen.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.yen.gulimall.common.to.es.SkuEsModel;
import com.yen.gulimall.search.config.ElasticConfig;
import com.yen.gulimall.search.constant.EsConstant;
import com.yen.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// https://youtu.be/PZW2rOit2s8?t=153

@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    // NOTE : we autowire org.elasticsearch.client.RestHighLevelClient
    @Autowired
    RestHighLevelClient  restHighLevelClient;

    @Override
    public Boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException {

        // save to ES
        // Step 1) setup ES index : product, set up mapping (data structure)

        // Step 2) save data into ES (with "product" index)
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel model: skuEsModelList){
            // setup save request
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());
            String jsonString = JSON.toJSONString(model); // transform json to string
            indexRequest.source(jsonString, XContentType.JSON); // add jsonString to index request
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticConfig.COMMON_OPTIONS);
        // TODO : deal with batch insert (ES) error
        boolean status = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.error("product on shelf error : id = {}", collect);
        return status;

        // Step 3)
    }

}
