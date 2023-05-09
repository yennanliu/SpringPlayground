package com.yen.gulimall.search.service;

// https://youtu.be/PZW2rOit2s8?t=129

import com.yen.gulimall.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

public interface ProductSaveService {
    public Boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException;
}
