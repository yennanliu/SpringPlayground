package com.yen.springBootPOC2AdminSystem.service;

import com.yen.springBootPOC2AdminSystem.bean.Product;

/** product service interface
 *  -> its implementation : ProductServiceImpl
 */

public interface ProductService {
    public Product getProduct(long id);
}
