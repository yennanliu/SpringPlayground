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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

// https://youtu.be/PZW2rOit2s8?t=153

@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    // NOTE : we autowire org.elasticsearch.client.RestHighLevelClient
    @Autowired
    RestHighLevelClient  restHighLevelClient; // as ES client

    @Override
    public Boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException {

        // TODO : fix why received skuEsModelList is always null and make it can be sent to ES
        // For DEBUG only
        SkuEsModel model1 = new SkuEsModel(11L, 6L, "prod_1", new BigDecimal("4.0"), null, 1L, true, 1L, 1L, 1L, "brand1", null, null, null);
        SkuEsModel model2 = new SkuEsModel(12L, 4L, "prod_2", new BigDecimal("4.0"), null, 1L, true, 1L, 1L, 1L, "brand1", null, null, null);
        List<SkuEsModel> _skuEsModelList = new LinkedList<>();
        _skuEsModelList.add(model1);
        _skuEsModelList.add(model2);
        skuEsModelList = _skuEsModelList;

        System.out.println(">>> skuEsModelList size = " + skuEsModelList.size()
                + " data = " + skuEsModelList.toArray().toString()
        );

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
            log.info("ES indexRequest = " + indexRequest);
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticConfig.COMMON_OPTIONS);
        // TODO : deal with batch insert (ES) error
        boolean status = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> item.getId()).collect(Collectors.toList());
        log.debug("Product on board (ES) : id = {}", collect + " has error ? = " + status);
        return status;

        // Step 3)
    }

}
