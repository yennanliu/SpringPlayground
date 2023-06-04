//package com.yen.gulimall.product;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.yen.gulimall.product.entity.BrandEntity;
//import com.yen.gulimall.product.service.BrandService;
//import com.yen.gulimall.product.service.CategoryService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// *   1) Basic CRUD test
// *      - https://youtu.be/Ky5BZim-Y94?t=637
//
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class GulimallProductApplicationTests {
//
//    @Autowired
//    BrandService brandService;
//
//    @Autowired
//    CategoryService categoryService;
//
//	@Test
//    public void contextLoads() {
//
//	}
//
//    @Test
//    public void test1(){
//        System.out.println(123);
//    }
//
//    @Test
//    public void CRUD_test1(){
//
//        System.out.println("start");
//
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("apple");
//        brandEntity.setBrandId(123L);
//
//        brandService.save(brandEntity);
//
//        System.out.println("end");
//    }
//
//    @Test
//    public void CRUD_test2(){
//
//        System.out.println("start");
//
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("google");
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("THIS IS TEST 123");
//
//        brandService.updateById(brandEntity);
//
//        System.out.println("end");
//    }
//
//    @Test
//    public void CRUD_test3(){
//
//        System.out.println("start");
//
//        List<BrandEntity> res = brandService.list(
//                new QueryWrapper<BrandEntity>().eq("brand_id", 1L)
//        );
//
//        res.forEach(x -> System.out.println(x));
//
//        System.out.println("end");
//    }
//
//    // https://youtu.be/GZk1IbmO1Nc?t=613
//    @Test
//    public void Test_CategoryService(){
//        Long[] catelogPath = categoryService.findCatelogPath(225L);
//        System.out.println(">>> catelogPath = " + Arrays.asList(catelogPath));
//    }
//
//}
