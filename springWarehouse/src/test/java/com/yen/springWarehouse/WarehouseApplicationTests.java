package com.yen.springWarehouse;

import com.yen.springWarehouse.bean.ProductType;
import com.yen.springWarehouse.mapper.ProductTypeMapper;
import com.yen.springWarehouse.service.ProductTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WarehouseApplicationTests {

//	@Test
//	void contextLoads() {
//	}

    @Autowired
    ProductTypeService productTypeService;

    @Autowired
    ProductTypeMapper productTypeMapper;

    @Test
    public void testProductTypeService(){

        ProductType pt_1= productTypeService.getById(1);
        System.out.println(">>> pt_1 = " + pt_1);

        ProductType pt_2 = productTypeMapper.selectById(1);
        System.out.println(">>> pt_2 = " + pt_2);

        List<ProductType> pt_list_1 = productTypeService.list();
        System.out.println(">>> pt_list_1 = " + pt_list_1);
    }

}
