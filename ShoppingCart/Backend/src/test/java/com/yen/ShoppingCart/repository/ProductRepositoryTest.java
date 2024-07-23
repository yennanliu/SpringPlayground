//package com.yen.ShoppingCart.repository;
//
//import com.yen.ShoppingCart.model.Product;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//public class ProductRepositoryTest {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @BeforeEach
//    public void setUp() {
//        Product product1 = new Product();
//        product1.setName("Apple iPhone");
//        product1.setDescription("Latest model of iPhone");
//        product1.setPrice(999.99);
//
//        Product product2 = new Product();
//        product2.setName("Samsung Galaxy");
//        product2.setDescription("Latest model of Samsung Galaxy");
//        product2.setPrice(899.99);
//
//        Product product3 = new Product();
//        product3.setName("Google Pixel");
//        product3.setDescription("Latest model of Google Pixel");
//        product3.setPrice(799.99);
//
//        productRepository.save(product1);
//        productRepository.save(product2);
//        productRepository.save(product3);
//    }
//
//    @Test
//    public void testSearchProductsByName() {
//        List<Product> results = productRepository.searchProductsByName("iphone");
//        assertThat(results).hasSize(1);
//        assertThat(results.get(0).getName()).isEqualTo("Apple iPhone");
//
//        results = productRepository.searchProductsByName("galaxy");
//        assertThat(results).hasSize(1);
//        assertThat(results.get(0).getName()).isEqualTo("Samsung Galaxy");
//
//        results = productRepository.searchProductsByName("google");
//        assertThat(results).hasSize(1);
//        assertThat(results.get(0).getName()).isEqualTo("Google Pixel");
//    }
//
//    @Test
//    public void testSearchProductsByNameIgnoreCase() {
//        List<Product> results = productRepository.searchProductsByName("IPHONE");
//        assertThat(results).hasSize(1);
//        assertThat(results.get(0).getName()).isEqualTo("Apple iPhone");
//
//        results = productRepository.searchProductsByName("GALAXY");
//        assertThat(results).hasSize(1);
//        assertThat(results.get(0).getName()).isEqualTo("Samsung Galaxy");
//
//        results = productRepository.searchProductsByName("GOOGLE");
//        assertThat(results).hasSize(1);
//        assertThat(results.get(0).getName()).isEqualTo("Google Pixel");
//    }
//
//    @Test
//    public void testSearchProductsByPartialName() {
//        List<Product> results = productRepository.searchProductsByName("gal");
//        assertThat(results).hasSize(1);
//        assertThat(results.get(0).getName()).isEqualTo("Samsung Galaxy");
//    }
//
//    @Test
//    public void testSearchProductsByNameNotFound() {
//        List<Product> results = productRepository.searchProductsByName("nokia");
//        assertThat(results).isEmpty();
//    }
//}